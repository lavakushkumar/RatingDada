package com.ratingdada.android;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.ratingdada.android.adapters.SitesAdapter;
import com.ratingdada.android.adapters.VideosAdapter;
import com.ratingdada.android.objects.MDetailsDO;
import com.ratingdada.android.objects.RatingsDo;
import com.ratingdada.android.objects.VideosDo;
import com.ratingdada.android.utils.AllUrls;
import com.ratingdada.android.webservices.AsyncRemoteCall;
import com.ratingdada.android.webservices.RequestParams;
import com.ratingdada.android.webservices.ServiceCalls;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Lavakush on 12-01-2016.
 */

public class MDetails extends BaseActivity implements AsyncRemoteCall.OnDataListener,View.OnClickListener {
    private int movieid;
    public TextView tvPageTitle, mReleseddate, mUserrating, mCritics, mBanner, mStars, mMusic, mDirector, mProducer, mCinimatographer,mTotal_sites,mTotal_users,tvTitle;
    private LinearLayout llAdds;
    private ArrayList<MDetailsDO> arrayMDetails = new ArrayList<MDetailsDO>();
    private SitesAdapter sitesadapter = null;
    private LinearLayout listSites,castandcrew,llTotal;
    private LinearLayout Ratethis,oncastandcrew,onvideos,oncricticreview;
    private Button mReadreview;
    private ImageView mMovieIcon,Goback;
    //private WebView listvideos;
    private VideosAdapter videosadapter = null;
    private LinearLayout show_youtube;
    private String current_request="";
    SharedPreferences pref ;
    SharedPreferences.Editor editor ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.mdetails);
        movieid = getIntent().getIntExtra("Movieid", 0);
        String pageTitle = getIntent().getStringExtra("Moviename");
        Log.e("movie id->", movieid + "");

        llAdds = (LinearLayout) findViewById(R.id.LinearLayoutAdd3);
        listSites = (LinearLayout) findViewById(R.id.listSites);
        tvPageTitle = (TextView) findViewById(R.id.tvPageTitle);
        Goback = (ImageView) findViewById(R.id.Goback);
        tvPageTitle.setText(pageTitle.toUpperCase());

        mMovieIcon = (ImageView) findViewById(R.id.mMovieIcon);
        mReleseddate = (TextView) findViewById(R.id.mReleseddate);
        mUserrating = (TextView) findViewById(R.id.mUserrating);
        mCritics = (TextView) findViewById(R.id.mCritics);
        mTotal_users = (TextView) findViewById(R.id.mTotal_users);
        mTotal_sites = (TextView) findViewById(R.id.mTotal_sites);
        //listvideos = (WebView) findViewById(R.id.listvideos);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        show_youtube= (LinearLayout) findViewById(R.id.show_youtube);

        mBanner = (TextView) findViewById(R.id.mBanner);
        mStars = (TextView) findViewById(R.id.mStars);
        mMusic = (TextView) findViewById(R.id.mMusic);
        mDirector = (TextView) findViewById(R.id.mDirector);
        mProducer = (TextView) findViewById(R.id.mProducer);
        mCinimatographer = (TextView) findViewById(R.id.mCinimatographer);
        mReadreview = (Button) findViewById(R.id.mReadreview);

        castandcrew = (LinearLayout) findViewById(R.id.castandcrew);
        Ratethis = (LinearLayout) findViewById(R.id.Ratethis);
        oncastandcrew = (LinearLayout) findViewById(R.id.oncastandcrew);
        onvideos = (LinearLayout) findViewById(R.id.onvideos);
        oncricticreview = (LinearLayout) findViewById(R.id.oncricticreview);
        //llTotal = (LinearLayout) findViewById(R.id.llTotal);



        setMovieDetails(movieid);

        Ratethis.setOnClickListener(this);
        Goback.setOnClickListener(this);
        oncastandcrew.setOnClickListener(this);
        onvideos.setOnClickListener(this);
        oncricticreview.setOnClickListener(this);




        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();



        // Create the adView.
        AdView adView = new AdView(this);
        adView.setAdUnitId("ca-app-pub-1273915068637077/1149200549");
        adView.setAdSize(AdSize.BANNER);

        //Add the adView to it.
        llAdds.addView(adView);
        //Initiate a generic request.
        AdRequest adRequest = new AdRequest.Builder().build();

        //Load the adView with the ad request.
        adView.loadAd(adRequest);
    }

    private void setMovieDetails(int MovieId) {
        current_request="movie_details";
        showLoader();
        RequestParams requestParams = new RequestParams();
        requestParams.url = AllUrls.mainhost + "mdetails.php?";
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("mid", MovieId + ""));

        requestParams.data = nameValuePairs;
        requestParams.typeOfRequest = AsyncRemoteCall.POST_REQUEST_DATA;

        ServiceCalls serviceCalls = new ServiceCalls(this, "MovieDetails");
        serviceCalls.execute(requestParams);
    }

    @Override
    public void onData(Object data, String error) {
        hideLoader();
        if (error == null) {
            if (null != data) {
                if(current_request=="movie_details")
                {
                MDetailsDO myData = (MDetailsDO) data;
                arrayMDetails.addAll(myData.mdetailsdo);

                Picasso.with(this)
                        .load("http://www.ratingdada.com/" + myData.Image_dir)
                        .placeholder(R.drawable.rd_splash_bw)
                        .into(mMovieIcon);
                mReleseddate.setText("Released On: " + myData.Released_date);
                mUserrating.setText(myData.User_votes);
                mCritics.setText(myData.Avgcritc_rating);
                mTotal_sites.setText(myData.Total_sites);
                mTotal_users.setText(myData.Total_users);

                mBanner.setText(myData.Banner);
                mStars.setText(myData.Casts);
                mMusic.setText(myData.Music);
                mDirector.setText(myData.Director);
                mProducer.setText(myData.Producer);
                mCinimatographer.setText(myData.Cinematographer);

                for (int i = 0; i < arrayMDetails.size(); i++) {
                    LayoutInflater inflater = null;
                    inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View mLinearView = inflater.inflate(R.layout.listsites, null);


                    Log.e("arrayMDetails.", arrayMDetails.get(i).Site_logo);
                    ImageView siteIcon = (ImageView) mLinearView.findViewById(R.id.siteIcon);
                    Picasso.with(this)
                            .load("http://www.ratingdada.com/images/site_logo/" + arrayMDetails.get(i).Site_logo)
                            .into(siteIcon);

                    TextView siteRating = (TextView) mLinearView.findViewById(R.id.siteRating);
                    siteRating.setText(arrayMDetails.get(i).Rating + "/5");

                    TextView siteReview = (TextView) mLinearView.findViewById(R.id.siteReview);
                    siteReview.setText(arrayMDetails.get(i).Summery);

//                    TextView siteName = (TextView) mLinearView.findViewById(R.id.siteName);
//                    siteName.setText(arrayMDetails.get(i).Site_name);

                    Button review = (Button) mLinearView.findViewById(R.id.mReadreview);

                    listSites.addView(mLinearView);
                    final String sitereviewlink = arrayMDetails.get(i).Review_link;
                    //on click of review button open the browser and show the review.
                    review.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(final View v) {

                            v.setClickable(false);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    v.setClickable(true);
                                }
                            }, 300);
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(sitereviewlink));
                            startActivity(browserIntent);

                        }
                    });


                }

                // WebView w= (WebView) findViewById(R.id.listvideos);
