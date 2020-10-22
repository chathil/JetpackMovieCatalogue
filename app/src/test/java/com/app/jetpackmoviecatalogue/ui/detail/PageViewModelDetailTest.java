package com.app.jetpackmoviecatalogue.ui.detail;

import com.app.jetpackmoviecatalogue.data.source.AppRepository;
import com.app.jetpackmoviecatalogue.utils.FakeDataDummy;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class PageViewModelDetailTest {
    private PageViewModelDetail pageViewModelDetail;
    private AppRepository repo = mock(AppRepository.class);

    @Before
    public void setUp() {
        pageViewModelDetail = new PageViewModelDetail(repo);
    }

    @Test
    public void setMovieContent() {
        pageViewModelDetail.setContent(FakeDataDummy.generateDummyMovies().get(0));
        assertEquals("290859", pageViewModelDetail.getContent().getId());
    }

    @Test
    public void setTVContent() {
        pageViewModelDetail.setContent(FakeDataDummy.generateDummyTvs().get(0));
        assertEquals("1412", pageViewModelDetail.getContent().getId());
    }
}