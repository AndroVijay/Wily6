package com.wily;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

//import com.facebook.FacebookSdk;


public class TorchFragment extends Fragment {
static ImageView flash,torchoff,onofstrep,sosimage;
    static ImageButton onofbutton,sosbutton;


    private static final int PERMS_REQUEST_CODE = 1;
    static private android.hardware.Camera camera;
   static  boolean isFlasOn;
    static private boolean hasFlash;
    static private android.hardware.Camera.Parameters parameters;
    MediaPlayer mp;
    static boolean active = false;
    SeekBar sosslider,seekBarf;
    static Handler handler;
    static Runnable runnable;
    boolean btsos=false;
    int count=150;
    private boolean isChecked=false;
    int sh;
  //  private AdView adView;
    TextView tl,tm,th;
    Button bt;
     ToggleButton Onofbutton;
ImageView bg;
BroadcastReceiver broadcastReceiver;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

          View v =  inflater.inflate(R.layout.activity_main,container,false);


        checkPermission();
        turnOffFlash();

        Onofbutton = (ToggleButton) v.findViewById(R.id.on_of_switch);
        Onofbutton.setButtonDrawable(R.drawable.off_button);
        Onofbutton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (permissionStatus())
             torchAction(isChecked);
                else
                    checkPermission();
            }
        });


        onofstrep = (ImageView) v.findViewById(R.id.on_ofstrep);
        flash = (ImageView) v.findViewById(R.id.flashon);
        torchoff = (ImageView) v.findViewById(R.id.torch);
        sosslider = (SeekBar) v.findViewById(R.id.seekBar);

        torchoff.setImageResource(R.drawable.torch);
        sosimage = (ImageView) v.findViewById(R.id.on_ofsos);

        /*RelativeLayout add = (RelativeLayout) v.findViewById(R.id.adLayout);
        adView = new AdView(getActivity(), "1652761751694518_1662859654018061", AdSize.BANNER_320_50);
        add.addView(adView);
        adView.loadAd();*/
       /* sosslider.setProgress(1);

        if (handler != null) {
            handler.removeCallbacks(runnable);
            runnable = null;
            handler = null;
        }*/
        sosslider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, final int progress, boolean fromUser) {


                if (progress < 1) {

                    if (handler != null) {
                        handler.removeCallbacks(runnable);
                        runnable = null;
                        handler = null;
                    }
                    seekBar.setProgress(1);
                    Onofbutton.setChecked(false);
                    sosimage.setImageResource(R.drawable.sos_off);
                    //torchAction(false);
                    sosslider.setProgress(1);

//                    sosimage.setImageResource(R.drawable.sos_off);
//                    onofstrep.setImageResource(R.drawable.off_strip);
//                    Onofbutton.setButtonDrawable(R.drawable.off_button);
//
                    //isChecked=false;
                }
                if (progress >= 5) {
                    seekBar.setProgress(4);
                }
                if (progress == 1) {
                    if (handler != null) {
                        handler.removeCallbacks(runnable);
                        runnable = null;
                        handler = null;
                    }
                }
                if (progress >= 2) {
                    sosimage.setImageResource(R.drawable.sos_on);
                    if (handler != null) {
                        handler.removeCallbacks(runnable);
                        handler = null;
                        runnable = null;
                    }
                    sosbutton(btsos);

                    switch (progress) {


                        case 2:
                        case 3:
                            if (handler != null) {
                                handler.removeCallbacks(runnable);
                                handler = null;
                                runnable = null;
                            }
                            sosbutton(btsos);


                            sh = 200;
                            break;
                        case 4:
                            if (handler != null) {
                                handler.removeCallbacks(runnable);
                                handler = null;
                                runnable = null;
                            }
                            sosbutton(btsos);
                            sh = 130;
                            break;
                        //case 5:
                        case 5:
                            if (handler != null) {
                                handler.removeCallbacks(runnable);
                                handler = null;
                                runnable = null;
                            }
                            sosbutton(btsos);

                            sh = 100;
                            break;
                        //case 7:
                        case 6:
                            if (handler != null) {
                                handler.removeCallbacks(runnable);
                                handler = null;
                                runnable = null;
                            }
                            sosbutton(btsos);
                            sh = 50;
                            break;


                    }

                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // sosbutton(btsos);

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // sosbutton(btsos);
            }
        });


        hasFlash = getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        if (!hasFlash) {
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
            alertDialog.setTitle("Error");
            alertDialog.setMessage("Sorry your device does not support tourch");
            final  AlertDialog alert = alertDialog.create();
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    alert.dismiss();
                }
            });
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            alert.show();

        }

        if (HomeActivity.IS_TURN_ON_TORCH)
        {
            if (permissionStatus())
            {
                Onofbutton.setChecked(true);
                torchAction(true);
            }


        }
        return v;
    }

    private boolean permissionStatus()
    {
        boolean status = false;
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {


            status = true;
          /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!Settings.canDrawOverlays(getActivity())) {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                Uri.parse("package:" + getActivity().getPackageName()));
                        startActivityForResult(intent, PERMS_REQUEST_CODE);

                    }
                }
                getActivity().startService(new Intent(getActivity(), MyServices.class));
            }*/
        }
        else
        {
            status = false;
        }
        return status;
    }
    private void torchAction(boolean isChecked)
    {

        getCamera();
        if (isChecked) {
            // The toggle is enabled
            if (handler != null) {
                handler.removeCallbacks(runnable);
                handler = null;
                runnable = null;
            }
            Onofbutton.setButtonDrawable(R.drawable.on_button);
            turnOnFlash();
            flash.setVisibility(View.VISIBLE);
            flash.setImageResource(R.drawable.on_bg);
            playSound();
            sosslider.setProgress(1);
            sosimage.setImageResource(R.drawable.sos_off);
            onofstrep.setImageResource(R.drawable.on_strip);

        } else {
            // The toggle is disabled
            if (handler != null) {
                handler.removeCallbacks(runnable);
                handler = null;
                runnable = null;
            }


            Onofbutton.setButtonDrawable(R.drawable.off_button);
            turnOffFlash();
            flash.setVisibility(View.INVISIBLE);
            playSound();
            sosslider.setProgress(1);
            sosimage.setImageResource(R.drawable.sos_off);
            onofstrep.setImageResource(R.drawable.off_strip);
        }
    }
    private void checkPermission()
    {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.INTERNET,
            }, PERMS_REQUEST_CODE);
        }
    }






    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PERMS_REQUEST_CODE) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                  ) {

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

  /*
SOS BUTTON METHOD
 */


    public  void sosbutton(boolean s){


            handler = new Handler();
            runnable = new Runnable() {
                @Override
                public void run() {
                    turnOnFlash();
                    flash.setVisibility(View.VISIBLE);
                    handler.postDelayed(runnable, sh);

                    turnOffFlash();
                    flash.setVisibility(View.INVISIBLE);

                }
            };

            handler.postDelayed(runnable, sh);
        }


    @Override
    public void onResume() {
        super.onResume();
      /*  if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ) {
            getCamera();
            if (!isMyServiceRunning(MyServices.class)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                    getActivity().startService(new Intent(getActivity(), MyServices.class));
                }
                else
                {
                    // Toast.makeText(this, "this phone does not support screenshot button please use power button + volume downe button to take screenshot ", Toast.LENGTH_SHORT).show();
                }

            }
        }*/
    }

    @Override
    public void onStop() {
        super.onStop();
       /* if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                ) {
            getCamera();
            if (!isMyServiceRunning(MyServices.class)) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                    getActivity().startService(new Intent(getActivity(), MyServices.class));
                }
                else
                {
                    // Toast.makeText(this, "this phone does not support screenshot button please use power button + volume downe button to take screenshot ", Toast.LENGTH_SHORT).show();
                }
            }
        }*/
    }

    @Override
    public void onPause() {
        super.onPause();
      /*  if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            getCamera();
            if (!isMyServiceRunning(MyServices.class)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                    getActivity().startService(new Intent(getActivity(), MyServices.class));
                }
                else
                {
                    // Toast.makeText(this, "this phone does not support screenshot button please use power button + volume downe button to take screenshot ", Toast.LENGTH_SHORT).show();
                }

            }
        }*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {


        switch (requestCode) {
            case PERMS_REQUEST_CODE:

                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                  //      && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
               // && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        ) {

                    getCamera();
                   /* if (!isMyServiceRunning(MyServices.class)) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                            getActivity().startService(new Intent(getActivity(), MyServices.class));
                        }
                        else
                        {
//                            Toast.makeText(this, "this phone does not support screenshot button please use power button + volume downe button to take screenshot ", Toast.LENGTH_SHORT).show();
                        }

                    }*/
                }


        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }




    /*
    METHOD FOR CMAERA GET
     */
    static void getCamera() {
        if (camera == null) {
            try {
                camera = android.hardware.Camera.open();
                parameters = camera.getParameters();
            } catch (RuntimeException e) {
                Log.e("Error", e.getMessage());

            }
        }
    }






   static void turnOnFlash() {
        if (!isFlasOn) {
            if (camera == null || parameters == null) {
                return;
            }

            parameters = camera.getParameters();
            parameters.setFlashMode(parameters.FLASH_MODE_TORCH);
            camera.setParameters(parameters);

            camera.startPreview();
            isFlasOn = true;

        }

    }

    /*
    FLASH OFFF METHOD
     */
    static void turnOffFlash() {
        if (isFlasOn) {
            if (camera == null || parameters == null) {
                return;
            }
            //flash.setVisibility(View.INVISIBLE);
            //flash.setImageResource(R.drawable.hi);
            parameters = camera.getParameters();
            parameters.setFlashMode(parameters.FLASH_MODE_OFF);
            camera.setParameters(parameters);
            camera.stopPreview();
            isFlasOn = false;

        }
    }


    /*
    TORCH ON OF SOUND
     */

    private void playSound() {
        if (isFlasOn) {
            mp = MediaPlayer.create(getActivity(), R.raw.as);
        } else {
            mp = MediaPlayer.create(getActivity(), R.raw.as);
        }
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                // TODO Auto-generated method stub
                mp.release();
            }
        });
        mp.start();
    }







    @Override
    public void onStart() {
        super.onStart();
        active = true;
        // on starting the app get the camera params
        getCamera();
       /* if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            getCamera();
            if (!isMyServiceRunning(MyServices.class)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                    getActivity().startService(new Intent(getActivity(), MyServices.class));
                }
                else
                {
//                    Toast.makeText(this, "this phone does not support screenshot button please use power button + volume downe button to take screenshot ", Toast.LENGTH_SHORT).show();
                }

            }
        }*/

    }

}
