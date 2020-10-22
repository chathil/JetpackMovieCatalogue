package com.app.jetpackmoviecatalogue.ui.main.favorite;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.jetpackmoviecatalogue.R;
import com.app.jetpackmoviecatalogue.adapter.ContentAdapter;
import com.app.jetpackmoviecatalogue.data.source.local.entity.Content;
import com.app.jetpackmoviecatalogue.ui.detail.DetailActivity;
import com.app.jetpackmoviecatalogue.viewmodel.ViewModelFactory;

import java.util.List;


public class FavFragment extends Fragment {

    private FavViewModel mViewModel;
    private static final String INDEX_BUNDLE = "index";
    private static final String TAG = "FavFragment";
    private ContentAdapter contentAdapter;
    private static final String EXTRA_INDEX = "index";
    private static final String EXTRA_CONTENT = "content";
    private static final String SPARSE_ARRAY_KEY = "SPARSE_ARRAY_KEY";
    private static final String BUNDLE_KEY = "BUNDLE_KEY";

    static FavFragment newInstance(int index) {
        FavFragment favFragment = new FavFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(INDEX_BUNDLE, index);
        favFragment.setArguments(bundle);
        return favFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentAdapter = new ContentAdapter(new ContentAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(int index, List<Content> contents) {
                startActivity(DetailActivity.newInstance(index, contents, mViewModel.getSavedGenres()));
            }

            @Override
            public void onLikeClicked(Content content) {
                mViewModel.setLike(content, !content.isLiked());
                contentAdapter.notifyDataSetChanged();
            }
        }, "");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fav_fragment, container, false);
        setupRvContent(root);
        return root;
    }

    private void setupRvContent(View root) {
        RecyclerView rvContent = root.findViewById(R.id.rv_content);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rvContent.setHasFixedSize(true);
        rvContent.setLayoutManager(linearLayoutManager);
        rvContent.setAdapter(contentAdapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = obtainViewModel(getActivity());
        setRetainInstance(true);
        assert getArguments() != null;
        int index = getArguments().getInt(INDEX_BUNDLE, 1);
        mViewModel.setIndex(index);
        String MOVIE_TYPE = "movie";
        String TV_TYPE = "tv";
        mViewModel.favorites(index == 0 ? MOVIE_TYPE : TV_TYPE).observe(this, listResource -> {
            switch (listResource.status) {
                case LOADING:
                    Log.d(TAG, "onActivityCreated: Loading...");
                    break;
                case SUCCESS:
                    contentAdapter.setData(listResource.data, false);
                    break;
                case ERROR:
                    Toast.makeText(getContext(), getResources().getString(R.string.err), Toast.LENGTH_SHORT).show();
                    break;
            }
        });
        mViewModel.getAllGenres(index == 0 ? MOVIE_TYPE : TV_TYPE).observe(this, genreSparseArray -> {
            mViewModel.setSavedGenres(genreSparseArray);
        });
    }

    @NonNull
    private static FavViewModel obtainViewModel(FragmentActivity activity) {
        ViewModelFactory factory = ViewModelFactory.getInstance();
        return ViewModelProviders.of(activity, factory).get(FavViewModel.class);
    }

}
