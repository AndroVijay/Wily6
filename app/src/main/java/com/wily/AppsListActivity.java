package com.wily;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.wily.adapters.AppsGridAdapter;
import com.wily.db.ATFavListTable;
import com.wily.db.AppDb;
import com.wily.db.Assistive_Db;
import com.wily.utils.ATItem;
import com.wily.utils.ATUtils;
import com.wily.utils.AppInfo;
import com.wily.utils.Constants;
import com.wily.view.AssistiveTouch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

@SuppressLint("NewApi")
public class AppsListActivity extends AppCompatActivity implements OnItemClickListener {

	private GridView appsGrid;
	private ProgressBar progress;
	private AppsGridAdapter adapter;
	private ArrayList<AppInfo> items;
	private int itemId;
	public  static boolean fav_click=true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_apps_list);

		itemId = getIntent().getIntExtra("itemId", 1);


		appsGrid = (GridView) findViewById(com.wily.R.id.apps_grid);
		progress = (ProgressBar) findViewById(com.wily.R.id.progress);
		appsGrid.setVisibility(View.GONE);
		progress.setVisibility(View.VISIBLE);
		new LoadItemsInGrid().execute();
	}

	class LoadItemsInGrid extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... params) {
			items = ATUtils.getInstance(AppsListActivity.this).getInstalledApps();
			Collections.sort(items);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			appsGrid.setVisibility(View.VISIBLE);
			progress.setVisibility(View.GONE);
			adapter = new AppsGridAdapter(AppsListActivity.this, items);
			appsGrid.setAdapter(adapter);
			appsGrid.setOnItemClickListener(AppsListActivity.this);
		}

	}

//	private ArrayList<AssistiveBean> assistiveBeanArrayList;
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		ATItem item = new ATItem();
		item.setItemId(itemId);
//		item.setImageNmae((items.get(position).getIcon()));
		item.setItemName(items.get(position).getName());
		item.setPackageName(items.get(position).getPackageName());
		item.setItemActionName(ATUtils.ACTION_APP);

        Assistive_Db db=new Assistive_Db(this);
        db.update_byID(itemId,items.get(position).getPackageName(), Constants.getByteFromDrawable(items.get(position).getIcon()),"true");

//		itemAssistive_fav_list.add(item);
		updateFavItem(AppsListActivity.this, item);
		Intent intent = new Intent(AppsListActivity.this, AssistiveTouch.class);
		intent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
		intent.putExtra("listNum", 1);
		AppsListActivity.this.startService(intent);
		AppsListActivity.this.finish();
	}

	public static void updateFavItem(Context mContext, ATItem item) {
		Vector<ATItem> array = new Vector<ATItem>();
		array.add(item);
		((ATFavListTable) AppDb.getInstance(mContext).getTableObject(ATFavListTable.TABLE_NAME)).updateData(array);
	}

}
