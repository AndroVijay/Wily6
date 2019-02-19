package com.wily;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.wily.applock.Aplock_Accessibilty;
import com.wily.applock.AppLockFragment;
import com.wily.utils.NotificationInterface;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;



import static android.content.ContentValues.TAG;
import static com.wily.TorchFragment.handler;
import static com.wily.TorchFragment.runnable;
import static com.wily.view.AssistiveCenter.hideAssistiveCenter;


/**
 * Created by neha on 05-04-2017.
 */

public class HomeActivity extends AppCompatActivity implements NotificationInterface {

    public static int ID_FLOATING_BTN = 100;
    public static boolean IS_TURN_ON_TORCH = false;
    ImageView option,search;
    EditText searchtext;
    boolean isOpening;
    public static boolean xioami_check=true;
    TextView selectedTab;
    private ArrayList<String> list;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private AdView adView;
    private Fragment frag1;
    private ActionBar actionBar;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        actionBar = getSupportActionBar();

        checkPermission();

      /*  String manufacturer = "xiaomi";
        if(xioami_check==true) {


            if (manufacturer.equalsIgnoreCase(android.os.Build.MANUFACTURER)) {
                //this will open auto start screen where user can enable permission for your app
                Intent intent = new Intent();
                xioami_check=!xioami_check;
                intent.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
                intent.putExtra("packegename","com.wily");
               // startActivity(intent);
            }
        }*/

        RelativeLayout add = (RelativeLayout) findViewById(R.id.adLayout);
        adView = new AdView(HomeActivity.this, "1327281057360151_1333942456694011", AdSize.BANNER_320_50);
        add.addView(adView);
        adView.loadAd();

        // selected tab name
        selectedTab = (TextView) findViewById(R.id.tabName);
//        printHashKey();

        option = (ImageView) findViewById(R.id.option_btn);
//        search=(ImageView)findViewById(R.id.search_btn);
//        searchtext= (EditText) findViewById(R.id.search_text);
        list = new ArrayList<>();
        list.add("Home");
        list.add("Torch");
       // list.add("Screenshot");
        //list.add("App lock");
        list.add("Assistive Touch");
        list.add("Rate us");
        list.add("Share");



        if (handler != null) {
            handler.removeCallbacks(runnable);
            runnable = null;
            handler = null;
        }


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setAlpha(.75f);


        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.drawer_open, R.string.drawer_close) {





/*
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                if (slideOffset == 0.f)
                {
                    isOpening = false;
                }
                else
                    isOpening = true;
            }

            @Override
            public void onDrawerStateChanged(int newState) {

                if (isOpening)
                    option.setVisibility(View.GONE);
                else
                    option.setVisibility(View.VISIBLE);

                super.onDrawerStateChanged(newState);
            }*/

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                //    getActionBar().setTitle(mTitle);
                option.setVisibility(View.VISIBLE);
                selectedTab.setVisibility(View.VISIBLE);

                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                option.setVisibility(View.GONE);
                selectedTab.setVisibility(View.GONE);

                //    getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }


        };

        try {
            // Set the drawer toggle as the DrawerListener
            mDrawerLayout.addDrawerListener(mDrawerToggle);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.hide();
        }catch (Exception e){e.printStackTrace();}


        option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    option.setVisibility(View.GONE);
                    mDrawerLayout.openDrawer(GravityCompat.START);
                } else {
                    option.setVisibility(View.VISIBLE);
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                }

            }
        });
        // Set the adapter for the list view
        LayoutInflater inflater = LayoutInflater.from(this);

        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.header, mDrawerList, false);
        ((ImageView) header.findViewById(R.id.back_but)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerAction(0);
            }
        });
        mDrawerList.addHeaderView(header, null, false);
        DrawerAdapter adapter = new DrawerAdapter(HomeActivity.this, list);
        mDrawerList.setAdapter(adapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HomeActivity.IS_TURN_ON_TORCH = false;
                drawerAction(position);
            }
        });
        String abc = getIntent().getStringExtra("splace");

        //System.out.println("hello1   "+abcd);
        /*if (!abc.equals("") && abc.equals("splace")) {
            drawerAction(2);
        }*/
        if (!abc.equals("") && abc.equals("torch")) {
            drawerAction(2);
        }
