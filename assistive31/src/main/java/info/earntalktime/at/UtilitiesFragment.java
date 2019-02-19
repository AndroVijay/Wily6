package info.earntalktime.at;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import info.earntalktime.at.adapters.ColorGridAdapter;
import info.earntalktime.at.adapters.IconGridAdapter;
import info.earntalktime.at.db.ATSettingsTable;
import info.earntalktime.at.db.AppDb;
import info.earntalktime.at.utils.ATUtils;
import info.earntalktime.at.utils.AppPrefs;
import info.earntalktime.at.utils.Constants;
import info.earntalktime.at.utils.NotificationInterface;
import info.earntalktime.at.utils.SharedPreferencesName;
import info.earntalktime.at.view.AssistiveTouch;

public class UtilitiesFragment extends Fragment implements OnClickListener {

	private View rootView;
	private ImageView atToggle;
	private LinearLayout color, icon, panel1, panel2;
	public static final int PERMS_REQUEST_CODE = 1;

	public static NotificationInterface intf;

	public void setActivity(NotificationInterface i)
	{
		intf = i;
	}

	private void checkPermission()
	{

		ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.CAMERA,
				android.Manifest.permission.INTERNET,
				android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
				android.Manifest.permission.READ_EXTERNAL_STORAGE,
				android.Manifest.permission.SYSTEM_ALERT_WINDOW,
				android.Manifest.permission.ACCESS_NETWORK_STATE,
				android.Manifest.permission.RECEIVE_BOOT_COMPLETED
		}, PERMS_REQUEST_CODE);
	}

	// private Button customize;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_utilities, null);

		atToggle = (ImageView) rootView.findViewById(R.id.at_toggle);
		// customize = (Button) rootView.findViewById(R.id.customize);
		initUI();
		atToggle.setOnClickListener(this);

		checkPermission();

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (!Settings.System.canWrite(getActivity())) {
				Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse("package:" +getActivity().getPackageName()));
				startActivityForResult(intent, 200);
			}
		}



		// customize.setOnClickListener(this);

		boolean isATActivated = AppPrefs.getInstance(getActivity()).getBooleanValue(SharedPreferencesName.KEY_IS_AT_ACTIVATED, false);
		if (isATActivated) {
			atToggle.setImageResource(R.drawable.toggle_on);
		} else {
			atToggle.setImageResource(R.drawable.toggle_off);
		}

		return rootView;
	}

	private void initUI() {
		color = (LinearLayout) rootView.findViewById(R.id.color_layout);
		icon = (LinearLayout) rootView.findViewById(R.id.icon_layout);
		panel1 = (LinearLayout) rootView.findViewById(R.id.panel1_layout);
		panel2 = (LinearLayout) rootView.findViewById(R.id.panel2_layout);
		color.setOnClickListener(this);
		icon.setOnClickListener(this);
		panel1.setOnClickListener(this);
		panel2.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		int id = v.getId();
		if (id == R.id.at_toggle) {
			boolean isATActivated = AppPrefs.getInstance(getActivity()).getBooleanValue(SharedPreferencesName.KEY_IS_AT_ACTIVATED, false);
			if (isATActivated) {
				atToggle.setImageResource(R.drawable.toggle_off);
				AppPrefs.getInstance(getActivity()).commitBooleanValue(SharedPreferencesName.KEY_IS_AT_ACTIVATED, false);
				Intent miniModeService = new Intent(getActivity(), AssistiveTouch.class);
				miniModeService.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
				getActivity().startService(miniModeService);
			} else {
				atToggle.setImageResource(R.drawable.toggle_on);
				AppPrefs.getInstance(getActivity()).commitBooleanValue(SharedPreferencesName.KEY_IS_AT_ACTIVATED, true);
				Intent miniModeService = new Intent(getActivity(), AssistiveTouch.class);
				miniModeService.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
				getActivity().startService(miniModeService);
			}

		} else if (id == R.id.color_layout) {
			showColorWindowPopup(getActivity());

		} else if (id == R.id.icon_layout) {
			showIconWindowPopup(getActivity());

		} else if (id == R.id.panel1_layout) {
			intent = new Intent(getActivity(), PanelActivity.class);
			intent.putExtra("listNumber", 0);
			startActivity(intent);

		} else if (id == R.id.panel2_layout) {
			intent = new Intent(getActivity(), PanelActivity.class);
			intent.putExtra("listNumber", 2);
			startActivity(intent);


			// case R.id.customize:
			// Intent intent = new Intent(getActivity(), ATCustomizeActivity.class);
			// startActivity(intent);
			// break;
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
