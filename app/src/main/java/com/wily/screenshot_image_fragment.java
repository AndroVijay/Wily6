package com.wily;

import android.Manifest;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import static com.wily.ScreenshotFragment.PERMS_REQUEST_CODE;

/**
 * Created by PANDEY on 19-06-2017.
 */


public class screenshot_image_fragment extends Fragment {
    GridView gridView;
    public ImageView imageView_show;


    public ImageView getDisplayImgView() {
        return imageView_show;
    }

    public class ImageAdapter extends BaseAdapter {

        private Context mContext;
        ArrayList<String> itemList = new ArrayList<String>();

        public void setItemList(ArrayList<String> _list)
        {
            this.itemList = _list;
        }
        public ArrayList<String> getItemList() {
            return itemList;
        }

        public ImageAdapter(Context c) {
            mContext = c;
        }

        void add(String path) {
            itemList.add(path);
        }

        @Override
        public int getCount() {
            return itemList.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub


            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {  // if it's not recycled, initialize some attributes
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 300));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                imageView.setPadding(8, 8, 8, 8);
            } else {
                imageView = (ImageView) convertView;
            }

            Bitmap bm = decodeSampledBitmapFromUri(itemList.get(position), ViewGroup.LayoutParams.WRAP_CONTENT, 300);

            imageView.setImageBitmap(bm);
            return imageView;
        }

        Bitmap bm;

        public Bitmap decodeSampledBitmapFromUri(String path, int reqWidth, int reqHeight) {

            Bitmap bm = null;
            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, options);

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            bm = BitmapFactory.decodeFile(path, options);

            return bm;
        }

        public int calculateInSampleSize(

                BitmapFactory.Options options, int reqWidth, int reqHeight) {
            // Raw height and width of image
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;

            if (height > reqHeight || width > reqWidth) {
                if (width > height) {
                    inSampleSize = Math.round((float) height / (float) reqHeight);
                } else {
                    inSampleSize = Math.round((float) width / (float) reqWidth);
                }
            }

            return inSampleSize;
        }

    }

    ImageAdapter myImageAdapter;

    View v;

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.INTERNET,
            }, PERMS_REQUEST_CODE);
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {


        v = inflater.inflate(R.layout.screenshot_image, container, false);




        checkPermission();

        gridView = (GridView) v.findViewById(R.id.screenshot_image);
        myImageAdapter = new ImageAdapter(getActivity());
        gridView.setAdapter(myImageAdapter);

        imageView_show = (ImageView) v.findViewById(R.id.show_image);
        TextView textView = (TextView) v.findViewById(R.id.text_msg);

        if (myImageAdapter.isEmpty()) {
            textView.setVisibility(View.VISIBLE);
            //textView.setText("Your Wily screenshot gallery is currently empty");
        }
//        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(final AdapterView<?> parent, final View view, final int position, long id) {

                                                String path_name = null;
                                                ImageView im = (ImageView) view;
                                                dialog = new Dialog(getActivity(), R.style.NewDialog);
                                                LayoutInflater inflater = getActivity().getLayoutInflater();
                                                final View layout = inflater.inflate(R.layout.image_dailog, null);
                                                dialog.setContentView(layout);

                                                dialog.setCancelable(false);
                                                ImageView show_image = (ImageView) layout.findViewById(R.id.image_show);
                                                show_image.setImageBitmap(((BitmapDrawable) im.getDrawable()).getBitmap());
                                                ImageView dialogCloseButton = (ImageView) layout.findViewById(R.id.cross_button);
                                                ImageView delet = (ImageView) layout.findViewById(R.id.delete);
                                                ImageView share = (ImageView) layout.findViewById(R.id.share);
                                                ImageView edit = (ImageView) layout.findViewById(R.id.edit);


                                                delet.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {


                                                        if (myImageAdapter != null) {

                                                            String path = myImageAdapter.getItemList().get(position);
                                                            File file = new File(path);
                                                            file.delete();
                                                          //  myImageAdapter.itemList.size();
                                                            myImageAdapter.getItemList().remove(position);
                                                            myImageAdapter.notifyDataSetChanged();

                                                            dialog.dismiss();

                                                        }
                                                    }
                                                });
                                                share.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        String path = null;
                                                        File file = null;
                                                        if (myImageAdapter != null) {

                                                            path = myImageAdapter.getItemList().get(position);
                                                            file = new File(path);

                                                        }
                                                        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                                                        sharingIntent.setDataAndType(Uri.fromFile(file), "image/*");
