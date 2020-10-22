package com.app.jetpackmoviecatalogue.data.source.local.room;

import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.app.jetpackmoviecatalogue.data.source.local.entity.Content;

import java.util.List;


@Dao
public interface AppDao {
    @Transaction
    @Query("SELECT * FROM content where contentType = :contentType")
    DataSource.Factory<Integer, Content> getAllContents(String contentType);
//    LiveData<List<Content>> getAllContents(String contentType);

    @Transaction
    @Query("SELECT * FROM content where contentType = :contentType and isLiked = :isLiked")
    DataSource.Factory<Integer, Content> getFavorites(String contentType, boolean isLiked);
//    LiveData<List<Content>> getFavorites(String contentType, boolean isLiked);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long[] cacheToDb(List<Content> contents);


    @Query("UPDATE content SET isLiked = :isLiked where contentId =:id")
    int setIsLiked(String id, boolean isLiked);
}
