package com.ksu.blooddonationproject;

import java.lang.reflect.Field;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.PushService;
import com.parse.SaveCallback;

public class MainActivity extends Activity {

	//Declare variables
	TextView mUsernameTV;
	Button mMyData, mBtn2, mBtn3;
	String mUserType;
	boolean mNotificationStatus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Get the view from activity_main.xml
		setContentView(R.layout.activity_main);
		// Remove the app logo from the actionbar
		getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

		// Locate Button and TextView in activity_main.xml
		mUsernameTV = (TextView) findViewById(R.id.usernameTV);
		mMyData = (Button) findViewById(R.id.btn1);
		mBtn2 = (Button) findViewById(R.id.btn2);
		mBtn3 = (Button) findViewById(R.id.btn3);
		
		// Applying font
		mUsernameTV.setTypeface(Font.getFont(this));
		mMyData.setTypeface(Font.getFont(this));
		mBtn2.setTypeface(Font.getFont(this));
		mBtn3.setTypeface(Font.getFont(this)); 

		// Check if the user is logged in
		ParseAnalytics.trackAppOpened(getIntent());
		ParseUser currentUser = ParseUser.getCurrentUser();
		getOverflowMenu();

		// if the the user logged out
		if (currentUser == null) {
			Intent intent = new Intent(this, HomeActivity.class);
			//If set, this activity will become the start of a new task on this history stack.
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			startActivity(intent);
		}
		// if the user is already logged in
		else {
			// Convert currentUser into String
			String username = currentUser.getUsername().toString();
			//display his name + welcome
			mUsernameTV.setText("مرحبا "+ username);
			
			//get notfi settings
			mNotificationStatus = currentUser.getBoolean("NS");			
			
			//get the userType
			mUserType = currentUser.getString("UserType");
			// if the user type is a patient then
			if(mUserType.equals("مريض")){
				mBtn2.setText("طلب تبرع");
				mBtn3.setText("حالة الطلب");				
			}
			// if the user type is a donator then
			else{
				mBtn2.setText("طلبات التبرع");
				mBtn3.setVisibility(View.GONE);
			}			
		}
		
		// Button Click Listener for mRequestDonation button
		mMyData.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {	
				Intent i = new Intent(MainActivity.this, MyDataActivity.class);
				startActivity(i);
				

			}
		});

		// Button Click Listener for mMakeDonation button
		mBtn2.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				
				// if the user type is a patient then
				if(mUserType.equals("مريض")){
					Intent i = new Intent(MainActivity.this, RequestDonationActivity.class);
					startActivity(i);			
				}
				// if the user type is a donator then
				else{
					Intent i = new Intent(MainActivity.this, MessagesListView.class);
					startActivity(i);
				}				
			}
		});

		// Button Click Listener for mMessagesBtn button
		mBtn3.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Intent i = new Intent(MainActivity.this, OrderStatusListView.class);
				startActivity(i);
			}
		});
	}
	
	// create menu
		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			MenuInflater inflater = getMenuInflater();
			// Get the view from activity_main_menu.xml
			inflater.inflate(R.menu.activity_main_menu, menu);
			return super.onCreateOptionsMenu(menu);
		}

		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			super.onOptionsItemSelected(item);
			switch (item.getItemId()) {
			// Locate logout
			case R.id.logout:
		   // call logout method
			Logout();
			break;
			case R.id.ns:
			notifications_setting();
			}
			return true;
		}
			
		public void Logout(){
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
	 
	        // Setting Dialog Message
	        alertDialog.setMessage("تسجيل الخروج؟");
	 
	        // Setting Icon to Dialog
	        alertDialog.setIcon(R.drawable.icon_logout);
	 
	        // Setting Positive "Yes" Button
	        alertDialog.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog,int which) {
	            	// Logout current user
					ParseUser.logOut();
					startActivity(new Intent(MainActivity.this, HomeActivity.class));
					finish();
	            }
	        });
	        // Setting Negative "NO" Button
	        alertDialog.setNegativeButton("لا", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	            // Write your code here to invoke NO event
	            dialog.cancel();
	            }
	        });
	 
	        // Showing Alert Message
	        alertDialog.show();

		}			
		//===============notifications_setting Method=====================
		@SuppressWarnings("deprecation")
		public void notifications_setting() {
			// get prompts_notifications_setting.xml view
			LayoutInflater li = LayoutInflater.from(this);
			View promptsView = li.inflate(R.layout.prompts_notifications_setting, null);
			
			//Creating AlertDialog
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
			
			// set prompts.xml to alertdialog builder
			alertDialogBuilder.setView(promptsView);
			
			// declare and link CheckBox prompts_notifications_setting.xml layout
			final CheckBox cb1 = ((CheckBox) promptsView.findViewById(R.id.cb1));

			if(mNotificationStatus == true){
				cb1.setChecked(false);
			}
			else{
				cb1.setChecked(true);
				
			}

			// when the user click on the CheckBox the do the following:
			cb1.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					//if the CheckBox is checked 
					if (isChecked) {
						// update user data into Parse.com Data Storage
						ParseUser user = ParseUser.getCurrentUser();
						user.put("NS", false);
						user.saveInBackground(new SaveCallback() {
							public void done(ParseException e) {
								//successful registration
								if (e == null) {
									Toast.makeText(getBaseContext(),"تم ايقاف التنبيهات",Toast.LENGTH_SHORT).show();
									mNotificationStatus=false;
									notification_off();
									
								}
								//unsuccessful registration
								else {
									setProgressBarIndeterminateVisibility(false);
									Toast.makeText(MainActivity.this,"error",Toast.LENGTH_LONG).show();								
								}
							}
						});					
					}
					// if the CheckBox is unchecked
					else{
						// update user data into Parse.com Data Storage
						ParseUser user = ParseUser.getCurrentUser();
						user.put("NS", true);
						user.saveInBackground(new SaveCallback() {
							public void done(ParseException e) {
								//successful registration
								if (e == null) {
									Toast.makeText(getBaseContext(),"تم تفعيل التنبيهات",Toast.LENGTH_SHORT).show();
									mNotificationStatus=true;
									notification_on();

								}
								//unsuccessful registration
								else {
									Toast.makeText(MainActivity.this,"error",Toast.LENGTH_LONG).show();

									
								}
							}
						});
						
					}
				}


			});
					 		
			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();
			
			// Setting OK Button
					alertDialog.setButton("الغاء", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// Write your code here to execute after dialog closed
						}
					});

			// show the alertDialog
			alertDialog.show();
		}
		
		//notification_on method, which will allow the user to receive notifications
		public void notification_on() {
		//	ParseAnalytics.trackAppOpened(getIntent());
			// inform the Parse Cloud that it is ready for notifications
			PushService.setDefaultPushCallback(this, MainActivity.class);
			
			ParseInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
				@Override
				public void done(ParseException e) {
					if (e == null) {				
					} else {
						e.printStackTrace();				
					}
				}
			});
			
		} 
		
		//notification_off method, which will stop the user from receiving notifications
		public void notification_off() {
		//	ParseAnalytics.trackAppOpened(getIntent());
			// inform the Parse Cloud that it is ready for notifications
			PushService.setDefaultPushCallback(this, null);
			//ParseInstallation.getCurrentInstallation().saveInBackground();
		} 
		
		// ========================= for overflow =========================
	private void getOverflowMenu() {

		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class .getDeclaredField("sHasPermanentMenuKey");
			if (menuKeyField != null) {
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(config, false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			}
		}

	
	// exist the app when user click on the back btn
	@Override
	public void onBackPressed() {
		moveTaskToBack(true); 

	}
}