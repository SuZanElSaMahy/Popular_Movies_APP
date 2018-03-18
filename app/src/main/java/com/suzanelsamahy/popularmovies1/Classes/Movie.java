package com.suzanelsamahy.popularmovies1.Classes;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {


    private int id;
    private String Title;
    private String Poster;
    private String ReleaseDate;
    private Double VoteAverage;
    private String Overview;

    public Movie() {

    }


    public int getID(){
        return id;
    }


    public void setID(int Id) {
        id = Id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getPoster() {
        return Poster;
    }

    public void setPoster(String poster) {
        Poster = poster;
    }

    public String getReleaseDate() {
        return ReleaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        ReleaseDate = releaseDate;
    }

    public Double getVoteAverage() {
        return VoteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        VoteAverage = voteAverage;
    }

    public String getOverview() {
        return Overview;
    }

    public void setOverview(String overview) {
        Overview = overview;
    }


    public Movie(Parcel in ) {

        readFromParcel( in );
    }




    public static final Parcelable.Creator CREATOR = new Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            return new Movie( in );
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(id);
        dest.writeString(Title);
        dest.writeString(Poster);
        dest.writeString(ReleaseDate);
        dest.writeString(Overview);
        dest.writeDouble(VoteAverage);

    }

    private void readFromParcel(Parcel in ) {

        id = in.readInt();
        Title = in.readString();
        Poster = in.readString();
        ReleaseDate = in.readString();
        Overview =in.readString();
        VoteAverage=in.readDouble();

    }

}

