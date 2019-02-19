package com.wily;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.appevents.AppEventsLogger;
import com.wily.applock.Aplock_Accessibilty;
import com.wily.applock.AppList;
import com.wily.assistivebean.AssistiveBean;
import com.wily.databasehandelr.DbHelper;
import com.wily.db.Assistive_Db;
import com.wily.utils.AppPrefs;
import com.wily.utils.Constants;
import com.wily.utils.Getclassname;
import com.wily.utils.SharedPreferencesName;
import com.wily.utilss.GlobalVariables;
import com.wily.view.AssistiveTouch;

import java.util.ArrayList;
import java.util.List;

/*import info.earntalktime.at.assistivebean.AssistiveBean;
import info.earntalktime.at.db.Assistive_Db;
import info.earntalktime.at.utils.AppPrefs;
import info.earntalktime.at.utils.Constants;
import info.earntalktime.at.utils.Getclassname;
import info.earntalktime.at.utils.SharedPreferencesName;
import info.earntalktime.at.view.AssistiveTouch;*/

public class SplashActivity extends Activity {
    //    Context context=getApplicationContext();
    public static DbHelper userDbHelper;
    public static SQLiteDatabase sqLiteDatabase;
    private SQLiteDatabase sql1;
    ImageView torch, screenshot, vertical, applock, hand_tutorial1, hand_tutorial2, hand_tutorial3, assistive_touch;
    PreferenceHandler prefHandler;
    TextView tutorial_text1, tutorial_text2, tutorial_text3;
    LinearLayout linearLayout, linearLayout1;
    private AdView adView;
    Cursor cursor;
    Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        torch = (ImageView) findViewById(R.id.torch_splac);
        HomeActivity.IS_TURN_ON_TORCH = false;
        init();


/*
        FacebookSdk.sdkInitialize(getApplicationContext());
*/
        RelativeLayout add = (RelativeLayout) findViewById(R.id.adLayout);
        adView = new AdView(SplashActivity.this, "1327281057360151_1333942456694011", AdSize.BANNER_320_50);
        add.addView(adView);
        adView.loadAd();


        Intent intent = new Intent(SplashActivity.this, HomeActivity.class);

        Getclassname.getIntent(intent);


        prefHandler = new PreferenceHandler(this);
        if (prefHandler.getScreenShotSwitchStatus()) {
            startService(new Intent(this, MyServices.class));
        }


        if (!prefHandler.SplaceStatus()) {
            // showChangeLangDialog();
//            Intent intent1 = new Intent(this, Wily_popUp.class);
//            startActivity(intent1);

            hand_tutorial1.setVisibility(View.GONE);
            hand_tutorial2.setVisibility(View.GONE);
            hand_tutorial3.setVisibility(View.GONE);
            tutorial_text1.setVisibility(View.GONE);
            tutorial_text2.setVisibility(View.GONE);
            tutorial_text3.setVisibility(View.GONE);
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    hand_tutorial1.setVisibility(View.INVISIBLE);
//                    tutorial_text1.setVisibility(View.INVISIBLE);
//                    hand_tutorial2.setVisibility(View.VISIBLE);
//                    tutorial_text2.setVisibility(View.VISIBLE);
//
//                }
//            }, 2000);
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    hand_tutorial2.setVisibility(View.INVISIBLE);
//                    tutorial_text2.setVisibility(View.INVISIBLE);
//                    hand_tutorial3.setVisibility(View.VISIBLE);
//                    tutorial_text3.setVisibility(View.VISIBLE);
//
//                }
//            }, 4000);
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    hand_tutorial3.setVisibility(View.INVISIBLE);
//                    tutorial_text3.setVisibility(View.INVISIBLE);
//                }
//            }, 6000);
       }


//        GlobalVariables.PACKAGES_LIST = getInstalledApps();
        create_databae();


        sql1 = openOrCreateDatabase(DbHelper.DATABASE_NAME, Context.MODE_PRIVATE, null);
//        if (sql1.isOpen())
//        openAndQueryDatabase();


        torch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SplashActivity.this, HomeActivity.class);
                i.putExtra("splace", "torch");
                linearLayout.setVisibility(View.INVISIBLE);
                linearLayout1.setVisibility(View.INVISIBLE);
                prefHandler.SplaceStatus(true);
                startActivity(i);

