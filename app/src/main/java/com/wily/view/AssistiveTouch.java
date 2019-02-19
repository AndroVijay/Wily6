package com.wily.view;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;

import com.wily.HomeActivity;
import com.wily.R;
import com.wily.UtilitiesFragment;
import com.wily.db.ATSettingsTable;
import com.wily.db.AppDb;
import com.wily.event.CenterStateEvent;
import com.wily.utils.ATUtils;
import com.wily.utils.AnimationUtil;
import com.wily.utils.Constants;
import com.wily.utils.FlashLightController;

import de.greenrobot.event.EventBus;

import static com.wily.MyServices.NOTIFICATIONCHANNEL_ID;
import static com.wily.MyServices.NOTIFICATION_NAME;

public class AssistiveTouch extends Service implements OnTouchListener {

	private static WindowManager windowManager;
	private static View view;
	Handler handler;

	private static int widthPixels;
	private static int heightPixels;

	private static SharedPreferences mSharedPreferences;

	private static int listNum = -1;

	public static FlashLightController flashController;

	@Override
	public void onCreate() {
		super.onCreate();

		EventBus.getDefault().register(this);
		mSharedPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);

		flashController = new FlashLightController();

		showAssistiveTouch();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		hideAssistiveTouch();
		EventBus.getDefault().unregister(this);
	}


	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent == null) {
			intiUI(intent);
		} else if (intent.getAction().equals(Constants.ACTION.STARTFOREGROUND_ACTION)) {
			intiUI(intent);
		} else if (intent.getAction().equals(Constants.ACTION.STOPFOREGROUND_ACTION)) {
			stopForeground(true);
			stopSelf();
		}
		return START_STICKY;
	}

	@SuppressLint("InlinedApi")
	public void intiUI(Intent intent) {
		Intent notificationIntent = null;

			notificationIntent=new Intent(this,HomeActivity.class);
//			notificationIntent = new Intent(this, Getclassname.setIntent());
			notificationIntent.putExtra("splace", "assistive");
			notificationIntent.setAction(Constants.ACTION.MAIN_ACTION);


			if (android.os.Build.VERSION.SDK_INT >= 15) {
				notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
			} else {
				notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			}

//			PendingIntent pendingIntent=PendingIntent.getActivity(this,0,Getclassname.setIntent(),0);
			PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
			Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);

		NotificationManager notifManager =(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			int importance = NotificationManager.IMPORTANCE_HIGH;
			NotificationChannel mChannel = notifManager.getNotificationChannel(NOTIFICATIONCHANNEL_ID);
			if (mChannel == null) {
				mChannel = new NotificationChannel(NOTIFICATIONCHANNEL_ID, NOTIFICATION_NAME, importance);
				notifManager.createNotificationChannel(mChannel);
			}
		}

			Notification notification = null;
			NotificationCompat.Builder builder = new NotificationCompat.Builder(this,NOTIFICATIONCHANNEL_ID);
			builder.setContentTitle("Assistive Touch");
			builder.setTicker("Assistive Touch");
