package com.example.database;


import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class MyApplication extends Application {

	MyAppsNotificationManager  myAppsNotificationManager;

	@Override
	public void onCreate() {
		super.onCreate();
		FirebaseDatabase.getInstance().setPersistenceEnabled(true);
		myAppsNotificationManager = MyAppsNotificationManager.getInstance(this);
		myAppsNotificationManager.registerNotificationChannelChannel(
				getString(R.string.NEWS_CHANNEL_ID),
				getString(R.string.CHANNEL_NEWS),
				getString(R.string.CHANNEL_DESCRIPTION));
	}

	public void triggerNotification(Class targetNotificationActivity, String channelId, String title, String text, String bigText, int priority, boolean autoCancel, int notificationId, int pendingIntentFlag){
		myAppsNotificationManager.triggerNotification(targetNotificationActivity,channelId,title,text, bigText, priority, autoCancel,notificationId, pendingIntentFlag);
	}

	public void triggerNotification(Class targetNotificationActivity, String channelId, String title, String text, String bigText, int priority, boolean autoCancel, int notificationId){
		myAppsNotificationManager.triggerNotification(targetNotificationActivity,channelId,title,text, bigText, priority, autoCancel,notificationId);
	}

	public void updateNotification(Class targetNotificationActivity,String title,String text, String channelId, int notificationId, String bigpictureString, int pendingIntentflag){
		myAppsNotificationManager.updateWithPicture(targetNotificationActivity, title, text, channelId, notificationId, bigpictureString, pendingIntentflag);
	}

	public void cancelNotification(int notificaitonId){
		myAppsNotificationManager.cancelNotification(notificaitonId);
	}



}
