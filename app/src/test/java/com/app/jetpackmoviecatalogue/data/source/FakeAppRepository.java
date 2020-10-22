package com.app.jetpackmoviecatalogue.data.source;

import android.util.Log;
import android.util.SparseArray;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.app.jetpackmoviecatalogue.data.source.local.LocalRepository;
import com.app.jetpackmoviecatalogue.data.source.local.entity.Content;
import com.app.jetpackmoviecatalogue.data.source.local.entity.Crew;
import com.app.jetpackmoviecatalogue.data.source.local.entity.Genre;
import com.app.jetpackmoviecatalogue.data.source.remote.ApiResponse;
import com.app.jetpackmoviecatalogue.data.source.remote.RemoteRepository;
import com.app.jetpackmoviecatalogue.data.source.remote.response.ContentResponse;
import com.app.jetpackmoviecatalogue.data.source.remote.response.CrewResponse;
import com.app.jetpackmoviecatalogue.data.source.remote.response.GenreResponse;
import com.app.jetpackmoviecatalogue.utils.AppExecutors;
import com.app.jetpackmoviecatalogue.vo.Resource;

import java.util.ArrayList;
import java.util.List;

public class FakeAppRepository implements AppDataSource {
    private volatile static FakeAppRepository INSTANCE = null;
    private final RemoteRepository remoteRepository;
    private final String MOVIE_TYPE = "movie";
    private final String TV_TYPE = "tv";
    private static final String TAG = "AppRepository";
    private final LocalRepository localRepository;
    private final AppExecutors appExecutors;

    public FakeAppRepository(@NonNull LocalRepository localRepository, @NonNull RemoteRepository remoteRepository, AppExecutors appExecutors) {
        this.localRepository = localRepository;
        this.remoteRepository = remoteRepository;
        this.appExecutors = appExecutors;
    }

