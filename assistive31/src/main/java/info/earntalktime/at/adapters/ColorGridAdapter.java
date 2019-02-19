package info.earntalktime.at.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import info.earntalktime.at.R;
import info.earntalktime.at.db.ATSettingsTable;
import info.earntalktime.at.db.AppDb;
import info.earntalktime.at.utils.ATUtils;

public class ColorGridAdapter extends BaseAdapter {

	private Context context;
	private String[] colorArray;

	public ColorGridAdapter(Context context, String[] colorArray) {
		this.context = context;
		this.colorArray = colorArray;
	}

	@Override
	public int getCount() {
		return colorArray.length;
	}

	@Override
	public Object getItem(int position) {
		return colorArray[position];
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
			convertView = mInflater.inflate(R.layout.window_color_item, null);
		}

		ImageView tick = (ImageView) convertView.findViewById(R.id.tick);
		LinearLayout colorCircle = (LinearLayout) convertView.findViewById(R.id.color_circle);
//		int bgColorPos = AppPrefs.getInstance(context).getIntValue(SharedPreferencesName.KEY_AT_WINDOW_COLOR, 0);
		int bgColorPos = ((ATSettingsTable)AppDb.getInstance(context).getTableObject(ATSettingsTable.TABLE_NAME)).getAllData(ATUtils.TAG_AT_WINDOW_COLOR);
		try {
			Drawable background = colorCircle.getBackground();
			if (background instanceof ShapeDrawable) {
				((ShapeDrawable) background).getPaint().setColor(Color.parseColor(colorArray[position]));
			} else if (background instanceof GradientDrawable) {
				((GradientDrawable) background).setColor(Color.parseColor(colorArray[position]));
			} else if (background instanceof ColorDrawable && android.os.Build.VERSION.SDK_INT >= 11) {
				((ColorDrawable) background).setColor(Color.parseColor(colorArray[position]));
			}
		} catch (Exception e) {
		}
		if (position == bgColorPos) {
			tick.setVisibility(View.VISIBLE);
		} else {
			tick.setVisibility(View.GONE);

		}

		return convertView;
	}

}
