package com.ratingdada.android.objects;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class MDetailsDO implements Serializable{
	public String Movie_id;
	public String Category_id;
	public String Movie_name;
	public String Released_date;
	public String Director;
	public String Producer;
	public String Music;
	public String Cinematographer;
	public String Casts;
	public String Banner;
	public String Image_dir;
	public String Image_thumb_dir;
	public String User_votes;
	public String Total_users;
	public String Trailers;
	public String Avgcritc_rating;
	public String Total_sites;

	//for sites revies..
	public String Site_id;
	public String Review_link;
	public String Rating;
	public String Total_rating;
	public String Site_order;
	public String Summery;
	public String Site_name;
	public String Site_logo;
	public String Site_link;
	public ArrayList<MDetailsDO> mdetailsdo;
	
}
