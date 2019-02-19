package info.earntalktime.at.utils;

import android.app.admin.DevicePolicyManager;
import android.bluetooth.BluetoothAdapter;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;

import de.greenrobot.event.EventBus;
import info.earntalktime.at.DeviceAdminActivity;
import info.earntalktime.at.R;
import info.earntalktime.at.ScreenShotActivity;
import info.earntalktime.at.event.CenterStateEvent;
import info.earntalktime.at.recievers.AdminReceiver;

public class ATUtils {

	public static ATUtils utils;
	public static Context context;

	public static final String ACTION_SETTING = "SETTING";
	public static final String ACTION_BACK = "BACK";
	public static final String ACTION_FAV = "FAV";
	public static final String ACTION_ADD_APP = "ADD_APP";
	public static final String ACTION_APP = "APP";

	public static final String ACTION_HOME = "HOME";
	public static final String ACTION_RECENT = "RECENT";
	public static final String ACTION_LOCK = "LOCK";
	public static final String ACTION_LOC = "LOCATION";
	public static final String ACTION_WIFI = "WIFI";
	public static final String ACTION_AIRPLANE = "AIRPLANE";
	public static final String ACTION_BLUETOOTH = "BLUETOOTH";
	public static final String ACTION_SCREEN_ROT = "SCREEN_ROT";
	public static final String ACTION_FLASHLIGHT = "FLASHLIGHT";
	public static final String ACTION_SOUND_MODE = "SOUND_MODE";
	public static final String ACTION_VOL_UP = "VOL_UP";
	public static final String ACTION_VOL_DOWN = "VOL_DOWN";
	public static final String ACTION_MEDIA_VOL_UP = "MEDIA_VOL_UP";
	public static final String ACTION_MEDIA_VOL_DOWN = "MEDIA_VOL_DOWN";
	public static final String ACTION_SCREENSHOT = "SCREENSHOT";

	public static final String TAG_AT_ICON = "atIcon";
	public static final String TAG_AT_WINDOW_COLOR = "windowColor";

	public static final String[] windowColorArray = new String[] { "#dd5262bc", "#dd86665a", "#dd3493e6", "#dd96c85b", "#dda7a7a7", "#ddd64444", "#dd1cb0f4", "#ddd1df4c", "#ddea3472", "#dd19c2d7", "#ddfeec4e", "#dda53cb7", "#dd199f93", "#ddfec620", "#dd6f8a96", "#dd6d49b8", "#dd529355", "#ddfea119" };
	public static final int[] iconDrawableArray = new int[] { R.drawable.at_icon_1, R.drawable.at_icon_2, R.drawable.at_icon_3, R.drawable.at_icon_4, R.drawable.at_icon_5, R.drawable.at_icon_6, R.drawable.at_icon_7, R.drawable.at_icon_8, R.drawable.at_icon_9, R.drawable.at_icon_10, R.drawable.at_icon_11, R.drawable.at_icon_12, R.drawable.at_icon_13, R.drawable.at_icon_14, R.drawable.at_icon_15, R.drawable.at_icon_16, R.drawable.at_icon_17, R.drawable.at_icon_18, R.drawable.at_icon_19, R.drawable.at_icon_20 };

	public static ATUtils getInstance(Context context) {
		ATUtils.context = context;
		return utils != null ? utils : new ATUtils();
	}

	public ATUtils() {
		utils = this;
	}

	/**
	 * GO TO HOME
	 **/
	public void homeIntent() {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		// intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// intent.setFlags(268435456);
		context.startActivity(intent);
	}

