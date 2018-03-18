package com.suzanelsamahy.popularmovies1.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.suzanelsamahy.popularmovies1.Classes.Movie;

/**
 * Created by Dell2014 on 25/01/16.
 */
public class MoviesDbHelper extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "movie.db";

    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String query = "CREATE TABLE " + MoviesContract.MoviesEntry.TABLE_NAME + " (" +
                MoviesContract.MoviesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MoviesContract.MoviesEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_POSTER + " TEXT, " +
                MoviesContract.MoviesEntry.COLUMN_OVERVIEW + " TEXT, " +
                MoviesContract.MoviesEntry.COLUMN_RATE + " INTEGER, " +
                MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE + " TEXT);";

        db.execSQL(query);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MoviesContract.MoviesEntry.TABLE_NAME);
        onCreate(db);
    }







    public static long AddFavourite(Context context,Movie Object) {
        ContentValues Values = new ContentValues();
        Values.put(MoviesContract.MoviesEntry.COLUMN_POSTER,Object.getPoster());
        Values.put(MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE,Object.getReleaseDate());
        Values.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_ID,Object.getID());
        Values.put(MoviesContract.MoviesEntry.COLUMN_RATE,Object.getVoteAverage());
        Values.put(MoviesContract.MoviesEntry.COLUMN_OVERVIEW,Object.getOverview());
        Values.put(MoviesContract.MoviesEntry.COLUMN_TITLE, Object.getTitle());

        MoviesDbHelper dh = new MoviesDbHelper(context);
        SQLiteDatabase db =dh.getWritableDatabase();
        long MovieRowId;
        MovieRowId = db.insert(MoviesContract.MoviesEntry.TABLE_NAME, null, Values);



        return  MovieRowId;

    }




    public Movie[] ShowFavourites(Cursor cursor) {


        SQLiteDatabase db = getWritableDatabase();
         cursor = db.query(
                MoviesContract.MoviesEntry.TABLE_NAME,  // Table to Query
                null, // all columns
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );
        int numRows = cursor.getCount();

        Movie[] FavoriteStr = new Movie[numRows];


        String poster;
        String title;
        String overView;
        String release_date;
        Double votes_average;
        int id;

        cursor.moveToFirst();

        for(int i=0;i<numRows;i++){


            poster = cursor.getString(cursor.getColumnIndex("poster_path"));
            title = cursor.getString(cursor.getColumnIndex("title"));
            overView =cursor.getString(cursor.getColumnIndex("overview"));
            release_date = cursor.getString(cursor.getColumnIndex("release_date"));
            votes_average = cursor.getDouble(cursor.getColumnIndex("vote_average"));
            id = cursor.getInt(cursor.getColumnIndex("id"));



            FavoriteStr[i] = new Movie();
            FavoriteStr[i].setOverview(overView);
            FavoriteStr[i].setTitle(title);
            FavoriteStr[i].setPoster(poster);
            FavoriteStr[i].setReleaseDate(release_date);
            FavoriteStr[i].setVoteAverage(votes_average);
            FavoriteStr[i].setID(id);

            cursor.moveToNext();

        }


        cursor.close();
        return FavoriteStr;
    }




    public static int deleteFavorite(Context context, int id) {
        MoviesDbHelper dh = new MoviesDbHelper(context);
        SQLiteDatabase db = dh.getReadableDatabase();
       return  db.delete(MoviesContract.MoviesEntry.TABLE_NAME,
                 MoviesContract.MoviesEntry.COLUMN_MOVIE_ID + " = ?",
                 new String[]{Integer.toString(id)});


    }


}