//                WebSettings webSettings = listvideos.getSettings();
//                webSettings.setJavaScriptEnabled(true);
//                listvideos.setWebViewClient(new wViewClient());
//                listvideos.loadUrl("http://www.ratingdada.com/app/youtube.php?trailers=" + myData.Trailers);
                    String s = myData.Trailers;
                    String items[] = s.split(",");
//                    ArrayList<String> items = new ArrayList(Arrays.asList(s.split("\\s*,\\s*")));
//                    videosadapter = new VideosAdapter(items,this);  //array list contextm inflater
//                    show_youtube.setAdapter(videosadapter);



                    for (int i = 0; i < items.length; i++) {
                        LayoutInflater inflater = null;
                        inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View mLinearView = inflater.inflate(R.layout.listvideos, null);
                        final String id = items[i];

                        Log.e("arrayMDetails.", items[i]);
                        final String st= "http://img.youtube.com/vi/" + id +"/mqdefault.jpg";
                        System.out.println("st-->" + st);
                        final ImageView videoicon = (ImageView) mLinearView.findViewById(R.id.videoicon);
                        Picasso.with(this)
                                .load(st)
                                .placeholder(R.drawable.rd_splash_bw)
                                .into(videoicon);


                        show_youtube.addView(mLinearView);

                        //on click of review button open the browser and show the review.
                        videoicon.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(final View v) {

                                v.setClickable(false);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        v.setClickable(true);
                                    }
                                }, 300);
                                try{
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
                                    startActivity(intent);
                                }catch (ActivityNotFoundException ex){
                                    Intent intent=new Intent(Intent.ACTION_VIEW,
                                            Uri.parse("http://www.youtube.com/watch?v="+id));
                                    startActivity(intent);
                                }

                            }
                        });


                    }



            } //if ofcurrent request
                else
                {
                    RatingsDo md = new RatingsDo();
                    md.ratingsdo = (ArrayList<RatingsDo>) data;
                    mTotal_users.setText(md.ratingsdo.get(0).total_users + " Votings");
                    mUserrating.setText(md.ratingsdo.get(0).user_votes);

                    editor.putString("" + movieid, "" + movieid);
                    editor.commit();

                    Toast.makeText(this, "Thank you for voting!!",Toast.LENGTH_LONG).show();
                    System.out.println("rating request"+md.ratingsdo.get(0).status);
                    System.out.println("rating request"+md.ratingsdo.get(0).user_votes);
                    System.out.println("rating request"+md.ratingsdo.get(0).total_users);
                }

        }


        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Ratethis:

                String id= pref.getString(""+movieid, null);
                if(id!=null) {
                    Toast.makeText(this, "You have already rated for this movie", Toast.LENGTH_SHORT).show();
                }else {
                    itemRatingPopup();
                }
                break;
            case R.id.Goback:
