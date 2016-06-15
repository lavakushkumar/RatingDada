package com.ratingdada.android;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;
import com.ratingdada.android.utils.Gcm;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

public class GCMIntentService extends GCMBaseIntentService {

    private static final String TAG = "GCMIntentService";

    public GCMIntentService() {
        super(CommonUtilities.SENDER_ID);
    }

    /**
     * Method called on device registered
     **/
    @Override
    protected void onRegistered(Context context, String registrationId) {
        Log.i(TAG, "Device registered: regId = " + registrationId);
        CommonUtilities.displayMessage(context, "Your device registred with GCM");
//        Log.d("NAME", MainActivity.name);
        // ServerUtilities.register(context, "", "", registrationId);
        GCMRegistrar.setRegisteredOnServer(context, true);
        Gcm mGcm = new Gcm(this);
        mGcm.setGCM(registrationId);
        System.out.println("registrationid gcm intentservice-->" + Gcm.getGCM());

    }

    /**
     * Method called on device un registred
     */
    @Override
    protected void onUnregistered(Context context, String registrationId) {
        Log.i(TAG, "Device unregistered");
        CommonUtilities.displayMessage(context, getString(R.string.gcm_unregistered));
//        ServerUtilities.unregister(context, registrationId);
    }

    /**
     * Method called on Receiving a new message
     */
    @Override
    protected void onMessage(Context context, Intent intent) {
        Log.i(TAG, "Received message");
        String message = intent.getExtras().getString("message");
        String movie_name = intent.getExtras().getString("movie_name");
        String movie_id = intent.getExtras().getString("movie_id");
        String movie_image = intent.getExtras().getString("movie_image");


        System.out.println("onmessage" + message + movie_id + movie_image + movie_name);
        CommonUtilities.displayMessage(context, message);
        // notifies user
        try {
            if(movie_image != null && !movie_image.isEmpty())
            {
                new generatePictureStyleNotification(this, message, movie_id, movie_image, movie_name).execute();
            }
            else
            {
                generateNotification(context, message,movie_id,movie_image,movie_name);
            }



        } catch (Exception e) {
        }
    }

    /**
     * Method called on receiving a deleted message
     */
    @Override
    protected void onDeletedMessages(Context context, int total) {
        Log.i(TAG, "Received deleted messages notification");
        String message = getString(R.string.gcm_deleted, total);
        CommonUtilities.displayMessage(context, message);
        // notifies user
        generateNotification(context, message, "", "", "");
    }

    /**
     * Method called on Error
     */
    @Override
    public void onError(Context context, String errorId) {
        Log.i(TAG, "Received error: " + errorId);
        CommonUtilities.displayMessage(context, getString(R.string.gcm_error, errorId));
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        // log message
        Log.i(TAG, "Received recoverable error: " + errorId);
        CommonUtilities.displayMessage(context, getString(R.string.gcm_recoverable_error,
                errorId));
        return super.onRecoverableError(context, errorId);
    }

    /**
     * Issues a notification to inform the user that server has sent a message.
     */
    private static void generateNotification(Context context, String message, String movie_id, String movie_image, String movie_name) {
        String title = context.getString(R.string.app_name);
        int mid = Integer.parseInt(movie_id);
        Intent intent = new Intent(context, MDetails.class).putExtra("Moviename", movie_name).putExtra("Movieid", mid);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        System.out.println("message in generate notification class->" + message);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);



        Notification notif = new NotificationCompat.Builder(context)
                .setContentIntent(pendingIntent)
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentTitle(title)
                .setContentText(message)
                .setColor(Color.parseColor("#404040"))
                .setSmallIcon(R.drawable.dashboard_dada)
                .setTicker(message)
                        //.setLargeIcon(result)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                        //.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(result))
                .build();
        notif.flags |= Notification.FLAG_AUTO_CANCEL;
        notif.defaults |= Notification.DEFAULT_SOUND;// Play default notification sound
        notif.defaults |= Notification.DEFAULT_VIBRATE;// Vibrate if vibrate is enabled
        notif.defaults |= Notification.DEFAULT_LIGHTS; // LED
        Random random = new Random();
        int m = random.nextInt(9999 - 1000) + 1000;
        notificationManager.notify(m, notif);

    }

    class generatePictureStyleNotification extends AsyncTask<String, Void, Bitmap> {

        private Context mContext;
        private String title, message, movie_id, movie_image, movie_name;

        public generatePictureStyleNotification(Context context, String message, String movie_id, String movie_image, String movie_name) {
            super();
            this.mContext = context;
            this.title = context.getString(R.string.app_name);
            this.message = message;
            this.movie_id = movie_id;
            this.movie_image = movie_image;
            this.movie_name = movie_name;
        }

        @Override
        protected Bitmap doInBackground(String... params) {

            InputStream in;
            try {
                URL url = new URL(this.movie_image);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                in = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(in);
                return myBitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            int mid = Integer.parseInt(movie_id);
            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.dashboard_dada);
            Intent intent = new Intent(mContext, MDetails.class).putExtra("Moviename", movie_name).putExtra("Movieid", mid);

            PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            System.out.println("message in generate notification class->" + message);
            NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

            NotificationCompat.BigPictureStyle notiStyle = new NotificationCompat.BigPictureStyle();
            notiStyle.setBigContentTitle(title);
            notiStyle.setSummaryText(message);
            notiStyle.bigPicture(result);

            Notification notif = new NotificationCompat.Builder(mContext)
                    .setContentIntent(pendingIntent)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setColor(Color.parseColor("#404040"))
                    .setSmallIcon(R.drawable.dashboard_dada)
                    .setTicker(message)
                    //.setLargeIcon(bm)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                    //.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(result))
                    .setStyle(notiStyle)
                    .build();
            notif.flags |= Notification.FLAG_AUTO_CANCEL;
            notif.defaults |= Notification.DEFAULT_SOUND;// Play default notification sound
            notif.defaults |= Notification.DEFAULT_VIBRATE;// Vibrate if vibrate is enabled
            notif.defaults |= Notification.DEFAULT_LIGHTS; // LED
            Random random = new Random();
            int m = random.nextInt(9999 - 1000) + 1000;
            notificationManager.notify(m, notif);
        }

    }

}
