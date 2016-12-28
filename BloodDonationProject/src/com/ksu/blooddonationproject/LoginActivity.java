package com.ksu.blooddonationproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

public class LoginActivity extends Activity {
	
	//Declare variables
	Button mLoginBtn;
	EditText mUserName, mPassword;
	TextView mRestPassword;
	String mUserEmail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//To use the Progress Bar in the ActionBar
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		// Get the view from activity_login.xml
		setContentView(R.layout.activity_login);
		// Remove the app logo from the actionbar
		getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
		
		// Locate Button, EditText and TextView in activity_login.xml
		mUserName  = (EditText) findViewById(R.id.usernameET);
		mPassword  = (EditText) findViewById(R.id.passwordET);
		mLoginBtn = (Button) findViewById(R.id.loginBtn);
		mRestPassword = (TextView) findViewById(R.id.restPasswordLabel);

        // Applying the font
        mUserName.setTypeface(Font.getFont(this));
        mPassword.setTypeface(Font.getFont(this));
        mLoginBtn.setTypeface(Font.getFont(this));
        mRestPassword.setTypeface(Font.getFont(this));

		
		// Button Click Listener for login button
		mLoginBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//Declare variables	and get the text from the EditText
				String username = mUserName.getText().toString();
				String password = mPassword.getText().toString();
				
				//when the user click on the login btn the app will check if the internet is available or not by calling isNetworkAvailable
				if (!isNetworkAvailable()) { 
					Toast.makeText(getApplicationContext(), "الرجاء التاكد من اتصالك بالانترنت", Toast.LENGTH_SHORT).show();
				} 
				//Check if EditTexts are not empty
				else if(username.isEmpty() || password.isEmpty()){
					Toast.makeText(getApplicationContext(), "يجب تعبئة جميع الحقول", Toast.LENGTH_SHORT).show();
				}
				
				//if the internet is available and EditTexts are not empty do the following
				else {			
				//show the Progress Bar in the ActionBar
				setProgressBarIndeterminateVisibility(true);	
				// Send data to Parse.com for verification
				ParseUser.logInInBackground(username, password,new LogInCallback() {
							public void done(ParseUser user, ParseException e) {
								// If user exist and authenticated, set the the PB invisible and send user to MainActivity.class
								if (user != null) {
									setProgressBarIndeterminateVisibility(false);
									ParseApplication.updateParseInstallation(ParseUser.getCurrentUser());
									Intent i = new Intent(LoginActivity.this, MainActivity.class);
									startActivity(i);
									finish();
								} 
								else {
									// If user is not exist, set the the PB invisible and display toast msg
									setProgressBarIndeterminateVisibility(false);
									Toast.makeText(LoginActivity.this,"اسم المستخدم او كلمة المرور غير صحيحة",Toast.LENGTH_LONG).show();
								}
							}
						});
					} 
				}
			
		});
		
		// TextView Click Listener for rest password
		mRestPassword.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//call RestPassword method
					RestPassword();
					
			}
		});		

	}
	
	//RestPassword method
	public void RestPassword(){
		// Get the view from rest_password_prompts.xml
		LayoutInflater li = LayoutInflater.from(this);
		View promptsView = li.inflate(R.layout.prompts_rest_password, null);

		// set rest_password_prompts.xml to alertdialog builder
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setView(promptsView);
		
		// Locate emailText, EditText in rest_password_prompts.xml 
		final EditText emailText = ((EditText) promptsView.findViewById(R.id.emailET));
		final TextView restPasswordTV = ((TextView) promptsView.findViewById(R.id.restPasswordTV));

		//Apply Font
		emailText.setTypeface(Font.getFont(this));
		restPasswordTV.setTypeface(Font.getFont(this));

		// set dialog message
		alertDialogBuilder.setCancelable(false).
		//Click Listener for استعادة button
		setPositiveButton("استعادة",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								
								if (!isNetworkAvailable()) { 
									Toast.makeText(getApplicationContext(), "الرجاء التاكد من اتصالك بالانترنت", Toast.LENGTH_SHORT).show();

								} 
								//if the internet is available
								else {
								//Declare variable and get the text from the EditText
								mUserEmail = emailText.getText().toString();
								//email validation
								String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
								//if the email is not valid
								 if (!mUserEmail.matches(emailPattern))
						        { 
						            Toast.makeText(getApplicationContext(),"الرجاء ادخال ايميل صحيح",Toast.LENGTH_SHORT).show();
									setProgressBarIndeterminateVisibility(false);
						        }
								//if the email is valid
								 else{
								// Starts an intent for the sign up activity
								ParseUser.requestPasswordResetInBackground( mUserEmail, new RequestPasswordResetCallback() {
											public void done(ParseException e) {
												if (e == null) {
													// An email was successfully sent
													Toast.makeText(getBaseContext(),"تم ارسال رابط استعاده كلمه المرور الى بريدك",Toast.LENGTH_SHORT).show();
												} 
												else {
													// Something went wrong
										            Toast.makeText(getApplicationContext(),"الرجاء التاكد من البريد المدخل",Toast.LENGTH_SHORT).show();

												}
											}
										});
								 	}
								}
							}
						})
				 //Click Listener for الغاء button	
				.setNegativeButton("الغاء",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								dialog.cancel();
							}

						});

		        
		// create alert dialog
	    final AlertDialog alertdialog = alertDialogBuilder.create();
		alertdialog.setOnShowListener(new OnShowListener() {			
			// Font path
            String fontPath = "fonts/font.ttf"; 
            // Loading Font Face
            Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
            
	        @Override
	        public void onShow(DialogInterface dialog) {	        	
	            ((Button)alertdialog.getButton(Dialog.BUTTON_POSITIVE)).setTypeface(tf);
	            ((Button)alertdialog.getButton(Dialog.BUTTON_NEGATIVE)).setTypeface(tf);
	        }
	    });
		// show it
		alertdialog.show();
 }
	
	// isNetworkAvailable method to check if the internet is Available or not 
		private boolean isNetworkAvailable() {
			// Using ConnectivityManager to check for Network Connection
			ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext()
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo activeNetworkInfo = connectivityManager .getActiveNetworkInfo();
			return activeNetworkInfo != null;
		}
}