    public static FakeAppRepository getInstance(LocalRepository localRepository, RemoteRepository remoteData, AppExecutors appExecutors) {
        if (INSTANCE == null) {
            synchronized (FakeAppRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new FakeAppRepository(localRepository, remoteData, appExecutors);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public LiveData<Resource<PagedList<Content>>> getAllContents(String contentType) {
        if (contentType.equals(MOVIE_TYPE)) {
            return new NetworkBoundResource<PagedList<Content>, List<ContentResponse>>(appExecutors) {

                @Override
                protected LiveData<PagedList<Content>> loadFromDB() {
                    return new LivePagedListBuilder<>(localRepository.getAllContents(contentType), 20).build();
                }

                @Override
                protected Boolean shouldFetch(PagedList<Content> data) {
                    return (data == null) || (data.size() == 0);
                }

                @Override
                protected LiveData<ApiResponse<List<ContentResponse>>> createCall() {
                    return remoteRepository.getAllMovies();
                }

                @Override
                protected void saveCallResult(List<ContentResponse> data) {
                    localRepository.cacheToDb(responseToContent(data));
                }
            }.asLiveData();
        } else {
            return new NetworkBoundResource<PagedList<Content>, List<ContentResponse>>(appExecutors) {

                @Override
                protected LiveData<PagedList<Content>> loadFromDB() {
                    return new LivePagedListBuilder<>(localRepository.getAllContents(contentType), 20).build();
                }

                @Override
                protected Boolean shouldFetch(PagedList<Content> data) {
                    return (data == null) || (data.size() == 0);
                }

                @Override
                protected LiveData<ApiResponse<List<ContentResponse>>> createCall() {
                    return remoteRepository.getAllTvs();
                }

                @Override
                protected void saveCallResult(List<ContentResponse> data) {
                    Log.d(TAG, "saveCallResult: " + data.size());
                    localRepository.cacheToDb(responseToContent(data));
                }
            }.asLiveData();
        }
    }

    @Override
    public LiveData<Resource<PagedList<Content>>> getFavorites(String contentType, boolean isLikes) {
        if (contentType.equals(MOVIE_TYPE)) {
            return new NetworkBoundResource<PagedList<Content>, List<ContentResponse>>(appExecutors) {

                @Override
                protected LiveData<PagedList<Content>> loadFromDB() {
                    return new LivePagedListBuilder<>(localRepository.getFavorites(contentType), 20).build();
                }

                @Override
                protected Boolean shouldFetch(PagedList<Content> data) {
                    return false;
                }

                @Override
                protected LiveData<ApiResponse<List<ContentResponse>>> createCall() {
                    return null;
                }

                @Override
                protected void saveCallResult(List<ContentResponse> data) {

                }
            }.asLiveData();
        } else {
            return new NetworkBoundResource<PagedList<Content>, List<ContentResponse>>(appExecutors) {

                @Override
                protected LiveData<PagedList<Content>> loadFromDB() {
                    return new LivePagedListBuilder<>(localRepository.getFavorites(contentType), 20).build();
                }

                @Override
                protected Boolean shouldFetch(PagedList<Content> data) {
                    return false;
                }

                @Override
                protected LiveData<ApiResponse<List<ContentResponse>>> createCall() {
                    return null;
                }

                @Override
                protected void saveCallResult(List<ContentResponse> data) {
                }
            }.asLiveData();
        }
    }


    @Override
    public LiveData<SparseArray<Crew>> getAllCrews(String id, String contentType) {
        MutableLiveData<SparseArray<Crew>> mCrews = new MutableLiveData<>();
        remoteRepository.getAllCrews(new RemoteRepository.LoadCrewsCallback() {
            @Override
            public void onCrewsReceived(SparseArray<CrewResponse> crews) {
                mCrews.setValue(responseToCrew(crews));
            }

            @Override
            public void onFailed(Exception e) {

            }
        }, id, contentType);
        return mCrews;
    }

    @Override
    public LiveData<SparseArray<Genre>> getAllGenres(String contentType) {
        MutableLiveData<SparseArray<Genre>> mGenres = new MutableLiveData<>();
        remoteRepository.getAllGenres(new RemoteRepository.LoadAllGenresCallback() {
            @Override
            public void onGenresLoaded(SparseArray<GenreResponse> genres) {
                mGenres.setValue(responseToGenre(genres));
            }

            @Override
            public void onFailed(Exception e) {

            }
        }, contentType);
        return mGenres;
    }

    @Override
    public void setLike(Content content, boolean isLike) {
        Runnable runnable = () -> localRepository.setLike(content, isLike);
        appExecutors.diskIO().execute(runnable);
    }

    private List<Content> responseToContent(List<ContentResponse> responses) {
        List<Content> contentList = new ArrayList<>();
        for (ContentResponse cr : responses) {
            Content content = new Content(cr.getTitle(),
                    cr.getDate(),
                    cr.getOverview(),
                    cr.getId(),
                    cr.getMvPoster(),
                    cr.getMvBackdrop(),
                    cr.getUserScore() * 10,
                    cr.getContentType());
            content.setGenreIds((ArrayList<Integer>) cr.getGenreIds());
            contentList.add(content);
        }
        return contentList;
    }

    private SparseArray<Crew> responseToCrew(SparseArray<CrewResponse> responses) {
        SparseArray<Crew> crewSparseArray = new SparseArray<>();
        for (int i = 0; i < responses.size(); i++) {
            Crew crew = new Crew(responses.valueAt(i).getId(), responses.valueAt(i).getName(), responses.valueAt(i).getCharacter(), responses.valueAt(i).getPhoto());
            crewSparseArray.put(responses.keyAt(i), crew);
        }
        return crewSparseArray;
    }

    private SparseArray<Genre> responseToGenre(SparseArray<GenreResponse> responses) {
        SparseArray<Genre> genreSparseArray = new SparseArray<>();
        for (int i = 0; i < responses.size(); i++) {
            Genre genre = new Genre(responses.valueAt(i).getId(), responses.valueAt(i).getName());
            genreSparseArray.put(responses.keyAt(i), genre);
        }
        return genreSparseArray;
    }
}
