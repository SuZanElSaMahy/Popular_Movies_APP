package com.suzanelsamahy.popularmovies1;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.suzanelsamahy.popularmovies1.Adapters.MovieAdapter;
import com.suzanelsamahy.popularmovies1.Classes.Movie;
import com.suzanelsamahy.popularmovies1.Data.MoviesContract;
import com.suzanelsamahy.popularmovies1.Data.MoviesDbHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MoviesFragment extends Fragment {


    MovieAdapter mMovieAdapter;
    private String SORT_Higest_Rated="vote_average.desc";
    private String SORT_Popularity = "popularity.desc";
    private String SORT_Favorite = "favorite";
    public static final String Sort ="sort_setting";
    public static final String Sort_file ="Sort_file";
    private static final String MOVIES_KEY = "movies";
    GridView gridView;
    SharedPreferences Shared_Preferences;


    private static final String[] MOVIE_COLUMNS = {
            MoviesContract.MoviesEntry._ID,
            MoviesContract.MoviesEntry.COLUMN_MOVIE_ID,
            MoviesContract.MoviesEntry.COLUMN_TITLE,
            MoviesContract.MoviesEntry.COLUMN_POSTER,
            MoviesContract.MoviesEntry.COLUMN_OVERVIEW,
            MoviesContract.MoviesEntry.COLUMN_RATE,
            MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE
    };

    private String SortBy =SORT_Popularity;
    ArrayList<Movie> M_Initial;
    private ArrayList<Movie> mMovies = null;



    public MoviesFragment() {
    }


    public interface Callback {
        void onItemSelected(Movie movie);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.sort, menu);

        MenuItem sort_by_popularity = menu.findItem(R.id.popular);
        MenuItem sort_by_rating = menu.findItem(R.id.rate);
        MenuItem sort_by_favorite = menu.findItem(R.id.favorite);



        if (SortBy.equals(SORT_Popularity)) {

            if (!sort_by_popularity.isChecked()){
                sort_by_popularity.setChecked(true);

           }

        }

         if (SortBy.equals(SORT_Higest_Rated)) {

             if (!sort_by_rating.isChecked()){
                sort_by_rating.setChecked(true);
            }

         }

       if (SortBy.equals(SORT_Favorite)) {

           if (!sort_by_favorite.isChecked()){
                sort_by_favorite.setChecked(true);
    }

        }
    }

    @Override

    public boolean onOptionsItemSelected(MenuItem item) {


        Shared_Preferences =getActivity().getSharedPreferences(
                Sort_file , Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = Shared_Preferences.edit();


        switch (item.getItemId()) {
            case R.id.popular:

                if (item.isChecked()) {
                item.setChecked(false);
            } else {
                item.setChecked(true);
            }
               SortBy=SORT_Popularity;
                updateMovie(SortBy);
                editor.putString(Sort, SortBy);
                editor.commit();
                return true;
            case R.id.rate:

                if (item.isChecked()) {
                    item.setChecked(false);
                } else {
                    item.setChecked(true);
                }
                SortBy=SORT_Higest_Rated;
                updateMovie(SortBy);
                editor.putString(Sort, SortBy);
                editor.commit();
                return true;
            case R.id.favorite:

                if (item.isChecked()) {
                    item.setChecked(false);
                } else {
                    item.setChecked(true);
                }
                SortBy=SORT_Favorite;
                updateMovie(SortBy);
                editor.putString(Sort, SortBy);
                editor.commit();


            default:
                return super.onOptionsItemSelected(item);
        }
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View rootView = inflater.inflate(R.layout.fragment_main, container, false);


        M_Initial= new ArrayList<>();

        mMovieAdapter = new MovieAdapter(getActivity().getApplicationContext(),R.layout.image_item_movie,M_Initial);

        gridView = (GridView) rootView.findViewById(R.id.movies_gridview);
        gridView.setAdapter(mMovieAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = (Movie) mMovieAdapter.getItem(position);
                ((Callback) getActivity()).onItemSelected(movie);
            }
        });


        if (savedInstanceState != null) {


            if (savedInstanceState.containsKey(Sort)) {
                SortBy = savedInstanceState.getString(Sort);
            }

            if (savedInstanceState.containsKey(MOVIES_KEY)) {
                mMovies = savedInstanceState.getParcelableArrayList(MOVIES_KEY);
            //    mMovieAdapter.setData(mMovies);

                for (Movie movie : mMovies) {
                    mMovieAdapter.add(movie);
                }
            } else {
                updateMovie(SortBy);
            }


        }

        else {
            updateMovie(SortBy);
        }


        return rootView;



    }



    private void updateMovie( String param ) {

        mMovieAdapter.clear();
        if (!param.contentEquals(SORT_Favorite)) {
            new FetchMoviessTask().execute(param);
        } else {
            new FetchFavoriteTask(getActivity()).execute();
        }
    }




    public void onSaveInstanceState(Bundle outState) {
        if (!SortBy.contentEquals(SORT_Popularity)) {
            outState.putString(Sort, SortBy);
        }
        if (mMovies != null) {
            outState.putParcelableArrayList(MOVIES_KEY, mMovies);
        }
        super.onSaveInstanceState(outState);
    }







    public class FetchMoviessTask extends AsyncTask<String, Void,Movie[]> {



        private Movie[] getMovieDataFromJson(String MovieJsonStr)
                throws JSONException {

            final String OMD_Results = "results";
            final String OMD_Poster = "poster_path";
            final String OMD_Title = "title";
            final String OMD_Overview = "overview";
            final String OMD_ReleaseDate = "release_date";
            final String OMD_VoteAverage = "vote_average";
            final String OMD_ID = "id";


            JSONObject MovieJson = new JSONObject(MovieJsonStr);
            JSONArray MovieArray = MovieJson.getJSONArray(OMD_Results);


            Movie[] resultStr = new Movie[MovieArray.length()];


            for (int i = 0; i < MovieArray.length(); i++) {

                String poster;
                String title;
                String overView;
                String release_date;
                Double votes_average;
                int id;


                JSONObject Movies = MovieArray.getJSONObject(i);




                poster = Movies.getString(OMD_Poster);
                title = Movies.getString(OMD_Title);
                overView = Movies.getString(OMD_Overview);
                release_date = Movies.getString(OMD_ReleaseDate);
                votes_average = Movies.getDouble(OMD_VoteAverage);
                id = Movies.getInt(OMD_ID);

                resultStr[i] = new Movie();
                resultStr[i].setOverview(overView);
                resultStr[i].setTitle(title);
                resultStr[i].setPoster(poster);
                resultStr[i].setReleaseDate(release_date);
                resultStr[i].setVoteAverage(votes_average);
                resultStr[i].setID(id);


            }



            return resultStr;
        }






        @Override

        protected Movie[] doInBackground(String... params) {


            if (params.length == 0) {
                return null;
            }


            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String MovieJsonStr = null;

            String AppID=getResources().getString(R.string.apiKey);


            try {
                final String Movie_BASE_URL =
                        "http://api.themoviedb.org/3/discover/movie?";

                final String API_PARAM = "api_key";
                final String SORT_PARAM = "sort_by";

                Uri builtUri = Uri.parse(Movie_BASE_URL).buildUpon()
                        .appendQueryParameter(SORT_PARAM, params[0])
                        .appendQueryParameter(API_PARAM, AppID)
                        .build();

                URL url = new URL(builtUri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();



                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {

                    return null;
                }
                MovieJsonStr = buffer.toString();



            } catch (IOException e) {
                return null;

            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                    }
                }
            }




            try {
                return getMovieDataFromJson(MovieJsonStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;




        }


        protected void onPostExecute( Movie[] result) {



            if (result != null) {
                mMovieAdapter.clear();
                for(Movie MovieStr : result) {
                    mMovieAdapter.add(MovieStr);
                }

            }
            if(mMovies!=null){
                Movie movie = (Movie) mMovieAdapter.getItem(0);
                ((Callback) getActivity()).onItemSelected(movie);
            }


        }



    }


    public class FetchFavoriteTask extends AsyncTask<Void, Void,Movie[]>{

        private Context mContext;
        public FetchFavoriteTask(Context context) {
            mContext = context;
        }



        private Movie[] getFavoriteMoviesFromDB(Cursor cursor) {


            MoviesDbHelper Db = new MoviesDbHelper(mContext);
            Movie[] resultStr = Db.ShowFavourites(cursor);
            return resultStr;
        }

        @Override
        protected Movie[] doInBackground(Void... params) {
            Cursor cursor = mContext.getContentResolver().query(
                    MoviesContract.MoviesEntry.CONTENT_URI,
                    MOVIE_COLUMNS,
                    null,
                    null,
                    null
            );

            return getFavoriteMoviesFromDB(cursor);
        }



        protected void onPostExecute( Movie[] result) {





            if (result != null) {
                mMovieAdapter.clear();
                for(Movie MovieStr : result) {
                    mMovieAdapter.add(MovieStr);
                }


            }

            if(mMovies!=null){
                if (result.length > 0){
                    Movie movie = (Movie) mMovieAdapter.getItem(0);
                    ((Callback) getActivity()).onItemSelected(movie);}
            }

    }

    }

}
