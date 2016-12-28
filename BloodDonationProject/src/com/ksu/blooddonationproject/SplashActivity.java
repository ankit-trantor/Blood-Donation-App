package com.ksu.blooddonationproject;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_splash);

		//Create Timer
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			
			//Method to run MainScreen Activity
			@Override
			public void run() {
				startActivity(new Intent(SplashActivity.this,MainActivity.class));
				finish();
			}
		//	set the timer for 2000=2 seconds. *in Java 1 second = 1000.
		}, 2000);
		timer.purge();
	}
	

	


}
