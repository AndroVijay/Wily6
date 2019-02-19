package com.wily;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.MediaPlayer;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;

import com.wily.utils.Constants;
import com.wily.view.AssistiveTouch;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.Locale;

import static com.wily.MyServices.floatingFaceBubble;


public class Transparent extends Activity {

    private MediaProjection mMediaProjection;
    private MediaProjectionManager mMediaProjectionManager;

    private int mResultCode,mScreenDensity;
    private Intent mResultData;
    private static final String STATE_RESULT_CODE = "result_code";
    private static final String STATE_RESULT_DATA = "result_data";

    private static final int REQUEST_MEDIA_PROJECTION = 1;
    private VirtualDisplay mVirtualDisplay;
    private Surface mSurface;
    private SurfaceView mSurfaceView;
    boolean isCapture = false;
    private ImageReader mImageReader;
    private int mWidth;
    private int mHeight;
    Handler mHandler;

    boolean isStartCapture = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transparent);




        if (savedInstanceState != null) {
            mResultCode = savedInstanceState.getInt(STATE_RESULT_CODE);
            mResultData = savedInstanceState.getParcelable(STATE_RESULT_DATA);
        }

        mHandler = new Handler();
        mSurfaceView = (SurfaceView) findViewById(R.id.surface);
        mSurface = mSurfaceView.getHolder().getSurface();

           // System.out.println("hello"+mSurfaceView.getWidth());


        Activity activity = Transparent.this;
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mScreenDensity = metrics.densityDpi;

        mMediaProjectionManager = (MediaProjectionManager)
                this.getSystemService(Context.MEDIA_PROJECTION_SERVICE);

    }


    private void setUpMediaProjection() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mMediaProjection = mMediaProjectionManager.getMediaProjection(mResultCode, mResultData);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_MEDIA_PROJECTION) {
            if (resultCode != Activity.RESULT_OK) {
              //  Toast.makeText(Transparent.this,"cancel", Toast.LENGTH_SHORT).show();
                isStartCapture = false;
               this.finish();
//                floatingFaceBubble.setVisibility(View.VISIBLE);
//                floatingFaceBubble.setClickable(true);


               // return;

            }
            else {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Activity activity = Transparent.this;
                        if (activity == null) {
                            return;
                        }


                        mResultCode = resultCode;
                        mResultData = data;

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            setUpMediaProjection();
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            setUpVirtualDisplay();
                        }
                        isStartCapture = false;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            mMediaProjection.registerCallback(new MediaProjectionStopCallback(), mHandler);
                        }

                    }
                },1000);

            }



        }
    }



    @Override
    protected void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            stopScreenCapture();
        }
        //  this.finish();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!isStartCapture)
        {
            isStartCapture = true;
            startScreenCapture();
        }


//        takeScreenshot();
       // playSound();