	/**
	 * OPEN RECENTS
	 **/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void recentsIntent() {
		try {
			Class localClass1 = Class.forName("android.os.ServiceManager");
			IBinder localIBinder = (IBinder) localClass1.getMethod("getService", new Class[] { String.class }).invoke(localClass1, new Object[] { "statusbar" });
			Class localClass2 = Class.forName(localIBinder.getInterfaceDescriptor());
			Object localObject = localClass2.getClasses()[0].getMethod("asInterface", new Class[] { IBinder.class }).invoke(null, new Object[] { localIBinder });
			Method localMethod = localClass2.getMethod("toggleRecentApps", new Class[0]);
			localMethod.setAccessible(true);
			localMethod.invoke(localObject, new Object[0]);
			return;
		} catch (ClassNotFoundException localClassNotFoundException) {
			localClassNotFoundException.printStackTrace();
			return;
		} catch (NoSuchMethodException localNoSuchMethodException) {
			localNoSuchMethodException.printStackTrace();
			return;
		} catch (IllegalAccessException localIllegalAccessException) {
			localIllegalAccessException.printStackTrace();
			return;
		} catch (IllegalArgumentException localIllegalArgumentException) {
			localIllegalArgumentException.printStackTrace();
			return;
		} catch (InvocationTargetException localInvocationTargetException) {
			localInvocationTargetException.printStackTrace();
			return;
		} catch (RemoteException localRemoteException) {
			localRemoteException.printStackTrace();
		}
	}

	/**
	 * GO TO AIRPLANE Mode Settings
	 **/
	public void airplaneModeIntent() {
		try {
			Intent localIntent = new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
			localIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(localIntent);
			return;
		} catch (ActivityNotFoundException localActivityNotFoundException) {
			showToast("This device is not supported!", Toast.LENGTH_SHORT);
		}
	}

	/**
	 * GET AIRPLANE Mode Status
	 **/
	public boolean isAirplaneModeOn() {
		int i = Settings.System.getInt(context.getContentResolver(), "airplane_mode_on", 0);
		boolean bool = false;
		if (i != 0) {
			bool = true;
		}
		return bool;
	}

	/**
	 * GET BLUETOOTH Mode Status
	 **/
	public boolean isBluetoothOn() {
		BluetoothAdapter b = BluetoothAdapter.getDefaultAdapter();
		try {
			boolean bool = b.isEnabled();
			return bool;
		} catch (NullPointerException localNullPointerException) {
		}
		return false;
	}

