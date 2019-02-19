package com.wily.applock;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import com.wily.PreferenceHandler;
import com.wily.SplashActivity;
import com.wily.utilss.GlobalVariables;

import java.util.List;

import static com.wily.SplashActivity.sqLiteDatabase;
import static com.wily.SplashActivity.userDbHelper;

public class Aplock_Accessibilty extends AccessibilityService {
    public static boolean check = true;
    public static boolean app_check = true;
    public static boolean isEnabled = false;
    private static long lastUnlockTimeSeconds;
    private static long leaverTime;
    private static boolean allowedLeaveAment;
    private static boolean lockState;
    Cursor cursor;
    String appName, packageName;
    String check_name = null;
    String app_name = null;
    String lastFrontAppPkg = "";
    PreferenceHandler prefHandler;

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Context ctx;
        prefHandler = new PreferenceHandler(this);

        if (prefHandler.getScreenShotSwitchStatus())
        {
            isEnabled = true;

        }
        else {


        }
        isEnabled = true;
        AccessibilityServiceInfo config = new AccessibilityServiceInfo();
        config.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;
        config.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;

        if (Build.VERSION.SDK_INT >= 16)
            //Just in case this helps
            config.flags = AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS;

        setServiceInfo(config);
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        filter.addAction(Intent.ACTION_REBOOT);
        filter.addAction(Intent.ACTION_PACKAGE_ADDED);
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);

//        filter.addAction(Intent.);
        ApplockBrodcast brodcast = new ApplockBrodcast();
        registerReceiver(brodcast, filter);


        /*ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService( Context.ACTIVITY_SERVICE );
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for(ActivityManager.RunningAppProcessInfo appProcess : appProcesses){
            if(appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND){
                Log.i("Foreground App", appProcess.processName);
            }
        }*/


    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            if (event.getPackageName() != null && event.getClassName() != null) {
                ComponentName componentName = new ComponentName(
                        event.getPackageName().toString(),
                        event.getClassName().toString()
                );


                ActivityInfo activityInfo = tryGetActivity(componentName);
                boolean isActivity = activityInfo != null;
                if (isActivity) {
//                    Toast.makeText(this, "" + componentName.getPackageName(), Toast.LENGTH_SHORT).show();
//                    Toast.makeText(this, "" + componentName.flattenToShortString(), Toast.LENGTH_SHORT).show();
                    Log.i("CurrentActivity", componentName.flattenToShortString());
//****************************************


                    packageName = componentName.getPackageName();
                    Log.e("packageName", packageName);
                    PackageManager packageManager = getApplicationContext().getPackageManager();
                    try {
                        appName = (String) packageManager.getApplicationLabel(packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA));


                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
//                    Toast.makeText(this, ""+appName, Toast.LENGTH_SHORT).show();
                    if (app_check) {
                        //if (check) {
                            try {

                                sqLiteDatabase = userDbHelper.getReadableDatabase();
                                cursor = userDbHelper.fetch_appname(sqLiteDatabase);

                               /* PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
                                boolean isScreenOn = false;
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT_WATCH) {
                                    isScreenOn = pm.isScreenOn();
                                }*/

                                KeyguardManager manager = (KeyguardManager) this.getSystemService(Context.KEYGUARD_SERVICE);
                                boolean lock=manager.isDeviceLocked();



                                if (cursor.moveToFirst()) {
        do {
//                String user_pass, user_check;
            app_name = cursor.getString(0);
            if (packageName.equals(app_name)) {
                if (GlobalVariables.check_open_app) {
                    check_name = packageName;
//                    GlobalVariables.check_open_app = !GlobalVariables.check_open_app;



if(!lock){

                        Intent intent = new Intent(this, Unlock_Activity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        // intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                        intent.putExtra("packagename", "" + componentName.getPackageName());
                        startActivity(intent);
//                        GlobalVariables.check_open_app = !GlobalVariables.check_open_app;

                    }
                   // check=!check;
//                  app_check=!app_check;
                    break;
                }

            }

        } while (cursor.moveToNext());

    }
                            } catch (Exception e) {
//    Toast.makeText(this, ""+e, Toast.LENGTH_SHORT).show();

                                System.out.println("hdfdfdhfdfhdj" + e);

                            }
                        }
                    }
                }
            }
        //}

        if (check_name!=null&&!packageName.equals(check_name)&& !packageName.equalsIgnoreCase("com.wily")) {

//            check=true;
            GlobalVariables.check_open_app = true;
//            check=false;
        }

