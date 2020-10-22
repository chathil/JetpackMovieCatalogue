package com.app.jetpackmoviecatalogue.data.source.local.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity(tableName = "content")
public class Content implements Parcelable {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "contentId")
    private String id = "";

    protected Content(Parcel in) {
        id = Objects.requireNonNull(in.readString());
        title = in.readString();
        date = in.readString();
        overview = in.readString();
        mvPoster = in.readString();
        mvBackdrop = in.readString();
        contentType = in.readString();
        userScore = in.readFloat();
        isLiked = in.readByte() != 0;
        genreIds = in.readArrayList(Integer.class.getClassLoader());
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
        dest.writeByte((byte) (isLiked ? 1 : 0));
        dest.writeList(genreIds);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Content> CREATOR = new Creator<Content>() {
        @Override
        public Content createFromParcel(Parcel in) {
            return new Content(in);
        }

        @Override
        public Content[] newArray(int size) {
            return new Content[size];
        }
    };

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setMvPoster(String mvPoster) {
        this.mvPoster = mvPoster;
    }

    public void setMvBackdrop(String mvBackdrop) {
        this.mvBackdrop = mvBackdrop;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setUserScore(float userScore) {
        this.userScore = userScore;
    }

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "overview")
    private String overview;

    @ColumnInfo(name = "mvPoster")
    private String mvPoster;

    @ColumnInfo(name = "mvBackdrop")
    private String mvBackdrop;

    @ColumnInfo(name = "contentType")
    private String contentType;

    @ColumnInfo(name = "userScore")
    private float userScore;

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    @ColumnInfo(name = "isLiked")
    private boolean isLiked;

    @ColumnInfo(name = "genreIds")
    @TypeConverters(GenreConverter.class)
    public ArrayList<Integer> genreIds = new ArrayList<>();

    @NotNull
    public String getId() {
        return id;
    }

    public void setId(@NotNull String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }


    public String getDate() {
        return date;
    }


    public String getOverview() {
        return overview;
    }


    public String getMvPoster() {
        return mvPoster;
    }


    public String getMvBackdrop() {
        return mvBackdrop;
    }


    public String getContentType() {
        return contentType;
    }


    public float getUserScore() {
        return userScore;
    }


    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(ArrayList<Integer> genreIds) {
        this.genreIds = genreIds;
    }

    public Content() {
    }

    public Content(String title, String date, String overview, String id, String mvPoster, String mvBackdrop, float userScore, String contentType) {
        this.title = title;
        this.date = date;
        this.overview = overview;
        this.id = id;
        this.contentType = contentType;
        this.mvPoster = mvPoster;
        this.mvBackdrop = mvBackdrop;
        this.userScore = userScore;
    }
}
