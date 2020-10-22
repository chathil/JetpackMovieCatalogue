package com.app.jetpackmoviecatalogue.ui.detail;

import android.util.SparseArray;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.app.jetpackmoviecatalogue.data.source.local.entity.Content;
import com.app.jetpackmoviecatalogue.data.source.local.entity.Crew;
import com.app.jetpackmoviecatalogue.data.source.local.entity.Genre;
import com.app.jetpackmoviecatalogue.data.source.AppRepository;

public class PageViewModelDetail extends ViewModel {

    private final AppRepository mAppRepo;
    private Content mContent = new Content();
    private MutableLiveData<Integer> mIndex = new MutableLiveData<>();
    public LiveData<Integer> indexLiveData = Transformations.map(mIndex, input -> input);
    private static final String TAG = "PageViewModelDetail";

    public SparseArray<Genre> getGenreSparseArray() {
        return genreSparseArray;
    }

    public void setGenreSparseArray(SparseArray<Genre> genreSparseArray) {
        this.genreSparseArray = genreSparseArray;
    }

    private SparseArray<Genre> genreSparseArray = new SparseArray<>();

    public PageViewModelDetail(AppRepository mAppRepo) {
        this.mAppRepo = mAppRepo;
    }

    public void setContent(Content content) {
        mContent = content;
    }

    public LiveData<SparseArray<Crew>> getAllCrews(String id, String contentType) {
        return mAppRepo.getAllCrews(id, contentType);
    }

    public Content getContent() {
        return mContent;
    }

    public void setIndex(int index) {
        mIndex.setValue(index);
    }

    public LiveData<Integer> getIndex() {
        return indexLiveData;
    }


}
