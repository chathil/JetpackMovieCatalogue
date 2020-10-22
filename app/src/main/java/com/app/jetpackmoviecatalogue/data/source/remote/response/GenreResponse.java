package com.app.jetpackmoviecatalogue.data.source.remote.response;

import android.os.Parcel;
import android.os.Parcelable;

public class GenreResponse implements Parcelable {
    private int id;
    private String name;

    public GenreResponse(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
    }

    protected GenreResponse(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
    }

    public static final Parcelable.Creator<GenreResponse> CREATOR = new Parcelable.Creator<GenreResponse>() {
        @Override
        public GenreResponse createFromParcel(Parcel source) {
            return new GenreResponse(source);
        }

        @Override
        public GenreResponse[] newArray(int size) {
            return new GenreResponse[size];
        }
    };
}
