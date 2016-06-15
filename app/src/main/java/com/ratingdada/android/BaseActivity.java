package com.ratingdada.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;




public class BaseActivity extends Activity {

    public ProgressDialog pDialog = null;
    public static final int FLAG_UPLOAD = 1012;
    public static final int REQ_CHANGENAME = 1010;
    //fbsuccess fbsu =null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.textTheme);

        //easyTracker = EasyTracker.getInstance(BaseActivity.this);

//        if (savedInstanceState != null) {
//            pendingPublishReauthorization =
//                    savedInstanceState.getBoolean(PENDING_PUBLISH_KEY, false);
//        }

    }

    public void showAlert(final String message) {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                AlertDialog.Builder builder2 = new AlertDialog.Builder(BaseActivity.this);
                AlertDialog alert2;
                builder2.setMessage(message);
                builder2.setCancelable(false).setPositiveButton("Ok", null);
                alert2 = builder2.create();
                alert2.show();

            }
        });

    }

//    private EasyTracker easyTracker = null;
//    public void GAPost(String action,String event_name,String button_name)
//    {
//        easyTracker.send(MapBuilder.createEvent(action,event_name, button_name, null).build());
//    }

    public boolean isInternetOn() {
        final ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            return true;
        } else {
            // notify user you are not online
            return false;
        }
    }

    public void showLoader() {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                pDialog = ProgressDialog.show(BaseActivity.this, "",
                        "Loading..");
            }
        });
    }

    public void hideLoader() {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (pDialog != null && pDialog.isShowing())
                    pDialog.cancel();
            }
        });

    }




    public void hideKeyboard()
    {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    // To hide soft keypad
    public void hideKeyboard(View v) {
        InputMethodManager inputMethodManageer = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManageer.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public void showKeyboard()
    {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }



}
