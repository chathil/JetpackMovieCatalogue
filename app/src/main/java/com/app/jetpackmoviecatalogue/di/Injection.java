package com.app.jetpackmoviecatalogue.di;

import com.app.jetpackmoviecatalogue.data.source.AppRepository;
import com.app.jetpackmoviecatalogue.data.source.local.LocalRepository;
import com.app.jetpackmoviecatalogue.data.source.local.room.AppDatabase;
import com.app.jetpackmoviecatalogue.data.source.remote.RemoteRepository;
import com.app.jetpackmoviecatalogue.utils.AppExecutors;


public class Injection {
    public static AppRepository provideRepository() {

        RemoteRepository remoteRepository = RemoteRepository.getInstance();
        AppDatabase database = AppDatabase.getInstance();
        AppExecutors appExecutors = new AppExecutors();
        LocalRepository localRepository = LocalRepository.getInstance(database.appDao());

        return AppRepository.getInstance(localRepository, remoteRepository, appExecutors);
    }
}