//                finish();
            }
        });


        boolean isATActivated = AppPrefs.getInstance(this).getBooleanValue(SharedPreferencesName.KEY_IS_AT_ACTIVATED, false);
        if (isATActivated) {
            Intent miniModeService = new Intent(this, AssistiveTouch.class);
            miniModeService.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
            this.startService(miniModeService);
        }


       /* screenshot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i1 = new Intent(SplashActivity.this, HomeActivity.class);
                i1.putExtra("splace", "splace");
                linearLayout.setVisibility(View.INVISIBLE);
                linearLayout1.setVisibility(View.INVISIBLE);
                prefHandler.SplaceStatus(true);
                startActivity(i1);

//                finish();

                *//*FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id., fragment); // fragmen container id in first parameter is the  container(Main layout id) of Activity
                transaction.addToBackStack(null);  // this will manage backstack
                transaction.commit();*//*

            }
        });*/
//        applock.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i1 = new Intent(SplashActivity.this, HomeActivity.class);
//                i1.putExtra("splace", "applock");
//                linearLayout.setVisibility(View.INVISIBLE);
//                linearLayout1.setVisibility(View.INVISIBLE);
//                prefHandler.SplaceStatus(true);
//                startActivity(i1);
//
////                finish();
//
//
//            }
//        });
        assistive_touch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i1 = new Intent(SplashActivity.this, HomeActivity.class);
                i1.putExtra("splace", "assistive");
                prefHandler.SplaceStatus(true);
                startActivity(i1);


            }
        });
       /* boolean isATActivated = AppPrefs.getInstance(this).getBooleanValue(SharedPreferencesName.KEY_IS_AT_ACTIVATED, false);
        if (isATActivated) {
            new Thread(new Runnable() {
                public void run() {
                    Intent miniModeService = new Intent(getApplicationContext(), AssistiveTouch.class);
                    miniModeService.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
                    startService(miniModeService);
                }
            }).start();
        }*/

       /* new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {


                sql1 = openOrCreateDatabase(DbHelper.DATABASE_NAME, Context.MODE_PRIVATE, null);
                if (sql1.isOpen())
                    openAndQueryDatabase();

                GlobalVariables.PACKAGES_LIST = getInstalledApps();

                return null;


            }
        };*/


    }

    private void init() {
        screenshot = (ImageView) findViewById(R.id.screenshot_splace);
        vertical = (ImageView) findViewById(R.id.seperator);
        applock = (ImageView) findViewById(R.id.applock_splace);
        hand_tutorial1 = (ImageView) findViewById(R.id.splace_tutorial1);
        hand_tutorial2 = (ImageView) findViewById(R.id.splace_tutorial2);
        hand_tutorial3 = (ImageView) findViewById(R.id.splace_tutorial3);
        tutorial_text1 = (TextView) findViewById(R.id.hand_tutorial_text1);
        tutorial_text2 = (TextView) findViewById(R.id.hand_tutorial_text2);
        tutorial_text3 = (TextView) findViewById(R.id.hand_tutorial_text3);
        linearLayout = (LinearLayout) findViewById(R.id.hand_tutorial);
        linearLayout1 = (LinearLayout) findViewById(R.id.hand_tutorial1);
        assistive_touch = (ImageView) findViewById(R.id.splace_assistive);
    }

    @Override
    protected void onStart() {


        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                GlobalVariables.PACKAGES_LIST = getInstalledApps();
                if (sql1.isOpen())
                    openAndQueryDatabase();
                return null;
            }
        }.execute();


//                GlobalVariables.PACKAGES_LIST = getInstalledApps();
//        new AsyncTask<String, Void, Boolean>() {
//            @Override
//            protected Boolean doInBackground(String... params) {
//

