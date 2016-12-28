package com.ksu.blooddonationproject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParseUser;

public class MakeDonationActivity extends Activity {

	Button mSubmitBtn;
	EditText mName;
    Spinner mBloodType, mCitiesList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_make_donation);
		getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
	
		mSubmitBtn = (Button) findViewById(R.id.submitBtn);		
		mName  = (EditText) findViewById(R.id.nameET);	
		
		// blood type spinner
		mBloodType = ((Spinner) findViewById(R.id.blood_types_spr));
		ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter .createFromResource(this, R.array.blood_type, R.layout.spinner_center_item);
		mBloodType.setAdapter(spinnerAdapter);
		
		// Cities List type spinner
		mCitiesList = ((Spinner) findViewById(R.id.cities_names_spr));
		ArrayAdapter<CharSequence> spinnerAdapter2 = ArrayAdapter .createFromResource(this, R.array.cities_list, R.layout.spinner_center_item);
		mCitiesList.setAdapter(spinnerAdapter2);
		
		 // Applying font
		mSubmitBtn.setTypeface(Font.getFont(this));
		mName.setTypeface(Font.getFont(this));
		
		// mSubmitBtn Button Click Listener
		mSubmitBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String name = mName.getText().toString();
				String bloodType = mBloodType.getSelectedItem().toString();
				String city = mCitiesList.getSelectedItem().toString();
				// Retrieve current user from Parse.com
				ParseUser currentUser = ParseUser.getCurrentUser();
				// Convert currentUser into String
				String username = currentUser.getUsername().toString();
				String userId = currentUser.getObjectId().toString();
				
				if(name.isEmpty()){
					Toast.makeText(getApplicationContext(), "يجب تعبئة جميع الحقول", Toast.LENGTH_SHORT).show();
				}
				else if(bloodType.equals("اختر فصيلة الدم")){
					Toast.makeText(getApplicationContext(), "الرجاء اختيار فصيلة الدم", Toast.LENGTH_SHORT).show();
				}
				else if(city.equals("اختر المدينة")){
					Toast.makeText(getApplicationContext(), "الرجاء اختيار المدينة", Toast.LENGTH_SHORT).show();
				}
				else{
				
				//Send data to Parse.com		
				ParseObject po = new ParseObject("DonatorsList");
				po.put("Username", username);
				po.put("UserId", userId);
				po.put("Name", name);
				po.put("BloodType", bloodType);
				po.put("City", city);
				po.saveInBackground();	
				Toast.makeText(getApplicationContext(), "شكرا تم استلام الطلب", Toast.LENGTH_SHORT).show();
				startActivity(new Intent(MakeDonationActivity.this, MainActivity.class));
				}
			}
		});

	}

}