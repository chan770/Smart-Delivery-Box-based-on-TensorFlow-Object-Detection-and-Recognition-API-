package com.example.database;

import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import com.example.database.models.FriendlyMessage;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class BasicActivity extends AppCompatActivity {
	private static final String CHILD_MESSAGES = "boxs";
	private Button  mButtonPush, mButtonRemove;
	private DatabaseReference mRootRef ;
	private Dialog mDialog;
	private String mUsername ="led";
	private String msg ="LOG Files" + "\n";
	private TextView mTextview2 ,meditText;
	private ValueEventListener mValueEventListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sample);
		initWidget();

		FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

		mRootRef = firebaseDatabase.getReference();

		setEventListener();
	}

	@Override
	protected void onStart() {
		super.onStart();
		mDialog.show();
		mValueEventListener = new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				mDialog.dismiss();
				mUsername = dataSnapshot.child("box").child("door").getValue(String.class);
				meditText.setText("Door: " + mUsername);
				Iterable<DataSnapshot> children = dataSnapshot.child(CHILD_MESSAGES).getChildren();
				while(children.iterator().hasNext()){
					String key= children.iterator().next().getKey();
					FriendlyMessage friendlyMessage = dataSnapshot.child(CHILD_MESSAGES).child(key).getValue(FriendlyMessage.class);
				}
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {
				mDialog.dismiss();
				mTextview2.setText(getString(R.string.fail_read, databaseError.getMessage()));
			}
		};
		mRootRef.addValueEventListener(mValueEventListener);
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (mValueEventListener != null) {
			mRootRef.removeEventListener(mValueEventListener);
		}
	}

	private void initWidget() {
		mTextview2=findViewById(R.id.txt_result2);
		meditText=findViewById(R.id.textView2);
		mTextview2.setMovementMethod(new ScrollingMovementMethod());
		mButtonPush = findViewById(R.id.btn_push);
		mButtonRemove = findViewById(R.id.btn_remove);
		mDialog = new Dialog(this, R.style.NewDialog);
		mDialog.addContentView(
				new ProgressBar(this),
				new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
		);
		mDialog.setCancelable(true);
	}
	private void setEventListener() {
		final MediaPlayer closed =MediaPlayer.create(this,R.raw.closed);
		final MediaPlayer open =MediaPlayer.create(this,R.raw.open);
		mButtonRemove.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				SimpleDateFormat formatter =new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				Date date =new Date();

				try{
					closed.start();
					mRootRef.child("box").child("door").setValue("close");
					mTextview2.append("Door closed  at  "  + formatter.format(date)+ "\n");
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
		mButtonPush.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				SimpleDateFormat formatter1 =new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				Date date1 =new Date();
				try{
					open.start();
					mRootRef.child("box").child("door").setValue("open");
					mTextview2.append("Door opened at  "  + formatter1.format(date1)  + "\n");
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
	}
}
