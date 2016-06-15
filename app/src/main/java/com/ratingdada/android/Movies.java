package com.ratingdada.android;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AbsListView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.ratingdada.android.adapters.MoviesAdapter;
import com.ratingdada.android.objects.MoviesDo;
import com.ratingdada.android.utils.AllUrls;
import com.ratingdada.android.webservices.AsyncRemoteCall;
import com.ratingdada.android.webservices.RequestParams;
import com.ratingdada.android.webservices.ServiceCalls;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

/**
 * Created by Lavakush on 11-01-2016.
 */
public class Movies extends BaseActivity implements AsyncRemoteCall.OnDataListener,View.OnClickListener {
    private LinearLayout llMyAccount, llMyFiles;
    private ImageView Goback;
    private TextView tvAccout, tvMyFiles;
    public TextView tvPageTitle;
    public ProgressDialog pDialog = null;
    public String currnetFragment = "";
    private LinearLayout llAdds;
    private LinearLayout llTotal, llForgetPSwd;
    private View llFragment = null;
    private String path ;
    private int catid;
    private GridView listviewMovies;
    private MoviesAdapter moviesadapter = null;
    private ArrayList<MoviesDo> arrayMovies = new ArrayList<MoviesDo>();
    LayoutInflater inflater;
    private Button btnLoadmore = null;
    int Page = 0;

    boolean userScrolled = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.movies);
        catid = getIntent().getIntExtra("Catid", 0);
        String pageTitle = getIntent().getStringExtra("Title");

        llAdds = (LinearLayout) findViewById(R.id.LinearLayoutAdd3);
        listviewMovies = (GridView) findViewById(R.id.listviewMovies);
        tvPageTitle = (TextView) findViewById(R.id.tvPageTitle);
        Goback = (ImageView) findViewById(R.id.Goback);
        Goback.setOnClickListener(this);
        tvPageTitle.setText(pageTitle);


        // Create the adView.
        AdView adView = new AdView(this);
        adView.setAdUnitId("ca-app-pub-1273915068637077/1149200549"); //
        adView.setAdSize(AdSize.BANNER);

        //Add the adView to it.
        llAdds.addView(adView);
        //Initiate a generic request.
        AdRequest adRequest = new AdRequest.Builder().build();

        //Load the adView with the ad request.
        adView.loadAd(adRequest);

        moviesadapter = new MoviesAdapter(arrayMovies,this);  //array list contextm inflater
        listviewMovies.setAdapter(moviesadapter);


        listviewMovies.setOnScrollListener(new OnScrollListener() {



            @Override
            public void onScrollStateChanged(AbsListView arg0, int scrollState) {
                // If scroll state is touch scroll then set userScrolled
                // true
                if (scrollState == OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    userScrolled = true;
                    System.out.println("userScrolled = true;");
                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                // Now check if userScrolled is true and also check if
                // the item is end then update list view and set
                // userScrolled to false
                if (userScrolled && firstVisibleItem + visibleItemCount == totalItemCount) {

                    userScrolled = false;
                    System.out.println("End of the item....s");
                    Page = Page+1;

                    setMovies(catid);
                }
            }
        });

        //then populate myListItems
        setMovies(catid);

    }




    private void setMovies(int catid) {
        if(Page==0) {
            showLoader();
        }
        RequestParams requestParams = new RequestParams();
        requestParams.url = AllUrls.mainhost + "movies.php?";
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("cat",catid+""));
        nameValuePairs.add(new BasicNameValuePair("page",Page+""));

        requestParams.data = nameValuePairs;
        requestParams.typeOfRequest = AsyncRemoteCall.POST_REQUEST_DATA;

        ServiceCalls serviceCalls = new ServiceCalls(this, "movies");
        serviceCalls.execute(requestParams);
    }

    @Override
    public void onData(Object data, String error) {
        if(Page==0) {
            hideLoader();
        }

        if(error == null)
        {
            if(null != data)
            {


               // MoviesDO mymovies = new MoviesDO();
                MoviesDo mymovies = new MoviesDo();
                mymovies.moviesdo = (ArrayList<MoviesDo>)data;
                arrayMovies.addAll(mymovies.moviesdo);
                Log.e("arry list size->", arrayMovies.size() + "");






                listviewMovies.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, final View view,
                                            final int position, long arg3) {
                        final MoviesDo objForm = (MoviesDo) view.getTag();
                        startActivity(new Intent(Movies.this, MDetails.class).putExtra("Movieid", Integer.parseInt(objForm.Movie_id)).putExtra("Moviename", objForm.Movie_name));
                        overridePendingTransition(R.anim.slide_left, R.anim.slide_right);
                        //Log.e("onclickitem",objForm.Movie_id);
                    }
                });

                if(!data.toString().equals("[]")) {
                    moviesadapter.refreshMenuList(arrayMovies);
                }

                try{
                }catch (NullPointerException e) {

                }catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }


    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.Goback:
                finish();
                break;
        }
    }
}
