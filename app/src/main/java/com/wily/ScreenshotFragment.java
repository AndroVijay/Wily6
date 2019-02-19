package com.wily;

import android.Manifest;
import android.app.ActivityManager;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.io.File;

import static com.wily.MyServices.ONGOING_NOTIFICATION_ID;
import static com.wily.MyServices.floatingFaceBubble;


/**
 * Created by neha on 06-04-2017.
 */

public class ScreenshotFragment extends Fragment implements MediaScannerConnection.MediaScannerConnectionClient {
    TextView text;

    public static final int PERMS_REQUEST_CODE = 1;
    Switch _switch;
    PreferenceHandler prefHandler;
    public static File myDirectory;


    public String[] allFiles;
    private String SCAN_PATH ;
    private static final String FILE_TYPE="image/*";

    private MediaScannerConnection conn;

   public static boolean switch_on=false;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.screenshot_fragment,container,false);


text= (TextView) v.findViewById(R.id.textView);

        _switch = (Switch) v.findViewById(R.id.switch1);
        checkversion();
        LinearLayout linearLayout= (LinearLayout) v.findViewById(R.id.view_text);
        TextView textView= (TextView) v.findViewById(R.id.view_screenshot);

        try
        {
             myDirectory = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/Wily_Screenshot/");

            if (myDirectory.exists()) {
                linearLayout.setVisibility(View.VISIBLE);
            }
            File folder = new File(Environment.getExternalStorageDirectory()+"/Wily_Screenshot/");
            allFiles = folder.list();
                SCAN_PATH=Environment.getExternalStorageDirectory()+"/Wily_Screenshot/"+allFiles[0];
//                System.out.println(" SCAN_PATH  " +SCAN_PATH);

                for(int i=0;i<allFiles.length;i++)
                {
                    Log.d("all file path"+i, allFiles[i]+allFiles.length);
                }

                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                       /* Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(
                                "content://media/internal/images/media"));
                        startActivity(intent);*/
//                          startScan();
/*
String Path=Environment.getExternalStorageDirectory()+"/Wily_Screenshot/";
                        Intent intent = new Intent(Intent.ACTION_VIEW);
//                        intent.setDataAndType(Uri.parse("file://"+Path),"**/

/*");
                            intent.setDataAndType(Uri.fromFile(myDirectory),"image*/
/*");
//                        startActivityForResult(Intent.createChooser(intent,"images"),1);


                        startActivity(intent);
*/





                        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                            Fragment fragment = new screenshot_image_fragment();
                            FragmentManager fragmentManager = getFragmentManager();
                            fragmentManager.beginTransaction().addToBackStack(null);
                            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                        }
                        else {
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,

                            }, PERMS_REQUEST_CODE);
                        }


                    }
                });

            }
//        }
        catch (Exception e){

            System.out.println("Willy_error"+e);
        }




        prefHandler = new PreferenceHandler(getActivity());
        if (prefHandler.getScreenShotSwitchStatus())
        {
            _switch.setChecked(true);
            _switch.setButtonDrawable(R.drawable.screenshot_enable);

        }
        else {
            _switch.setChecked(false);
            _switch.setButtonDrawable(R.drawable.screenshot_disable);


        }
      //  _switch.setChecked();
        _switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    switch_on=true;

                    _switch.setButtonDrawable(R.drawable.screenshot_enable);
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                            && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.SYSTEM_ALERT_WINDOW) == PackageManager.PERMISSION_GRANTED
                            ) {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                           /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (!Settings.canDrawOverlays(getActivity())) {
                                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                            Uri.parse("package:" + getActivity().getPackageName()));
                                    startActivityForResult(intent, PERMS_REQUEST_CODE);

                                }
                            }*/
                            prefHandler.setScreenShotSwitchStatus(true);
                            getActivity().startService(new Intent(getActivity(), MyServices.class));


                        }
                    }
                    else
                    {
                        checkPermission();

                    }
                }
                else
                {
                    _switch.setButtonDrawable(R.drawable.screenshot_disable);
                    if(isMyServiceRunning(MyServices.class)) {
                        prefHandler.setScreenShotSwitchStatus(false);
                        Intent myService = new Intent(getActivity(), MyServices.class);
                        getActivity().stopService(myService);
                        switch_on = false;
                        floatingFaceBubble.setVisibility(View.GONE);
                        ONGOING_NOTIFICATION_ID=0;
                    }

                }
            }
        });

        return v;
    }



    private void startScan()
    {
        Log.d("Connected","success"+conn);
        if(conn!=null)
        {
            conn.disconnect();
        }
        conn = new MediaScannerConnection(getActivity().getApplicationContext(),this);
        conn.connect();
    }
    @Override
    public void onMediaScannerConnected() {
        Log.d("onMediaScannerConnected","success"+conn);
        conn.scanFile(SCAN_PATH, FILE_TYPE);
    }
    @Override
    public void onScanCompleted(String path, Uri uri) {
        try {
            Log.d("onScanCompleted",uri + "success"+conn);
            System.out.println("URI " + uri);
            if (uri != null)
            {
                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setData(uri);
                intent.setDataAndType(uri,"image/*");
                startActivity(intent);
            }
        } finally
        {
            conn.disconnect();
            conn = null;
        }
    }



    private void checkversion(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            _switch.setVisibility(View.VISIBLE);
            //text.setVisibility(View.VISIBLE);

        }else
        {
//            _switch.setEnabled(false);
            _switch.setVisibility(View.GONE);
            text.setVisibility(View.VISIBLE);

        }

    }

    private void checkPermission()
    {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.INTERNET,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.SYSTEM_ALERT_WINDOW,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.RECEIVE_BOOT_COMPLETED
            }, PERMS_REQUEST_CODE);
    }

    private void startservice(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(getActivity())) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + getActivity().getPackageName()));
                    startActivityForResult(intent, PERMS_REQUEST_CODE);

                }
            }
            prefHandler.setScreenShotSwitchStatus(true);
            getActivity().startService(new Intent(getActivity(), MyServices.class));


        }
    }


    @Override
    public void onStop() {
        super.onStop();
    //    getActivity().finish();
       /* if(switch_on){
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.SYSTEM_ALERT_WINDOW) == PackageManager.PERMISSION_GRANTED
                    ) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (!Settings.canDrawOverlays(getActivity())) {
                            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                    Uri.parse("package:" + getActivity().getPackageName()));
                            startActivityForResult(intent, PERMS_REQUEST_CODE);

                        }
                    }
                    prefHandler.setScreenShotSwitchStatus(true);
                    getActivity().startService(new Intent(getActivity(), MyServices.class));


                }
            }


        }*/

    }









    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }



    @Override
    public void onPause() {
        super.onPause();

    }

    public boolean isMyServiceRunning(Class<MyServices> serviceClass) {
        ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PERMS_REQUEST_CODE) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                    &&ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    ) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (!Settings.canDrawOverlays(getActivity())) {
                            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                    Uri.parse("package:" + getActivity().getPackageName()));
                            startActivityForResult(intent, PERMS_REQUEST_CODE);

                        }
                    }
                    prefHandler.setScreenShotSwitchStatus(true);
                    getActivity().startService(new Intent(getActivity(), MyServices.class));
                }

               /* if (!isMyServiceRunning(MyServices.class)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                        getActivity().startService(new Intent(getActivity(), MyServices.class));
                    }
                    else
                    {
//                        Toast.makeText(this, "this phone does not support screenshot button please use power button + volume downe button to take screenshot ", Toast.LENGTH_SHORT).show();
                    }

                }*/
            }
        }
    }





}
