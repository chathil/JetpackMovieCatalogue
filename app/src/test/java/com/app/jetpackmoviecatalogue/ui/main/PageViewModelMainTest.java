package com.app.jetpackmoviecatalogue.ui.main;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.paging.PagedList;

import com.app.jetpackmoviecatalogue.data.source.local.entity.Content;
import com.app.jetpackmoviecatalogue.data.source.AppRepository;
import com.app.jetpackmoviecatalogue.utils.FakeDataDummy;
import com.app.jetpackmoviecatalogue.vo.Resource;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PageViewModelMainTest {
    private PageViewModelMain pageViewModelMain;
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private AppRepository repo = mock(AppRepository.class);

    @Before
    public void setUp() {
        pageViewModelMain = new PageViewModelMain(repo);
    }

    @Test
    public void setMovieContents() {
        MutableLiveData<Resource<PagedList<Content>>> dummyMovies = new MutableLiveData<>();
        PagedList<Content> pagedList = mock(PagedList.class);
        dummyMovies.setValue(Resource.success(pagedList));
        when(repo.getAllContents("movie")).thenReturn(dummyMovies);
        Observer<Resource<PagedList<Content>>> observer = mock(Observer.class);
        pageViewModelMain.getContent("movie").observeForever(observer);
        verify(observer).onChanged(Resource.success(pagedList));
    }

    @Test
    public void setTVContents() {
        MutableLiveData<Resource<PagedList<Content>>> dummyTvs = new MutableLiveData<>();
        PagedList<Content> pagedList = mock(PagedList.class);
        dummyTvs.setValue(Resource.success(pagedList));
        when(repo.getAllContents("tv")).thenReturn(dummyTvs);
        Observer<Resource<PagedList<Content>>> observer = mock(Observer.class);
        pageViewModelMain.getContent("tv").observeForever(observer);
        verify(observer).onChanged(Resource.success(pagedList));
    }
}