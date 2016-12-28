package com.ksu.blooddonationproject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends Activity {


	//declare variables for the two buttons 
	Button mLoginBtn, mSignUpBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Get the view from activity_home.xml
		setContentView(R.layout.activity_home);
		// Remove the ActionBar from the Activity
		getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

		// Locate Button in activity_home.xml
		mLoginBtn = (Button) findViewById(R.id.loginBtn);
		mSignUpBtn = (Button) findViewById(R.id.signUpBtn);
		
        // Applying font
        mLoginBtn.setTypeface(Font.getFont(this));
        mSignUpBtn.setTypeface(Font.getFont(this));
        
		// Button Click Listener for login button
		mLoginBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// to start the  Activity
				Intent i = new Intent(HomeActivity.this, LoginActivity.class);
				startActivity(i);
			}

		});

		// Button Click Listener for sign up btn
		mSignUpBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// to start the WebsitesList_gov Activity
				Intent i = new Intent(getApplicationContext(), SignUpActivity.class);
				startActivity(i);

			}

		});

	}

}