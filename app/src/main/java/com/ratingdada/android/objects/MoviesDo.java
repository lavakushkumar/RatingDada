package com.ratingdada.android.objects;

import java.io.Serializable;
import java.util.ArrayList;

public class MoviesDo implements Serializable{
	private static final long serialVersionUID = 1L;
	//for movies....
	public String Movie_id;
	public String Movie_name;
	public String Image_thumb_dir;
	public String Released_date;
	public String Image_dir;
	public String User_votes;
	public String Critics;
	public ArrayList<MoviesDo> moviesdo;

}
