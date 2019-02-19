package info.earntalktime.at.view;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import info.earntalktime.at.AppsListActivity;
import info.earntalktime.at.R;
import info.earntalktime.at.adapters.ATItemsGridAdapter;
import info.earntalktime.at.assistivebean.AssistiveBean;
import info.earntalktime.at.db.ATFavListTable;
import info.earntalktime.at.db.ATMainListTable;
import info.earntalktime.at.db.ATSettingsListTable;
import info.earntalktime.at.db.ATSettingsTable;
import info.earntalktime.at.db.AppDb;
import info.earntalktime.at.db.Assistive_Db;
import info.earntalktime.at.event.CenterStateEvent;
import info.earntalktime.at.utils.ATItem;
import info.earntalktime.at.utils.ATUtils;
import info.earntalktime.at.utils.Constants;
import info.earntalktime.at.utils.FlashLightController;
import rationalheads.circlemenu.CircleImageView;
import rationalheads.circlemenu.CircleLayout;

import static info.earntalktime.at.AppsListActivity.fav_click;
import static info.earntalktime.at.utils.ATUtils.context;
import static info.earntalktime.at.utils.ATUtils.utils;

public class AssistiveCenter {

	private static WindowManager windowManager;
	public static View view;
	private static RelativeLayout main_layout/* , layout1, layout2 */;
	static Animation zoomInFadein, zoomOutFadeout, zoomIn, zoomOut;

	private static Context mContext;
	AlertDialog.Builder builder;


	private CircleLayout circleView;
	ImageView assistive_back;
	private MyGridView gridView;
	private ATItemsGridAdapter adapter;
	private ArrayList<ATItem> itemList;

	private ArrayList<AssistiveBean> itemAssistive;
	private ArrayList<AssistiveBean> itemAssistive_list2;
	public static ArrayList<AssistiveBean> itemAssistive_fav_list;
	private int boxWidth = 0;
	public static final int animDur = 200;
	AssistiveBean bean;
	private  int status=0;
	boolean st_pass=true;

//	public void setItemAssistive(ArrayList<AssistiveBean> itemAssistive) {
//		this.itemAssistive = itemAssistive;
//	}

	int icons[] = {
			R.drawable.assistive_favourite_apps,
            R.drawable.home1,
			R.drawable.assistive_lock,
			R.drawable.assistive_screenshot,
			R.drawable.assistive_settings,
			R.drawable.assistive_torch,
			R.drawable.assistive_silent
//            R.drawable.assistive_gps,
//            R.drawable.assistive_silent,
//            R.drawable.assistive_back,
//            R.drawable.assistive_add_shortcut,
//            R.drawable.assistive_wifi,
//            R.drawable.assistive_touch_menu


    };
    int icons_list2[]={
            R.drawable.assistive_gps,
//            R.drawable.assistive_back,
//            R.drawable.assistive_add_shortcut,
            R.drawable.assistive_wifi,
			R.drawable.airplane_mode,
			R.drawable.bluetooth,
			R.drawable.assistive_assistive_volume_up,
			R.drawable.assistive_volume_low,
			R.drawable.asistive_rotate,
//			R.drawable.vibration,
//			R.drawable.silent

    };




    int backgroung[]={

			R.drawable.circle,
			R.drawable.circle1,
			R.drawable.circle3,
			R.drawable.circle4,
			R.drawable.circle5,
			R.drawable.circle6,
			R.drawable.circle7

	};




	private void initlistdate(){

		itemAssistive=new ArrayList<>();
		itemAssistive_list2=new ArrayList<>();
		itemAssistive_fav_list=new ArrayList<>();
		for (int i = 0; i < icons.length; i++) {
			bean = new AssistiveBean();
//            bean.setIcon(new BitmapDrawable());
			bean.setIcon(mContext.getResources().getDrawable(icons[i]));
			bean.setItemId(i);
			bean.setBackground(mContext.getResources().getDrawable(backgroung[i]));
			itemAssistive.add(bean);
		}


		for (int i = 0; i <icons_list2.length ; i++) {
			bean = new AssistiveBean();
			bean.setIcon(mContext.getResources().getDrawable(icons_list2[i]));
			bean.setItemId(icons.length+i);
			itemAssistive_list2.add(bean);
		}
		assistive_db = new Assistive_Db(mContext);
		db = assistive_db.getWritableDatabase();
		assistive_db.onCreate(db);
		db=assistive_db.getWritableDatabase();

		 cursor = assistive_db.getData("SELECT * FROM Wily_Assist");
		for (int i = 0; i <7; i++) {
			while (cursor.moveToNext()) {
				bean = new AssistiveBean();

				bean.setItemId(Integer.parseInt(cursor.getString(0)));
				bean.setPackageName(cursor.getString(1));
				bean.setIcon((Constants.getDrawableFromByte(cursor.getBlob(2))));
				bean.setIsclicked(cursor.getString(3));


//			list.add(new Food(name, price, image, id));

				itemAssistive_fav_list.add(bean);
			}
		}
//			itemAssistive_fav_list=getFavList(mContext);


//		userDbHelper.close();





	}

