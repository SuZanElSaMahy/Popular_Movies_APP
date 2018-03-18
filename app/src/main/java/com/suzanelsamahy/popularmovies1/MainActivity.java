package com.suzanelsamahy.popularmovies1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import com.suzanelsamahy.popularmovies1.Classes.Movie;


public class MainActivity extends AppCompatActivity implements MoviesFragment.Callback {

    private boolean mTwoPane;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.movie_detail_container) != null) {
            mTwoPane = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, new MoviesDetailFragment(),
                                MoviesDetailFragment.DET_MOV)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    public void onItemSelected(Movie movie) {
        if (mTwoPane) {
            Bundle arguments = new Bundle();

            arguments.putParcelable(MoviesDetailFragment.DET_MOV, movie);

            MoviesDetailFragment fragment = new MoviesDetailFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment, MoviesDetailFragment.DET_MOV)
                    .commit();



        } else {
            Intent intent = new Intent(this, MoviesDetail.class)
                    .putExtra(MoviesDetailFragment.DET_MOV, movie);
            startActivity(intent);
        }
    }
}
