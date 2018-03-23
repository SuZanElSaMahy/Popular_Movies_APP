package com.suzanelsamahy.popularmovies1.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.suzanelsamahy.popularmovies1.classes.Movie;
import com.suzanelsamahy.popularmovies1.R;

import java.util.ArrayList;


public class MovieAdapter extends ArrayAdapter {

    private Context mContext;
    ArrayList<Movie> M_array;
    int resources;

    String URL ="http://image.tmdb.org/t/p/w185/";


   public static class Holder
    {
        ImageView imageItem;

    }


    public MovieAdapter(Context context, int resource, ArrayList objects) {
        super(context,resource, objects);
        this.mContext = context;
        this.M_array= objects;
        this.resources=resource;

    }






    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v= convertView;
        Holder hold ;

        if(v == null){


            v = LayoutInflater.from(getContext()).inflate(resources, parent, false);
            hold = new Holder();

            hold.imageItem = (ImageView) v.findViewById(R.id.image_item_movies);

            v.setTag(hold);



        }

        hold = (Holder) v.getTag();



        Picasso.with(mContext)
                .load(URL+M_array.get(position).getPoster())
                .into(hold.imageItem);
        return v;
    }





    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return M_array.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return M_array.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }



}



