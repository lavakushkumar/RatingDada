package com.ratingdada.android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;


//import com.lav.ratingdada.tracking.android.EasyTracker;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.ratingdada.android.utils.AllUrls;
import com.ratingdada.android.utils.Gcm;
import com.ratingdada.android.webservices.AsyncRemoteCall;
import com.ratingdada.android.webservices.RequestParams;
import com.ratingdada.android.webservices.ServiceCalls;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

public class DashBoard extends BaseActivity implements OnClickListener, AsyncRemoteCall.OnDataListener {

	private LinearLayout hindi, tamil, telugu, kannada, malayalam;
	public Gcm mGcm;
	//private ImageView ivUpload;
	private LinearLayout LlAdds;
//	private ListView listviewMovies;
//	private MoviesAdapter_dashboard moviesadapter = null;
//	private ArrayList<MoviesDo> arrayMovies = new ArrayList<MoviesDo>();
//	int Page = 0;

	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

//		if (Build.VERSION.SDK_INT >= 21) {
//			getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));
//			getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
//		}
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.dashboard);

		hindi = (LinearLayout) findViewById(R.id.hindi);
		tamil = (LinearLayout) findViewById(R.id.tamil);
		telugu = (LinearLayout) findViewById(R.id.telugu);
		kannada = (LinearLayout) findViewById(R.id.kannada);
		malayalam = (LinearLayout) findViewById(R.id.malayalam);

		LlAdds = (LinearLayout)findViewById(R.id.LlAdds);

		hindi.setOnClickListener(this);
		tamil.setOnClickListener(this);
		telugu.setOnClickListener(this);
		kannada.setOnClickListener(this);
		malayalam.setOnClickListener(this);

//		listviewMovies = (ListView) findViewById(R.id.listviewMovies);
//		moviesadapter = new MoviesAdapter_dashboard(arrayMovies,this);  //array list contextm inflater
//		listviewMovies.setAdapter(moviesadapter);
//		setMovies(1);

		// Create the adView.
		AdView adView = new AdView(this);
		adView.setAdUnitId("ca-app-pub-1273915068637077/1149200549"); //ca-app-pub-6271303335574399/6785315468
		adView.setAdSize(AdSize.BANNER);

		 //Add the adView to it.
		LlAdds.addView(adView);
		 //Initiate a generic request.
		AdRequest adRequest = new AdRequest.Builder().build();

		 //Load the adView with the ad request.
		adView.loadAd(adRequest);


		//for gcm insert
		if(isInternetOn()) {
			mGcm = new Gcm(this);
			System.out.println("Appconstants.gcmId->in dashbord" + mGcm.getGCM());

			RequestParams requestParams = new RequestParams();
			requestParams.url = AllUrls.mainhost + "gcm_insert.php?";
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("gcid", mGcm.getGCM()));
			nameValuePairs.add(new BasicNameValuePair("dtype", "android"));
			nameValuePairs.add(new BasicNameValuePair("app_version", BuildConfig.VERSION_NAME));

			requestParams.data = nameValuePairs;
			requestParams.typeOfRequest = AsyncRemoteCall.POST_REQUEST_DATA;

			ServiceCalls serviceCalls = new ServiceCalls(this, "default");
			serviceCalls.execute(requestParams);
		}
		
		

	}


//	private void setMovies(int catid) {
//		if(Page==0) {
//			showLoader();
//		}
//		RequestParams requestParams = new RequestParams();
//		requestParams.url = AllUrls.mainhost + "movies.php?";
//		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//		nameValuePairs.add(new BasicNameValuePair("cat",catid+""));
//		nameValuePairs.add(new BasicNameValuePair("page",Page+""));
//
//		requestParams.data = nameValuePairs;
//		requestParams.typeOfRequest = AsyncRemoteCall.POST_REQUEST_DATA;
//
//		ServiceCalls serviceCalls = new ServiceCalls(this, "movies");
//		serviceCalls.execute(requestParams);
//	}
//
//	@Override
//	public void onData(Object data, String error) {
//		if(Page==0) {
//			hideLoader();
//		}
//
//		if(error == null)
//		{
//			if(null != data)
//			{
//
//
//				// MoviesDO mymovies = new MoviesDO();
//				MoviesDo mymovies = new MoviesDo();
//				mymovies.moviesdo = (ArrayList<MoviesDo>)data;
//				arrayMovies.addAll(mymovies.moviesdo);
//				Log.e("arry list size->", arrayMovies.size() + "");
//
//
//
//
//
//
//				listviewMovies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//					@Override
//					public void onItemClick(AdapterView<?> arg0, final View view,
//											final int position, long arg3) {
//						final MoviesDo objForm = (MoviesDo) view.getTag();
//						startActivity(new Intent(DashBoard.this, MDetails.class).putExtra("Movieid", Integer.parseInt(objForm.Movie_id)).putExtra("Moviename", objForm.Movie_name));
//						overridePendingTransition(R.anim.slide_left, R.anim.slide_right);
//						//Log.e("onclickitem",objForm.Movie_id);
//					}
//				});
//
//				if(!data.toString().equals("[]")) {
//					moviesadapter.refreshMenuList(arrayMovies);
//				}
//
//				try{
//				}catch (NullPointerException e) {
//
//				}catch (Exception e) {
//					e.printStackTrace();
//				}
//
//			}
//
//		}
//
//
//	}



	
	@Override
	public void onStart() {
		super.onStart();
		//EasyTracker.getInstance(this).activityStart(this);
	}

	@Override
	public void onStop() {
		super.onStop();
		//EasyTracker.getInstance(this).activityStop(this);
	}
	

	@Override
	public void onClick(View v) {

		if(!isInternetOn())
		{
			Toast.makeText(this, "Please connect to Internet", Toast.LENGTH_LONG).show();
			return;
		}
		switch (v.getId()) {
		case R.id.hindi:
				startActivity(new Intent(DashBoard.this, Movies.class).putExtra("Catid", 1).putExtra("Title", "Hindi Movies"));
				overridePendingTransition(R.anim.slide_left, R.anim.slide_right);
			Log.e("Dashboard-->","hindi");
			break;
		case R.id.tamil:
			startActivity(new Intent(DashBoard.this, Movies.class).putExtra("Catid", 2).putExtra("Title", "Tamil Movies"));
			overridePendingTransition(R.anim.slide_left, R.anim.slide_right);
			Log.e("Dashboard-->","tamil");
			break;

		case R.id.telugu:
			startActivity(new Intent(DashBoard.this, Movies.class).putExtra("Catid", 3).putExtra("Title", "Telugu Movies"));
			overridePendingTransition(R.anim.slide_left, R.anim.slide_right);
			break;
		case R.id.kannada:
			startActivity(new Intent(DashBoard.this, Movies.class).putExtra("Catid", 4).putExtra("Title", "Kannada Movies"));
			overridePendingTransition(R.anim.slide_left, R.anim.slide_right);
			Log.e("Dashboard-->","kannada");
			break;
		case R.id. malayalam:
			startActivity(new Intent(DashBoard.this, Movies.class).putExtra("Catid", 5).putExtra("Title", "Malayalam Movies"));
			overridePendingTransition(R.anim.slide_left, R.anim.slide_right);
			Log.e("Dashboard-->","malayalam");
			break;

		default:
			break;
		}

	}

	@Override
	public void onData(Object data, String error) {


		if (error == null) {
			if(null != data)
			{
			}
		}

	}
}
