package com.example.database;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

public class SwitchActivity extends AppCompatActivity {
	MyAppsNotificationManager myAppsNotificationManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_switch);
		FirebaseMessaging.getInstance().setAutoInitEnabled(true);
		FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
			@Override
			public void onComplete(@NonNull Task<InstanceIdResult> task) {
				if(task.isSuccessful()){
					Log.i(getString(R.string.DEBUG),"Task Failed");
					return;
				}
				Log.i(getString(R.string.DEBUG),"The result is :"+task.getResult().getToken());
			}
		});
	}
	public void gotoAdvance(View view) {
		startActivity(new Intent(this, SignInActivity.class));
		finish();
	}
}