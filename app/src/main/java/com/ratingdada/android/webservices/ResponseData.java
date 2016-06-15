package com.ratingdada.android.webservices;
public class ResponseData {
		public String data;
		public String error;

		public String toString() {
			if (error == null)
				return data + " , No error";
			else
				return error;
		}
	}