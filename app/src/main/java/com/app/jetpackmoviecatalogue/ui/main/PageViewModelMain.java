package com.app.jetpackmoviecatalogue.ui.main;

import android.util.SparseArray;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.PagedList;

import com.app.jetpackmoviecatalogue.data.source.AppRepository;
import com.app.jetpackmoviecatalogue.data.source.local.entity.Content;
import com.app.jetpackmoviecatalogue.data.source.local.entity.Genre;
import com.app.jetpackmoviecatalogue.vo.Resource;

import java.util.ArrayList;


public class PageViewModelMain extends ViewModel {
    private AppRepository appRepository;
    private ArrayList<Content> mContents = new ArrayList<>();
    private SparseArray<Genre> savedGenres = new SparseArray<>();

    public PageViewModelMain(AppRepository appRepository) {
        this.appRepository = appRepository;
    }

    public LiveData<Resource<PagedList<Content>>> getContent(String contentType) {
        return appRepository.getAllContents(contentType);
    }

    public LiveData<SparseArray<Genre>> getAllGenres(String contentType) {
        return appRepository.getAllGenres(contentType);
    }

    public void setSavedGenres(SparseArray<Genre> savedGenres) {
        this.savedGenres = savedGenres;
    }

    public SparseArray<Genre> getSavedGenres() {
        return savedGenres;
    }

    public void setLike(Content content, boolean isLiked) {
        appRepository.setLike(content, isLiked);
    }
}