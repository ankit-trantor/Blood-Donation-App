package com.ksu.blooddonationproject;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class OrderStatusListView extends Activity {
	// Declare Variables
	ListView listview;
	List<ParseObject> ob;
	ProgressBar mProgressBar;
	OrderStatusListViewAdapter adapter;
	private List<BloodDonation> orderlist = null;
	ArrayList<Intent> mListIntent;
	TextView mOrdersEmptyTV;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Get the view from listview_main.xml
		setContentView(R.layout.listview_main);
		getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
		mOrdersEmptyTV = (TextView) findViewById(R.id.tv1);


		// Execute RemoteDataTask AsyncTask
		new RemoteDataTask().execute();
	}

	// RemoteDataTask AsyncTask
	private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Create a progressBar
			mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
			mProgressBar.setVisibility(View.VISIBLE);

		}

		@Override
		protected Void doInBackground(Void... params) {
			// Create the array
			orderlist = new ArrayList<BloodDonation>();
			try {
				// Locate the class table named "Country" in Parse.com
				ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("DonReqsList");
				// by createdAt
				query.orderByDescending("createdAt");
				// Retrieve current user from Parse.com
				ParseUser currentUser = ParseUser.getCurrentUser();
				// Convert currentUser into String
				String username = currentUser.getUsername().toString();
				query.whereEqualTo("Username", username);

				//query.whereNotEqualTo("NumOfDonators", 0);
				ob = query.find();
				for (ParseObject po : ob) {
					// Locate images in image column
					BloodDonation msg = new BloodDonation();
					msg.setOrderId((String) po.get("OrderId"));
					msg.setRemainingNumber((int) po.getInt("RemainingNum"));
					
					orderlist.add(msg);			

				}
			} catch (ParseException e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			
			// Locate the listview in listview_main.xml
			listview = (ListView) findViewById(R.id.listview);
			// Pass the results into ListViewAdapter.java
			adapter = new OrderStatusListViewAdapter(OrderStatusListView.this,orderlist);
			// Binds the Adapter to the ListView
			listview.setAdapter(adapter);
			
			mOrdersEmptyTV = (TextView) findViewById(R.id.tv1);
			if(orderlist.isEmpty()){
				 
				mOrdersEmptyTV.setText("لايوجد طلبات تبرع جديدة");
				mOrdersEmptyTV.setTypeface(Font.getFont(getApplicationContext()));

			 }
			else{
				mOrdersEmptyTV.setText("");
			}
			
			// Close the progress Bar
			mProgressBar.setVisibility(View.INVISIBLE);

		}
	}
	
	// Menu
		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.activity_messages_menu, menu);
			return super.onCreateOptionsMenu(menu);
		}

		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			super.onOptionsItemSelected(item);
			super.onBackPressed();
			switch (item.getItemId()) {
			// =================================================================================================
			case R.id.refresh:

				Toast.makeText(getBaseContext(), "تحديث", Toast.LENGTH_SHORT) .show();
				Intent i = new Intent(getApplicationContext(), OrderStatusListView.class);
				startActivity(i);
				break;

			}

			return true;

		}

}