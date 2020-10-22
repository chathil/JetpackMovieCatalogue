package com.app.jetpackmoviecatalogue.data.source.remote;

import android.util.Log;
import android.util.SparseArray;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.app.jetpackmoviecatalogue.BuildConfig;
import com.app.jetpackmoviecatalogue.application.MyApplication;
import com.app.jetpackmoviecatalogue.data.source.remote.response.ContentResponse;
import com.app.jetpackmoviecatalogue.data.source.remote.response.CrewResponse;
import com.app.jetpackmoviecatalogue.data.source.remote.response.GenreResponse;
import com.app.jetpackmoviecatalogue.utils.EspressoIdlingResource;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class RemoteRepository {
    private static RemoteRepository INSTANCE;
    private FirebaseDatabase db;
    private static final String TAG = "RemoteRepository";
    private final String API_KEY = BuildConfig.API_KEY;
    private final String MOVIE_TYPE = "movie";
    private final String TV_TYPE = "tv";

    private RemoteRepository() {
    }

    public static RemoteRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RemoteRepository();
        }
        return INSTANCE;
    }

    public LiveData<ApiResponse<List<ContentResponse>>> getAllMovies() {
        EspressoIdlingResource.increment();
        final List<ContentResponse> contents = new ArrayList<>();
        MutableLiveData<ApiResponse<List<ContentResponse>>> mutableLiveData = new MutableLiveData<>();
        db = FirebaseDatabase.getInstance();
        db.getReference("movies").child("results").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot aSnap : dataSnapshot.getChildren()) {
                    ContentResponse response = new ContentResponse(
                            aSnap.child("title").getValue(String.class),
                            aSnap.child("release_date").getValue(String.class),
                            aSnap.child("overview").getValue(String.class),
                            String.valueOf(aSnap.child("id").getValue(Long.class)),
                            aSnap.child("poster_path").getValue(String.class),
                            aSnap.child("backdrop_path").getValue(String.class),
                            aSnap.child("vote_average").getValue(Float.class), MOVIE_TYPE);
                    List<Integer> mGenreIds = new ArrayList<>();
                    DataSnapshot genreIdsSnap = aSnap.child("genre_ids");
                    for (int i = 0; i < genreIdsSnap.getChildrenCount(); i++) {
                        mGenreIds.add(genreIdsSnap.child(String.valueOf(i)).getValue(Integer.class));
                    }
                    response.setGenreIds(mGenreIds);
                    contents.add(response);
                    contentArrayGenerator(response);
                }
                mutableLiveData.setValue(ApiResponse.success(contents));
                EspressoIdlingResource.decrement();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mutableLiveData.setValue(ApiResponse.error(databaseError.getMessage(), contents));
            }
        });
        return mutableLiveData;
    }

    public LiveData<ApiResponse<List<ContentResponse>>> getAllTvs() {
        EspressoIdlingResource.increment();
        final List<ContentResponse> contents = new ArrayList<>();
        MutableLiveData<ApiResponse<List<ContentResponse>>> mutableLiveData = new MutableLiveData<>();
        db = FirebaseDatabase.getInstance();
        db.getReference("tvs").child("results").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot aSnap : dataSnapshot.getChildren()) {
                    ContentResponse response = new ContentResponse(
                            aSnap.child("name").getValue(String.class),
                            aSnap.child("first_air_date").getValue(String.class),
                            aSnap.child("overview").getValue(String.class),
                            String.valueOf(aSnap.child("id").getValue(Long.class)),
                            aSnap.child("poster_path").getValue(String.class),
                            aSnap.child("backdrop_path").getValue(String.class),
                            aSnap.child("vote_average").getValue(Float.class), TV_TYPE);
                    List<Integer> mGenreIds = new ArrayList<>();
                    DataSnapshot genreIdsSnap = aSnap.child("genre_ids");
                    for (int i = 0; i < genreIdsSnap.getChildrenCount(); i++) {
                        mGenreIds.add(genreIdsSnap.child(String.valueOf(i)).getValue(Integer.class));
                    }
                    response.setGenreIds(mGenreIds);
                    contents.add(response);

                }
                mutableLiveData.setValue(ApiResponse.success(contents));
                EspressoIdlingResource.decrement();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mutableLiveData.setValue(ApiResponse.error(databaseError.getMessage(), contents));
            }
        });
        return mutableLiveData;
    }

    public void getAllCrews(LoadCrewsCallback callback, String id, String contentType) {
        EspressoIdlingResource.increment();
        String endPoint = contentType.equals(MOVIE_TYPE) ? "https://api.themoviedb.org/3/movie/" + id + "/credits?api_key=" + API_KEY : "https://api.themoviedb.org/3/tv/" + id + "/credits?api_key=" + API_KEY + "&language=en-US";
        SparseArray<CrewResponse> crews = new SparseArray<>();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, endPoint, null, response -> {
                    try {
                        JSONArray jsonArray = response.getJSONArray("cast");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject cast = jsonArray.getJSONObject(i);
                            CrewResponse crew = new CrewResponse(Integer.parseInt(id), cast.getString("name"),
                                    cast.getString("character"),
                                    cast.getString("profile_path"));
                            crews.put(cast.getInt("id"), crew);
                        }
                        EspressoIdlingResource.decrement();
                        callback.onCrewsReceived(crews);
                    } catch (JSONException e) {
                        callback.onFailed(e);
                    }
                }, callback::onFailed);
        Volley.newRequestQueue(MyApplication.getAppContext()).add(jsonObjectRequest);
    }

    public void getAllGenres(LoadAllGenresCallback callback, String contentType) {
        EspressoIdlingResource.increment();
        String endPoint = contentType.equals(MOVIE_TYPE) ? "https://api.themoviedb.org/3/genre/movie/list?api_key=" + API_KEY + "&language=en-US" : "https://api.themoviedb.org/3/genre/tv/list?api_key=" + API_KEY + "&language=en-US";
        Log.d(TAG, "getAllGenres: " + endPoint);
        SparseArray<GenreResponse> genres = new SparseArray<>();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, endPoint, null, response -> {
                    try {
                        JSONArray jsonArray = response.getJSONArray("genres");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject genre = jsonArray.getJSONObject(i);
                            GenreResponse mGenre = new GenreResponse(genre.getInt("id"), genre.getString("name"));
                            genres.put(genre.getInt("id"), mGenre);
//                            Log.d(TAG, "getAllGenres: " + genres.get(i).getName());
//                            Log.d(TAG, "getAllGenres: " + genre.getInt("id") +genre.getString("name"));
                        }
                        for (int i = 0; i < genres.size(); i++) {
                            Log.d(TAG, "getAllGenres: " + i + " " + genres.keyAt(i) + " " + genres.valueAt(i).getName());
                        }
                        callback.onGenresLoaded(genres);
                        EspressoIdlingResource.decrement();
                    } catch (JSONException e) {
                        callback.onFailed(e);
                    }
                }, callback::onFailed);
        Volley.newRequestQueue(MyApplication.getAppContext()).add(jsonObjectRequest);
    }

    private void contentArrayGenerator(ContentResponse cr) {
        String step1 = "ContentResponse res" + cr.getId() + " = new ContentResponse(\"" + cr.getTitle() + "\", \""
                + cr.getDate() + "\", \"" + cr.getOverview() + "\", \"" + cr.getId()
                + "\", \"" + cr.getMvPoster() + "\", \"" + cr.getMvBackdrop() + "\", "
                + cr.getUserScore() + "f, " + "\" " + cr.getContentType() + "\");"
                + "List<Integer> genreIds" + cr.getId() + " = new ArrayList<>();"
                + "contents.add(res" + cr.getId() + ");";

        StringBuilder step2 = new StringBuilder();

        for (int id : cr.getGenreIds()) {
            step2.append("genreIds").append(cr.getId()).append(".add(").append(id).append(");");
        }
        String step3 = "res" + cr.getId() + ".setGenreIds(genreIds" + cr.getId() + ");";

        Log.d(TAG, "contentArrayGenerator: " + step1 + step2 + step3);

    }

    public interface LoadCrewsCallback {
        void onCrewsReceived(SparseArray<CrewResponse> crews);

        void onFailed(Exception e);
    }

    public interface LoadAllGenresCallback {
        void onGenresLoaded(SparseArray<GenreResponse> genres);

        void onFailed(Exception e);
    }
}
