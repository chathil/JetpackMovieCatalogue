package com.app.jetpackmoviecatalogue.data.source;

import android.util.SparseArray;

import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;

import com.app.jetpackmoviecatalogue.data.source.local.entity.Content;
import com.app.jetpackmoviecatalogue.data.source.local.entity.Crew;
import com.app.jetpackmoviecatalogue.data.source.local.entity.Genre;
import com.app.jetpackmoviecatalogue.vo.Resource;

public interface AppDataSource {
    LiveData<Resource<PagedList<Content>>> getAllContents(String contentType);

    LiveData<Resource<PagedList<Content>>> getFavorites(String contentType, boolean isLikes);

    LiveData<SparseArray<Crew>> getAllCrews(String id, String contentType);

    LiveData<SparseArray<Genre>> getAllGenres(String contentType);

    void setLike(Content content, boolean isLike);
}
