package com.app.jetpackmoviecatalogue.data.source.local.room;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.app.jetpackmoviecatalogue.application.MyApplication;
import com.app.jetpackmoviecatalogue.data.source.local.entity.Content;

@Database(entities = {Content.class}, version = 1, exportSchema = false)

public abstract class AppDatabase extends RoomDatabase {
    private static final Object sLock = new Object();
    private static AppDatabase INSTANCE;

    public static AppDatabase getInstance() {
        synchronized (sLock) {
            if(INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(MyApplication.getAppContext(),
                        AppDatabase.class, "Academies.db")
                        .build();
            }
        }
        return INSTANCE;
    }

    public abstract AppDao appDao();

}
