package com.app.jetpackmoviecatalogue.ui.detail;

import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.app.jetpackmoviecatalogue.R;
import com.app.jetpackmoviecatalogue.application.MyApplication;
import com.app.jetpackmoviecatalogue.data.source.local.entity.Content;
import com.app.jetpackmoviecatalogue.data.source.local.entity.Genre;
import com.app.jetpackmoviecatalogue.databinding.ActivityDetailBinding;
import com.app.jetpackmoviecatalogue.utils.ShrinkPageTransformer;
import com.app.jetpackmoviecatalogue.viewmodel.ViewModelFactory;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = DetailActivity.class.getSimpleName();
    private ActivityDetailBinding binding;
    private DetailActivityViewModel viewModel;
    private Content currentContent;
    private static final String SPARSE_ARRAY_KEY = "SPARSE_ARRAY_KEY";
    private static final String BUNDLE_KEY = "BUNDLE_KEY";
    private static final String EXTRA_INDEX = "index";
    private static final String EXTRA_CONTENT = "content";

    public static Intent newInstance(int index, List<Content> contents, SparseArray<Genre> genreSparseArray) {
        Intent intent = new Intent(MyApplication.getAppContext(), DetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSparseParcelableArray(SPARSE_ARRAY_KEY, genreSparseArray);
        intent.putExtra(BUNDLE_KEY, bundle);
        intent.putExtra(EXTRA_INDEX, index);
        intent.putParcelableArrayListExtra(EXTRA_CONTENT, new ArrayList<>(contents));
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        binding.setActivity(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        viewModel = obtainViewModel(this);
        viewModel.setContents(getIntent().getParcelableArrayListExtra(EXTRA_CONTENT));
        viewModel.setIndex(getIntent().getIntExtra(EXTRA_INDEX, 0));

        Bundle bundle = getIntent().getBundleExtra(BUNDLE_KEY);
        assert bundle != null;
        viewModel.setGenreSparseArray(bundle.getSparseParcelableArray(SPARSE_ARRAY_KEY));
        SectionsPagerAdapterDetail sectionsPagerAdapterDetail = new SectionsPagerAdapterDetail(this, getSupportFragmentManager(), viewModel.getContents(), viewModel.getGenreSparseArray());
        binding.viewPager.setAdapter(sectionsPagerAdapterDetail);
        binding.viewPager.setPageTransformer(false, new ShrinkPageTransformer());
        binding.viewPager.setCurrentItem(viewModel.getIndex());
        currentContent = viewModel.getContents().get(viewModel.getIndex());
    }

    @NonNull
    private static DetailActivityViewModel obtainViewModel(FragmentActivity activity) {
        ViewModelFactory factory = ViewModelFactory.getInstance();
        return ViewModelProviders.of(activity, factory).get(DetailActivityViewModel.class);
    }


}
