package com.ksu.blooddonationproject;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class RequestDonationActivity extends Activity {

	Button mSubmitBtn;
	EditText mHospitalNameET;
	Spinner mBloodTypeSpr, mCitiesListSpr, mNumOfDonatorsSpr;
	List<ParseObject> ob;
	String mUserId, mUsername, mPatientName, mPatientFileNum;
	int mOrderId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_request_donation);
		getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

		mSubmitBtn = (Button) findViewById(R.id.submitBtn);
		mHospitalNameET = (EditText) findViewById(R.id.hospitalNameET);
		
		 // Applying font
		mSubmitBtn.setTypeface(Font.getFont(this));
		mHospitalNameET.setTypeface(Font.getFont(this));
	
		// Locate spinners  activity_request_donation.xml
		mBloodTypeSpr = ((Spinner) findViewById(R.id.bloodTypesSpr));
		ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.blood_type,R.layout.spinner_center_item);
		mBloodTypeSpr.setAdapter(spinnerAdapter);
		// Cities List spinner
		mCitiesListSpr = ((Spinner) findViewById(R.id.citiesNameSpr));
		ArrayAdapter<CharSequence> spinnerAdapter2 = ArrayAdapter.createFromResource(this, R.array.cities_list, R.layout.spinner_center_item);
		mCitiesListSpr.setAdapter(spinnerAdapter2);
		// number of donators List spinner
		mNumOfDonatorsSpr = ((Spinner) findViewById(R.id.numOfDonatorsSpr));
		ArrayAdapter<CharSequence> spinnerAdapter3 = ArrayAdapter.createFromResource(this, R.array.num_of_donators, R.layout.spinner_center_item);
		mNumOfDonatorsSpr.setAdapter(spinnerAdapter3);

		// submitBtn Button Click Listener
		mSubmitBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				// Retrieve current user from Parse.com
				ParseUser currentUser = ParseUser.getCurrentUser();
				// Convert currentUser into String
				mUsername = currentUser.getUsername().toString();
				mUserId = currentUser.getObjectId().toString();
				mPatientName = currentUser.get("Name").toString();
				mPatientFileNum = currentUser.get("PatientFileNum").toString();
				
				//Declare variables	and get the text from the EditText and spinners
				String hospitalName = mHospitalNameET.getText().toString();
				String bloodType = mBloodTypeSpr.getSelectedItem().toString();
				String numOfPpl = mNumOfDonatorsSpr.getSelectedItem().toString();
				String city= mCitiesListSpr.getSelectedItem().toString();

				//validation
				if(hospitalName.isEmpty()){
					Toast.makeText(getApplicationContext(), "يجب تعبئة جميع الحقول", Toast.LENGTH_SHORT).show();
				}
				else if(bloodType.equals("اختر فصيلة الدم")){
					Toast.makeText(getApplicationContext(), "الرجاء اختيار فصيلة الدم", Toast.LENGTH_SHORT).show();
				}
				else if(numOfPpl.equals("عدد الاشخاص المطلوب")){
					Toast.makeText(getApplicationContext(), "الرجاء اختيار عدد الاشخاص المطلوب", Toast.LENGTH_SHORT).show();
				}
				else if(city.equals("اختر المدينة")){
					Toast.makeText(getApplicationContext(), "الرجاء اختيار المدينة", Toast.LENGTH_SHORT).show();
				}
				
				
				else{	
				//Random object to Create Msg unique Id
				Random random = new Random();
				//create 5 random numbers
				//This gives a random integer between 10000 (inclusive) and 100000 (exclusive), one of 10000,10001,...,99998,99999.
				mOrderId = random.nextInt(100000 - 10000) + 10000;// (to - from) + 10000
					
				// Send data to Parse.com to DonationRequestsList class
				ParseObject po = new ParseObject("DonReqsList");
				po.put("Username", mUsername);
				po.put("UserId", mUserId);
				po.put("OrderId", "10"+ mOrderId);
				po.put("PatientName", mPatientName);
				po.put("BloodType", bloodType);
				po.put("NumOfDonators", Integer.parseInt(mNumOfDonatorsSpr.getSelectedItem().toString()));
				po.put("RemainingNum", Integer.parseInt(mNumOfDonatorsSpr.getSelectedItem().toString()));
				po.put("City", city);
				po.put("HospitalName", hospitalName);
				po.put("PatientFileNum", mPatientFileNum);
				//save the data
				po.saveInBackground();
				
				
				// if blood type is A+
				if (mBloodTypeSpr.getSelectedItem().toString().equals("A+")) {
					//create array list
					String[] bloodMatches = { "A+", "A-", "O+", "O-" };
					//call FindMatchesUsers method
					FindMatchesUsers(bloodMatches);
				}
					// if blood type is A-
				 else if (mBloodTypeSpr.getSelectedItem().toString().equals("A-")) {
					//create array list
					String[] bloodMatches = { "A-", "O-" };
					//call FindMatchesUsers method
					FindMatchesUsers(bloodMatches);					
				}
				// if blood type is B+
				 else if (mBloodTypeSpr.getSelectedItem().toString().equals("B+")) {
					//create array list
					String[] bloodMatches = { "B+", "B-", "O+", "O-" };
					//call FindMatchesUsers method
					FindMatchesUsers(bloodMatches);
				} 
				// if blood type is B-
				 else if (mBloodTypeSpr.getSelectedItem().toString().equals("B-")) {
					//create array list
					String[] bloodMatches = { "B-", "O-" };
					//call FindMatchesUsers method
					FindMatchesUsers(bloodMatches);
				}
				// if blood type is O+
				 else if (mBloodTypeSpr.getSelectedItem().toString().equals("O+")) {
					//create array list
					String[] bloodMatches = { "O+", "O-" };
					//call FindMatchesUsers method
					FindMatchesUsers(bloodMatches);
				}
				// if blood type is O-
				 else if (mBloodTypeSpr.getSelectedItem().toString().equals("O-")) {
					//create array list
					String[] bloodMatches = { "O-" };
					//call FindMatchesUsers method
					FindMatchesUsers(bloodMatches);
				}
				// if blood type is AB+
				 else if (mBloodTypeSpr.getSelectedItem().toString().equals("AB+")) {
					//create array list
					String[] bloodMatches = { "A+", "A-", "B+", "B-", "O+", "O-","AB+", "AB-" };
					//call FindMatchesUsers method
					FindMatchesUsers(bloodMatches);
				} 
				// if blood type is AB-
				 else if (mBloodTypeSpr.getSelectedItem().toString().equals("AB-")) {
					//create array list
					String[] bloodMatches = { "A-", "B-", "O-", "AB-" };
					//call FindMatchesUsers method
					FindMatchesUsers(bloodMatches);
				}

				//if there is any error
				else {
					Toast.makeText(getApplicationContext(), "Error blood type!", Toast.LENGTH_SHORT).show();
				}

				//return to the MainActivity
				startActivity(new Intent(RequestDonationActivity.this, MainActivity.class));
			  }
			}
		});

	}

	// FindMatchesUsers Method
	public void FindMatchesUsers(String[] bloodMatches) {
		//ParseQuery to search in DonatorsList in pars.com
		ParseQuery<ParseObject> query = ParseQuery.getQuery("DonatorsList");
		//query to search in BloodType column based on the selected blood type
		query.whereContainedIn("BloodType", Arrays.asList(bloodMatches));
		try {
			ob = query.find();
		} catch (ParseException e) {
			Log.e("Error", e.getMessage());
			e.printStackTrace();
		}
		//create for loop and ParseObject
		for (ParseObject po : ob) {
						
			// to make sure that the user cannot send message to himself
			if(!mUserId.equals(po.get("UserId").toString())){
			//query to search in ParseInstallation class 
			ParseQuery<ParseInstallation> query2 = ParseInstallation.getQuery();
			query2.whereNotEqualTo("userId", mUserId);
			query2.whereEqualTo("userId", po.get("UserId").toString());

			// send push notification based on query2
			ParsePush push = new ParsePush();
			push.setQuery(query2);
			//set message for the notification
			push.setMessage("لديك رسالة جديدة");
			//send
			push.sendInBackground();
			
			// create Messages class in parse.com
			ParseObject message = new ParseObject("Messages");
			message.put("SenderId", ParseUser.getCurrentUser().getObjectId());
			message.put("SenderUserName", ParseUser.getCurrentUser().getUsername());
			message.put("PatientName", mPatientName);
			message.put("OrderId", "10"+mOrderId);
			message.put("BloodType", mBloodTypeSpr.getSelectedItem().toString());
			message.put("NumOfDonators", Integer.parseInt(mNumOfDonatorsSpr.getSelectedItem().toString()));
			message.put("City", mCitiesListSpr.getSelectedItem().toString());
			message.put("HospitalName", mHospitalNameET.getText().toString());
			message.put("PatientFileNum", mPatientFileNum);
			message.put("Recipient_Id", po.get("UserId").toString());
			message.put("Recipient_Username", po.get("Username").toString());
			message.saveInBackground();
			//Toast Message
			}
		}
		Toast.makeText(getApplicationContext(), "تم ارسال الطلب", Toast.LENGTH_SHORT).show();

	}
}