//
// this.finish();
    }


    @Override
    protected void onStart() {
        super.onStart();
//        startScreenCapture();
    }





    private void stopScreenCapture() {
        if (mVirtualDisplay == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mVirtualDisplay.release();
        }
        mVirtualDisplay = null;
        if (mImageReader != null)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                mImageReader.setOnImageAvailableListener(null, null);
            }

    }


    private void startScreenCapture() {
//        floatingBubble();
        Activity activity = Transparent.this;
        if (mSurface == null || activity == null) {
            return;
        }
        if (mMediaProjection != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                setUpVirtualDisplay();
            }
        } else if (mResultCode != 0 && mResultData != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                setUpMediaProjection();
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                setUpVirtualDisplay();
            }
        } else {

            // This initiates a prompt dialog for the user to confirm screen projection.
            mSurfaceView.setVisibility(View.GONE);
//            playSound();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                startActivityForResult(
                        mMediaProjectionManager.createScreenCaptureIntent(),
                        REQUEST_MEDIA_PROJECTION);
            }
            // playSound();
        }
    }


    private void setUpVirtualDisplay() {

        if (!isCapture) {
            isCapture = true;

            Point size = new Point();
//            Display  display =  getWindowManager().getDefaultDisplay();
            getWindowManager().getDefaultDisplay().getSize(size);
//            display.getSize(size);
            mWidth = size.x;
            mHeight = size.y;


            //System.out.println("screen_width1"+getWindowManager().getDefaultDisplay().getWidth());

            // start capture reader
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                mImageReader = ImageReader.newInstance(mWidth, mHeight, PixelFormat.RGBA_8888, 2);
            }
            // mVirtualDisplay = .createVirtualDisplay(SCREENCAP_NAME, mWidth, mHeight, mDensity, VIRTUAL_DISPLAY_FLAGS, mImageReader.getSurface(), null, mHandler);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mVirtualDisplay = mMediaProjection.createVirtualDisplay("ScreenCapture",
                        mWidth, mHeight, mScreenDensity,
                        DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                        mImageReader.getSurface(), null, mHandler);
            }

            // System.out.println("screen_width" + mScreenDensity);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                mImageReader.setOnImageAvailableListener(new ImageAvailableListener(), mHandler);
            }


         /*   File filename;
            BitmapDrawable drawable = (BitmapDrawable) iv.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            try {
                String path = Environment.getExternalStorageDirectory().toString();

                new File(path + "/folder/subfolder").mkdirs();
                filename = new File(path + "/folder/subfolder/"+(et1.getText().toString())+".jpg");

                FileOutputStream out = new FileOutputStream(filename);

                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                out.flush();
                out.close();

                MediaStore.Images.Media.insertImage(getContentResolver(), filename.getAbsolutePath(), filename.getName(), filename.getName());

                Toast.makeText(getApplicationContext(), "File is Saved in  " + filename,1000).show();
            } catch (Exception e) {
                e.printStackTrace();
            }*/

 /*Image image = null;
            FileOutputStream fos = null;
            Bitmap bitmap = null;

            try {
                image = mImageReader.acquireLatestImage();
                if (image != null) {
                    Image.Plane[] planes = image.getPlanes();
                    ByteBuffer buffer = planes[0].getBuffer();
                    int pixelStride = planes[0].getPixelStride();
                    int rowStride = planes[0].getRowStride();
                    int rowPadding = rowStride - pixelStride * mWidth;

                    // create bitmap
                    bitmap = Bitmap.createBitmap(mWidth + rowPadding / pixelStride, mHeight, Bitmap.Config.ARGB_8888);
                    bitmap.copyPixelsFromBuffer(buffer);

                    // write bitmap to a file

                    Date now = new Date();
                    android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

                    try {

                        File myDirectory = new File(Environment.getExternalStorageDirectory() , "/Wily_Screenshot");

                        if (!myDirectory.exists()) {
                            myDirectory.mkdirs();
                        }


                        String mPath = Environment.getExternalStorageDirectory().toString() + "/Wily_Screenshot/"+now + ".jpg";

                        File imageFile = new File(mPath);
                        fos = new FileOutputStream(imageFile);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                        fos.flush();
                        fos.close();

                        ContentValues values = new ContentValues();
                        values.put(MediaStore.Images.Media.TITLE,imageFile.getName() );
                        values.put(MediaStore.Images.Media.DESCRIPTION, imageFile.getName());
                        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis ());
                        values.put(MediaStore.Images.ImageColumns.BUCKET_ID, imageFile.toString().toLowerCase(Locale.US).hashCode());
                        values.put(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME, imageFile.getName().toLowerCase(Locale.US));
                        values.put("_data", imageFile.getAbsolutePath());

                        ContentResolver cr = getContentResolver();
                        cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                       // MediaStore.Images.Media.insertImage(getContentResolver(), imageFile.getAbsolutePath(), imageFile.getName(), imageFile.getName());

                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                }

                if (bitmap != null) {
                    bitmap.recycle();
                }

                if (image != null) {
                    image.close();
                }
            }

        }*/
        }

    }
