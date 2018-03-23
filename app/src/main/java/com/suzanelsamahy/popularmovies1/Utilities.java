package com.suzanelsamahy.popularmovies1;

import android.content.Context;
import android.database.Cursor;

import com.suzanelsamahy.popularmovies1.data.MoviesContract;


public class Utilities {


    public static int IsFavorite(Context context, int id) {
        Cursor cursor = context.getContentResolver().query(
                MoviesContract.MoviesEntry.CONTENT_URI,
                null,   // projection
                MoviesContract.MoviesEntry.COLUMN_MOVIE_ID + " = ?", // selection
                new String[] { Integer.toString(id) },   // selectionArgs
                null    // sort order
        );
        int numRows = cursor.getCount();
        cursor.close();
        return numRows;
    }

}
