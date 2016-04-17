package com.example.YuCeClient.widget.photo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.YuCeClient.R;

import java.util.ArrayList;

public class SimpleTextViewAdapter extends ArrayAdapter<String> {
	LayoutInflater mLayoutInflater = null;
	
	public SimpleTextViewAdapter(Context context, int textViewResourceId,
			ArrayList<String> items) {
		super(context, textViewResourceId, items);
		mLayoutInflater = (LayoutInflater) context
		.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		// Debug.warn("LeaderBoardActivity", "bind view... ");

		// if (convertView == null) {
		final String item = getItem(position);
		convertView = mLayoutInflater.inflate(R.layout.simple_list_item, null);
		TextView mNameView = (TextView) convertView
				.findViewById(R.id.text1);
		 mNameView.setText(item);
 
		return convertView;
	}
}

