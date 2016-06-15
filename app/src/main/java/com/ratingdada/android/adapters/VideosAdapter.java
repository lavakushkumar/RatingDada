package com.ratingdada.android.adapters;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ratingdada.android.R;
import com.ratingdada.android.objects.MoviesDo;
import com.ratingdada.android.objects.VideosDo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Lavakush on 11-01-2016.
 */
public class VideosAdapter extends BaseAdapter {
     ArrayList<String> vectForm;
     Context ctx;
     LayoutInflater inflator;

    @SuppressWarnings("unchecked")
    public VideosAdapter(ArrayList<String> vectForm, Context ctx) //, LayoutInflater inflator
    {
        Log.e("came to adapter","");
        this.vectForm = (ArrayList<String>) vectForm.clone();
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
                itemView = inflator.inflate(R.layout.listvideos, parent, false);
            }
             final String objForm = vectForm.get(position);
            itemView.setTag(objForm);
            Log.e("adapterviddo->", objForm.toString());

        final String st= "http://img.youtube.com/vi/" + objForm.toString()+"/mqdefault.jpg";
        System.out.println("st-->" + st);
            ImageView movieicon = (ImageView) itemView.findViewById(R.id.videoicon);
            Picasso.with(ctx).load(st).into(movieicon);


        itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try{
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + objForm.toString()));
                    ctx.startActivity(intent);
                }catch (ActivityNotFoundException ex){
                    Intent intent=new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://www.youtube.com/watch?v="+objForm.toString()));
                    ctx.startActivity(intent);
                }
            }
        });

        return itemView;
    }


}


