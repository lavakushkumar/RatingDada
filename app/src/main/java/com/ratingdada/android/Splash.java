package com.ratingdada.android;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.google.android.gcm.GCMRegistrar;
import com.ratingdada.android.utils.AllUrls;
import com.ratingdada.android.utils.Gcm;
import com.ratingdada.android.webservices.AsyncRemoteCall;
import com.ratingdada.android.webservices.RequestParams;
import com.ratingdada.android.webservices.ServiceCalls;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Lavakush on 24-12-2015.
 */
public class Splash extends Activity implements AsyncRemoteCall.OnDataListener {
    public Gcm mGcm;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash);
        //Push notification

        GCMRegistrar.checkDevice(this);// Make sure the device has the proper dependencies.

        GCMRegistrar.checkManifest(this); // Make sure the manifest was properly set - comment out this line

//				GCMRegistrar.setRegisteredOnServer(this, false);
//				GCMRegistrar.unregister(this);
//				lblMessage = (TextView) findViewById(R.id.lblMessage);

        registerReceiver(mHandleMessageReceiver, new IntentFilter(CommonUtilities.DISPLAY_MESSAGE_ACTION));

        // Get GCM registration id
        final String regId = GCMRegistrar.getRegistrationId(this);

        mGcm = new Gcm(this);
        // Check if regid already presents
        if (regId.equals("")) {
            // Registration is not present, register now with GCM
            GCMRegistrar.register(this, CommonUtilities.SENDER_ID);
        } else {

            try {
                mGcm.setGCM(regId);
            }catch (Exception e)
            {
                e.printStackTrace();
            }


            // Device is already registered on GCM
            if (GCMRegistrar.isRegisteredOnServer(this)) {
                // Skips registration.
						/*Toast.makeText(getApplicationContext(),
								"Already registered with GCM", Toast.LENGTH_LONG)
								.show();*/
            } else {
                // Try to register again, but not in the UI thread.
                // It's also necessary to cancel the thread onDestroy(),
                // hence the use of AsyncTask instead of a raw thread.
                final Context context = this;

                GCMRegistrar.setRegisteredOnServer(this, true);

                try {
                    mGcm.setGCM(regId);
                }catch (Exception e)
                {
                    e.printStackTrace();
                }

                System.out.println("else regId splash->"+regId);

						/*mRegisterTask = new AsyncTask<Void, Void, Void>() {

							@Override
							protected Void doInBackground(Void... params) {
								// Register on our server
								// On server creates a new user
								ServerUtilities.register(context, "", "", regId);
								return null;
							}

							@Override
							protected void onPostExecute(Void result) {
								mRegisterTask = null;
							}

						};
						mRegisterTask.execute(null, null, null);*/
            }
        }


        System.out.println("Appconstants.gcmId->in splash" + mGcm.getGCM());

//        RequestParams requestParams = new RequestParams();
//        requestParams.url = AllUrls.mainhost + "gcm_insert.php?";
//        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//        nameValuePairs.add(new BasicNameValuePair("gcid", mGcm.getGCM()));
//        nameValuePairs.add(new BasicNameValuePair("dtype", "android"));
//        nameValuePairs.add(new BasicNameValuePair("app_version", BuildConfig.VERSION_NAME));
//
//        requestParams.data = nameValuePairs;
//        requestParams.typeOfRequest = AsyncRemoteCall.POST_REQUEST_DATA;
//
//        ServiceCalls serviceCalls = new ServiceCalls(this, "default");
//        serviceCalls.execute(requestParams);

        Thread thread = new Thread(){

            public void run()
            {
                try{
                    sleep(2000); //milliseconds
                }

                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    startActivity(new Intent(Splash.this,DashBoard.class));
                }
            }
        }; thread.start();

        //END
    }

    // Asyntask
    AsyncTask<Void, Void, Void> mRegisterTask;

    public void onPause()
    {
        super.onPause();
        finish();
    }

    /**
     * Receiving push messages
     * */
    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String newMessage = intent.getExtras().getString(CommonUtilities.EXTRA_MESSAGE);
            // Waking up mobile if it is sleeping

            System.out.println("BroadcastReceiver in splash");
            WakeLocker.acquire(getApplicationContext());

            /**
             * Take appropriate action on this message
             * depending upon your app requirement
             * For now i am just displaying it on the screen
             * */

            // Showing received message
//			lblMessage.append(newMessage + "\n");
//			Toast.makeText(getApplicationContext(), "New Message: " + newMessage, Toast.LENGTH_LONG).show();

            // Releasing wake lock
            WakeLocker.release();
        }
    };

    @Override
    protected void onDestroy() {
        if (mRegisterTask != null) {
            mRegisterTask.cancel(true);
        }
        try {
            unregisterReceiver(mHandleMessageReceiver);
            GCMRegistrar.onDestroy(this);
        } catch (Exception e) {
            //Log.e();
            System.out.println("UnRegister Receiver Error  " + e.getMessage());
        }
        super.onDestroy();
    }

    @Override
    public void onData(Object data, String error) {


        if (error == null) {
            if(null != data)
            {
            }
        }
        startActivity(new Intent(Splash.this,DashBoard.class));
    }
}
