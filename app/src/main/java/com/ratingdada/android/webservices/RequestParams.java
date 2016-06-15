package com.ratingdada.android.webservices;

public class RequestParams {
	
	public Object data = "";
	public int typeOfRequest;
	public String url;
	
	public String toString() {
		return url + " , " + typeOfRequest + ", " + data.toString();
	}
}


	