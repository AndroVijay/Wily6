package com.wily.applock;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.util.Iterator;
import java.util.List;

/**
 * Created by PANDEY on 01-05-2017.
 */

public class AppLockService extends Service {
    private static final String TAG ="name" ;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
     //   applock();
    }
    public void applock(){

        /*final PackageManager pm = getPackageManager();
//get a list of installed apps.
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo packageInfo : packages) {
            Log.d(TAG, "Installed package :" + packageInfo.packageName);
            Log.d(TAG, "Source dir : " + packageInfo.sourceDir);
            Log.d(TAG, "Launch Activity :" + pm.getLaunchIntentForPackage(packageInfo.packageName));
        }
        */


        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo)(i.next());
            try {

                CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(
                        info.processName, PackageManager.GET_META_DATA));

                Toast.makeText(this, ""+c.toString(), Toast.LENGTH_SHORT).show();
                Log.w("LABEL", c.toString());
            } catch (Exception e) {
// Name Not FOund Exception
                e.printStackTrace();
            }
        }
    }

 /*public class ApplockBrodcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            intent.A
            Intent launch_intent = new Intent("android.intent.action.MAIN");
            launch_intent.setComponent(new ComponentName("info.com.wily","com.example.helloworld.MainActivity"));
            launch_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            launch_intent.addCategory(Intent.CATEGORY_LAUNCHER);
            launch_intent.putExtra("some_data", "value");
            context.startActivity(launch_intent);

        }
    }*/
}