//        if(!appName.equalsIgnoreCase("wily") && !appName.equals(check_name))
//            GlobalVariables.check_open_app =true;


    }

  /*  private void doAction() {
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            try {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    if (!lastFrontAppPkg.equals((String) appProcess.pkgList[0])) {
                        AppList apkInfo = getInfoFromPackageName(appProcess.pkgList[0], getApplicationContext());
                        if (apkInfo == null || (*//*apkInfo.getPackagename().applicationInfo.flags && *//*ApplicationInfo.FLAG_SYSTEM) == 1) {
                            // System app                                             continue;
                        } *//*else if (((apkInfo.getP().versionName == null)) || (apkInfo.getP().requestedPermissions == null)) {
                            //Application that comes preloaded with the device
                            continue;
                        }*//* else {
                            lastFrontAppPkg = (String) appProcess.pkgList[0];
                        }
                        //kill the app
                        //Here do the pupop with password to launch the lastFrontAppPkg if the pass is correct
                    }
                }
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }

    }*/

    public AppList getInfoFromPackageName(String pkgName,
                                          Context mContext) {
        AppList newInfo = new AppList();
        try {
            PackageInfo p = mContext.getPackageManager().getPackageInfo(
                    pkgName, PackageManager.GET_PERMISSIONS);

            newInfo.setName(p.applicationInfo.loadLabel(
                    mContext.getPackageManager()).toString());
            newInfo.setPackagename(p.packageName);
            //   newInfo.versionName = p.versionName;
            //   newInfo.versionCode = p.versionCode;
            newInfo.setIcon(p.applicationInfo.loadIcon(mContext
                    .getPackageManager()));
            // newInfo.setP(p);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return newInfo;
    }

    private boolean inWhiteList(String packgeName) {
        return packgeName.equals("info.com.wily");
    }

    /* public boolean isAppForground(Context mContext) {
         ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
         List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
         if (!tasks.isEmpty()) {
             ComponentName topActivity = tasks.get(0).topActivity;
             if (!topActivity.getPackageName().equals(mContext.getPackageName())) {
                 return false;
             }
         }
         return true;
     }*/
    private boolean isAppOnForeground(Context context, String appPackageName) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(appPackageName)) {
                return false;
            }
        }
        return true;
/*        if (appProcesses == null) {
            return false;
        }
        final String packageName = appPackageName;
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) {
                //                Log.e("app",appPackageName);
                return true;
            }
        }
        return false;
    }*/
    }

    private ActivityInfo tryGetActivity(ComponentName componentName) {
        try {
            return getPackageManager().getActivityInfo(componentName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    @Override
    public void onInterrupt() {
    }

    @Override
    public void onDestroy() {
        isEnabled = false;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
//        Toast.makeText(getApplicationContext(), "unbind", Toast.LENGTH_SHORT).show();
    }

    public class ApplockBrodcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("actionaction", intent.getAction().toString());
            if (Intent.ACTION_SCREEN_ON.equals(intent.getAction())) {
//                Toast.makeText(context, "SCREEN ON", Toast.LENGTH_SHORT).show();

                GlobalVariables.check_open_app = true;
                check = true;
            }

            if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {

                // Toast.makeText(context, "SCREEN OFF", Toast.LENGTH_SHORT).show();
                check = true;
                GlobalVariables.check_open_app = true;
            }


            if (Intent.ACTION_USER_PRESENT.equals(intent.getAction())) {
//                Toast.makeText(context, "USER _PRESENT", Toast.LENGTH_SHORT).show();

                GlobalVariables.check_open_app = true;
                check = true;

//cursor=userDbHelper.fetch_appname(sqLiteDatabase);
            }


            if (Intent.ACTION_PACKAGE_ADDED.equals(intent.getAction())) {
                //GlobalVariables.check_open_app=true;
                SplashActivity splashActivity = new SplashActivity();
                GlobalVariables.PACKAGES_LIST = splashActivity.getInstalledApps();
            }
            if (Intent.ACTION_PACKAGE_REMOVED.equals(intent.getAction())) {

                SplashActivity splashActivity = new SplashActivity();
                GlobalVariables.PACKAGES_LIST = splashActivity.getInstalledApps();

            }
            if(Intent.ACTION_REBOOT.equals(intent.getAction())){

                SplashActivity splashActivity=new SplashActivity();
                GlobalVariables.PACKAGES_LIST= splashActivity.getInstalledApps();
            }




            /*Intent launch_intent = new Intent("android.intent.action.MAIN");
            launch_intent.setComponent(new ComponentName("info.com.wily","com.example.helloworld.MainActivity"));
            launch_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            launch_intent.addCategory(Intent.CATEGORY_LAUNCHER);
            launch_intent.putExtra("some_data", "value");
            context.startActivity(launch_intent);*/

        }
    }





  /* private boolean isNamedProcessRunning(String processName) {
       if (processName == null)
           return false;

       ActivityManager manager =(ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
       List<ActivityManager.RunningAppProcessInfo> processes = manager.getRunningAppProcesses();
       for (ActivityManager.RunningAppProcessInfo process : processes) {
           if (processName.equals(process.processName)) {
               return true;
           }


       }
                  return false;
   }*/

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;

    }

}
