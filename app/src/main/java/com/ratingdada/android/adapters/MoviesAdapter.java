package com.ratingdada.android.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ratingdada.android.R;
import com.ratingdada.android.objects.MoviesDo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Lavakush on 11-01-2016.
 */
public class MoviesAdapter extends BaseAdapter {
     ArrayList<MoviesDo> vectForm;
     Context ctx;
     LayoutInflater inflator;

    @SuppressWarnings("unchecked")
    public MoviesAdapter(ArrayList<MoviesDo> vectForm, Context ctx) //, LayoutInflater inflator
    {
        Log.e("came to adapter","");
        this.vectForm = (ArrayList<MoviesDo>) vectForm.clone();
        this.ctx = ctx;
        inflator = LayoutInflater.from(this.ctx);
        //this.inflator = inflator;

    }

    @Override
    public int getCount() {
        if (vectForm != null)
            return vectForm.size();
        return 0;
    }

    @Override
    public Object getItem(int arg0) {
        return arg0;
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //try {
            View itemView = convertView;
            if (itemView == null) {
                itemView = inflator.inflate(R.layout.listmovies, parent, false);
            }
             final MoviesDo objForm = vectForm.get(position);
            itemView.setTag(objForm);
            ImageView movieicon = (ImageView) itemView.findViewById(R.id.movieIcon);
            Picasso.with(ctx)
                .load("http://www.ratingdada.com/" + objForm.Image_thumb_dir)
                        .placeholder(R.drawable.rd_splash_bw)
                        .into(movieicon);
        Log.e("adapter->", objForm.Image_thumb_dir);
//            TextView moviename = (TextView) itemView.findViewById(R.id.MovieName);
//            moviename.setText("" + objForm.Movie_name);

//            TextView relesedate = (TextView) itemView.findViewById(R.id.RelesedDate);
//            relesedate.setText(" Relesed Date: " + objForm.Released_date);

            TextView uservotes = (TextView) itemView.findViewById(R.id.UserVotes);
             uservotes.setText("" + objForm.User_votes);

            TextView critics = (TextView) itemView.findViewById(R.id.Critics);
            critics.setText(" " + objForm.Critics);
        return itemView;
    }

    @SuppressWarnings("unchecked")
    public void refreshMenuList(ArrayList<MoviesDo> vectForm) {

        this.vectForm = (ArrayList<MoviesDo>) vectForm.clone();
        this.notifyDataSetChanged();

    }
}


