package com.app.jetpackmoviecatalogue.ui.main.favorite;

import android.util.SparseArray;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.PagedList;

import com.app.jetpackmoviecatalogue.data.source.AppRepository;
import com.app.jetpackmoviecatalogue.data.source.local.entity.Content;
import com.app.jetpackmoviecatalogue.data.source.local.entity.Genre;
import com.app.jetpackmoviecatalogue.vo.Resource;

public class FavViewModel extends ViewModel {
    private final AppRepository mAppRepo;
    private int index;
    private SparseArray<Genre> savedGenres = new SparseArray<>();


    public FavViewModel(AppRepository mAppRepo) {
        this.mAppRepo = mAppRepo;
    }

    public LiveData<Resource<PagedList<Content>>> favorites(String contentType) {
        return mAppRepo.getFavorites(contentType, true);
    }

    public void setSavedGenres(SparseArray<Genre> savedGenres) {
        this.savedGenres = savedGenres;
    }

    public int getIndex() {
        return index;
    }

    public LiveData<SparseArray<Genre>> getAllGenres(String contentType) {
        return mAppRepo.getAllGenres(contentType);
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public SparseArray<Genre> getSavedGenres() {
        return savedGenres;
    }

    public void setLike(Content content, boolean isLiked) {
        mAppRepo.setLike(content, isLiked);
    }

}
