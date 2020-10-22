package com.app.jetpackmoviecatalogue.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.jetpackmoviecatalogue.R;
import com.app.jetpackmoviecatalogue.adapter.ContentAdapter;
import com.app.jetpackmoviecatalogue.data.source.local.entity.Content;
import com.app.jetpackmoviecatalogue.databinding.FragmentMainBinding;
import com.app.jetpackmoviecatalogue.ui.detail.DetailActivity;
import com.app.jetpackmoviecatalogue.viewmodel.ViewModelFactory;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragmentMain extends Fragment {
    private final String TAG = PlaceholderFragmentMain.class.getSimpleName();
    private static final String ARG_SECTION_NUMBER = "section_number";
    private PageViewModelMain pageViewModelMain;
    private ContentAdapter contentAdapter;
    private final String LOAD_MOVIE = "movie";
    private final String LOAD_TV = "tv";
    private int index = 1;
    private FragmentMainBinding binding;

    static PlaceholderFragmentMain newInstance(int sectionNumber) {
        PlaceholderFragmentMain fragment = new PlaceholderFragmentMain();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModelMain = obtainViewModel(getActivity());
        setRetainInstance(true);
        index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        String sectionLabel = index == 1 ? getResources().getString(R.string.movie) : getResources().getString(R.string.tvshow);
        contentAdapter = new ContentAdapter(new ContentAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(int index, List<Content> contents) {
                startActivity(DetailActivity.newInstance(index, contents, pageViewModelMain.getSavedGenres()));
            }

            @Override
            public void onLikeClicked(Content content) {
                pageViewModelMain.setLike(content, !content.isLiked());
                contentAdapter.notifyDataSetChanged();
            }
        }, sectionLabel);
        contentAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater, container, false);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        binding.rvContent.setHasFixedSize(true);
        binding.rvContent.setLayoutManager(linearLayoutManager);
        binding.rvContent.setNestedScrollingEnabled(false);
        if (getActivity() != null && savedInstanceState == null) {
            pageViewModelMain.getContent(index == 1 ? LOAD_MOVIE : LOAD_TV).observe(this, listResource -> {
                if (listResource != null) {
                    switch (listResource.status) {
                        case LOADING:
                            binding.shimmerLayout.setVisibility(View.VISIBLE);
                            break;
                        case SUCCESS:
                            pageViewModelMain.getAllGenres(index == 1 ? LOAD_MOVIE : LOAD_TV).observe(this, genreSparseArray -> {
                                pageViewModelMain.setSavedGenres(genreSparseArray);
                            });
                            contentAdapter.setData(listResource.data, true);
                            binding.shimmerLayout.setVisibility(View.GONE);
                            break;
                        case ERROR:
                            binding.shimmerLayout.setVisibility(View.VISIBLE);
                            Snackbar.make(binding.getRoot(), getString(R.string.err), Snackbar.LENGTH_SHORT);
                            break;
                    }
                }
            });
        }

        binding.rvContent.setAdapter(contentAdapter);
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @NonNull
    private static PageViewModelMain obtainViewModel(FragmentActivity activity) {
        ViewModelFactory factory = ViewModelFactory.getInstance();
        return ViewModelProviders.of(activity, factory).get(PageViewModelMain.class);
    }

}