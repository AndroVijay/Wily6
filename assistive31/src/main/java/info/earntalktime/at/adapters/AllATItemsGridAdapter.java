package info.earntalktime.at.adapters;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import info.earntalktime.at.R;
import info.earntalktime.at.utils.ATItem;
import info.earntalktime.at.utils.ATUtils;

public class AllATItemsGridAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<ATItem> dataBeans;

	public AllATItemsGridAdapter(Context context, ArrayList<ATItem> dataBeans) {
		this.context = context;
		this.dataBeans = dataBeans;
	}

	@Override
	public int getCount() {
		return dataBeans.size();
	}

	@Override
	public Object getItem(int position) {
		return dataBeans.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.at_dialog_item, null);
			holder = new ViewHolder();
			holder.icon = (ImageView) convertView.findViewById(R.id.icon);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		setItemUIAccordingly(holder, dataBeans.get(position));

		return convertView;
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	private void setItemUIAccordingly(ViewHolder holder, ATItem atItem) {
		ATUtils utils = ATUtils.getInstance(context);
		Drawable drawable = null;
		String itemAction = atItem.getItemActionName();
		if (ATUtils.ACTION_HOME.equals(itemAction)) {
			drawable = ATUtils.changeColorOfDrawable(context, 1f, ContextCompat.getColor(context, R.color.at_icon_blue), R.drawable.home);
			holder.title.setText(atItem.getItemName());
		} else if (ATUtils.ACTION_RECENT.equals(itemAction)) {
			drawable = ATUtils.changeColorOfDrawable(context, 1f, ContextCompat.getColor(context, R.color.at_icon_blue), R.drawable.redent);
			holder.title.setText(atItem.getItemName());
		} else if (ATUtils.ACTION_SETTING.equals(itemAction)) {
			drawable = ATUtils.changeColorOfDrawable(context, 1f, ContextCompat.getColor(context, R.color.at_icon_blue), R.drawable.setting);
			holder.title.setText(atItem.getItemName());
		} else if (ATUtils.ACTION_FAV.equals(itemAction)) {
			drawable = ATUtils.changeColorOfDrawable(context, 1f, ContextCompat.getColor(context, R.color.at_icon_blue), R.drawable.favor);
			holder.title.setText(atItem.getItemName());
		} else if (ATUtils.ACTION_BACK.equals(itemAction)) {
			drawable = ATUtils.changeColorOfDrawable(context, 1f, ContextCompat.getColor(context, R.color.at_icon_blue), R.drawable.back_small);
			holder.title.setVisibility(View.GONE);
		} else if (ATUtils.ACTION_LOCK.equals(itemAction)) {
			drawable = ATUtils.changeColorOfDrawable(context, 1f, ContextCompat.getColor(context, R.color.at_icon_blue), R.drawable.lock);
			holder.title.setText(atItem.getItemName());
		} else if (ATUtils.ACTION_ADD_APP.equals(itemAction)) {
			drawable = ATUtils.changeColorOfDrawable(context, 1f, ContextCompat.getColor(context, R.color.at_icon_blue), R.drawable.none);
			holder.title.setText(atItem.getItemName());
		} else if (ATUtils.ACTION_AIRPLANE.equals(itemAction)) {
			drawable = ATUtils.changeColorOfDrawable(context, 1f, ContextCompat.getColor(context, R.color.at_icon_blue), R.drawable.airplane_mode);
			holder.title.setText(atItem.getItemName());
		} else if (ATUtils.ACTION_BLUETOOTH.equals(itemAction)) {
			drawable = ATUtils.changeColorOfDrawable(context, 1f, ContextCompat.getColor(context, R.color.at_icon_blue), R.drawable.bluetooth);
			holder.title.setText(atItem.getItemName());
		} else if (ATUtils.ACTION_FLASHLIGHT.equals(itemAction)) {
			drawable = ATUtils.changeColorOfDrawable(context, 1f, ContextCompat.getColor(context, R.color.at_icon_blue), R.drawable.flashlight);
			holder.title.setText(atItem.getItemName());
		} else if (ATUtils.ACTION_LOC.equals(itemAction)) {
			drawable = ATUtils.changeColorOfDrawable(context, 1f, ContextCompat.getColor(context, R.color.at_icon_blue), R.drawable.location);
			holder.title.setText(atItem.getItemName());
		} else if (ATUtils.ACTION_SCREEN_ROT.equals(itemAction)) {
			drawable = ATUtils.changeColorOfDrawable(context, 1f, ContextCompat.getColor(context, R.color.at_icon_blue), R.drawable.ratate);
			holder.title.setText(atItem.getItemName());
		} else if (ATUtils.ACTION_SCREENSHOT.equals(itemAction)) {
			drawable = ATUtils.changeColorOfDrawable(context, 1f, ContextCompat.getColor(context, R.color.at_icon_blue), R.drawable.screenshot);
			holder.title.setText(atItem.getItemName());
		} else if (ATUtils.ACTION_SOUND_MODE.equals(itemAction)) {
			drawable = ATUtils.changeColorOfDrawable(context, 1f, ContextCompat.getColor(context, R.color.at_icon_blue), R.drawable.sound);
			holder.title.setText(atItem.getItemName());
		} else if (ATUtils.ACTION_WIFI.equals(itemAction)) {
			drawable = ATUtils.changeColorOfDrawable(context, 1f, ContextCompat.getColor(context, R.color.at_icon_blue), R.drawable.wifi);
			holder.title.setText(atItem.getItemName());
		} else if (ATUtils.ACTION_VOL_UP.equals(itemAction)) {
			drawable = ATUtils.changeColorOfDrawable(context, 1f, ContextCompat.getColor(context, R.color.at_icon_blue), R.drawable.volume_up);
			holder.title.setText(atItem.getItemName());
		} else if (ATUtils.ACTION_VOL_DOWN.equals(itemAction)) {
			drawable = ATUtils.changeColorOfDrawable(context, 1f, ContextCompat.getColor(context, R.color.at_icon_blue), R.drawable.volume_down);
			holder.title.setText(atItem.getItemName());
		} else if (ATUtils.ACTION_MEDIA_VOL_UP.equals(itemAction)) {
			drawable = ATUtils.changeColorOfDrawable(context, 1f, ContextCompat.getColor(context, R.color.at_icon_blue), R.drawable.media_vol_up);
			holder.title.setText(atItem.getItemName());
		} else if (ATUtils.ACTION_MEDIA_VOL_DOWN.equals(itemAction)) {
			drawable = ATUtils.changeColorOfDrawable(context, 1f, ContextCompat.getColor(context, R.color.at_icon_blue), R.drawable.media_vol_down);
			holder.title.setText(atItem.getItemName());
		} else if (ATUtils.ACTION_APP.equals(itemAction)) {
			drawable = utils.getApplicationIconFromPackageName(atItem.getPackageName());
			holder.title.setText(utils.getApplicationNameFromPackageName(atItem.getPackageName()));
		} else {
			drawable = ATUtils.changeColorOfDrawable(context, 1f, ContextCompat.getColor(context, R.color.at_icon_blue), R.drawable.none);
			holder.title.setText(context.getResources().getString(R.string.none));
		}
		if (drawable != null) {
			if (android.os.Build.VERSION.SDK_INT > 16) {
				holder.icon.setBackground(drawable);
			} else {
				holder.icon.setBackgroundDrawable(drawable);
			}
		}

	}

	public class ViewHolder {
		private ImageView icon;
		private TextView title;
	}

}