//			builder.setContentText("Service Runnning");
			builder.setSmallIcon(R.drawable.ic_launcher);
			builder.setLargeIcon(Bitmap.createScaledBitmap(icon, 128, 128, false));
			builder.setContentIntent(pendingIntent);
			builder.setOngoing(true);
			builder.setPriority(Notification.PRIORITY_MIN);
			notification = builder.build();
			startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, notification);
			if (intent != null) {
				listNum = intent.getIntExtra("listNum", -1);
			}
			showAssistiveTouch();
			if (listNum >= 0) {
				AssistiveCenter assistiveCenter = new AssistiveCenter(AssistiveTouch.this);
				assistiveCenter.showAssistiveCenter(listNum);
				listNum = -1;
			}
		}


	@SuppressLint("RtlHardcoded")
	public void showAssistiveTouch() {
		try {
			hideAssistiveTouch();
			windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
			DisplayMetrics outMetrics = new DisplayMetrics();
			windowManager.getDefaultDisplay().getMetrics(outMetrics);
			if (ATUtils.getInstance(this).getScreenOrientation() == 1) {
				widthPixels = outMetrics.widthPixels;
				heightPixels = outMetrics.heightPixels;
			} else {
				widthPixels = outMetrics.widthPixels;
				heightPixels = outMetrics.heightPixels;
			}
			view = View.inflate(this, R.layout.assistive_touch, null);
			int iconPos = ((ATSettingsTable) AppDb.getInstance(AssistiveTouch.this).getTableObject(ATSettingsTable.TABLE_NAME)).getAllData(ATUtils.TAG_AT_ICON);
			int iconId = ATUtils.iconDrawableArray[iconPos];
			((ImageView) view.findViewById(R.id.iv)).setImageResource(iconId);



			view.setOnTouchListener(this);


//					view.setAlpha(.5f);



			final LayoutParams params = new LayoutParams();

			params.gravity = Gravity.LEFT + Gravity.TOP;

			params.x = mSharedPreferences.getInt("Left", 0);
			params.y = mSharedPreferences.getInt("Top", 0);
			if (params.x > 0) {
				params.x = widthPixels - (int) ATUtils.convertDpToPixel(50, this);
			}

			params.height = LayoutParams.WRAP_CONTENT;
			params.width = LayoutParams.WRAP_CONTENT;
			params.flags = LayoutParams.FLAG_NOT_FOCUSABLE;
			//params.type=LayoutParams.TYPE_SYSTEM_ERROR;
			// |
			// WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE|
			// WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
			params.format = PixelFormat.TRANSLUCENT;
			params.type = LayoutParams.TYPE_APPLICATION_OVERLAY;


			windowManager.addView(view, params);


			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					EventBus.getDefault().post(new CenterStateEvent(5));
				}
			},3000);


		/*	view.setAlpha(.05f);
			windowManager.updateViewLayout(view,params);*/


		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void hideAssistiveTouch() {
		try {
			if (null != windowManager && view != null) {
				windowManager.removeView(view);
				windowManager = null;
				view = null;


			//	EventBus.getDefault().post(new CenterStateEvent(2));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	

	private float rawX;
	private float rawY;
	private float startX;
	private float startY;
	private float endX;
	private float endY;

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		try {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				EventBus.getDefault().post(new CenterStateEvent(3));
				rawX = event.getRawX();
				rawY = event.getRawY();
				startX = event.getRawX();
				startY = event.getRawY();
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						EventBus.getDefault().post(new CenterStateEvent(5));
					}
				},3000);
				break;
			case MotionEvent.ACTION_UP:
				EventBus.getDefault().post(new CenterStateEvent(0));
				endX = event.getRawX();
				endY = event.getRawY();
				int deltaX = Math.abs((int) (startX - endX));
				int deltaY = Math.abs((int) (startY - endY));
				if (deltaX < (view.getWidth() / 2) && deltaY < (view.getWidth() / 2)) {
					AssistiveCenter assistiveCenter = new AssistiveCenter(AssistiveTouch.this);
					assistiveCenter.showAssistiveCenter(0);
				}

				LayoutParams params = (LayoutParams) view.getLayoutParams();
				int left = params.x;
				int top = params.y;
				//params.type=LayoutParams.TYPE_SYSTEM_ERROR;
				if (left < (widthPixels / 2)) {
					left = 0;
				} else {
					left = widthPixels - view.getWidth();
					;
				}
				params.x = left;
				windowManager.updateViewLayout(view, params);
				mSharedPreferences.edit().putInt("Left", left).putInt("Top", top).commit();
				rawX = left;
				break;
			case MotionEvent.ACTION_MOVE:
				float newRawX = event.getRawX();
				float newRawY = event.getRawY();
				float moveX = newRawX - rawX;
				float moveY = newRawY - rawY;

				LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
				layoutParams.x += moveX;
				layoutParams.y += moveY;
				//layoutParams.type=LayoutParams.TYPE_SYSTEM_ERROR;

				if (layoutParams.x < 0) {
					layoutParams.x = 0;
				}
				if (layoutParams.y < 0) {
					layoutParams.y = 0;
				}
				if (layoutParams.x + view.getWidth() > widthPixels) {
					layoutParams.x = widthPixels - view.getWidth();
				}
				if (layoutParams.y + view.getHeight() > heightPixels - 30) {
					layoutParams.y = heightPixels - 30 - view.getHeight();
				}

				windowManager.updateViewLayout(view, layoutParams);

				rawX = newRawX;
				rawY = newRawY;
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						EventBus.getDefault().post(new CenterStateEvent(5));
					}
				},3000);
				break;
			default:
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// Used only in case of bound services.
		return null;
	}

	/**
	 * EventBus
	 *
	 * @param event
	 */
	public void onEvent(CenterStateEvent event) {


		if(view!=null)
			AnimationUtil.alphaAnimation(view.findViewById(R.id.iv), event.isOpen(),(event.isOpen()==5?3000:1000));
//		AnimationUtil.alphaAnimation(view.findViewById(R.id.iv), event.isOpen());
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		DisplayMetrics outMetrics = new DisplayMetrics();
		windowManager.getDefaultDisplay().getMetrics(outMetrics);
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			widthPixels = outMetrics.widthPixels;
			heightPixels = outMetrics.heightPixels;
		} else {
			widthPixels = outMetrics.widthPixels;
			heightPixels = outMetrics.heightPixels;
		}
		if (AssistiveCenter.view == null) {
			showAssistiveTouch();
		} else {
			EventBus.getDefault().post(new CenterStateEvent(2));
		}
	}
}