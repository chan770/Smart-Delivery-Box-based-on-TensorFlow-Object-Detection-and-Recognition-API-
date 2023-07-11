package com.example.database;

import android.app.PendingIntent;
import android.app.Service;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    @Override
    public void onNewToken(String s){
        super.onNewToken(s);
        Log.i(getString(R.string.DEBUG),"new Token  "  +s);
    }
    @Override
    public  void onMessageReceived(RemoteMessage remoteMessage){
        super.onMessageReceived(remoteMessage);
        Log.i(getString(R.string.DEBUG),"meg recevied");
        ((MyApplication)getApplication()).triggerNotification(MyAppsNotificationManager.class,
                getString(R.string.NEWS_CHANNEL_ID),
                remoteMessage.getNotification().getTitle(),
                remoteMessage.getNotification().getBody(),
                "Request to open the box",
                NotificationCompat.PRIORITY_HIGH,
                true,
                getResources().getInteger(R.integer.Id),
                PendingIntent.FLAG_CANCEL_CURRENT);
    }
}