	private Assistive_Db assistive_db;
	private SQLiteDatabase db;
	Cursor cursor;

	public  void updateFaverateItem( AssistiveBean id){
		for (int i = 0; i <itemAssistive_fav_list.size() ; i++) {

			bean=new AssistiveBean();
					if(id.getItemId()==itemAssistive_fav_list.get(i).getItemId())
 					{

//						bean.set

						//bean.setIcon();


					}


		}

	}

	public AssistiveCenter(Context context) {
		mContext = context;
		zoomInFadein = AnimationUtils.loadAnimation(mContext, R.anim.zoomin_fadein);
		zoomOutFadeout = AnimationUtils.loadAnimation(mContext, R.anim.zoomout_fadeout);
		zoomIn = AnimationUtils.loadAnimation(mContext, R.anim.zoom_in);
		zoomOut = AnimationUtils.loadAnimation(mContext, R.anim.zoom_out);
	}

	@SuppressLint("NewApi")
	public void showAssistiveCenter(int listNumber) {
		try {
			hideAssistiveCenter();
			EventBus.getDefault().post(new CenterStateEvent(2));
			windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
			view = View.inflate(mContext, R.layout.assistive_center, null);
			main_layout = (RelativeLayout) view.findViewById(R.id.main_layout);

			circleView = (CircleLayout) view.findViewById(R.id.items_grid1);
			assistive_back= (ImageView) view.findViewById(R.id.assistive_back);
			assistive_back.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					circleView.startAnimation(zoomOut);
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {

							if (circleView != null && circleView.getChildCount() > 0)
								circleView.removeAllViews();

							status=0;
							for (int i = 0; i < itemAssistive.size(); i++) {
								CircleImageView newMenu = new CircleImageView(mContext);
								newMenu.setBackgroundDrawable(itemAssistive.get(i).getBackground());
								final int pos = i;
								newMenu.setImageDrawable(itemAssistive.get(i).getIcon());
								circleView.addView(newMenu);
							}



							circleView.startAnimation(zoomIn);
						}
					},animDur);


				}
			});
//			CircleLayout gridViewt= (CircleLayout) view.findViewById(R.id.items_grid);
			gridView = (MyGridView) view.findViewById(R.id.items_grid);
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
			windowManager.getDefaultDisplay().getMetrics(outMetrics);
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
				itemList = getFavList(mContext);
			} else if (listNumber == 2) { // list setting
				itemList = getSettingsList(mContext);
			} else {
				itemList = getMainList(mContext);
			}
			adapter = new ATItemsGridAdapter(mContext, itemList, boxWidth / 3);
			gridView.setAdapter(adapter);
			gridView.setOnItemClickListener(onItemClickListener);
			gridView.setOnItemLongClickListener(onItemLongClickListener);


			new AsyncTask<Void,Void,Void>(){

				@Override
				protected Void doInBackground(Void... params) {



					return null;
				}
			};

initlistdate();

			final Drawable sDraw = mContext.getResources().getDrawable( info.earntalktime.at.R.drawable.assistive_add_shortcut);

