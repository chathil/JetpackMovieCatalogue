package com.app.jetpackmoviecatalogue.data.source;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.paging.DataSource;
import androidx.paging.PagedList;

import com.app.jetpackmoviecatalogue.data.source.local.LocalRepository;
import com.app.jetpackmoviecatalogue.data.source.local.entity.Content;
import com.app.jetpackmoviecatalogue.data.source.remote.RemoteRepository;
import com.app.jetpackmoviecatalogue.data.source.remote.response.ContentResponse;
import com.app.jetpackmoviecatalogue.utils.FakeDataDummy;
import com.app.jetpackmoviecatalogue.utils.InstantAppExecutors;
import com.app.jetpackmoviecatalogue.utils.PagedListUtil;
import com.app.jetpackmoviecatalogue.vo.Resource;

import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AppRepositoryTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private RemoteRepository remote = mock(RemoteRepository.class);
    private LocalRepository local = mock(LocalRepository.class);
    private InstantAppExecutors instantAppExecutors = mock(InstantAppExecutors.class);
    private FakeAppRepository appRepository = new FakeAppRepository(local, remote, instantAppExecutors);

    @Test
    public void getAllMovies() {
        DataSource.Factory<Integer, Content> dummyMoviesFactory = mock(DataSource.Factory.class);
        when(local.getAllContents("movie")).thenReturn(dummyMoviesFactory);
        appRepository.getAllContents("movie");
        Resource<PagedList<Content>> result = Resource.success(PagedListUtil.mockPagedList(FakeDataDummy.generateDummyMovies()));
        verify(local).getAllContents("movie");
        assertNotNull(result.data);
        assertEquals(FakeDataDummy.generateDummyMovies().size(), result.data.size());
    }

    @Test
    public void getAllTvs() {
        DataSource.Factory<Integer, Content> dummyTvsFactory = mock(DataSource.Factory.class);
        when(local.getAllContents("tv")).thenReturn(dummyTvsFactory);
        appRepository.getAllContents("tv");
        Resource<PagedList<Content>> result = Resource.success(PagedListUtil.mockPagedList(FakeDataDummy.generateDummyTvs()));
        verify(local).getAllContents("tv");
        assertNotNull(result.data);
        assertEquals(FakeDataDummy.generateDummyTvs().size(), result.data.size());
    }
}