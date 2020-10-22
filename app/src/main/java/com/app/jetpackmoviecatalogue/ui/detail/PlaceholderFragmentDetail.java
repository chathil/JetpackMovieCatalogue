package com.app.jetpackmoviecatalogue.ui.detail;

import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.jetpackmoviecatalogue.R;
import com.app.jetpackmoviecatalogue.adapter.CastAdapter;
import com.app.jetpackmoviecatalogue.adapter.GenreAdapter;
import com.app.jetpackmoviecatalogue.data.source.local.entity.Content;
import com.app.jetpackmoviecatalogue.data.source.local.entity.Genre;
import com.app.jetpackmoviecatalogue.databinding.FragmentDetailBinding;
import com.app.jetpackmoviecatalogue.utils.GlideApp;
import com.app.jetpackmoviecatalogue.viewmodel.ViewModelFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PlaceholderFragmentDetail extends Fragment {
    private PageViewModelDetail pageViewModelDetail;
    private static final String EXTRA_CONTENT_DATA = "content_data";
    private static final String EXTRA_INDEX = "index";
    private static final String EXTRA_GENRE_IDS = "genre_ids";
    private static String TAG = PlaceholderFragmentDetail.class.getSimpleName();
    private final String imagePath = "https://image.tmdb.org/t/p/w185";
    private FragmentDetailBinding binding;
    private CastAdapter castAdapter;
    private GenreAdapter genreAdapter;

    static PlaceholderFragmentDetail newInstance(Content content, int index, SparseArray<Genre> genreSparseArray) {
        PlaceholderFragmentDetail fragment = new PlaceholderFragmentDetail();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_CONTENT_DATA, content);
        bundle.putInt(EXTRA_INDEX, index);
        bundle.putSparseParcelableArray("GENRE_SPARSE_ARRAY", genreSparseArray);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModelDetail = obtainViewModel(getActivity());
        setRetainInstance(true);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDetailBinding.inflate(inflater, container, false);
        binding.setFragment(this);
        binding.setViewModel(pageViewModelDetail);
        binding.setLifecycleOwner(this);
        binding.mvDetailSv.getViewTreeObserver().addOnScrollChangedListener(new ScrollPositionObserver());
        castAdapter = new CastAdapter();
        genreAdapter = new GenreAdapter();
        if (getArguments() != null) {
            pageViewModelDetail.setContent(Objects.requireNonNull(getArguments().getParcelable(EXTRA_CONTENT_DATA)));
            pageViewModelDetail.setIndex(getArguments().getInt(EXTRA_INDEX));
            pageViewModelDetail.setGenreSparseArray(getArguments().getSparseParcelableArray("GENRE_SPARSE_ARRAY"));
        }
        Content content = pageViewModelDetail.getContent();
        Log.d(TAG, "onCreateView: " + content.isLiked());
        if (getActivity() != null) {
            binding.detailLoading.setVisibility(View.VISIBLE);
            pageViewModelDetail.getAllCrews(content.getId(), content.getContentType()).observe(this, crewSparseArray -> {
                castAdapter.setData(crewSparseArray, content.getMvPoster());
                binding.detailLoading.setVisibility(View.GONE);
            });

            SparseArray<Genre> genreSparseArray = pageViewModelDetail.getGenreSparseArray();
            List<String> tempGenres = new ArrayList<>();
            for (int integer : content.getGenreIds()) {
                tempGenres.add(genreSparseArray.get(integer, new Genre(0, getString(R.string.genre_not_found))).getName());
            }
            genreAdapter.setData(tempGenres);
        }
        setupView(pageViewModelDetail.getContent());
        setupRvCrews();
        setupRvGenres();
        return binding.getRoot();
    }

    private void setupRvCrews() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        binding.rvCrews.setLayoutManager(linearLayoutManager);
        binding.rvCrews.setHasFixedSize(false);
        binding.rvCrews.setAdapter(castAdapter);
        binding.rvCrews.setNestedScrollingEnabled(false);
    }

    private void setupRvGenres() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        binding.rvGenre.setLayoutManager(linearLayoutManager);
        binding.rvGenre.setHasFixedSize(true);
        binding.rvGenre.setAdapter(genreAdapter);
    }

    private void setupView(Content content) {
        pageViewModelDetail.getIndex().observe(this, integer -> {
            GlideApp.with(Objects.requireNonNull(getContext()))
                    .load(imagePath + content.getMvBackdrop())
                    .into(binding.mvPosterBackdrop);
            binding.mvRatingChart.setProgress(content.getUserScore());
        });
    }

    @NonNull
    private static PageViewModelDetail obtainViewModel(FragmentActivity activity) {
        ViewModelFactory factory = ViewModelFactory.getInstance();
        return ViewModelProviders.of(activity, factory).get(PageViewModelDetail.class);
    }

    private class ScrollPositionObserver implements ViewTreeObserver.OnScrollChangedListener {

        private int mImageViewHeight;

        ScrollPositionObserver() {
            mImageViewHeight = 240;
        }

        @Override
        public void onScrollChanged() {
            int scrollY = Math.min(Math.max(binding.mvDetailSv.getScrollY(), 0), mImageViewHeight);
            float alpha = scrollY / (float) mImageViewHeight;
            binding.mvPosterContainer.setAlpha((float) 1.0 - alpha);
        }
    }

}
