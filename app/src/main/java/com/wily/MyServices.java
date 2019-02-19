package com.wily;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.RemoteControlClient;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.Layout;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;

//import static com.facebook.ads.internal.e.f.a.a;
//import static rationalheads.flashlight.TorchFragment.sensor;

/**
 * Created by PANDEY on 08-Mar-17.
 */


public class MyServices extends Service {
    private SensorManager sm;
    Sensor sensor;

    private WindowManager windowManager, windowManager1, windowManager2;
    public static ImageView floatingFaceBubble;
    boolean isSansorChanged = false;
    boolean isFlasOn = false;
    Layout layout;
    //int count=0;
    BroadcastReceiver broadcastReceiver;
    public static int ONGOING_NOTIFICATION_ID = 1;
    public static String NOTIFICATIONCHANNEL_ID = "willy";
    public static String NOTIFICATION_NAME = "mychannel";

    int id = 1;
    static LinearLayout linearLayout, linearLayout1;
    static ImageView imageView, tutorial;
    static ImageView imageView1, imageView2;
    static TextView tutorial_text;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    @Override
    public void onCreate() {


        super.onCreate();


        floatingFaceBubble = new ImageView(this);
        floatingFaceBubble.setId(R.id.floatingbutton);


        if (ONGOING_NOTIFICATION_ID == 0) {
            stopservice();
            ONGOING_NOTIFICATION_ID = 1;
        }

      /*  //a face floating bubble as imageView
        Bitmap bitMap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);

        bitMap = bitMap.copy(bitMap.getConfig(), true);
        // Construct a canvas with the specified bitmap to draw into
        Canvas canvas = new Canvas(bitMap);
        // Create a new paint with default settings.
        Paint paint = new Paint();
        // smooths out the edges of what is being drawn
        paint.setAntiAlias(true);
        // set color
        paint.setColor(Color.WHITE);

        // set style
        paint.setStyle(Paint.Style.FILL);
        // set stroke
        paint.setStrokeWidth(4.5f);
        // draw circle with radius 30

        canvas.drawCircle(50, 50, 30, paint);
        // set on ImageView or any other view*/


        floatingFaceBubble.setImageResource(R.drawable.dot);


//        floatingFaceBubble.setImageResource(R.drawable.ic_camera_alt_notic);
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        //here is all the science of params
        final WindowManager.LayoutParams myParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_TOAST,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        myParams.gravity = Gravity.TOP | Gravity.LEFT;
        myParams.x = 0;
        myParams.y = 200;

        // add a floatingfacebubble icon in window


        windowManager.addView(floatingFaceBubble
                , myParams);


        intentnotification();


        // layout param
//******************************
        windowManager2 = (WindowManager) getSystemService(WINDOW_SERVICE);
        //here is all the science of params
        final WindowManager.LayoutParams myParams2 = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_TOAST,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        myParams2.gravity = Gravity.TOP | Gravity.LEFT;
        myParams2.x = 100;
        myParams2.y = 250;

        linearLayout1 = new LinearLayout(MyServices.this);
//        linearLayout1.setBackgroundResource(R.drawable.transparent);
        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams1.setMargins(0, 10, 0, 0);
        linearLayout1.setOrientation(LinearLayout.VERTICAL);
        linearLayout1.setBackgroundColor(Color.TRANSPARENT);
        tutorial = new ImageView(MyServices.this);
//        tutorial.setBackgroundResource(R.drawable.transparent);
        tutorial_text = new TextView(MyServices.this);
        tutorial_text.setTextColor(Color.WHITE);
        tutorial_text.setText("One Taps Smart Shortcut");
        tutorial.setImageResource(R.drawable.hand_tutorial);
        linearLayout1.addView(tutorial, layoutParams1);
        linearLayout1.addView(tutorial_text, layoutParams1);
        windowManager2.addView(linearLayout1, myParams2);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                linearLayout1.setVisibility(View.GONE);
                windowManager2.removeView(linearLayout1);
            }
        }, 2000);


        //***********************************************