//		int	itemId = mContext.getIntent().getIntExtra("itemId", 1);

			if (circleView!=null && circleView.getChildCount()>0)
				circleView.removeAllViews();
			for (int i = 0; i <itemAssistive.size(); i++) {

				CircleImageView newMenu = new CircleImageView(mContext);
				ImageView imageView = new ImageView(mContext);
//				imageView.setLayoutParams(new ViewGroup.LayoutParams(100, 100));


				newMenu.setBackgroundDrawable(itemAssistive.get(i).getBackground());
				final int pos = i;

//				if(fav_click)
				newMenu.setImageDrawable(itemAssistive.get(i).getIcon());
//				else{
//					newMenu.setImageDrawable(itemAssistive_fav_list.get(i).getIcon());
//				bean.setItemId((icons.length)+(icons_list2.length)+i);
//				itemAssistive_fav_list.add(bean);
				//}
				circleView.addView(newMenu);


				circleView.setOnItemClickListener(new CircleLayout.OnItemClickListener() {
					@Override
					public void onItemClick(View view) {

						int id;
						if(status==0)
						id=itemAssistive.get(circleView.indexOfChild(view)).getItemId();
						else if(status==1)
							id=itemAssistive_list2.get(circleView.indexOfChild(view)).getItemId();
						else if(status==2)
							id=itemAssistive_fav_list.get(circleView.indexOfChild(view)).getItemId();
						else
							id=itemAssistive_fav_list.get(circleView.indexOfChild(view)).getItemId();
//						int id2=itemAssistive_list2.get(circleView.indexOfChild(view)).getItemId();
//							String a=""+itemAssistive.get(id).getItemId();
//						Toast.makeText(mContext, ""+itemAssistive.get(circleView.indexOfChild(view)).getItemId(), Toast.LENGTH_SHORT).show();


						Drawable fDraw = ((ImageView)view).getDrawable();


//						Drawable fDraw=circleView.getBackground();
					switch (id) {


						case 0:

							circleView.startAnimation(zoomOut);
							new Handler().postDelayed(new Runnable() {
								@Override
								public void run() {

									if (circleView != null && circleView.getChildCount() > 0)
										circleView.removeAllViews();
									status=2;

									for (int i = 0; i <7; i++) {
										CircleImageView newMenu = new CircleImageView(mContext);
										newMenu.setLayoutParams(new ViewGroup.LayoutParams(160,160));
										newMenu.setBackgroundDrawable(itemAssistive.get(i).getBackground());
										newMenu.setPadding(30,30,30,30);
										final int pos = i;
										newMenu.setImageDrawable(itemAssistive_fav_list.get(i).getIcon());
										circleView.addView(newMenu);

									}
									circleView.startAnimation(zoomIn);
								}

							}, animDur);

//							updateFaverateItem()


							break;

						case 1:
							hideAssistiveCenter();
							utils.homeIntent();


							break;

						case 2:
							hideAssistiveCenter();
							if (utils.haveLockPermission()) {
								utils.lockPhone();
							} else {
								utils.intentLockPermission();
							}

							break;

						case 3:

							if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
								hideAssistiveCenter();
								utils.takeScreenShot();
							}else
							{
								hideAssistiveCenter();

								showToast();
								/*Toast toast = new Toast(mContext);
								toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
								toast.setDuration(Toast.LENGTH_LONG);
//								toast.setView("");
								toast.setText("“Unfortunately, the screenshot feature may not be available for Android Kitkat or below.”");
								toast.show();*/

//								alertDialog();

							}


							break;

						case 4:

								circleView.startAnimation(zoomOut);


								new Handler().postDelayed(new Runnable() {
									@Override
									public void run() {

										if (circleView != null && circleView.getChildCount() > 0)
											circleView.removeAllViews();
											status=1;
							for (int i = 0; i < itemAssistive_list2.size(); i++) {
								CircleImageView newMenu = new CircleImageView(mContext);
								newMenu.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
								newMenu.setBackgroundDrawable(itemAssistive.get(i).getBackground());
								final int pos = i;
								newMenu.setImageDrawable(itemAssistive_list2.get(i).getIcon());
								circleView.addView(newMenu);

							}
										circleView.startAnimation(zoomIn);
										}

								}, animDur);




							break;

						case 5:


							if (AssistiveTouch.flashController == null) {
								AssistiveTouch.flashController = new FlashLightController();
							}
							if (AssistiveTouch.flashController.isFlashOn()) {
								AssistiveTouch.flashController.turnFlash_ON_OFF(false);
								// itemList.get(position).setStatus(0);
							} else {
								//itemList.get(position).setStatus(1);
								AssistiveTouch.flashController.turnFlash_ON_OFF(true);
							}


							break;
						case 6:

//							utils.changeSoundMode();
							utils.decreaseVolume();
							break;

						case 7:
							utils.gpsIntent();
							hideAssistiveCenter();

							break;

						case 8:
							if (utils.getWiFiStatus()) {
								utils.enableDisableWiFi(false);

							} else {
								utils.enableDisableWiFi(true);

							}
							break;
						case 9:

							utils.airplaneModeIntent();
							hideAssistiveCenter();

							break;
						case 10:

							if (utils.isBluetoothOn()) {
								utils.turnBluetoothOn(0);
							} else {
								utils.turnBluetoothOn(1);
							}
							new Handler().postDelayed(new Runnable() {
								@Override
								public void run() {
//									adapter.notifyDataSetChanged();
								}
							}, 3000);



							break;
						case 11 :

							utils.increaseVolume();


							break;
						case 12:
							utils.decreaseVolume();


							break;
						case 13:
							if (utils.getScreenRotation() == 1) {
								utils.setScreenRotation(0);
							} else {
								utils.setScreenRotation(1);
							}

							break;
						case 14:

							if (itemAssistive_fav_list.get(0).getIsclicked().equals("false")) {
								applist(id);
							}else {
								openApp(id);
							}
							//	status=3;

							break;
						case 15:

							if (itemAssistive_fav_list.get(1).getIsclicked().equals("false")) {
								applist(id);
							}else {
								openApp(id);
							}



							break;
						case 16:

							if (itemAssistive_fav_list.get(2).getIsclicked().equals("false")) {
								applist(id);
							}else {
								openApp(id);
							}

							break;
						case 17:
							//Drawable fDraw = view.getBackground();

							if (itemAssistive_fav_list.get(3).getIsclicked().equals("false")) {
								applist(id);
							}else {
								openApp(id);
							}


						/*	if (itemAssistive_fav_list.get(3).getIsclicked().equals("false")) {
//							if (fav_click) {
								itemList = getFavList(mContext);
								Intent intent = new Intent(mContext, AppsListActivity.class);
								intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								intent.putExtra("itemId", id);
								mContext.startActivity(intent);
								hideAssistiveCenter();
								fav_click = false;
							}else {

								*//*Assistive_Db db = new Assistive_Db(mContext);
								db.getReadableDatabase();
								cursor = assistive_db.getData("SELECT package FROM Wily_Assist WHERE app_id="+id+"");
								String p_name = null;
								while (cursor.moveToFirst()) {
									p_name = cursor.getString(1);

								}

								hideAssistiveCenter();
								utils.getIntentByPackage(p_name);*//*
								Assistive_Db db=new Assistive_Db(mContext);
								String p_name=db.getSingleDate(""+id);
								hideAssistiveCenter();
								utils.getIntentByPackage(p_name);
							}
//							Toast.makeText(mContext, ""+17, Toast.LENGTH_SHORT).show();
*/

							break;
						case 18:
							//Drawable fDraw = view.getBackground();
							if (itemAssistive_fav_list.get(4).getIsclicked().equals("false")) {
								applist(id);
							}else {
								openApp(id);
							}

							break;
						case 19:
							if (itemAssistive_fav_list.get(5).getIsclicked().equals("false")) {
								applist(id);
							}else {
								openApp(id);
							}
							break;
						case  20:
							if (itemAssistive_fav_list.get(6).getIsclicked().equals("false")) {
								applist(id);
							}else {
								openApp(id);
							}
							break;

					}
				}






					//}
				});
			}








			RelativeLayout back = (RelativeLayout) view.findViewById(R.id.back);
			back.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					hideAssistiveCenter();
				}
			});

			final LayoutParams params = new LayoutParams();

			params.gravity = Gravity.CENTER;
			params.height = LayoutParams.MATCH_PARENT;
			params.width = LayoutParams.MATCH_PARENT;
			params.flags |= LayoutParams.FLAG_FULLSCREEN;
			// params.flags |= LayoutParams.FLAG_NOT_TOUCH_MODAL;
			// params.flags |= LayoutParams.FLAG_NOT_FOCUSABLE;
			// WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
			// WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;

			params.format = PixelFormat.TRANSLUCENT;

			params.type = LayoutParams.TYPE_TOAST;

			windowManager.addView(view, params);
			main_layout.startAnimation(zoomInFadein);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void showToast() {
		Toast toast = Toast.makeText(mContext, "“Unfortunately, the screenshot feature may not be available for Android Kitkat or below.”", Toast.LENGTH_LONG);
		View toastView = toast.getView(); //This'll return the default View of the Toast.

        /* And now you can get the TextView of the default View of the Toast. */
		TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
		toastMessage.setTextSize(16);
		toastMessage.setTextColor(Color.BLACK);
//                toastMessage.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_launcher_round, 0, 0, 0);
		toastMessage.setGravity(Gravity.CENTER_VERTICAL);
//                toastMessage.setCompoundDrawablePadding(16);
		toastView.setBackgroundColor(Color.WHITE);
		toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
		toast.show();
	}

	private void alertDialog()
	{
		/*builder = new AlertDialog.Builder(mContext);
		builder.setTitle("Wily Alert")
				.setMessage("“Unfortunately, the screenshot feature may not be available for Android Kitkat or below.”")
				.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// continue with delete

					}
				})
				*//*.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// do nothing
					}
				})*//*
				.setIcon(android.R.drawable.ic_dialog_alert)
				.show();*/


		AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
		builder1.setMessage("Write your message here.");
		builder1.setCancelable(true);

		builder1.setPositiveButton(
				"Yes",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});

		builder1.setNegativeButton(
				"No",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});

		AlertDialog alert11 = builder1.create();
		alert11.show();


	}
	private void applist(int id ) {
		itemList = getFavList(mContext);
		Intent intent = new Intent(mContext, AppsListActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("itemId", id);
		mContext.startActivity(intent);
		hideAssistiveCenter();
		fav_click = false;
	}
	private void openApp(int id){
		Assistive_Db db = new Assistive_Db(mContext);
		String p_name=db.getSingleDate(""+id);
		hideAssistiveCenter();
		utils.getIntentByPackage(p_name);

	}


	public static void hideAssistiveCenter() {
		try {
			if (null != windowManager && view != null) {
				main_layout.startAnimation(zoomOutFadeout);
				new Handler().postDelayed(new Runnable() {
					public void run() {
						if (null != windowManager && view != null) {
							windowManager.removeView(view);
							windowManager = null;
							view = null;
							EventBus.getDefault().post(new CenterStateEvent(4));
						}
					}
				}, 100);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}




	OnItemClickListener onItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			clickItemsAccordingly(itemList.get(position), position);
		}
	};

	OnItemLongClickListener onItemLongClickListener = new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
			ATItem atItem = itemList.get(position);
			String itemAction = atItem.getItemActionName();
			if (ATUtils.ACTION_APP.equals(itemAction)) {
				ATItem item = new ATItem();
				item.setItemId(atItem.getItemId());
				item.setItemActionName(ATUtils.ACTION_ADD_APP);
				item.setItemName(mContext.getResources().getString(R.string.add_item));
				itemList.add(item);
				AppsListActivity.updateFavItem(mContext, item);
				itemList = getFavList(mContext);
				adapter = new ATItemsGridAdapter(mContext, itemList, boxWidth / 3);
				gridView.setAdapter(adapter);
				gridView.setOnItemClickListener(onItemClickListener);
				gridView.setOnItemLongClickListener(onItemLongClickListener);
				return true;
			} else {
				return false;
			}
//		return false;
		}

	};
	@SuppressLint("NewApi")
	private void clickItemsAccordingly(ATItem atItem, int position) {
		ATUtils utils = ATUtils.getInstance(mContext);
		String itemAction = atItem.getItemActionName();
		if (ATUtils.ACTION_HOME.equals(itemAction)) {
			hideAssistiveCenter();
			utils.homeIntent();
		} else if (ATUtils.ACTION_RECENT.equals(itemAction)) {
			hideAssistiveCenter();
			utils.recentsIntent();
		} else if (ATUtils.ACTION_LOCK.equals(itemAction)) {
			hideAssistiveCenter();
			if (utils.haveLockPermission()) {
				utils.lockPhone();
			} else {
				utils.intentLockPermission();
			}
		} else if (ATUtils.ACTION_SETTING.equals(itemAction)) {
			gridView.startAnimation(zoomOut);
			new Handler().postDelayed(new Runnable() {
				public void run() {
					itemList = getSettingsList(mContext);
					adapter = new ATItemsGridAdapter(mContext, itemList, boxWidth / 3);
					gridView.setAdapter(adapter);
					gridView.setOnItemClickListener(onItemClickListener);
					gridView.setOnItemLongClickListener(onItemLongClickListener);
					gridView.startAnimation(zoomIn);
				}
			}, animDur);
		} else if (ATUtils.ACTION_FAV.equals(itemAction)) {
			gridView.startAnimation(zoomOut);
			new Handler().postDelayed(new Runnable() {
				public void run() {
					itemList = getFavList(mContext);
					adapter = new ATItemsGridAdapter(mContext, itemList, boxWidth / 3);
					gridView.setAdapter(adapter);
					gridView.setOnItemClickListener(onItemClickListener);
					gridView.setOnItemLongClickListener(onItemLongClickListener);
					gridView.startAnimation(zoomIn);
				}
			}, animDur);
		} else if (ATUtils.ACTION_ADD_APP.equals(itemAction)) {
			Intent intent = new Intent(mContext, AppsListActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra("itemId", atItem.getItemId());
			mContext.startActivity(intent);
			hideAssistiveCenter();
		} else if (ATUtils.ACTION_APP.equals(itemAction)) {
			hideAssistiveCenter();
			utils.getIntentByPackage(atItem.getPackageName());
		} else if (ATUtils.ACTION_AIRPLANE.equals(itemAction)) {
			utils.airplaneModeIntent();
			hideAssistiveCenter();
		} else if (ATUtils.ACTION_BLUETOOTH.equals(itemAction)) {
			if (utils.isBluetoothOn()) {
				utils.turnBluetoothOn(0);
			} else {
				utils.turnBluetoothOn(1);
			}
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					adapter.notifyDataSetChanged();
				}
			}, 3000);
		} else if (ATUtils.ACTION_FLASHLIGHT.equals(itemAction)) {
			if (AssistiveTouch.flashController == null) {
				AssistiveTouch.flashController = new FlashLightController();
			}
			if (AssistiveTouch.flashController.isFlashOn()) {
				AssistiveTouch.flashController.turnFlash_ON_OFF(false);
				itemList.get(position).setStatus(0);
			} else {
				itemList.get(position).setStatus(1);
				AssistiveTouch.flashController.turnFlash_ON_OFF(true);
			}
			adapter.notifyDataSetChanged();
		} else if (ATUtils.ACTION_LOC.equals(itemAction)) {
			utils.gpsIntent();
			hideAssistiveCenter();
		} else if (ATUtils.ACTION_SCREEN_ROT.equals(itemAction)) {
			if (utils.getScreenRotation() == 1) {
				utils.setScreenRotation(0);
			} else {
				utils.setScreenRotation(1);
			}
			adapter.notifyDataSetChanged();
		} else if (ATUtils.ACTION_SCREENSHOT.equals(itemAction)) {
			hideAssistiveCenter();
			utils.takeScreenShot();
		} else if (ATUtils.ACTION_SOUND_MODE.equals(itemAction)) {
			utils.changeSoundMode();
			adapter.notifyDataSetChanged();
		} else if (ATUtils.ACTION_WIFI.equals(itemAction)) {
			if (utils.getWiFiStatus()) {
				utils.enableDisableWiFi(false);
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						adapter.notifyDataSetChanged();
					}
				}, 1000);
			} else {
				utils.enableDisableWiFi(true);
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						adapter.notifyDataSetChanged();
					}
				}, 6000);
			}
		} else if (ATUtils.ACTION_VOL_UP.equals(itemAction)) {
			utils.increaseVolume();
		} else if (ATUtils.ACTION_VOL_DOWN.equals(itemAction)) {
			utils.decreaseVolume();
		} else if (ATUtils.ACTION_MEDIA_VOL_UP.equals(itemAction)) {
			utils.increaseMediaVolume();
		} else if (ATUtils.ACTION_MEDIA_VOL_DOWN.equals(itemAction)) {
			utils.decreaseMediaVolume();
		} else if (ATUtils.ACTION_BACK.equals(itemAction)) {
			gridView.startAnimation(zoomOut);
			new Handler().postDelayed(new Runnable() {
				public void run() {
					itemList = getMainList(mContext);
					adapter = new ATItemsGridAdapter(mContext, itemList, boxWidth / 3);
					gridView.setAdapter(adapter);
					gridView.setOnItemClickListener(onItemClickListener);
					gridView.setOnItemLongClickListener(onItemLongClickListener);
					gridView.startAnimation(zoomIn);
				}
			}, animDur);
		} else {
		}
	}

	public static ArrayList<ATItem> getMainList(Context mContext) {
		return ((ATMainListTable) AppDb.getInstance(mContext).getTableObject(ATMainListTable.TABLE_NAME)).checkAndGetData(mContext);
	}

	public static ArrayList<ATItem> getSettingsList(Context mContext) {
		return ((ATSettingsListTable) AppDb.getInstance(mContext).getTableObject(ATSettingsListTable.TABLE_NAME)).checkAndGetData(mContext);
	}

	public static ArrayList<ATItem> getFavList(Context mContext) {
		return ((ATFavListTable) AppDb.getInstance(mContext).getTableObject(ATFavListTable.TABLE_NAME)).checkAndGetData(mContext);
	}


}
