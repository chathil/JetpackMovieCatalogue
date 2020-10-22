package com.app.jetpackmoviecatalogue.data.source.remote.response;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class ContentResponse implements Parcelable {
    private String id;
    private String title;
    private String date;
    private String overview;
    private String mvPoster;
    private String mvBackdrop;
    private String contentType;
    private float userScore;
    private List<Integer> genreIds = new ArrayList<>();


    public ContentResponse(String title, String date, String overview, String id, String mvPoster, String mvBackdrop, float userScore, String contentType) {
        this.title = title;
        this.date = date;
        this.overview = overview;
        this.id = id;
        this.contentType = contentType;
        this.mvPoster = mvPoster;
        this.mvBackdrop = mvBackdrop;
        this.userScore = userScore;
    }

    protected ContentResponse(Parcel in) {
        id = in.readString();
        title = in.readString();
        date = in.readString();
        overview = in.readString();
        mvPoster = in.readString();
        mvBackdrop = in.readString();
        contentType = in.readString();
        userScore = in.readFloat();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(date);
        dest.writeString(overview);
        dest.writeString(mvPoster);
        dest.writeString(mvBackdrop);
        dest.writeString(contentType);
        dest.writeFloat(userScore);
    }

    public static final Creator<ContentResponse> CREATOR = new Creator<ContentResponse>() {
        @Override
        public ContentResponse createFromParcel(Parcel in) {
            return new ContentResponse(in);
        }

        @Override
        public ContentResponse[] newArray(int size) {
            return new ContentResponse[size];
        }
    };

    public String getContentType() {
        return contentType;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getId() {
        return id;
    }

    public String getMvPoster() {
        return mvPoster;
    }

    public String getMvBackdrop() {
        return mvBackdrop;
    }

    public String getDate() {
        return date;
    }


    public float getUserScore() {
        return userScore;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds.addAll(genreIds);
    }
}