//                GlobalVariables.PACKAGES_LIST = getInstalledApps();
//                return null;
//
//            }
//        };

        super.onStart();
    }

    public ArrayList<AppList> getInstalledApps() {
        ArrayList<AppList> res = new ArrayList<AppList>();
//        List<PackageInfo> packs = getPackageManager().getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
        final PackageManager pm = getApplicationContext().getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> apps = pm.queryIntentActivities(intent, PackageManager.GET_META_DATA);
        for (int i = 0; i < apps.size(); i++) {
            ResolveInfo p = apps.get(i);
            AppList app = new AppList();
            String appName = p.loadLabel(getPackageManager()).toString();


            Drawable icon = p.loadIcon(getPackageManager());
            String pack = p.activityInfo.packageName;
            if (!pack.equalsIgnoreCase("com.wily")) {
                app.setName(appName);
                app.setIcon(icon);
                app.setPackagename(pack);
                res.add(app);
            } else
                Log.e("removed", "removed Wily");
        }
        return res;
    }


    int[] fav_icon = {
            R.drawable.assistive_add_shortcut,
            R.drawable.assistive_add_shortcut,
            R.drawable.assistive_add_shortcut,
            R.drawable.assistive_add_shortcut,
            R.drawable.assistive_add_shortcut,
            R.drawable.assistive_add_shortcut,
            R.drawable.assistive_add_shortcut,
    };
    String click[] = {"false", "false", "false", "false", "false", "false", "false"};
    String id[] = {"14", "15", "16", "17", "18", "19", "20"};
    String pck_name[] = {"", "", "", "", "", "", ""};

    AssistiveBean bean;
    private Assistive_Db assistive_db;
    private SQLiteDatabase db;

    private void create_databae() {
        assistive_db = new Assistive_Db(getApplicationContext());
        db = assistive_db.getWritableDatabase();
        assistive_db.onCreate(db);
        userDbHelper = new DbHelper(getApplicationContext());
        sqLiteDatabase = userDbHelper.getWritableDatabase();
        userDbHelper.onCreate(sqLiteDatabase);
        userDbHelper.close();
        String count = "SELECT count(*) FROM Wily_Assist";
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        if (icount > 0) {

        }
//leave
        else {
//populate table


            for (int i = 0; i < fav_icon.length; i++) {
//            bean = new AssistiveBean();


//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                assistive_db.insertData(id[i], pck_name[i], Constants.getByteFromDrawable(getResources().getDrawable(fav_icon[i])), click[i]);
//                    }
//            bean.setIcon(getResources().getDrawable(fav_icon[i]));
//            bean.setItemId((icons.length)+(icons_list2.length)+i);
//            bean.setPackageName("");
//            itemAssistive_fav_list.add(bean);
            }

        }
    }


    /* private class asynktask extends AsyncTask<String,Void,Boolean>{


         @Override
         protected Boolean doInBackground(String... params) {

             GlobalVariables.PACKAGES_LIST = getInstalledApps();




             return null;
         }
     }*/
    AlertDialog b;

    public void showChangeLangDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this, R.style.NewDialog);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.show_dialog, null);
        dialogBuilder.setView(dialogView);
        Button button = (Button) dialogView.findViewById(R.id.ok);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.dismiss();
            }
        });


        b = dialogBuilder.create();
        b.show();
    }

    @Override
    protected void onStop() {
//        finish();
        super.onStop();
        if (isMyServiceRunning(MyServices.class))
            this.finish();
        if (Aplock_Accessibilty.isEnabled)
            this.finish();


//        if (isMyServiceRunning(MyServices.class))
//            this.finish();

//        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        finish();
        /*  AppEventsLogger.deactivateApp(this);*/

        if (isMyServiceRunning(MyServices.class))
            this.finish();
        if (Aplock_Accessibilty.isEnabled)
            this.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

/*
        AppEventsLogger.activateApp(this);
*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adView != null) {
            adView.destroy();
        }
//        finish();
    }

    public boolean isMyServiceRunning(Class<MyServices> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    // hide keyboard after edittext
    public static void closeKeyboard(Context c, IBinder windowToken) {
        InputMethodManager mgr = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(windowToken, 0);
    }

    boolean isAppAvailable = false;

    private void openAndQueryDatabase() {
        try {
            // sql1 = userDbHelper.getWritableDatabase();
            sqLiteDatabase = userDbHelper.getReadableDatabase();
            cursor = userDbHelper.fetch_appname(sqLiteDatabase);
            //    cursor = sql1.rawQuery(sql, null);
            GlobalVariables.PACKAGES_LIST = getInstalledApps();

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        isAppAvailable = false;
//                        String app_name = cursor.getString(0);
                        String pack = cursor.getString(0);

                        for (int i = 0; i < getInstalledApps().size(); i++) {
                            AppList data1 = getInstalledApps().get(i);
                            String app = data1.getPackagename();
                            if (app.equalsIgnoreCase(pack)) {
                                isAppAvailable = true;
                                break;
                            } else
                                isAppAvailable = false;

                        }

                        if (!isAppAvailable) {
                            // delete from db
                            userDbHelper.deleteDataFromDatabase(pack);
                        }

                    } while (cursor.moveToNext());
                }
            }
        } catch (SQLiteException se) {
            Log.e(getClass().getSimpleName(), "Could not create or Open database");
        }

    }


}