//    ImageReader mImageReader;
//    public void store(){
//        mImageReader = ImageReader.newInstance(mSurfaceView.getWidth(), mSurfaceView.getHeight(), ImageFormat.RGB_565, 2);
//
//
//
//
//    }


    MediaPlayer mp;
    private void playSound() {


        mp = MediaPlayer.create(this, R.raw.camera);

        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                // TODO Auto-generated method stub
                mp.release();
            }
        });
        mp.start();
    }
    ///****************************************************************************************
//private static final String TAG = "ScreenCaptureFragment";
//
//    private static final String STATE_RESULT_CODE = "result_code";
//    private static final String STATE_RESULT_DATA = "result_data";
//
//    private static final int REQUEST_MEDIA_PROJECTION = 1;
//
//    private int mScreenDensity;
//
//    private int mResultCode;
//    private Intent mResultData;
//
//    private Surface mSurface;
//    private MediaProjection mMediaProjection;
//    private VirtualDisplay mVirtualDisplay;
//    private MediaProjectionManager mMediaProjectionManager;
//   // private Button mButtonToggle;
//    private SurfaceView mSurfaceView;
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.activity_transparent, container, false);
//    }
//
//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//        mSurfaceView = (SurfaceView) view.findViewById(R.id.surface);
//        mSurface = mSurfaceView.getHolder().getSurface();
//       // mButtonToggle = (Button) view.findViewById(R.id.toggle);
//        //mButtonToggle.setOnClickListener(this);
//    }
//
//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//        mSurfaceView = (SurfaceView) view.findViewById(R.id.surface);
//        mSurface = mSurfaceView.getHolder().getSurface();
//
//    }
//
//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        Activity activity = getActivity();
//        DisplayMetrics metrics = new DisplayMetrics();
//        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
//        mScreenDensity = metrics.densityDpi;
//        mMediaProjectionManager = (MediaProjectionManager)
//                activity.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
//    }
//
//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        if (mResultData != null) {
//            outState.putInt(STATE_RESULT_CODE, mResultCode);
//            outState.putParcelable(STATE_RESULT_DATA, mResultData);
//        }
//    }
//
//
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_MEDIA_PROJECTION) {
//            if (resultCode != Activity.RESULT_OK) {
//                Log.i(TAG, "User cancelled");
//                Toast.makeText(getApplicationContext(), "0R.string.user_cancelled", Toast.LENGTH_SHORT).show();
//                return;
//            }
//            Activity activity = getActivity();
//            if (activity == null) {
//                return;
//            }
//            Log.i(TAG, "Starting screen capture");
//            mResultCode = resultCode;
//            mResultData = data;
//            setUpMediaProjection();
//            setUpVirtualDisplay();
//        }
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        stopScreenCapture();
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        tearDownMediaProjection();
//    }
//
//    private void setUpMediaProjection() {
//        mMediaProjection = mMediaProjectionManager.getMediaProjection(mResultCode, mResultData);
//    }
//
//    private void tearDownMediaProjection() {
//        if (mMediaProjection != null) {
//            mMediaProjection.stop();
//            mMediaProjection = null;
//        }
//    }
//
//
//    private void startScreenCapture() {
//        Activity activity = getActivity();
//        if (mSurface == null || activity == null) {
//            return;
//        }
//        if (mMediaProjection != null) {
//            setUpVirtualDisplay();
//        } else if (mResultCode != 0 && mResultData != null) {
//            setUpMediaProjection();
//            setUpVirtualDisplay();
//        } else {
//            Log.i(TAG, "Requesting confirmation");
//            // This initiates a prompt dialog for the user to confirm screen projection.
//            startActivityForResult(
//                    mMediaProjectionManager.createScreenCaptureIntent(),
//                    REQUEST_MEDIA_PROJECTION);
//        }
//    }
//
//    private void setUpVirtualDisplay() {
//        Log.i(TAG, "Setting up a VirtualDisplay: " +
//                mSurfaceView.getWidth() + "x" + mSurfaceView.getHeight() +
//                " (" + mScreenDensity + ")");
//        mVirtualDisplay = mMediaProjection.createVirtualDisplay("ScreenCapture",
//                mSurfaceView.getWidth(), mSurfaceView.getHeight(), mScreenDensity,
//                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
//                mSurface, null, null);
//        //mButtonToggle.setText(R.string.stop);
//    }
//
//    private void stopScreenCapture() {
//        if (mVirtualDisplay == null) {
//            return;
//        }
//        mVirtualDisplay.release();
//        mVirtualDisplay = null;
//        //mButtonToggle.setText(R.string.start);
//    }
//
//
//
///*****************************************************************************




    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private class MediaProjectionStopCallback extends MediaProjection.Callback {
        @Override
        public void onStop() {
            Log.e("ScreenCapture", "stopping projection.");
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mVirtualDisplay != null) mVirtualDisplay.release();
                    if (mImageReader != null)
                        mImageReader.setOnImageAvailableListener(null, null);

//                    if (mOrientationChangeCallback != null) mOrientationChangeCallback.disable();
//                    sMediaProjection.unregisterCallback(MediaProjectionStopCallback.this);

                }
            });
        }
    }
    File imageFile;
    String mPath;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private class ImageAvailableListener implements ImageReader.OnImageAvailableListener {

        @Override
        public void onImageAvailable(ImageReader reader) {
            Image image = null;
            FileOutputStream fos = null;
            Bitmap bitmap = null;

            try {
                image = mImageReader.acquireLatestImage();
                if (image != null) {
                    Image.Plane[] planes = image.getPlanes();
                    ByteBuffer buffer = planes[0].getBuffer();
                    int pixelStride = planes[0].getPixelStride();
                    int rowStride = planes[0].getRowStride();
                    int rowPadding = rowStride - pixelStride * mWidth;



                   // System.out.println("width"+mSurfaceView.getHolder().getSurfaceFrame());
                    //System.out.println("width"+getWindowManager().getDefaultDisplay().getWidth());

                    // create bitmap
//                    mSurfaceView.setDrawingCacheEnabled(true);
//                    bitmap=mSurfaceView.getDrawingCache();
                    bitmap = Bitmap.createBitmap(mWidth + rowPadding / pixelStride, mHeight, Bitmap.Config.ARGB_8888);
                    bitmap.copyPixelsFromBuffer(buffer);

                    // write bitmap to a file

                    Date now = new Date();
                    DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

                    try {
                        File myDirectory = new File(Environment.getExternalStorageDirectory() , "/Wily_Screenshot");

                        if (!myDirectory.exists()) {
                            myDirectory.mkdirs();
                        }
                        // image naming and path  to include sd card  appending name you choose for file

                         mPath = Environment.getExternalStorageDirectory()+"/Wily_Screenshot/"+"Screenshots"+now+".jpg";

                        imageFile = new File(mPath);
                        fos = new FileOutputStream(imageFile);
                        //   fos = new FileOutputStream(STORE_DIRECTORY + "/myscreen_" + IMAGES_PRODUCED + ".png");
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                        fos.flush();
                        fos.close();

                        ContentValues values = new ContentValues();
                        values.put(MediaStore.Images.Media.TITLE,imageFile.getName() );
                        values.put(MediaStore.Images.Media.DESCRIPTION, imageFile.getName());
                        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis ());
                        values.put(MediaStore.Images.ImageColumns.BUCKET_ID, imageFile.toString().toLowerCase(Locale.US).hashCode());
                        values.put(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME, imageFile.getName().toLowerCase(Locale.US));
                        values.put("_data", imageFile.getAbsolutePath());

                        ContentResolver cr = getContentResolver();
                        cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);


                       // MediaStore.Images.Media.insertImage(getContentResolver(), imageFile.getAbsolutePath(), imageFile.getName(), imageFile.getName());
//                        playSound();
                       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            stopScreenCapture();
                           mMediaProjection.stop();
                        }
//                        floatingFaceBubble.setVisibility(View.VISIBLE);

                       // linearLayout.setVisibility(View.VISIBLE);
////                        Notification notif = new Notification.Builder(mContext)
//                                  .setContentTitle("New photo from " + sender.toString())
//                                .setContentText(subject)
//                                .setSmallIcon(R.drawable.new_post)
//                                .setLargeIcon(aBitmap)
//                                .setStyle(new Notification.BigPictureStyle()
//                                        .bigPicture(aBigBitmap))
//                                .build();
//                                .build();

//                         new Notification.Builder(getBaseContext())
//                                .setContentTitle("Screen_Capture" )
//                                .setContentText("subject")
//                                .setSmallIcon(R.drawable.torch)
//                                //.setLargeIcon(imageFile)
//                                //.setStyle(new Notification.BigPictureStyle()
//                                       // .bigPicture(imageFile))
//                                .build();
                   createNotification();
                        Intent startServiceIntent = new Intent(Transparent.this, AssistiveTouch.class);
                        startServiceIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
                        Transparent.this.startService(startServiceIntent);
                       /* Intent startServiceIntent = new Intent(ScreenShotActivity.this, AssistiveTouch.class);
                        startServiceIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
                        ScreenShotActivity.this.startService(startServiceIntent);*/
//                        NotificationManager nm=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
//                        nm.notify();

                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                 //   IMAGES_PRODUCED++;
                 //   Log.e(TAG, "captured image: " + IMAGES_PRODUCED);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                }

                if (bitmap != null) {
                    bitmap.recycle();
                }

                if (image != null) {
                    image.close();
                }
                //floatingBubble();
                Transparent.this.finish();
            }
        }
    }



    public void createNotification() {
        // Prepare intent which is triggered if the
        // notification is selected
//        Intent intent = new Intent(this, Transparent.class);
//        Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(String.valueOf(imageFile)));
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + mPath), "image");
//        intent.setDataAndType(Uri.parse(mPath),"image");

