package com.suzanelsamahy.popularmovies1.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.suzanelsamahy.popularmovies1.classes.Review;
import com.suzanelsamahy.popularmovies1.R;

import java.util.ArrayList;

public class ReviewAdapter extends ArrayAdapter {

    private Context Rcontext;
    ArrayList<Review> R_array;
    int resources;




    public ReviewAdapter(Context context, int resource, ArrayList objects) {
        super(context,resource, objects);
        this.Rcontext = context;
        this.R_array= objects;
        this.resources=resource;

    }







    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        Holder hold ;

        if (v == null) {


            v = LayoutInflater.from(getContext()).inflate(resources, parent, false);
            hold = new Holder();

            hold.AuthorItem = (TextView) v.findViewById(R.id.AuthView);
            hold.ContentItem = (TextView) v.findViewById(R.id.ContentView);
            v.setTag(hold);


        } else {

            hold = (Holder) v.getTag();
        }


        Review item = R_array.get(position);
        hold.AuthorItem.setText(item.getAuthor());
      hold.ContentItem.setText(item.getContent());


        return  v;

    }


    public static class Holder
    { TextView AuthorItem;
        TextView ContentItem;
    }



    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return R_array.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return R_array.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }








}
