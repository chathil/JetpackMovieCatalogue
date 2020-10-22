package com.app.jetpackmoviecatalogue.ui.detail;

import android.util.SparseArray;

import androidx.lifecycle.ViewModel;

import com.app.jetpackmoviecatalogue.data.source.AppRepository;
import com.app.jetpackmoviecatalogue.data.source.local.entity.Content;
import com.app.jetpackmoviecatalogue.data.source.local.entity.Genre;

import java.util.ArrayList;

public class DetailActivityViewModel extends ViewModel {

    private final AppRepository mAppRepo;
    private ArrayList<Content> contents = new ArrayList<>();
    private int index;

    public SparseArray<Genre> getGenreSparseArray() {
        return genreSparseArray;
    }

    public void setGenreSparseArray(SparseArray<Genre> genreSparseArray) {
        this.genreSparseArray = genreSparseArray;
    }

    private SparseArray<Genre> genreSparseArray = new SparseArray<>();

    public DetailActivityViewModel(AppRepository mAppRepo) {
        this.mAppRepo = mAppRepo;
    }

    public ArrayList<Content> getContents() {
        return contents;
    }

    public void setContents(ArrayList<Content> contents) {
        this.contents = contents;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

}