//                                                            sharingIntent.setDataAndType()
                                                        sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));

//                                                            sharingIntent.setType("image/*");
                                                        // sharingIntent.putExtra(Intent.EXTRA_TEXT, "Hey, I am using this app it really makes my life easy, you can also download com.wily. Click https://play.google.com/store/apps/details?id=com.com.wily");
                                                        startActivity(Intent.createChooser(sharingIntent, "share using"));


                                                    }
                                                });


                                                // itemList.get(position)
                                                // getString(position);


                                                dialogCloseButton.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        // TODO Auto-generated method stub
                                                        dialog.dismiss();
                                                        //finishAffinity();

                                                    }
                                                });

                                                edit.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                       /* String path = null;
                                                        File file = null;
                                                        if (myImageAdapter != null) {

                                                            path = myImageAdapter.getItemList().get(position);
                                                            file = new File(path);

                                                        }
                                                        performCrop(path);*/
                                                    }
                                                });

                                                dialog.show();

                                                // click_on_image();

                                                   /* if (imageView_show.getVisibility() == View.GONE) {
                                                        imageView_show.setVisibility(View.VISIBLE);
                                                        ImageView im = (ImageView) view;

                                                        imageView_show.setImageBitmap(((BitmapDrawable) im.getDrawable()).getBitmap());
                                                    }*/
//                                                Toast.makeText(getActivity(), ""+position, Toast.LENGTH_SHORT).show();
                                            }
                                        }
        );
//        }

        String ExternalStorageDirectoryPath = Environment
                .getExternalStorageDirectory()
                .getAbsolutePath();

        String targetPath = ExternalStorageDirectoryPath + "/Wily_Screenshot/";

//        Toast.makeText(getApplicationContext(), targetPath, Toast.LENGTH_LONG).show();
        File targetDirector = new File(targetPath);

        File[] files = targetDirector.listFiles();
        for (File file : files) {
            myImageAdapter.add(file.getAbsolutePath());
        }


        return v;
    }

    Dialog dialog;


    private void performCrop(String picUri) {
        try {
            //Start Crop Activity

            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            File f = new File(picUri);
            Uri contentUri = Uri.fromFile(f);

            cropIntent.setDataAndType(contentUri, "image/*");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 200);
            cropIntent.putExtra("outputY", 150);

            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, 56);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "your device doesn't support the crop action!";
            Toast toast = Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    Bitmap bitmap = null;
   /* @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode==56 && resultCode==RESULT_OK)
        {





            File myDirectory = new File(Environment.getExternalStorageDirectory() , "/Wily_Screenshot");

            if (!myDirectory.exists()) {
                myDirectory.mkdirs();
            }
            // image naming and path  to include sd card  appending name you choose for file
            Date now = new Date();
            DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);
           String mPath = Environment.getExternalStorageDirectory()+"/Wily_Screenshot/"+"Screenshots"+now+".jpg";

            File imageFile = new File(mPath);
            FileOutputStream  fos = null;
            try {
                fos = new FileOutputStream(imageFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
            //   fos = new FileOutputStream(STORE_DIRECTORY + "/myscreen_" + IMAGES_PRODUCED + ".png");



*//*
//            Uri Selected_image=data.getData();
            Bundle extras = data.getExtras();
            Bitmap bb = extras.getParcelable("data");
//            Intent intent=new Intent(selectimage.this,MainActivity.class);
            String fileName = "myImage";//no .png or .jpg needed
            try {
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bb.compress(Bitmap.CompressFormat.PNG, 100, bytes);
                FileOutputStream fo =  getActivity().openFileOutput(fileName,MODE_PRIVATE);
                fo.write(bytes.toByteArray());
                // remember close file output
                fo.close();
            } catch (Exception e) {
                e.printStackTrace();
                fileName = null;
            }
            intent.putExtra("mybm",Environment.getExternalStorageDirectory()+"/Wily_Screenshot/" );
            startActivity(intent);*//*
        }





    }*/

    private void click_on_image() {

        dialog = new Dialog(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View layout = inflater.inflate(R.layout.image_dailog, null);
        dialog.setContentView(layout);

        dialog.setCancelable(false);
        ImageView show_image = (ImageView) layout.findViewById(R.id.image_show);
        ImageView dialogCloseButton = (ImageView) layout.findViewById(R.id.cross_button);


        dialogCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialog.dismiss();
                //finishAffinity();

            }
        });


        dialog.show();


    }


}