//        if (!abc.equals("") && abc.equals("applock")) {
//
//
//            drawerAction(3);
//
//        }
        if (!abc.equals("") && abc.equals("assistive")) {


            drawerAction(3);

        }







      /*  else
        {
            drawerAction(0);
        }*/
        //drawerAction(0);
        // Set the list's click listener
        //  mDrawerList.setOnItemClickListener(new DrawerItemClickListener());


        // action bar modifications

//        selectedTab= (TextView) findViewById(R.id.tabName);
//        selectedTab.setText("varsha");

    }

    private void  checkPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, ID_FLOATING_BTN);

            }
        }
    }

    public void printHashKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.wily", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                System.out.println("hashkey---- " + hashKey);
                Toast.makeText(this, "" + hashKey, Toast.LENGTH_SHORT).show();
                Log.i(TAG, "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "printHashKey()", e);
        } catch (Exception e) {
            Log.e(TAG, "printHashKey()", e);
        }
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                if (data != null)
                {
                    try
                    {

                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());

                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }

                }
            } else if (resultCode == Activity.RESULT_CANCELED)
            {
                Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_SHORT).show();
            }
        }
    }*/

    @Override
    protected void onPause() {
        super.onPause();
        if (isMyServiceRunning(MyServices.class))
        {
            if (getSupportFragmentManager().findFragmentById(R.id.content_frame) instanceof screenshot_image_fragment)
            {
                screenshot_image_fragment frag = (screenshot_image_fragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);
                if (frag.getDisplayImgView().getVisibility() == View.VISIBLE)
                {
                    frag.getDisplayImgView().setVisibility(View.GONE);

                    Fragment  fragment = new screenshot_image_fragment();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().addToBackStack(null);
                    fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();


                }
                else
                    finish();
            }
        }
            //finish();
        if (Aplock_Accessibilty.isEnabled)
        {
            if (getSupportFragmentManager().findFragmentById(R.id.content_frame) instanceof screenshot_image_fragment)
            {
                screenshot_image_fragment frag = (screenshot_image_fragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);
                if (frag.getDisplayImgView().getVisibility() == View.VISIBLE)
                {
                    frag.getDisplayImgView().setVisibility(View.GONE);

                    Fragment  fragment = new screenshot_image_fragment();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().addToBackStack(null);
                    fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();


                }
                else
                    finish();
            }
        }
//            finish();



    }

    @Override
    protected void onStop() {
        //  if (getFragmentManager().findFragmentById(R.id.content_frame) instanceof  ScreenshotFragment)

        super.onStop();

        if (isMyServiceRunning(MyServices.class))
            this.finish();
        if (Aplock_Accessibilty.isEnabled)
            this.finish();

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adView != null) {
            adView.destroy();
        }


       /* PreferenceHandler prefHandler;
        prefHandler = new PreferenceHandler(getApplicationContext());
        if(prefHandler.getScreenShotSwitchStatus()){
            this.startService(new Intent(this, MyServices.class));

        }*/