//        Intent intent=new Intent(Intent.ACTION_VIEW,);

        playSound();

        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, PendingIntent.FLAG_ONE_SHOT);

        // Build notification
        // Actions are just fake
        Notification noti = null;



        Bitmap decodeFile = BitmapFactory.decodeFile(mPath);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            noti = new Notification.Builder(this)
                    .setContentTitle("Screen Captured")
                    //.setContentText("Subject")
                    .setSmallIcon(R.drawable.ic_launcher)
//                    .setLargeIcon(decodeFile)

                    .setContentIntent(pIntent)

//                    .setStyle(new Notification.BigPictureStyle())

                    .build();
          /*  Notification.BigPictureStyle bigPictureStyle = new Notification.BigPictureStyle(builder);
            bigPictureStyle.bigLargeIcon(decodeFile);
            bigPictureStyle.bigPicture(decodeFile);*/
        }
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, noti);


    }
//    public class MyNotification extends BroadcastReceiver
//    {
//        @Override
//        public void onReceive(Context context, Intent intent)
//        {
//            Log.d("ME", "Notification started");
//            Toast.makeText(context, "Onrecive", Toast.LENGTH_SHORT).show();
//
//            NotificationCompat.Builder mBuilder =
//                    (NotificationCompat.Builder) new NotificationCompat.Builder(context)
//                            .setSmallIcon(R.drawable.torch)
//                            .setContentTitle("My notification")
//                            .setContentText("Hello World!");
//
//            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//            mNotificationManager.notify(1, mBuilder.build());
//        }
//    }

/*
    private static Intent screenshotPermission = null;

    protected  void getScreenshotPermission() {
        try {
            if (hasScreenshotPermission()) {
                if(null != mMediaProjection) {
                 //   mMediaProjection.stop();
                    mMediaProjection = null;
                }
                mediaProjection = mediaProjectionManager.getMediaProjection(Activity.RESULT_OK, (Intent) screenshotPermission.clone());
            } else {
                openScreenshotPermissionRequester();
            }
        } catch (final RuntimeException ignored) {
            openScreenshotPermissionRequester();
        }
    }

    protected  void openScreenshotPermissionRequester(){
        final Intent intent = new Intent(Transparent.this, AcquireScreenshotPermissionIntent.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

   private boolean hasScreenshotPermission()
   {
       return screenshotPermission==null ? false: true;
   }

    protected  void setScreenshotPermission(final Intent permissionIntent) {
        screenshotPermission = permissionIntent;
    }*/

}





