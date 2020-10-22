package com.app.jetpackmoviecatalogue.ui.detail;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.app.jetpackmoviecatalogue.data.source.local.entity.Content;
import com.app.jetpackmoviecatalogue.data.source.local.entity.Genre;

import java.util.ArrayList;

public class SectionsPagerAdapterDetail extends FragmentStatePagerAdapter {
    private final SparseArray<Genre> genreSparseArray;
    private ArrayList<Content> contents;
    private static final String TAG = "SectionsPagerAdapterDet";

    public SectionsPagerAdapterDetail(Context context, FragmentManager fm, ArrayList<Content> contents, SparseArray<Genre> genreSparseArray) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.contents = contents;
        this.genreSparseArray = genreSparseArray;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Log.d(TAG, "getItem: " + position);
        return PlaceholderFragmentDetail.newInstance(contents.get(position), position, genreSparseArray);
    }

    @Override
    public int getCount() {
        return contents.size();
    }

}
