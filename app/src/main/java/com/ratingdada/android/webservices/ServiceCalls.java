package com.ratingdada.android.webservices;

import android.util.Log;

import com.ratingdada.android.objects.MDetailsDO;
import com.ratingdada.android.objects.MoviesDo;
import com.ratingdada.android.objects.RatingsDo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ServiceCalls extends AsyncRemoteCall{
	
	private String tag;

	private ArrayList<Object> data;

	
	public ServiceCalls(OnDataListener context, String tag) {
		super(context);

		this.tag = tag;
		Log.e("sevc 1******", tag);
		data =null;
	}
	
	public ServiceCalls(OnDataListener context, String tag, String option) {
		super(context);

		this.tag = tag;
		Log.e("sevc 2******", tag);
		data =null;
	}

	
	@Override
	protected void onPostExecute(ResponseData result) {
		// TODO Auto-generated method stub
		Log.e("Resonse******", result.toString());
		
		if(result.error == null){
			
			parse(result);
			
		}else{
			
			mDataListener.onData(null, result.error);			
		}
		super.onPostExecute(result);
	}
	
	public void parse(ResponseData result){

		Log.e("came to parse...v******", tag);
		data = new ArrayList<Object>();
		
		try {

			if(tag.equalsIgnoreCase("movies")){
				data.clear();
				JSONArray mainJson = new JSONArray(result.data);
				for (int i = 0; i < mainJson.length(); i++) {
					MoviesDo movie = new MoviesDo();
					try {
						JSONObject jo = mainJson.getJSONObject(i);
						movie.Movie_id = jo.getString("Movie_id");
						movie.Movie_name = jo.getString("Movie_name");
						movie.Image_thumb_dir = jo.getString("Image_thumb_dir");
						movie.Released_date = jo.getString("Released_date");
						movie.Image_dir = jo.getString("Image_dir");
						movie.User_votes = jo.getString("User_votes");
						movie.Critics = jo.getString("Critics");

					} catch (JSONException e) {
						e.printStackTrace();
					}
					data.add(movie);
				}

				mDataListener.onData(data, null);
			}
			else if(tag.equalsIgnoreCase("MovieDetails")){
				data.clear();
				MDetailsDO mdetails = new MDetailsDO();
				mdetails.mdetailsdo = new ArrayList<MDetailsDO>();

				System.out.println(result.data);
				JSONObject mainJson;

				try{
					mainJson = new JSONObject(result.data);
				}catch(JSONException e)
				{
					mainJson = new JSONObject(result.data.substring(1));
				}

				JSONArray jArray = mainJson.getJSONArray("site_d");
				for (int i = 0; i < jArray.length(); i++) {
					MDetailsDO md = new MDetailsDO();
					JSONObject jo = jArray.getJSONObject(i);
					md.Site_id = jo.getString("Site_id");
					md.Review_link = jo.getString("Review_link");
					md.Rating = jo.getString("Rating");
					md.Total_rating = jo.getString("Total_rating");
					md.Site_order = jo.getString("Site_order");
					md.Summery = jo.getString("Summery");
					md.Site_name = jo.getString("Site_name");
					md.Site_logo = jo.getString("Site_logo");
					md.Site_link = jo.getString("Site_link");

					mdetails.mdetailsdo.add(md);
				}

				JSONObject minfo = mainJson.getJSONObject("movie_d");
				mdetails.Movie_id = minfo.getString("Movie_id");
				mdetails.Category_id = minfo.getString("Category_id");
				mdetails.Movie_name = minfo.getString("Movie_name");
				mdetails.Released_date = minfo.getString("Released_date");
				mdetails.Director = minfo.getString("Director");
				mdetails.Producer = minfo.getString("Producer");
				mdetails.Music = minfo.getString("Music");
				mdetails.Cinematographer = minfo.getString("Cinematographer");
				mdetails.Casts = minfo.getString("Casts");
				mdetails.Banner = minfo.getString("Banner");
				mdetails.Image_dir = minfo.getString("Image_dir");
				mdetails.Image_thumb_dir = minfo.getString("Image_thumb_dir");
				mdetails.User_votes = minfo.getString("User_votes");
				mdetails.Total_users = minfo.getString("Total_users");
				mdetails.Trailers = minfo.getString("Trailers");
				mdetails.Avgcritc_rating = minfo.getString("Avgcritc_rating");
				mdetails.Total_sites = minfo.getString("Total_sites");
				Log.e("mainjson0 banner->", "" + minfo.getString("Banner"));
				mDataListener.onData(mdetails, null);
			}
			else if(tag.equalsIgnoreCase("user_rating")){
				data.clear();
				System.out.println("result data in rating->"+result.data);
				JSONObject jo = new JSONObject(result.data);
				RatingsDo rat = new RatingsDo();
				rat.status = jo.getString("status");
				rat.user_votes = jo.getString("user_votes");
				rat.total_users = jo.getString("total_users");
				data.add(rat);
				mDataListener.onData(data, null);
			}
			else if(tag.equalsIgnoreCase("default"))
			{
				mDataListener.onData(result.data, null);
			}

		} catch (Exception e) {
			e.printStackTrace();
			result.error = "Unable to connect server, please try again later";
			mDataListener.onData(null, result.error);
//			mDataListener.onData(null, result.toString());
			
		}
		
//		mDataListener.onData(null, result.error);
		data = null;
		
	}
}				