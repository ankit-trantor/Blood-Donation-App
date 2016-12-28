package com.ksu.blooddonationproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
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

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends Activity {
	
	//declare variables for the two buttons 
	EditText mUserName, mPassword, mConfirmPassword, mEmail, mName, mPatientFileNum;
	Button mSignUpBtn;
	TextView mUserTypeTV;
    Spinner mBloodTypeSpr, mCityNameSpr;
	private RadioButton mPatientRB, mDonatorRB;
	String mUserType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//To use the Progress Bar in the ActionBar
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		// Get the view from activity_signup.xml
		setContentView(R.layout.activity_signup);
		// Remove the app logo from the actionbar
		getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

		// Locate Buttons and EditText in activity_login.xml
		mUserName  = (EditText) findViewById(R.id.usernameET);
		mPassword  = (EditText) findViewById(R.id.passwordET);
		mConfirmPassword  = (EditText) findViewById(R.id.confirmPasswordET);
		mEmail  = (EditText) findViewById(R.id.emailET);
		mName  = (EditText) findViewById(R.id.nameET); 
		mPatientFileNum  = (EditText) findViewById(R.id.patientFileNumET);	
		mUserTypeTV  = (TextView) findViewById(R.id.userTypeTV);	
		mDonatorRB = (RadioButton) findViewById(R.id.donatorRB);
		mPatientRB = (RadioButton) findViewById(R.id.patientRB);
		mSignUpBtn = (Button) findViewById(R.id.signUpBtn);
		
		// blood type spinner
		mBloodTypeSpr = ((Spinner) findViewById(R.id.blood_types_spr));
		ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter .createFromResource(this, R.array.blood_type, R.layout.spinner_center_item);
		mBloodTypeSpr.setAdapter(spinnerAdapter);		
		// Cities List type spinner
		mCityNameSpr = ((Spinner) findViewById(R.id.cities_names_spr));
		ArrayAdapter<CharSequence> spinnerAdapter2 = ArrayAdapter .createFromResource(this, R.array.cities_list, R.layout.spinner_center_item);
		mCityNameSpr.setAdapter(spinnerAdapter2);
				
		 // Applying font
        mUserName.setTypeface(Font.getFont(this));
        mPassword.setTypeface(Font.getFont(this));
        mConfirmPassword.setTypeface(Font.getFont(this));
        mEmail.setTypeface(Font.getFont(this)); 
        mName.setTypeface(Font.getFont(this)); 
        mPatientFileNum.setTypeface(Font.getFont(this));    
        mUserTypeTV.setTypeface(Font.getFont(this));
        mDonatorRB.setTypeface(Font.getFont(this));
        mPatientRB.setTypeface(Font.getFont(this));
        mSignUpBtn.setTypeface(Font.getFont(this));
        
        mPatientRB.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
				if (isChecked) {
					mName.setVisibility(View.VISIBLE);
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
					mName.setVisibility(View.VISIBLE);
		        	mBloodTypeSpr.setVisibility(View.VISIBLE);
		        	mCityNameSpr.setVisibility(View.VISIBLE);
		        	mPatientFileNum.setVisibility(View.GONE); 
		        	mUserType ="متبرع";				
				}
			}
		});

		// Button Click Listener for mSignUpBtn button
		mSignUpBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
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

								
				//email validation
				String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
				
				//when the user click on the login btn the app will check if the internet is available or not by calling isNetworkAvailable
				if (!isNetworkAvailable()) { 
					Toast.makeText(getApplicationContext(), "الرجاء التاكد من اتصالك بالانترنت", Toast.LENGTH_SHORT).show();
				} 
				//if the internet is available				
				//Check if EditTexts are not empty
				else if(username.isEmpty() || password.isEmpty() || confirm_password.isEmpty() || email.isEmpty()){
					Toast.makeText(getApplicationContext(), "يجب تعبئة جميع الحقول", Toast.LENGTH_SHORT).show();
					setProgressBarIndeterminateVisibility(false);
				}				
				//check if the passwords and the confirm password are the same
				else if(!confirm_password.equals(password)){
					Toast.makeText(getApplicationContext(), "كلمة المرور وتاكيد كلمة المرور غير متطابقتين", Toast.LENGTH_SHORT).show();
					setProgressBarIndeterminateVisibility(false);		
				}
				//if the email is not valid
				else if (!email.matches(emailPattern))
		        { 
		            Toast.makeText(getApplicationContext(),"الرجاء ادخال ايميل صحيح",Toast.LENGTH_SHORT).show();
					setProgressBarIndeterminateVisibility(false);
		        }
						
				else if (!mDonatorRB.isChecked() && !mPatientRB.isChecked()) {
					Toast.makeText(getApplicationContext(), "الرجاء تحديد نوع المستخدم", Toast.LENGTH_SHORT).show();
					setProgressBarIndeterminateVisibility(false);
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
								
				//if all user inputs are valid allow sign up			
				else{
					//show the Progress Bar in the ActionBar
					setProgressBarIndeterminateVisibility(true);
					//Sign up using ParseUser		
					// Save new user data into Parse.com Data Storage
					ParseUser user = new ParseUser();
					user.setUsername(username);
					user.setPassword(password);
					user.setEmail(email);	
					user.put("UserType", mUserType);
					user.put("Name", name);	
					user.put("NS", true);	
					//if the user type is a patient then
					if(mUserType.equals("مريض")){
						user.put("PatientFileNum", patientFileNum);	
						user.put("BloodType", "لم يتم التحديد بعد");	
						user.put("BloodTypePosition", 0);	
						user.put("CityNamePosition", 0);			
						user.put("CityName", "لم يتم التحديد بعد");	
					}
					//if the user type is a Donator then
					else{
					user.put("BloodType", bloodType);	
					user.put("BloodTypePosition", bloodTypePosition);	
					user.put("CityName", city);	
					user.put("CityNamePosition", cityPosition);			
					user.put("PatientFileNum", "لايوجد");				
					}
					
					user.signUpInBackground(new SignUpCallback() {
						public void done(ParseException e) {
							//successful registration
							if (e == null) {
								setProgressBarIndeterminateVisibility(false);
								// Hooray! Let them use the app now.
								ParseApplication.updateParseInstallation(ParseUser.getCurrentUser());
								startActivity(new Intent(SignUpActivity.this,MainActivity.class));
								
								// Retrieve current user from Parse.com
								ParseUser currentUser = ParseUser.getCurrentUser();
								String userId = currentUser.getObjectId().toString();
								// if the user type is a Donator create class called DonatorsList and save the Donators data in it.		
							    if(mUserType.equals("متبرع"))  {
								ParseObject po = new ParseObject("DonatorsList");
								po.put("Username", username);
								po.put("UserId", userId);						
								po.put("Name", name);
								po.put("BloodType", bloodType);
								po.put("City", city);
								po.saveInBackground();
								}
								
								finish();
							}
							//unsuccessful registration
							else {
								setProgressBarIndeterminateVisibility(false);
								Toast.makeText(SignUpActivity.this,"اسم المستخدم او الايميل مسجلة من قبل",Toast.LENGTH_LONG).show();
								
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