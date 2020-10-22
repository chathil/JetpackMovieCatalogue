package com.app.jetpackmoviecatalogue.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.app.jetpackmoviecatalogue.data.source.AppRepository;
import com.app.jetpackmoviecatalogue.di.Injection;
import com.app.jetpackmoviecatalogue.ui.detail.DetailActivityViewModel;
import com.app.jetpackmoviecatalogue.ui.detail.PageViewModelDetail;
import com.app.jetpackmoviecatalogue.ui.main.PageViewModelMain;
import com.app.jetpackmoviecatalogue.ui.main.favorite.FavViewModel;

public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private static volatile ViewModelFactory INSTANCE;
    private final AppRepository mAppRepo;

    private ViewModelFactory(AppRepository mAppRepo) {
        this.mAppRepo = mAppRepo;
    }

    public static ViewModelFactory getInstance() {
        if (INSTANCE == null) {
            synchronized (ViewModelFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ViewModelFactory(Injection.provideRepository());
                }
            }
        }
        return INSTANCE;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        if (modelClass.isAssignableFrom(PageViewModelMain.class)) {
            //noinspection unchecked
            return (T) new PageViewModelMain(mAppRepo);
        } else if (modelClass.isAssignableFrom(PageViewModelDetail.class)) {
            //noinspection unchecked
            return (T) new PageViewModelDetail(mAppRepo);
        } else if(modelClass.isAssignableFrom(DetailActivityViewModel.class)) {
            //noinspection unchecked
            return (T) new DetailActivityViewModel(mAppRepo);
        } else if(modelClass.isAssignableFrom(FavViewModel.class)) {
            return (T) new FavViewModel(mAppRepo);
        }

        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }

}
