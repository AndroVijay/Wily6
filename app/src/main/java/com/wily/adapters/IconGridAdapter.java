package com.wily.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.wily.R;

//import info.earntalktime.at.R;

public class IconGridAdapter extends BaseAdapter {

	private Context context;
	private int[] iconArray;

	public IconGridAdapter(Context context, int[] iconArray) {
		this.context = context;
		this.iconArray = iconArray;
	}

	@Override
	public int getCount() {
		return iconArray.length;
	}

	@Override
	public Object getItem(int position) {
		return iconArray[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint({ "InflateParams", "NewApi" })
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.at_icon_item, null);
		}

		ImageView icon = (ImageView) convertView.findViewById(R.id.icon);
		icon.setImageResource(iconArray[position]);

		return convertView;
	}

}
