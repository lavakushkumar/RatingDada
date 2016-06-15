package com.ratingdada.android.webservices;

import android.app.Application;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

public class Global extends Application {
	
	HttpClient httpclient = new DefaultHttpClient();

}
