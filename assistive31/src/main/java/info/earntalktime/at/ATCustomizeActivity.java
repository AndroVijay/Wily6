package info.earntalktime.at;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import info.earntalktime.at.adapters.ColorGridAdapter;
import info.earntalktime.at.adapters.IconGridAdapter;
import info.earntalktime.at.db.ATSettingsTable;
import info.earntalktime.at.db.AppDb;
import info.earntalktime.at.utils.ATUtils;
import info.earntalktime.at.utils.AppPrefs;
import info.earntalktime.at.utils.Constants;
import info.earntalktime.at.utils.SharedPreferencesName;
import info.earntalktime.at.view.AssistiveTouch;

@SuppressLint("NewApi")
public class ATCustomizeActivity extends AppCompatActivity implements OnClickListener {

	private LinearLayout color, icon, panel1, panel2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_at_cutomize);

		initUI();
	}

	private void initUI() {
		color = (LinearLayout) findViewById(R.id.color_layout);
		icon = (LinearLayout) findViewById(R.id.icon_layout);
		panel1 = (LinearLayout) findViewById(R.id.panel1_layout);
		panel2 = (LinearLayout) findViewById(R.id.panel2_layout);
		color.setOnClickListener(this);
		icon.setOnClickListener(this);
		panel1.setOnClickListener(this);
		panel2.setOnClickListener(this);
		findViewById(R.id.back).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		int id = v.getId();
		if (id == R.id.color_layout) {
			showColorWindowPopup(ATCustomizeActivity.this);

		} else if (id == R.id.icon_layout) {
			showIconWindowPopup(ATCustomizeActivity.this);

		} else if (id == R.id.panel1_layout) {
			intent = new Intent(ATCustomizeActivity.this, PanelActivity.class);
			intent.putExtra("listNumber", 0);
			startActivity(intent);

		} else if (id == R.id.panel2_layout) {
			intent = new Intent(ATCustomizeActivity.this, PanelActivity.class);
			intent.putExtra("listNumber", 2);
			startActivity(intent);

		} else if (id == R.id.back) {
			finish();

		}
	}

	public void showColorWindowPopup(final Context context) {
		final Dialog layout = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
		layout.setContentView(R.layout.at_dialog_layout);
		layout.setCancelable(true);
		layout.setCanceledOnTouchOutside(true);

		((TextView) layout.findViewById(R.id.title)).setText(context.getResources().getString(R.string.background_color));

		GridView gridView = (GridView) layout.findViewById(R.id.gridView);
		ColorGridAdapter adapter = new ColorGridAdapter(context, ATUtils.windowColorArray);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				((ATSettingsTable) AppDb.getInstance(context).getTableObject(ATSettingsTable.TABLE_NAME)).updateData(ATUtils.TAG_AT_WINDOW_COLOR, position);
				boolean isATActivated = AppPrefs.getInstance(context).getBooleanValue(SharedPreferencesName.KEY_IS_AT_ACTIVATED, false);
				if (isATActivated) {
					Intent intent = new Intent(context, AssistiveTouch.class);
					intent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
					context.startService(intent);
					new Handler().postDelayed(new Runnable() {
						public void run() {
							Intent intent2 = new Intent(context, AssistiveTouch.class);
							intent2.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
							context.startService(intent2);
						}
					}, 100);
				}
				layout.dismiss();
			}
		});
		layout.show();
	}

	public void showIconWindowPopup(final Context context) {
		final Dialog layout = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
		layout.setContentView(R.layout.at_dialog_layout);
		layout.setCancelable(true);
		layout.setCanceledOnTouchOutside(true);

		((TextView) layout.findViewById(R.id.title)).setText(context.getResources().getString(R.string.icon));

		GridView gridView = (GridView) layout.findViewById(R.id.gridView);
		IconGridAdapter adapter = new IconGridAdapter(context, ATUtils.iconDrawableArray);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				((ATSettingsTable) AppDb.getInstance(context).getTableObject(ATSettingsTable.TABLE_NAME)).updateData(ATUtils.TAG_AT_ICON, position);
				boolean isATActivated = AppPrefs.getInstance(context).getBooleanValue(SharedPreferencesName.KEY_IS_AT_ACTIVATED, false);
				if (isATActivated) {
					Intent intent = new Intent(context, AssistiveTouch.class);
					intent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
					context.startService(intent);
					new Handler().postDelayed(new Runnable() {
						public void run() {
							Intent intent2 = new Intent(context, AssistiveTouch.class);
							intent2.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
							context.startService(intent2);
						}
					}, 100);
				}
				layout.dismiss();
			}
		});
		layout.show();
	}

}
