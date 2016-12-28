package com.ksu.blooddonationproject;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class MessagesListViewAdapter extends BaseAdapter {

	// Declare Variables
	Context context;
	LayoutInflater inflater;
	private List<BloodDonation> messageslist = null;
	private ArrayList<BloodDonation> arraylist;
	List<ParseObject> ob;

	public MessagesListViewAdapter(Context context, List<BloodDonation> messageslist) {
		this.context = context;
		this.messageslist = messageslist;
		inflater = LayoutInflater.from(context);
		this.arraylist = new ArrayList<BloodDonation>();
		this.arraylist.addAll(messageslist);
	}

	public class ViewHolder {
		TextView mPatientNameTV, mCityNameTV, mHospitalNameTV, mPatientFileNumTV;
		TextView mPatientNameLabel, mCityNameLabel, mHospitalNameLabel, mPatientFileNumLabel;

		Button mAcceptBtn, mRejectBtn;
	}

	@Override
	public int getCount() {
		
		return messageslist.size();
		
	}

	@Override
	public Object getItem(int position) {
		return messageslist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View view, ViewGroup parent) {
		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.listview_message_item, null);
			// Locate the TextViews in listview_item.xml
			holder.mPatientNameTV = (TextView) view.findViewById(R.id.patientNameTV);
			holder.mCityNameTV = (TextView) view.findViewById(R.id.cityNameTV);
			holder.mHospitalNameTV = (TextView) view.findViewById(R.id.hospitalNameTV);
			holder.mPatientFileNumTV = (TextView) view.findViewById(R.id.patientFileNumTV);
			
			holder.mPatientNameLabel = (TextView) view.findViewById(R.id.patientNameLabel);
			holder.mCityNameLabel = (TextView) view.findViewById(R.id.cityNameLabel);
			holder.mHospitalNameLabel = (TextView) view.findViewById(R.id.hospitalNameLabel);
			holder.mPatientFileNumLabel= (TextView) view.findViewById(R.id.patientFileNumLabel);
			view.setTag(holder);

			 // Applying font
			holder.mPatientNameTV.setTypeface(Font.getFont(context));
			holder.mCityNameTV.setTypeface(Font.getFont(context));
			holder.mHospitalNameTV.setTypeface(Font.getFont(context));
			
			holder.mPatientNameLabel.setTypeface(Font.getFont(context));
			holder.mCityNameLabel.setTypeface(Font.getFont(context));
			holder.mHospitalNameLabel.setTypeface(Font.getFont(context));
			holder.mPatientFileNumLabel.setTypeface(Font.getFont(context));

		} else {
			holder = (ViewHolder) view.getTag();
		}
		// Set the results into TextViews
		holder.mPatientNameTV.setText(messageslist.get(position).getPatientName());
		holder.mCityNameTV.setText(messageslist.get(position).getCityName());
		holder.mHospitalNameTV.setText(messageslist.get(position).getHospitalName());
		holder.mPatientFileNumTV.setText(messageslist.get(position).getPatientFileNum());
		
		
		// Accept Button
		holder.mAcceptBtn = (Button) view.findViewById(R.id.acceptBtn);
		holder.mAcceptBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ParseQuery<ParseObject> query = ParseQuery.getQuery("Messages");
				try {
					ob = query.find();
				} catch (ParseException e) {
					Log.e("Error", e.getMessage());
					e.printStackTrace();
				}
				for (ParseObject po : ob) {

					if (messageslist.get(position).getOrderId().toString().equals(po.get("OrderId"))) {
						String NumOfDonators = po.get("NumOfDonators").toString();
						int DecreaseNumOfPpl = Integer.parseInt(NumOfDonators);
						int NewNumOfDonators = DecreaseNumOfPpl - 1;
						po.put("NumOfDonators", NewNumOfDonators);
						po.saveInBackground();
					}					
				}
				ParseQuery<ParseObject> query2 = ParseQuery.getQuery("DonReqsList");
				try {
					ob = query2.find();
				} catch (ParseException e) {
					Log.e("Error", e.getMessage());
					e.printStackTrace();
				}
				for (ParseObject po : ob) {

					if (messageslist.get(position).getOrderId().toString().equals(po.get("OrderId"))) {
						String NumOfDonators = po.get("NumOfDonators").toString();
						int DecreaseNumOfPpl = Integer.parseInt(NumOfDonators);
						int NewNumOfDonators = DecreaseNumOfPpl - 1;
						po.put("RemainingNum", NewNumOfDonators);
						po.saveInBackground();
					}					
				}
				
				ParseObject.createWithoutData("Messages",messageslist.get(position).getObjectId()).deleteEventually();
				Intent intent = new Intent(context, MainActivity.class);
				Toast.makeText(v.getContext(), "شكرا لتبرعك",Toast.LENGTH_SHORT).show();
				context.startActivity(intent);
				((Activity) context).finish();

			}
		});

		// Reject Button
		holder.mRejectBtn = (Button) view.findViewById(R.id.rejectBtn);
		holder.mRejectBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ParseObject.createWithoutData("Messages",messageslist.get(position).getObjectId()).deleteEventually();
				Intent intent = new Intent(context, MainActivity.class);
				Toast.makeText(v.getContext(), "تم رفض الطلب",Toast.LENGTH_SHORT).show();
				context.startActivity(intent);
				((Activity) context).finish();

			}
		});

		return view;
	}

}
