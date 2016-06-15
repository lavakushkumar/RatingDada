package com.ratingdada.android.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ratingdada.android.R;
import com.ratingdada.android.objects.MDetailsDO;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Lavakush on 11-01-2016.
 */
public class SitesAdapter extends BaseAdapter {
     ArrayList<MDetailsDO> vectForm;
     Context ctx;
     LayoutInflater inflator;

    @SuppressWarnings("unchecked")
    public SitesAdapter(ArrayList<MDetailsDO> vectForm, Context ctx) //, LayoutInflater inflator

    {
        Log.e("came to adapter","");
        this.vectForm = (ArrayList<MDetailsDO>) vectForm.clone();
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
                itemView = inflator.inflate(R.layout.listsites, parent, false);
            }
             final MDetailsDO objForm = vectForm.get(position);
            itemView.setTag(objForm);
            ImageView siteIcon = (ImageView) itemView.findViewById(R.id.siteIcon);
            Picasso.with(ctx)
                .load("http://www.ratingdada.com/images/site_logo/"+objForm.Site_logo)
                .into(siteIcon);
       // Log.e("adapter->", objForm.Movie_name);
            TextView siteRating = (TextView) itemView.findViewById(R.id.siteRating);
            siteRating.setText("Rating: " + objForm.Rating);

            TextView siteReview = (TextView) itemView.findViewById(R.id.siteReview);
            siteReview.setText(objForm.Summery);

//            TextView siteName = (TextView) itemView.findViewById(R.id.siteName);
//            siteName.setText(objForm.Site_name);

        Button review = (Button) itemView.findViewById(R.id.mReadreview);

        //on click of review button open the browser and show the review.
        review.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {

//                v.setClickable(false);
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        v.setClickable(true);
//                    }
//                }, 300);
                //Log.e("onclick review",objForm.Review_link);

				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(objForm.Review_link));
                ctx.startActivity(browserIntent);

            }
        });


        return itemView;
    }
}