/*
if(switch_on){
    this.startService(new Intent(this, MyServices.class));
}
*/

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (getSupportFragmentManager().findFragmentById(R.id.content_frame) instanceof ScreenshotFragment) {

            ScreenshotFragment frag = (ScreenshotFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);
            if (ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    ) {

                if (!frag.isMyServiceRunning(MyServices.class)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                        new PreferenceHandler(this).setScreenShotSwitchStatus(true);
                        startService(new Intent(HomeActivity.this, MyServices.class));
                    } else {
//                            Toast.makeText(this, "this phone does not support screenshot button please use power button + volume downe button to take screenshot ", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        }

    }

    private void drawerAction(int position) {
        Fragment fragment = null;
        if (position == 0) {
//            fragment = new TorchFragment();
//            FragmentManager fragmentManager = getFragmentManager();
//            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
//
//            // Highlight the selected item, update the title, and close the drawer
//            mDrawerList.setItemChecked(position, true);
//            setTitle(list.get(position));
            mDrawerLayout.closeDrawer(mDrawerList);
        }
        if(position==1)
        {
            Intent  intent=new Intent(this,SplashActivity.class);
                startActivity(intent);
            finish();

        }
        if (position == 2) {
            fragment = new TorchFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().addToBackStack(null);
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
            selectedTab.setVisibility(View.VISIBLE);
            selectedTab.setText("Torch");
            check_drower=false;
            // Highlight the selected item, update the title, and close the drawer
            mDrawerList.setItemChecked(position, true);
            setTitle(list.get(position));
            mDrawerLayout.closeDrawer(mDrawerList);
         /*else if (position == 2) {
            fragment = new ScreenshotFragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().addToBackStack(null);
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
            selectedTab.setText("Screenshot");
            check_drower=false;
            selectedTab.setVisibility(View.VISIBLE);

            // Highlight the selected item, update the title, and close the drawer
            mDrawerList.setItemChecked(position, true);
            setTitle(list.get(position));
            mDrawerLayout.closeDrawer(mDrawerList);
        }*/ //else if (position == 3) {
           /* search.setVisibility(View.VISIBLE);
            search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    search.setVisibility(View.GONE);
                    searchtext.setVisibility(View.VISIBLE);
                }
            });*/
//            frag1 = new AppLockFragment();
//            selectedTab.setVisibility(View.VISIBLE);
//            check_drower=true;
//
//            selectedTab.setText("Applock");
//            fragment = new AppLockFragment();
//            FragmentManager fragmentManager =getSupportFragmentManager();
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            fragmentTransaction.addToBackStack(null);
//            fragmentTransaction.replace(R.id.content_frame, fragment);
//            fragmentTransaction.commit();

            // Highlight the selected item, update the title, and close the drawer
//            mDrawerList.setItemChecked(position, true);
//            setTitle(list.get(position));
//            mDrawerLayout.closeDrawer(mDrawerList);


        } else if (position == 3){
           /* search.setVisibility(View.VISIBLE);
            search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    search.setVisibility(View.GONE);
                    searchtext.setVisibility(View.VISIBLE);
                }
            });*/

            frag1 = new UtilitiesFragment();
//            frag1 = new File_lock_Fragment();
            selectedTab.setVisibility(View.VISIBLE);
            check_drower=true;

            selectedTab.setText("Assistive Touch");
            fragment = new UtilitiesFragment();
//            ((UtilitiesFragment)fragment).setActivity(this);

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.content_frame, fragment);
            fragmentTransaction.commit();

            // Highlight the selected item, update the title, and close the drawer
            mDrawerList.setItemChecked(position, true);
            setTitle(list.get(position));
            mDrawerLayout.closeDrawer(mDrawerList);
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





        }


        else if (position == 4) {
            try {
                check_drower=false;
                selectedTab.setVisibility(View.GONE);
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.wily")));
            } catch (Exception e) {

            }

        } else if (position == 5) {
            try {
                selectedTab.setVisibility(View.GONE);
                check_drower=false;
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, "Hey, I am using this app it really makes my life easy, you can also download com.wily. Click https://play.google.com/store/apps/details?id=com.wily");
                startActivity(Intent.createChooser(sharingIntent, "Share using"));
            } catch (Exception e) {

            }
        }

        /*if(getIntent().getExtras().getString("splace")=="splace")
        {
            fragment = new ScreenshotFragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

            // Highlight the selected item, update the title, and close the drawer
            mDrawerList.setItemChecked(position, true);
            setTitle(list.get(position));
            mDrawerLayout.closeDrawer(mDrawerList);

        }*/

    }

    boolean check_drower=false;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        searchtext.setVisibility(View.GONE);
//        search.setVisibility(View.VISIBLE);

        /*if (getFragmentManager().findFragmentById(R.id.content_frame) instanceof screenshot_image_fragment)
        {
               screenshot_image_fragment frag = (screenshot_image_fragment) getFragmentManager().findFragmentById(R.id.content_frame);
            if (frag.getDisplayImgView().getVisibility() == View.VISIBLE)
            {
                frag.getDisplayImgView().setVisibility(View.GONE);

                Fragment  fragment = new screenshot_image_fragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().addToBackStack(null);
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();


            }
            else
                finish();
        }
        else
            finish();*/



        hideAssistiveCenter();
        finish();
    }

    @Override
    public Class getNotificationIntent() {
        return HomeActivity.class;
    }
}