//        myParams1.x= 0;
//
//        myParams1.y=0;
        windowManager1 = (WindowManager) getSystemService(WINDOW_SERVICE);
        //here is all the science of params
        final WindowManager.LayoutParams myParams1 = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_TOAST,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        myParams1.gravity = Gravity.CENTER | Gravity.CENTER;


        linearLayout = new LinearLayout(MyServices.this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(10, 0, 10, 0);


        // linearLayout.setLayoutParams(relativeLayoutParams);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setBackgroundResource(R.drawable.layout_bg);
        linearLayout.setAlpha(.5f);

//        linearLayout.setBackgroundColor(Color.BLACK);

        imageView = new ImageView(MyServices.this);
        imageView.setImageResource(R.drawable.screenshot);
        imageView1 = new ImageView(MyServices.this);
        imageView1.setImageResource(R.drawable.torch1);
        imageView2 = new ImageView(MyServices.this);
        imageView2.setImageResource(R.drawable.applock_noti);


        linearLayout.addView(imageView, layoutParams);
        linearLayout.addView(imageView1, layoutParams);
        linearLayout.addView(imageView2, layoutParams);
        //   linearLayout.setPadding((int) (floatingFaceBubble.getX()+floatingFaceBubble.getWidth()),0,0,0);

        linearLayout.setVisibility(View.GONE);

       /* imageView.setVisibility(View.GONE);
        imageView1.setVisibility(View.GONE);*/


        floatingFaceBubble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(context, "sree", Toast.LENGTH_SHORT).show();
                //**********************
                floatingFaceBubble.setClickable(false);
//floatingFaceBubble.setVisibility(View.GONE);
               /* imageView.setVisibility(View.VISIBLE)
                imageView1.setVisibility(View.VISIBLE);*/

               /* try {
                    boolean foregroud = new ForegroundCheckTask().execute(getApplicationContext()).get();
                    if (!foregroud)
                    {
                       System.exit(0);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }*/

                windowManager1.addView(linearLayout, myParams1);
                linearLayout.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        linearLayout.setVisibility(View.GONE);
                        windowManager1.removeView(linearLayout);
                        floatingFaceBubble.setClickable(true);
//                        floatingFaceBubble.setVisibility(View.VISIBLE);
                    }
                }, 3000)


                //**************************

                //floatingFaceBubble.setVisibility(View.GONE);
//                    floatingFaceBubble.setVisibility(View.GONE);
               /* Intent i = new Intent(getApplicationContext(),Transparent.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(i);*/

                /*new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        floatingFaceBubble.setVisibility(View.VISIBLE);
                    }
                },4000)*/;


//                takeScreenshot();
//                playSound();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floatingFaceBubble.setVisibility(View.GONE);
                linearLayout.setVisibility(View.GONE);
                Intent i = new Intent(getApplicationContext(), Transparent.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(i);
            }
        });
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout.setVisibility(View.GONE);
                HomeActivity.IS_TURN_ON_TORCH = true;
                Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                i.putExtra("splace", "torch");
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(i);
            }
        });
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout.setVisibility(View.GONE);
                //HomeActivity.IS_TURN_ON_TORCH = true;
                Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                i.putExtra("splace", "applock");

                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(i);
            }
        });

