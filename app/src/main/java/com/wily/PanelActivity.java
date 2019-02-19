package com.wily;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wily.adapters.ATPanelItemsAdapter;
import com.wily.adapters.AllATItemsGridAdapter;
import com.wily.db.ATMainListTable;
import com.wily.db.ATSettingsListTable;
import com.wily.db.ATSettingsTable;
import com.wily.db.AppDb;
import com.wily.event.CenterStateEvent;
import com.wily.utils.ATItem;
import com.wily.utils.ATUtils;
import com.wily.utils.AppPrefs;
import com.wily.utils.Constants;
import com.wily.utils.DefaultActions;
import com.wily.utils.SharedPreferencesName;
import com.wily.view.AssistiveCenter;
import com.wily.view.AssistiveTouch;

//import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

@SuppressLint("NewApi")
public class PanelActivity extends AppCompatActivity implements OnClickListener {

	private static RelativeLayout main_layout/* , layout1, layout2 */;

	private static Context mContext;

	private GridView gridView;
	private ATPanelItemsAdapter adapter;
	private ArrayList<ATItem> itemList;
	private ArrayList<ATItem> dialogItemList;
	private int boxWidth = 0;
	private int listNumber = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mContext = this;

		setContentView(R.layout.activity_panel);

		if (getIntent() != null) {
			listNumber = getIntent().getIntExtra("listNumber", 0);
		} else {
			listNumber = 0;
		}

		findViewById(R.id.back).setOnClickListener(this);

		if (listNumber == 2) {
			((TextView) findViewById(R.id.title)).setText(getResources().getString(R.string.setting_panel));
		} else {
			((TextView) findViewById(R.id.title)).setText(getResources().getString(R.string.main_panel));
		}
		showAssistiveCenter();
	}

	public void showAssistiveCenter() {
		try {
			EventBus.getDefault().post(new CenterStateEvent(2));
			main_layout = (RelativeLayout) findViewById(R.id.main_layout);
			gridView = (GridView) findViewById(R.id.items_grid);
			int bgColorPos = ((ATSettingsTable) AppDb.getInstance(mContext).getTableObject(ATSettingsTable.TABLE_NAME)).getAllData(ATUtils.TAG_AT_WINDOW_COLOR);
			try {
				Drawable background = main_layout.getBackground();
				if (background instanceof ShapeDrawable) {
					((ShapeDrawable) background).getPaint().setColor(Color.parseColor(ATUtils.windowColorArray[bgColorPos]));
				} else if (background instanceof GradientDrawable) {
					((GradientDrawable) background).setColor(Color.parseColor(ATUtils.windowColorArray[bgColorPos]));
				} else if (background instanceof ColorDrawable && android.os.Build.VERSION.SDK_INT >= 11) {
					((ColorDrawable) background).setColor(Color.parseColor(ATUtils.windowColorArray[bgColorPos]));
				}
			} catch (Exception e) {
			}
			DisplayMetrics outMetrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
			int width = 0;
			if (ATUtils.getInstance(mContext).getScreenOrientation() == 1) {
				width = outMetrics.widthPixels;
			} else {
				width = outMetrics.heightPixels;
			}

			boxWidth = width - ((int) ATUtils.convertDpToPixel(80, mContext));

			ViewGroup.LayoutParams params1 = main_layout.getLayoutParams();
			params1.width = boxWidth;
			params1.height = boxWidth;
			main_layout.setLayoutParams(params1);

			ViewGroup.LayoutParams params2 = gridView.getLayoutParams();
			params2.width = boxWidth;
			params2.height = boxWidth;
			gridView.setLayoutParams(params2);

			if (listNumber == 1) { // list fav
				itemList = AssistiveCenter.getFavList(mContext);
			} else if (listNumber == 2) { // list setting
				itemList = AssistiveCenter.getSettingsList(mContext);
			} else {
				itemList = AssistiveCenter.getMainList(mContext);
			}
			adapter = new ATPanelItemsAdapter(mContext, itemList, boxWidth / 3);
			gridView.setAdapter(adapter);
			gridView.setOnItemClickListener(onItemClickListener);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	OnItemClickListener onItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if (!(listNumber != 0 && position == 4)) {
				showAllItemsDialog(mContext, position);
			}
		}
	};

	public void showAllItemsDialog(final Context context, final int pos) {
		final Dialog layout = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
		layout.setContentView(R.layout.at_dialog_layout2);
		layout.setCancelable(true);
		layout.setCanceledOnTouchOutside(true);

		((TextView) layout.findViewById(R.id.title)).setText(context.getResources().getString(R.string.select_action));

		if (listNumber == 2) {
			dialogItemList = DefaultActions.getAllActions(mContext, 2);
		} else {
			dialogItemList = DefaultActions.getAllActions(mContext, 0);
		}

		GridView gridView = (GridView) layout.findViewById(R.id.gridView);
		AllATItemsGridAdapter adapter = new AllATItemsGridAdapter(mContext, dialogItemList);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ATItem bean = dialogItemList.get(position);
				bean.setItemId(pos + 1);
				updateItem(bean);
				showAssistiveCenter();
				boolean isATActivated = AppPrefs.getInstance(context).getBooleanValue(SharedPreferencesName.KEY_IS_AT_ACTIVATED, false);
				if (isATActivated) {
					Intent startServiceIntent = new Intent(context, AssistiveTouch.class);
					startServiceIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
					context.startService(startServiceIntent);
				}
				layout.dismiss();
			}
		});
		layout.show();
	}

	public void updateItem(ATItem bean) {
		if (listNumber == 2) {
			((ATSettingsListTable) AppDb.getInstance(mContext).getTableObject(ATSettingsListTable.TABLE_NAME)).updateData(bean);
		} else {
			((ATMainListTable) AppDb.getInstance(mContext).getTableObject(ATMainListTable.TABLE_NAME)).updateData(bean);
		}
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.back) {
			finish();

		}
	}

}
