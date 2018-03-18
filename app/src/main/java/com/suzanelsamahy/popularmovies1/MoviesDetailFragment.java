package com.suzanelsamahy.popularmovies1;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.suzanelsamahy.popularmovies1.Adapters.MovieAdapter;
import com.suzanelsamahy.popularmovies1.Adapters.ReviewAdapter;
import com.suzanelsamahy.popularmovies1.Adapters.TrailersAdapter;
import com.suzanelsamahy.popularmovies1.Classes.Movie;
import com.suzanelsamahy.popularmovies1.Classes.Review;
import com.suzanelsamahy.popularmovies1.Classes.Trailers;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MoviesDetailFragment extends Fragment {

    private final String LOG_TAG =  MoviesDetailFragment.class.getSimpleName();
    public static final String DET_MOV ="DET_MOV";
    private Movie mMovie;
    ReviewAdapter mReviewAdapter;
    TrailersAdapter mTrailersAdapter;

    private ShareActionProvider mShareActionProvider;
    private ImageView mImage;
    private TextView mTitle;
    private TextView mOverview;
    private TextView mDate;
    private TextView mVoteAvg;
    private ListView mReview;
    private ListView mTrailers;
    private Toast mToast;
    private Trailers mTrailer;
    private ImageView mImageTrailer;
    private TextView mName;
    private ArrayList<Movie> Marray = new ArrayList<>();
    MovieAdapter mMovieAdapter;



    public MoviesDetailFragment() {
    }





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        if(mMovie!=null){

            inflater.inflate(R.menu.menu_movie_detail, menu);
        final MenuItem action_favorite = menu.findItem(R.id.action_favorites);
            MenuItem action_share = menu.findItem(R.id.action_share);

        action_favorite.setIcon(Utilities.IsFavorite(getActivity(),mMovie.getID()) == 1 ?
                R.drawable.btn_star_big_on_pressed :
                R.drawable.btn_star_big_off);


            mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(action_share);

            if (mTrailer != null) {
                mShareActionProvider.setShareIntent(createShareMovieIntent());
            }


        }

    }




    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_favorites:


                if (mMovie != null) {


                    if (Utilities.IsFavorite(getContext(),mMovie.getID())==0)

                    {
                        item.setIcon(R.drawable.btn_star_big_on_pressed);
                       MoviesDbHelper.AddFavourite(getContext(), mMovie);
                        if (mToast != null) {
                            mToast.cancel();
                        }
                        mToast = Toast.makeText(getActivity(), "Added To Favorites", Toast.LENGTH_SHORT);
                        mToast.show();

                    }

                   else
                    {

                     MoviesDbHelper.deleteFavorite(getContext(), mMovie.getID());



                        item.setIcon(R.drawable.btn_star_big_off);

                        if (mToast != null) {
                            mToast.cancel();
                        }
                        mToast = Toast.makeText(getActivity(),"Removed From Favorites", Toast.LENGTH_SHORT);
                        
                        mToast.show();

                    }

                }

                return true;



            default:
                return super.onOptionsItemSelected(item);
        }





    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView=inflater.inflate(R.layout.fragment_movie_detail, container, false);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mMovie = arguments.getParcelable(MoviesDetailFragment.DET_MOV);
        }


        mImage=(ImageView) rootView.findViewById(R.id.ThumbView);
        mTitle=(TextView)rootView.findViewById(R.id.TitleView);
        mDate=(TextView)rootView.findViewById(R.id.dateView);
        mOverview=(TextView)rootView.findViewById(R.id.overView);
        mVoteAvg=(TextView)rootView.findViewById(R.id.avgView);
        mImageTrailer=(ImageView) rootView.findViewById(R.id.trailer_image);
        mName=(TextView)rootView.findViewById(R.id.trailer_name);


        if(mMovie!=null){
        String URL = "http://image.tmdb.org/t/p/w185" ;
        Picasso.with(getActivity())
                .load(URL+mMovie.getPoster())
                .into(mImage);


        mTitle.setText(mMovie.getTitle());
        mOverview.setText(mMovie.getOverview());

        String Movie_date = mMovie.getReleaseDate();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            String date = DateUtils.formatDateTime(getActivity(),
             format.parse(Movie_date).getTime(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR);
            mDate.setText(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        mVoteAvg.setText(Double.toString(mMovie.getVoteAverage()));}


        ArrayList<Review> R_Initial;
        ArrayList<Trailers> T_Initial ;
        R_Initial= new ArrayList<>();
        mReviewAdapter = new ReviewAdapter(getActivity().getApplicationContext(),R.layout.list_item_review,R_Initial);
        mReview=(ListView) rootView.findViewById(R.id.RevList);
        mReview.setAdapter(mReviewAdapter);

        T_Initial= new ArrayList<>();
        mTrailersAdapter=new TrailersAdapter(getActivity().getApplicationContext(),R.layout.list_item_trailer,T_Initial);
        mTrailers=(ListView) rootView.findViewById(R.id.TrailList);
        mTrailers.setAdapter(mTrailersAdapter);

        mTrailers.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Trailers trailer = (Trailers) mTrailersAdapter.getItem(i);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://www.youtube.com/watch?v=" + trailer.getKey()));
                startActivity(intent);
            }
        });






        return rootView;


    }








    private void updateMovie() {
        FetchMoviesReview ReviewTask = new FetchMoviesReview();
        ReviewTask.execute(Integer.toString(mMovie.getID()));

        FetchMoviesTrailer TrailerTask= new FetchMoviesTrailer();
        TrailerTask.execute(Integer.toString(mMovie.getID()));

    }


    @Override
    public void onStart() {
        super.onStart();
        if(mMovie!=null)
        { updateMovie();}
    }

    private Intent createShareMovieIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mMovie.getTitle() + " " +
                "http://www.youtube.com/watch?v=" + mTrailer.getKey());
        return shareIntent;
    }



    //-----------------------------------------------------------------------------------------------------------------------

    public class FetchMoviesReview extends AsyncTask<String, Void,Review[]> {



        private Review[] getMovieDataFromJson(String ReviewJsonStr)
                throws JSONException {
            // These are the names of the JSON objects that need to be extracted from movie DB
            final String OMD_Results = "results";
            final String OMD_ID = "id";
            final String OMD_Author ="author";
            final String OMD_Content ="content";


            JSONObject ReviewJson = new JSONObject(ReviewJsonStr);
            JSONArray ReviewArray = ReviewJson.getJSONArray(OMD_Results);


            Review[] resultStr = new Review[ReviewArray.length()];


            for (int i = 0; i < ReviewArray.length(); i++) {
                String id;
                String Author;
                String Content;


                JSONObject Reviews =ReviewArray.getJSONObject(i);


                // get every attribute of result array in jason formatter


                id = Reviews.getString(OMD_ID);
                Author=Reviews.getString(OMD_Author);
                Content=Reviews.getString(OMD_Content);

                resultStr[i] = new Review();
                resultStr[i].setRevID(id);
                resultStr[i].setAuthor(Author);
                resultStr[i].setContent(Content);


            }



            return resultStr;
        }






        @Override

        protected Review[] doInBackground(String... params) {



            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String ReviewJsonStr = null;

            String AppID=getResources().getString(R.string.apiKey);
            final String API_PARAM = "api_key";



            try {
                final String Movie_BASE_URL =
                        "http://api.themoviedb.org/3/movie/" + params[0] + "/reviews";


                Uri builtUri = Uri.parse(Movie_BASE_URL).buildUpon()
                        .appendQueryParameter(API_PARAM ,AppID)
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
                ReviewJsonStr = buffer.toString();



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
                return getMovieDataFromJson(ReviewJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }


            return null;

        }


        protected void onPostExecute( Review[] result) {



        if (result != null) {


                mReviewAdapter.clear();
                for(Review MovieStr : result) {
                    mReviewAdapter.add(MovieStr);
                }

            }






        }



    }







    public class FetchMoviesTrailer extends AsyncTask<String, Void,Trailers[]> {



        private Trailers[] getMovieDataFromJson(String TrailerJsonStr)
                throws JSONException {

            final String OMD_Results = "results";
            final String OMD_ID = "id";
            final String OMD_Name ="name";
            final String OMD_Key ="key";
            final String OMD_Site ="site";
            final String OMD_Type ="type";




            JSONObject TrailerJson = new JSONObject(TrailerJsonStr);

            JSONArray TrailerArray = TrailerJson.getJSONArray(OMD_Results);


            Trailers[] resultStr = new Trailers[TrailerArray.length()];


            for (int i = 0; i < TrailerArray.length(); i++) {
                String id;
                String name;
                String key;
                String site;
                String type;

                JSONObject Trailers =TrailerArray.getJSONObject(i);

                site=Trailers.getString(OMD_Site);
                if(site.contentEquals("YouTube")) {

                id = Trailers.getString(OMD_ID);
                key=Trailers.getString(OMD_Key);
                name=Trailers.getString(OMD_Name);
                type=Trailers.getString(OMD_Type);


                resultStr[i] = new Trailers();
                resultStr[i].setTrailerID(id);
                resultStr[i].setKey(key);
                resultStr[i].setName(name);
                resultStr[i].setType(type);
                resultStr[i].setSite(site);



            }}


            for (Trailers s : resultStr) {

            }


            return resultStr;
        }






        @Override

        protected Trailers[] doInBackground(String... params) {



            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
//********************************************************************
            String TrailerJsonStr = null;

            String AppID=getResources().getString(R.string.apiKey);
            final String API_PARAM = "api_key";



            try {
                final String Movie_BASE_URL =
                        "http://api.themoviedb.org/3/movie/" + params[0] + "/videos";


                Uri builtUri = Uri.parse(Movie_BASE_URL).buildUpon()
                        .appendQueryParameter(API_PARAM ,AppID)
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
                TrailerJsonStr = buffer.toString();



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
                return getMovieDataFromJson(TrailerJsonStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }





            return null;




        }


        protected void onPostExecute( Trailers[] result) {



            if (result != null) {

                mTrailersAdapter.clear();
                for(Trailers MovieStr : result) {
                    mTrailersAdapter.add(MovieStr);
                }
                // New data is back from the server.  Hooray!
            }
            if (result.length > 0){
            mTrailer =result[0];
            if (mShareActionProvider != null) {
                mShareActionProvider.setShareIntent(createShareMovieIntent());
            }}


        }



    }




















}
