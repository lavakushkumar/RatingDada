package com.ratingdada.android.webservices;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class AsyncRemoteCall extends AsyncTask<RequestParams, Void, ResponseData> {
	
	public static final int GET_REQUEST = 0;
	public static final int POST_REQUEST_XML = 1;
	public static final int POST_REQUEST_DATA = 2;
	public static final int PUT_REQUEST = 3;
	public final int CONNECTION_TIMEOUT = 30*1000;
	public final int SO_TIMEOUT = 30*1000;
	OnDataListener mDataListener;
	
	public AsyncRemoteCall() {
	}

	public AsyncRemoteCall(OnDataListener context) {
		mDataListener = context;
	}

	@Override
	protected ResponseData doInBackground(RequestParams... params) {
		// TODO Auto-generated method stub
		
		RequestParams requestParams = params[0];
		Log.e("Request", requestParams.toString());
		if(requestParams.typeOfRequest == GET_REQUEST){
			return getData(requestParams.url);
		}else if (requestParams.typeOfRequest == POST_REQUEST_XML) {
			return postData((String) requestParams.data, requestParams.url);
		}else if (requestParams.typeOfRequest == POST_REQUEST_DATA) {
			Log.e("asnctak->","POST_REQUEST_DATA");
			return postRequest(requestParams.data, requestParams.url);
		}else if(requestParams.typeOfRequest == PUT_REQUEST){
			return postData((String) requestParams.data, requestParams.url);
		}

		return null;
	}
	
	

	
	public ResponseData getData(String url) {

		ResponseData response = new ResponseData();

		try {
			
			HttpGet httpGet = new HttpGet(url);
			
			HttpClient httpClient = new DefaultHttpClient();
			httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, CONNECTION_TIMEOUT);
			httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, SO_TIMEOUT);
			BasicHttpResponse httpResponse = (BasicHttpResponse) httpClient.execute(httpGet);
			
			int responseCode = httpResponse.getStatusLine().getStatusCode();
			StringBuilder data = new StringBuilder();

			if (responseCode == 200) {
				InputStream in = httpResponse.getEntity().getContent();
				InputStreamReader is = new InputStreamReader(in,"utf-8");
				
				//
//				BufferedReader br = new BufferedReader(is);
				BufferedReader br = new BufferedReader(is,8);
				String read = br.readLine();

				while (read != null) {
					// System.out.println(read);
					data.append(read);
					read = br.readLine();

				}
				response.data = data.toString();
				
			} else {
				
				response.error = Utils.SERVER_ERROR;
//				response.error = httpResponse.getStatusLine().toString();
			}

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.error = Utils.SERVER_ERROR;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.error = Utils.SERVER_ERROR;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.error = Utils.SERVER_ERROR;
		}

		return response;

	}

	public ResponseData postData(String param, String url) {

		ResponseData response = new ResponseData();

		try {
			
			HttpPost httpPost = new HttpPost(url);
			Log.e("url", url);
			StringEntity content = new StringEntity(param, HTTP.UTF_8);
			content.setContentType("text/xml");
			httpPost.setHeader("Content-Type", "application/xml");
			httpPost.setEntity(content);

			HttpClient httpClient = new DefaultHttpClient();
			httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, CONNECTION_TIMEOUT);
			httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, SO_TIMEOUT);
			BasicHttpResponse httpResponse = (BasicHttpResponse) httpClient.execute(httpPost);
			
			int responseCode = httpResponse.getStatusLine().getStatusCode();
			StringBuilder data = new StringBuilder();

			if (responseCode == 200) {
				InputStream in = httpResponse.getEntity().getContent();
				InputStreamReader is = new InputStreamReader(in);
				BufferedReader br = new BufferedReader(is);
				String read = br.readLine();

				while (read != null) {
					// System.out.println(read);
					data.append(read);
					read = br.readLine();

				}
				response.data = data.toString();
			} else {
				
				response.error = Utils.SERVER_ERROR;
//				response.error = httpResponse.getStatusLine().toString();
			}

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.error = Utils.SERVER_ERROR;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.error = Utils.SERVER_ERROR;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.error = Utils.SERVER_ERROR;
		}

		return response;
	}
	
	
	public ResponseData postRequest(Object param, String url){


		ArrayList<NameValuePair> params = (ArrayList<NameValuePair>) param;
		ResponseData response = new ResponseData();

		try {
			
			HttpPost httpPost = new HttpPost(url);
			Log.e("url", url);
			
			if(params != null)
				httpPost.setEntity(new UrlEncodedFormEntity(params));
			
			HttpClient httpClient = new DefaultHttpClient();
			//httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, CONNECTION_TIMEOUT);
			//httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, SO_TIMEOUT);
			BasicHttpResponse httpResponse = (BasicHttpResponse) httpClient.execute(httpPost);
			
			int responseCode = httpResponse.getStatusLine().getStatusCode();
			StringBuilder data = new StringBuilder();

			if (responseCode == 200) {
				Log.e("url 200", url);

				InputStream in = httpResponse.getEntity().getContent();
				InputStreamReader is = new InputStreamReader(in);
				BufferedReader br = new BufferedReader(is);
				String read = br.readLine();

				while (read != null) {
					// System.out.println(read);
					data.append(read);
					read = br.readLine();

				}
				response.data = data.toString();
//				Log.e("", "data.toString() "+data.toString());


			} else {
				
				Log.e("url error", url);

				response.error = Utils.SERVER_ERROR;
//				response.error = httpResponse.getStatusLine().toString();
			}

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.error = Utils.SERVER_ERROR;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.error = Utils.SERVER_ERROR;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.error = Utils.SERVER_ERROR;
		}

		return response;
	}

	public ResponseData putData(String param, String url) {

		ResponseData response = new ResponseData();

		try {
			HttpPut httpput = new HttpPut(url);
			Log.e("url", url);
			StringEntity content = new StringEntity(param, HTTP.UTF_8);
			content.setContentType("text/xml");
			httpput.setHeader("Content-Type", "application/xml");
			httpput.setEntity(content);

			HttpClient httpClient = new DefaultHttpClient();
			httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, CONNECTION_TIMEOUT);
			httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, SO_TIMEOUT);
			BasicHttpResponse httpResponse = (BasicHttpResponse) httpClient.execute(httpput);
			int responseCode = httpResponse.getStatusLine().getStatusCode();
			StringBuilder data = new StringBuilder();

			if (responseCode == 200) {
				InputStream in = httpResponse.getEntity().getContent();
				InputStreamReader is = new InputStreamReader(in);
				BufferedReader br = new BufferedReader(is);
				String read = br.readLine();

				while (read != null) {
					// System.out.println(read);
					data.append(read);
					read = br.readLine();

				}
				response.data = data.toString();
				
			} else {
				
				response.error = Utils.SERVER_ERROR;
//				response.error = httpResponse.getStatusLine().toString();
			}

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.error = Utils.SERVER_ERROR;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.error = Utils.SERVER_ERROR;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.error = Utils.SERVER_ERROR;
		}

		return response;
	}

	public interface OnDataListener {
		void onData(Object data, String error);
	}

}