//
////        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
////              if (!Settings.canDrawOverlays(this)) {
////                  Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
////                          Uri.parse("package:" + getPackageName()));
////                  startActivityForResult(intent, PERMS_REQUEST_CODE);
////              }
////          }
//
//
//            floatingFaceBubble = new ImageView(this);
//        //a face floating bubble as imageView
//        floatingFaceBubble.setImageResource(R.drawable.ic_camera_alt_notic);
//        windowManager = (WindowManager)getSystemService(WINDOW_SERVICE);
//        //here is all the science of params
//        final WindowManager.LayoutParams myParams = new WindowManager.LayoutParams(
//                WindowManager.LayoutParams.WRAP_CONTENT,
//                WindowManager.LayoutParams.WRAP_CONTENT,
//                WindowManager.LayoutParams.TYPE_TOAST,
//                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
//                PixelFormat.TRANSLUCENT);
//        myParams.gravity = Gravity.TOP | Gravity.LEFT;
//        myParams.x=0;
//        myParams.y=100;
//        // add a floatingfacebubble icon in window
//
//
//
//        windowManager.addView(floatingFaceBubble, myParams);
//        floatingFaceBubble.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Toast.makeText(context, "sree", Toast.LENGTH_SHORT).show();
//
//                Intent i = new Intent(getApplicationContext(),Transparent.class);
//                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                getApplicationContext().startActivity(i);
//
//
//
//
////                takeScreenshot();
////                playSound();
//            }
//        });


        try {
            //for moving the picture on touch and slide
            floatingFaceBubble.setOnTouchListener(new View.OnTouchListener() {
                WindowManager.LayoutParams paramsT = myParams;
                private int initialX;
                private int initialY;
                private float initialTouchX;
                private float initialTouchY;
                private long touchStartTime = 0;


                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    //remove face bubble on long press
                    if (System.currentTimeMillis() - touchStartTime > ViewConfiguration.getLongPressTimeout() && initialTouchX == event.getX()) {
                        windowManager.removeView(floatingFaceBubble);
                        //  stopSelf();
                        return false;
                    }
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            touchStartTime = System.currentTimeMillis();
                            initialX = myParams.x;
                            initialY = myParams.y;
                            initialTouchX = event.getRawX();
                            initialTouchY = event.getRawY();
                            break;
                        case MotionEvent.ACTION_UP:


//                            Toast.makeText(context, "take a pic ", Toast.LENGTH_SHORT).show();

                            //takeScreenshot();
                            break;
                        case MotionEvent.ACTION_MOVE:
                            myParams.x = initialX + (int) (event.getRawX() - initialTouchX);
                            myParams.y = initialY + (int) (event.getRawY() - initialTouchY);
                            windowManager.updateViewLayout(v, myParams);
                            break;
                    }
                    return false;
                }

            });

        } catch (Exception e) {
            e.printStackTrace();
        }


    }



   /* public static void addShortcutForShopping(Context context, int icon) {
        Intent shortcutIntent = new Intent(context, shortcutClass);
        shortcutIntent.setAction(Intent.ACTION_MAIN);
        Intent addIntent = new Intent();
        addIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, shorcutName);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(context, icon));
        // addIntent.putExtra("duplicate", false);
        addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        context.sendBroadcast(addIntent);
    }*/


    //***********************
    public void intentnotification() {
        RemoteViews remoteView = new RemoteViews(getPackageName(), R.layout.custome_notification);


        Bitmap lorgicon = (((BitmapDrawable) getResources().getDrawable(R.drawable.noti_icon)).getBitmap());
        Intent notificationIntent = new Intent(this, HomeActivity.class);
        notificationIntent.putExtra("splace", "splace");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Intent notificationIntent1 = new Intent(this, HomeActivity.class);
        notificationIntent1.putExtra("splace", "torch");
        PendingIntent pendingIntent1 = PendingIntent.getActivity(this, 1, notificationIntent1, 0);

        Intent notificationIntent2 = new Intent(this, HomeActivity.class);
        notificationIntent2.putExtra("splace", "applock");
        PendingIntent pendingIntent2 = PendingIntent.getActivity(this, 2, notificationIntent2, 0);


        //
        remoteView.setOnClickPendingIntent(R.id.screenshot_noti, pendingIntent);
        remoteView.setOnClickPendingIntent(R.id.torch_noti, pendingIntent1);
        remoteView.setOnClickPendingIntent(R.id.applock_noti, pendingIntent2);

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = new Notification.Builder(this,NOTIFICATIONCHANNEL_ID)
                    .setContentTitle(getText(R.string.notification_title))
                    .setContentText(getText(R.string.notification_message))
                    .setContent(remoteView)
                    .setLargeIcon(lorgicon)
                    .setSmallIcon(R.drawable.ic_camera_alt_noti)
//                    .setContentIntent(pendingIntent)
                    .setOngoing(true)
                    .setChannelId(NOTIFICATIONCHANNEL_ID)
                    .setPriority(Notification.PRIORITY_MIN)
//                    .setTicker(getText(R.string.ticker_text))
                    .build();
        }

        startForeground(ONGOING_NOTIFICATION_ID, notification);
    }

//    public  void stopservice(){
//        stopForeground(true);
//    }


    //**********************


//    public  void floatingBubble(){
//        floatingFaceBubble.setVisibility(View.VISIBLE);
//    }


    boolean checkFlash = true;

    long startTime = 0;
    long stopTime = 0;
    int count = 0;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

    private ComponentName remoteComponentName;
    private RemoteControlClient remoteControlClient;


    boolean currentVersionSupportBigNotification = currentVersionSupportBigNotification();

    public static boolean currentVersionSupportBigNotification() {
        int sdkVersion = Build.VERSION.SDK_INT;
        if (sdkVersion >= Build.VERSION_CODES.JELLY_BEAN) {
            return true;
        }
        return false;
    }

    int NOTIFICATION_ID = 1111;


    // }


    @Override
    public void onDestroy() {
        super.onDestroy();
       /* prefHandler = new PreferenceHandler(getApplicationContext());
        if(prefHandler.getScreenShotSwitchStatus()){
           this.startService(new Intent(this, MyServices.class));

        }

*/
    }


    public void stopservice() {
        stopForeground(true);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;
//        return super.onStartCommand(intent, flags, startId);


    }


}