	/**
	 * Turn Bluetooth ON/OFF
	 **/
	public void turnBluetoothOn(int paramInt) {
		BluetoothAdapter b = BluetoothAdapter.getDefaultAdapter();
		try {
			if (paramInt == 0) {
				b.disable();
				return;
			} else {
				b.enable();
				return;
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open BLUETOOTH settings
	 **/
	public void bluetoothIntent() {
		Intent localIntent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
		localIntent.setFlags(268435456);
		context.startActivity(localIntent);
	}

	/**
	 * Open Application INTENT By Package Name
	 **/
	public boolean getIntentByPackage(String paramString) {
		PackageManager b = context.getPackageManager();
		Intent localIntent = b.getLaunchIntentForPackage(paramString);
		boolean bool = false;
		try {
			if (localIntent != null) {
				context.startActivity(localIntent);
				bool = true;
				return bool;
			}
		} catch (ActivityNotFoundException localActivityNotFoundException) {
			showToast("Not found", Toast.LENGTH_SHORT);
		}
		return false;
	}

	/**
	 * GET GPS status
	 **/
	public boolean isGPSEnabled() {
		LocationManager b = ((LocationManager) context.getSystemService(Context.LOCATION_SERVICE));
		if (b.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			return true;
		}
		return false;
	}

	/**
	 * Go To GPS settings
	 **/
	public void gpsIntent() {
		Intent localIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		localIntent.setFlags(268435456);
		context.startActivity(localIntent);
	}

	/**
	 * GET Screen Orientation status
	 * 
	 * @return paramInt : 0-Porttrait, 1-Auto
	 **/
	public int getScreenRotation() {
		try {
			int i = Settings.System.getInt(context.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION);
			return i;
		} catch (Settings.SettingNotFoundException localSettingNotFoundException) {
			localSettingNotFoundException.printStackTrace();
		}
		return 0;
	}

	/**
	 * GET Screen Orientation status
	 * 
	 * @return paramInt : 1-Porttrait, 2-Landscape
	 **/
	public int getScreenOrientation() {
		int orientation = context.getResources().getConfiguration().orientation;
		return orientation;
	}

	/**
	 * SET Screen Orientation status
	 * 
	 * @param paramInt
	 *            : 0-Porttrait, 1-Auto
	 **/
	public void setScreenRotation(int paramInt) {
		Settings.System.putInt(context.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, paramInt);
	}

	/**
	 * Have Lock Permission
	 **/
	public boolean haveLockPermission() {
		DevicePolicyManager mgr = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
		ComponentName cn = new ComponentName(context, AdminReceiver.class);
		if (mgr.isAdminActive(cn)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Lock Phone
	 **/
	public void lockPhone() {
		DevicePolicyManager mgr = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
		mgr.lockNow();
	}

	/**
	 * Intent Lock Permission
	 **/
	public void intentLockPermission() {
		Intent intentLock = new Intent(context, DeviceAdminActivity.class);
		intentLock.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intentLock);
	}

	/**
	 * Get installed apps list
	 **/
	@SuppressWarnings("rawtypes")
	public ArrayList<AppInfo> getInstalledApps() {
		ArrayList<AppInfo> applist = new ArrayList<AppInfo>();
		PackageManager localPackageManager = context.getApplicationContext().getPackageManager();
		Intent localIntent = new Intent("android.intent.action.MAIN", null);
		localIntent.addCategory("android.intent.category.LAUNCHER");
		Iterator localIterator = ((ArrayList) context.getApplicationContext().getPackageManager().queryIntentActivities(localIntent, 0)).iterator();
		while (localIterator.hasNext()) {
			ResolveInfo localResolveInfo = (ResolveInfo) localIterator.next();
			AppInfo localAppInfo = new AppInfo();
			localAppInfo.setName(localResolveInfo.loadLabel(localPackageManager).toString());
			localAppInfo.setIcon(localResolveInfo.loadIcon(localPackageManager));
			localAppInfo.setPackageName(localResolveInfo.activityInfo.packageName);
			applist.add(localAppInfo);
		}
		return applist;
	}

	/**
	 * Get App icon by Package Name
	 * 
	 * @return Drawable
	 **/
	public Drawable getApplicationIconFromPackageName(String packageName) {
		Drawable d = null;
		try {
			d = context.getPackageManager().getApplicationIcon(packageName);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		return d;
	}

	/**
	 * Get App icon by Package Name
	 * 
	 * @return String : name of the app
	 **/
	public String getApplicationNameFromPackageName(String packageName) {
		PackageInfo p;
		try {
			p = context.getPackageManager().getPackageInfo(packageName, 0);
			return p.applicationInfo.loadLabel(context.getPackageManager()).toString();
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * Increase phone volume by 1
	 */
	public void increaseVolume() {
		final AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
		int presentVolume = audioManager.getStreamVolume(AudioManager.STREAM_RING);
		if (maxVolume > presentVolume) {
			audioManager.setStreamVolume(AudioManager.STREAM_RING, presentVolume + 1, AudioManager.FLAG_SHOW_UI | AudioManager.FLAG_PLAY_SOUND);
		} else {
			audioManager.setStreamVolume(AudioManager.STREAM_RING, presentVolume, AudioManager.FLAG_SHOW_UI | AudioManager.FLAG_PLAY_SOUND);
		}
	}

	/**
	 * Decrease phone volume by 1
	 */
	public void decreaseVolume() {
		final AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		int minVolume = 0;
		int presentVolume = audioManager.getStreamVolume(AudioManager.STREAM_RING);
		if (presentVolume > minVolume) {
			audioManager.setStreamVolume(AudioManager.STREAM_RING, presentVolume - 1, AudioManager.FLAG_SHOW_UI | AudioManager.FLAG_PLAY_SOUND);
		} else {
			audioManager.setStreamVolume(AudioManager.STREAM_RING, presentVolume, AudioManager.FLAG_SHOW_UI | AudioManager.FLAG_PLAY_SOUND);
		}
	}
	
	/**
	 * Increase media volume by 1
	 */
	public void increaseMediaVolume() {
		final AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		int presentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		if (maxVolume > presentVolume) {
			audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, presentVolume + 1, AudioManager.FLAG_SHOW_UI | AudioManager.FLAG_PLAY_SOUND);
		} else {
			audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, presentVolume, AudioManager.FLAG_SHOW_UI | AudioManager.FLAG_PLAY_SOUND);
		}
	}
	
	/**
	 * Decrease phone volume by 1
	 */
	public void decreaseMediaVolume() {
		final AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		int minVolume = 0;
		int presentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		if (presentVolume > minVolume) {
			audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, presentVolume - 1, AudioManager.FLAG_SHOW_UI | AudioManager.FLAG_PLAY_SOUND);
		} else {
			audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, presentVolume, AudioManager.FLAG_SHOW_UI | AudioManager.FLAG_PLAY_SOUND);
		}
	}

	/**
	 * Change Sound mode of phone
	 */
	public void changeSoundMode() {
		final AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		if (audioManager.getRingerMode() == 0) {
			audioManager.setRingerMode(2);
			return;
		}
		audioManager.setRingerMode(-1 + audioManager.getRingerMode());
	}

	/**
	 * Get Sound mode of phone
	 * 
	 * @return ringerMode : 0-Silent 1-Vibrate 2-Normal
	 */
	public int getSoundMode() {
		final AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		return audioManager.getRingerMode();
	}

	/**
	 * Enable/Disable WiFi
	 * 
	 * @param mobileData
	 * @value true-enable wifi
	 * @value false-disable wifi
	 */
	public void enableDisableWiFi(boolean mobileData) {
		try {
			WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			wifiManager.setWifiEnabled(mobileData);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get WiFi status
	 * 
	 * @return boolean
	 * @value true-wifi enabled
	 * @value false-wifi disabled
	 */
	public boolean getWiFiStatus() {
		try {
			WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			if (wifi.isWifiEnabled()) {
				return true;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Take Screenshot
	 */
	public void takeScreenShot() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				EventBus.getDefault().post(new CenterStateEvent(2));
			}
		}, 200);
		try {
			context.startActivity(new Intent(context, ScreenShotActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
//					EventBus.getDefault().post(new CenterStateEvent(4));
				}
			}, 4000);
		} catch (Exception e) {
			EventBus.getDefault().post(new CenterStateEvent(4));
		}
	}

	/**
	 * Change Color of the image
	 */
	public static Drawable changeColorOfDrawable(Context context, float alpha, int color, int iconId) {
		int iColor = color;
		int red = (iColor & 0xFF0000) / 0xFFFF;
		int green = (iColor & 0xFF00) / 0xFF;
		int blue = iColor & 0xFF;
		float[] matrix = { 0, 0, 0, 0, red, 0, 0, 0, 0, green, 0, 0, 0, 0, blue, 0, 0, 0, alpha, 0 };
		ColorFilter colorFilter = new ColorMatrixColorFilter(matrix);
		Drawable drawable = ContextCompat.getDrawable(context, iconId);
		drawable.mutate().setColorFilter(colorFilter);
		return drawable;
	}

	/**
	 * Show Toast
	 **/
	public void showToast(String text, int duration) {
		Toast.makeText(context, text, duration).show();
	}

	/**
	 * This method converts dp unit to equivalent pixels, depending on device
	 * density.
	 * 
	 * @param dp
	 *            A value in dp (density independent pixels) unit. Which we need
	 *            to convert into pixels
	 * @param context
	 *            Context to get resources and device specific display metrics
	 * @return A float value to represent px equivalent to dp depending on
	 *         device density
	 */
	public static float convertDpToPixel(float dp, Context context) {
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
		return px;
	}

	/**
	 * This method converts device specific pixels to density independent
	 * pixels.
	 * 
	 * @param px
	 *            A value in px (pixels) unit. Which we need to convert into db
	 * @param context
	 *            Context to get resources and device specific display metrics
	 * @return A float value to represent dp equivalent to px value
	 */
	public static float convertPixelsToDp(float px, Context context) {
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float dp = px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
		return dp;
	}
}
