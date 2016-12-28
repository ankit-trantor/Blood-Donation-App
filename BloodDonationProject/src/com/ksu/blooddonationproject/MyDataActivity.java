package com.ksu.blooddonationproject;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class MyDataActivity extends Activity {
	
	//declare variables for the two buttons 
	EditText mUserName, mPassword, mConfirmPassword, mEmail, mName, mPatientFileNum;
	Button mUpdateBtn;
    Spinner mBloodTypeSpr, mCityNameSpr;
	private RadioButton mPatientRB, mDonatorRB;
	String mUserType,  mDonatorId;
	TextView mUserTypeTV, mUserNameLabel, mPasswordLabel, mConfirmPasswordLabel, mEmailLabel, mNameLabel, mPatientFileNumLabel;
	List<ParseObject> ob;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//To use the Progress Bar in the ActionBar
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		// Get the view from activity_signup.xml
		setContentView(R.layout.activity_my_data);
		// Remove the app logo from the actionbar
		getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
		
		// Retrieve current user from Parse.com
		ParseUser currentUser = ParseUser.getCurrentUser();
		// Convert currentUser into String
		String username = currentUser.getUsername().toString();
		String email = currentUser.getEmail().toString();
		String userType = currentUser.getString("UserType");
		
		String name = currentUser.getString("Name");
		String patientFileNum = currentUser.getString("PatientFileNum");
		int bloodTypePosition = currentUser.getInt("BloodTypePosition");
		int cityNamePosition = currentUser.getInt("CityNamePosition");

		// Locate Buttons and EditText in activity_login.xml
		mUserName  = (EditText) findViewById(R.id.usernameET);
		mPassword  = (EditText) findViewById(R.id.passwordET);
		mConfirmPassword  = (EditText) findViewById(R.id.confirmPasswordET);
		mEmail  = (EditText) findViewById(R.id.emailET);
		mName  = (EditText) findViewById(R.id.nameET); 
		mPatientFileNum  = (EditText) findViewById(R.id.patientFileNumET);	
		mDonatorRB = (RadioButton) findViewById(R.id.donatorRB);
		mPatientRB = (RadioButton) findViewById(R.id.patientRB);
		mUpdateBtn = (Button) findViewById(R.id.signUpBtn);
		//TextViews
		mUserNameLabel  = (TextView) findViewById(R.id.usernameLabel);
		mPasswordLabel = (TextView) findViewById(R.id.passwordLabel);
		mConfirmPasswordLabel  = (TextView) findViewById(R.id.confirmPasswordLabel);
		mEmailLabel  = (TextView) findViewById(R.id.emailLabel);
		mNameLabel  = (TextView) findViewById(R.id.nameLabel); 
		mPatientFileNumLabel  = (TextView) findViewById(R.id.patientFileNumLabel);	
		mUserTypeTV  = (TextView) findViewById(R.id.userTypeTV);	
	
		// blood type spinner
		mBloodTypeSpr = ((Spinner) findViewById(R.id.blood_types_spr));
		ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter .createFromResource(this, R.array.blood_type, R.layout.spinner_center_item);
		mBloodTypeSpr.setAdapter(spinnerAdapter);		
		// Cities List type spinner
		mCityNameSpr = ((Spinner) findViewById(R.id.cities_names_spr));
		ArrayAdapter<CharSequence> spinnerAdapter2 = ArrayAdapter .createFromResource(this, R.array.cities_list, R.layout.spinner_center_item);
		mCityNameSpr.setAdapter(spinnerAdapter2);
		
		mUserName.setText(username);
		mEmail.setText(email);
		mName.setText(name);
		mPatientFileNum.setText(patientFileNum);
		mBloodTypeSpr.setSelection(bloodTypePosition);
		mCityNameSpr.setSelection(cityNamePosition);
			
		if(userType.equals("مريض")){
			mPatientRB.setChecked(true);
			mNameLabel.setVisibility(View.VISIBLE);
			mName.setVisibility(View.VISIBLE);
        	mPatientFileNumLabel.setVisibility(View.VISIBLE); 
        	mPatientFileNum.setVisibility(View.VISIBLE); 
        	mBloodTypeSpr.setVisibility(View.GONE);
        	mCityNameSpr.setVisibility(View.GONE);
        	mUserType ="مريض";
		}
		else{
			mDonatorRB.setChecked(true);
			mNameLabel.setVisibility(View.VISIBLE);
			mName.setVisibility(View.VISIBLE);
        	mBloodTypeSpr.setVisibility(View.VISIBLE);
        	mCityNameSpr.setVisibility(View.VISIBLE);
        	mPatientFileNum.setVisibility(View.GONE); 
        	mPatientFileNumLabel.setVisibility(View.GONE); 
        	mUserType ="متبرع";	
		}
						
		 // Applying font
        mUserName.setTypeface(Font.getFont(this));
        mPassword.setTypeface(Font.getFont(this));
        mConfirmPassword.setTypeface(Font.getFont(this));
        mEmail.setTypeface(Font.getFont(this)); 
        mName.setTypeface(Font.getFont(this)); 
        mPatientFileNum.setTypeface(Font.getFont(this)); 
        mDonatorRB.setTypeface(Font.getFont(this));
        mPatientRB.setTypeface(Font.getFont(this));
        mUpdateBtn.setTypeface(Font.getFont(this));
        //textViews
        mUserNameLabel.setTypeface(Font.getFont(this));
        mPasswordLabel.setTypeface(Font.getFont(this));
        mConfirmPasswordLabel.setTypeface(Font.getFont(this));
        mEmailLabel.setTypeface(Font.getFont(this)); 
        mNameLabel.setTypeface(Font.getFont(this)); 
        mPatientFileNumLabel.setTypeface(Font.getFont(this)); 
        mUserTypeTV.setTypeface(Font.getFont(this)); 
             
        mPatientRB.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
				if (isChecked) {
					mNameLabel.setVisibility(View.VISIBLE);
					mName.setVisibility(View.VISIBLE);
		        	mPatientFileNumLabel.setVisibility(View.VISIBLE); 
		        	mPatientFileNum.setVisibility(View.VISIBLE); 
		        	mBloodTypeSpr.setVisibility(View.GONE);
		        	mCityNameSpr.setVisibility(View.GONE);
		        	mUserType ="مريض";
				}
			}
		});
               
        mDonatorRB.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
				if (isChecked) {
					mNameLabel.setVisibility(View.VISIBLE);
					mName.setVisibility(View.VISIBLE);
		        	mPatientFileNumLabel.setVisibility(View.GONE); 
		        	mBloodTypeSpr.setVisibility(View.VISIBLE);
		        	mCityNameSpr.setVisibility(View.VISIBLE);
		        	mPatientFileNum.setVisibility(View.GONE); 
		        	mUserType ="متبرع";				
				}
			}
		});

		// Button Click Listener for mSignUpBtn button
		mUpdateBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ParseUser currentUser = ParseUser.getCurrentUser();
				final String userId = currentUser.getObjectId().toString();
				//Declare variables	and get the text from the EditTexts
				final String username = mUserName.getText().toString();
				String password = mPassword.getText().toString();
				String confirm_password = mConfirmPassword.getText().toString();
				String email = mEmail.getText().toString();	
				
				final String name = mName.getText().toString();
				String patientFileNum = mPatientFileNum.getText().toString();
				
				 final String bloodType = mBloodTypeSpr.getSelectedItem().toString();
				 final String city = mCityNameSpr.getSelectedItem().toString();
				 final int bloodTypePosition = mBloodTypeSpr.getSelectedItemPosition();
				 final int cityPosition = mCityNameSpr.getSelectedItemPosition();
				 
				 
				// query to get the donator Id in order to delete from the DonatorsList 
				 ParseQuery<ParseObject> query = ParseQuery.getQuery("DonatorsList");
					//query to search in BloodType column based on the selected blood type
					query.whereEqualTo("UserId", userId );
					try {
						ob = query.find();
					} catch (ParseException e) {
						Log.e("Error", e.getMessage());
						e.printStackTrace();
					}
					for (ParseObject po : ob) {
						 mDonatorId = po.getObjectId();			
					}
		
				//email validation
				String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
				
				//when the user click on the login btn the app will check if the internet is available or not by calling isNetworkAvailable
				if (!isNetworkAvailable()) { 
					Toast.makeText(getApplicationContext(), "الرجاء التاكد من اتصالك بالانترنت", Toast.LENGTH_SHORT).show();
				} 
				//if the internet is available				
				//Check if EditTexts are not empty
				else if(username.isEmpty() || email.isEmpty()){
					Toast.makeText(getApplicationContext(), "يجب تعبئة جميع الحقول", Toast.LENGTH_SHORT).show();
				}				
				//check if the passwords and the confirm password are the same
				else if(!confirm_password.equals(password)){
					Toast.makeText(getApplicationContext(), "كلمة المرور وتاكيد كلمة المرور غير متطابقتين", Toast.LENGTH_SHORT).show();
				}
				//if the email is not valid
				else if (!email.matches(emailPattern))
		        { 
		            Toast.makeText(getApplicationContext(),"الرجاء ادخال ايميل صحيح",Toast.LENGTH_SHORT).show();
		        }
				
				else if(mName.getVisibility() == View.VISIBLE && name.isEmpty()){
					Toast.makeText(getApplicationContext(), "يجب تعبئة جميع الحقول", Toast.LENGTH_SHORT).show();					
				}
				
				else if(mPatientFileNum.getVisibility() == View.VISIBLE && patientFileNum.isEmpty()){
					Toast.makeText(getApplicationContext(), "يجب تعبئة جميع الحقول", Toast.LENGTH_SHORT).show();					
				}
				
				else if(mBloodTypeSpr.getVisibility() == View.VISIBLE && bloodType.equals("اختر فصيلة الدم")){
					Toast.makeText(getApplicationContext(), "الرجاء اختيار فصيلة الدم", Toast.LENGTH_SHORT).show();
				}
				else if(mCityNameSpr.getVisibility() == View.VISIBLE && city.equals("اختر المدينة")){
					Toast.makeText(getApplicationContext(), "الرجاء اختيار المدينة", Toast.LENGTH_SHORT).show();
				}
								
				//if all user inputs are valid allow update			
				else{
					//show the Progress Bar in the ActionBar
					setProgressBarIndeterminateVisibility(true);
					// update user data into Parse.com Data Storage
					ParseUser user = ParseUser.getCurrentUser();
					user.setUsername(username);
					//if the password and confirm_password editTexts are not empty update the password
					if(!password.isEmpty() && !confirm_password.isEmpty()){
					user.setPassword(password);}
					user.setEmail(email);	
					user.put("UserType", mUserType);
					user.put("Name", name);	
					//if the user type is a patient then
					if(mUserType.equals("مريض")){
						user.put("PatientFileNum", patientFileNum);	
						user.put("BloodType", "لم يتم التحديد بعد");	
						user.put("CityName", "لم يتم التحديد بعد");	
						user.put("BloodTypePosition", 0);	
						user.put("CityNamePosition", 0);			
						ParseObject.createWithoutData("DonatorsList", mDonatorId).deleteEventually();

					}
					//if the user type is a Donator then
					else{					
						
						user.put("BloodType", bloodType);	
						user.put("BloodTypePosition", bloodTypePosition);	
						user.put("CityName", city);	
						user.put("CityNamePosition", cityPosition);			
						user.put("PatientFileNum", "لايوجد");	
						
						if(mDonatorId==null){
							ParseObject po = new ParseObject("DonatorsList");
							po.put("Username", username);
							po.put("UserId", userId);						
							po.put("Name", name);
							po.put("BloodType", bloodType);
							po.put("City", city);
							po.saveInBackground();
							
						}
						
	
						ParseQuery<ParseObject> query2 = ParseQuery.getQuery("DonatorsList");					 
						// Retrieve the object by id
						query2.getInBackground(mDonatorId, new GetCallback<ParseObject>() {
						  public void done(ParseObject po, ParseException e) {
						    if (e == null) {
						      // Now let's update it with some new data. In this case, only cheatMode and score
						      // will get sent to the Parse Cloud. playerName hasn't changed.
						    	po.put("Username", username);
								po.put("UserId", userId);						
								po.put("Name", name);
								po.put("BloodType", bloodType);
								po.put("City", city);
								po.saveInBackground();
						    }
						  }
						});
					}
					
					user.saveInBackground(new SaveCallback() {
						public void done(ParseException e) {
							//successful registration
							if (e == null) {
								setProgressBarIndeterminateVisibility(false);
								// Hooray! Let them use the app now.
								ParseApplication.updateParseInstallation(ParseUser.getCurrentUser());
								startActivity(new Intent(MyDataActivity.this,MainActivity.class));
								finish();
							}
							//unsuccessful registration
							else {
								setProgressBarIndeterminateVisibility(false);
								Toast.makeText(MyDataActivity.this,"اسم المستخدم او الايميل مسجلة من قبل",Toast.LENGTH_LONG).show();
								
							}
						}
					});
				}
			  }
			 	
		});

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