//                listvideos.onPause();
//                listvideos.pauseTimers();
                finish();
                break;
            case R.id.oncastandcrew:
                tvTitle.setText("CAST AND CREW");
//                listvideos.onPause();
                listSites.setVisibility(View.GONE);
                show_youtube.setVisibility(View.GONE);
                castandcrew.setVisibility(View.VISIBLE);

                oncastandcrew.setBackgroundResource(R.drawable.grdbutton_inverted);
                onvideos.setBackgroundResource(R.drawable.grdbutton);
                oncricticreview.setBackgroundResource(R.drawable.grdbutton);

                break;
            case R.id.onvideos:
                tvTitle.setText("VIDEOS");
//                listvideos.onResume();
                listSites.setVisibility(View.GONE);
                castandcrew.setVisibility(View.GONE);
                show_youtube.setVisibility(View.VISIBLE);

                oncastandcrew.setBackgroundResource(R.drawable.grdbutton);
                onvideos.setBackgroundResource(R.drawable.grdbutton_inverted);
                oncricticreview.setBackgroundResource(R.drawable.grdbutton);
                break;
            case R.id.oncricticreview:
                tvTitle.setText("ALL CRITICS REVIEWS");
//                listvideos.onPause();
                castandcrew.setVisibility(View.GONE);
                show_youtube.setVisibility(View.GONE);
                listSites.setVisibility(View.VISIBLE);

                oncastandcrew.setBackgroundResource(R.drawable.grdbutton);
                onvideos.setBackgroundResource(R.drawable.grdbutton);
                oncricticreview.setBackgroundResource(R.drawable.grdbutton_inverted);
                break;

        }

    }

    public void itemRatingPopup() {
        final AlertDialog.Builder popDialog = new AlertDialog.Builder(this);
        final TextView tv = new TextView(this);
        tv.setText("");

        final RatingBar rating = new RatingBar(this);
        rating.setNumStars(5);
        rating.setStepSize(0.1f);
        //rating.setFocusable(false);
        //rating.setOnClickListener(this);
        rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                tv.setText(String.valueOf(rating)+"/5");
            }
        });

        rating.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        LinearLayout parent = new LinearLayout(this);
        parent.setGravity(Gravity.CENTER);
        parent.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        parent.addView(rating);
        parent.addView(tv);


        // popDialog.setIcon(android.R.drawable.btn_star_big_on);
        //popDialog.setTitle(this.getResources().getString(R.string.user_ratings));
        popDialog.setView(parent);


        // Button OK
        popDialog.setPositiveButton("Vote",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        if (rating.getRating()!=0.0) {

                            setrating(rating.getRating());
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Please select your rating!!", Toast.LENGTH_LONG).show();
                            itemRatingPopup();
                        }
                        dialog.dismiss();

                    }
                }).setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        popDialog.create();
        popDialog.show();




    }

    public void setrating(float userrating) {


        current_request="user_rating";
        showLoader();
        RequestParams requestParams = new RequestParams();
        requestParams.url = AllUrls.mainhost + "user_rating.php?";
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("Movie_id", movieid + ""));
        nameValuePairs.add(new BasicNameValuePair("rate", userrating + ""));

        requestParams.data = nameValuePairs;
        requestParams.typeOfRequest = AsyncRemoteCall.POST_REQUEST_DATA;

        ServiceCalls serviceCalls = new ServiceCalls(this, "user_rating");
        serviceCalls.execute(requestParams);

        System.out.println(movieid + userrating);
    }

//    private class wViewClient extends WebViewClient {
//        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            view.loadUrl(url);
//            return true;
//        }
//    }

//    @Override
//    public void onPause() {
//        super.onPause();
//        listvideos.onPause();
//        listvideos.pauseTimers();
//    }
//    @Override
//    public void onResume()
//    {
//        super.onResume();
//        listvideos.onResume();
//        listvideos.resumeTimers();
//    }
//
//    @Override
//    protected void onDestroy()
//    {
//        super.onDestroy();
//
//        listvideos.stopLoading();
//        listvideos.loadData("", "text/html", "utf-8");
//        listvideos.destroy();
//    }



}


