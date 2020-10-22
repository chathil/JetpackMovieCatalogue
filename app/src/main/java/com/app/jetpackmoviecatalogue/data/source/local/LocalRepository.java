package com.app.jetpackmoviecatalogue.data.source.local;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;

import com.app.jetpackmoviecatalogue.data.source.local.entity.Content;
import com.app.jetpackmoviecatalogue.data.source.local.room.AppDao;

import java.util.List;

public class LocalRepository {
    private static LocalRepository INSTANCE;
    private final AppDao mAppDao;

    public LocalRepository(AppDao appDao) {
        mAppDao = appDao;
    }

    public static LocalRepository getInstance(AppDao appDao) {
        if(INSTANCE == null) {
            INSTANCE = new LocalRepository(appDao);
        }
        return INSTANCE;
    }

    public DataSource.Factory<Integer, Content> getAllContents(String contentType) {
        return mAppDao.getAllContents(contentType);
    }

    public DataSource.Factory<Integer, Content>  getFavorites(String contentType) {
        return mAppDao.getFavorites(contentType, true);
    }

    public void cacheToDb(List<Content> contents) {
        mAppDao.cacheToDb(contents);
    }

    public int setLike(Content content, boolean isLiked){
        return mAppDao.setIsLiked(content.getId(), isLiked);
    }
}
