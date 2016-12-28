package com.ksu.blooddonationproject;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.parse.ParseObject;

public class OrderStatusListViewAdapter extends BaseAdapter {

	// Declare Variables
	Context context;
	LayoutInflater inflater;
	private List<BloodDonation> orderlist = null;
	private ArrayList<BloodDonation> arraylist;
	List<ParseObject> ob;

	public OrderStatusListViewAdapter(Context context, List<BloodDonation> orderlist) {
		this.context = context;
		this.orderlist = orderlist;
		inflater = LayoutInflater.from(context);
		this.arraylist = new ArrayList<BloodDonation>();
		this.arraylist.addAll(orderlist);
	}

	public class ViewHolder {
		TextView mOrderIdTV, mRemainingTV;
		TextView mOrderIdLabel, mRemainingLabel;
	}

	@Override
	public int getCount() {
		
		return orderlist.size();
		
	}

	@Override
	public Object getItem(int position) {
		return orderlist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View view, ViewGroup parent) {
		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.listview_order_status_item, null);
			// Locate the TextViews in listview_item.xml
			holder.mOrderIdTV = (TextView) view.findViewById(R.id.orderIdTV);
			holder.mRemainingTV = (TextView) view.findViewById(R.id.remainingTV);
			
			holder.mOrderIdLabel = (TextView) view.findViewById(R.id.orderIdLabel);
			holder.mRemainingLabel = (TextView) view.findViewById(R.id.remainingLabel);
			view.setTag(holder);

			 // Applying font
			holder.mOrderIdLabel.setTypeface(Font.getFont(context));
			holder.mRemainingLabel.setTypeface(Font.getFont(context));
		

		} else {
			holder = (ViewHolder) view.getTag();
		}
		String remainingNumber = String.valueOf(orderlist.get(position).getRemainingNumber());
		// Set the results into TextViews
		holder.mOrderIdTV.setText(orderlist.get(position).getOrderId());
		holder.mRemainingTV.setText(remainingNumber);
		
		return view;
	}

}
