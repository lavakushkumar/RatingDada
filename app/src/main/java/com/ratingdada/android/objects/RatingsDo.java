package com.ratingdada.android.objects;

import java.io.Serializable;
import java.util.ArrayList;

public class RatingsDo implements Serializable{
	private static final long serialVersionUID = 1L;
	//for movies....
	public String status;
	public String user_votes;
	public String total_users;

	public ArrayList<RatingsDo> ratingsdo;
}
