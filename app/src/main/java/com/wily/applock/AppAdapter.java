package com.wily.applock;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wily.R;
import com.wily.utilss.GlobalVariables;

import java.util.List;

import static com.wily.SplashActivity.sqLiteDatabase;
import static com.wily.SplashActivity.userDbHelper;

public class AppAdapter extends BaseAdapter {

    public static int position_no;
    Context _ctx;
    //    ImageView lock;
    Cursor cursor;
    boolean check_app_exist = false;
    private LayoutInflater layoutInflater;
    private List<AppList> listStorage;
    private boolean[] itemToggled = new boolean[GlobalVariables.PACKAGES_LIST.size()];

    public AppAdapter(Context context, List<AppList> customizedListView) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        listStorage = customizedListView;
        _ctx = context;
        for (int i = 0; i < itemToggled.length; i++) {
            itemToggled[i] = false;
        }

//        itemToggled = new boolean[];
    }

    @Override
    public int getCount() {
        return listStorage.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    ;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder listViewHolder;
        if (convertView == null) {
            listViewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.installed_app_list, parent, false);


            listViewHolder.textInListView = (TextView) convertView.findViewById(R.id.list_app_name);
            listViewHolder.imageInListView = (ImageView) convertView.findViewById(R.id.app_icon);
            listViewHolder.main = (LinearLayout) convertView.findViewById(R.id.main_lay);
            listViewHolder.lock = (ImageView) convertView.findViewById(R.id.lockimage);
//            listViewHolder.lock=(ImageView)convertView.getc


            convertView.setTag(listViewHolder);
        } else {
            listViewHolder = (ViewHolder) convertView.getTag();
        }
        listViewHolder.textInListView.setText(listStorage.get(position).getName());
        listViewHolder.imageInListView.setImageDrawable(listStorage.get(position).getIcon());

        AppList data = GlobalVariables.PACKAGES_LIST.get(position);
        final String dt = data.getName();
        final String pack = data.getPackagename();
try {


    sqLiteDatabase = userDbHelper.getReadableDatabase();
    cursor = userDbHelper.fetch_appname(sqLiteDatabase);
    if (cursor.moveToFirst()) {
        do {
//                String user_pass, user_check;
            String app_name = cursor.getString(0);


            for (int i = 0; i < itemToggled.length; i++) {
//                        if (itemToggled[position])
//                        {

                AppList data1 = GlobalVariables.PACKAGES_LIST.get(i);
                String dt1 = data1.getPackagename();


                if (app_name.equals(dt1)) {
                    itemToggled[i] = true;
//                        check_app_exist = true;
//                                 listViewHolder.lock.setImageResource(R.drawable.tag_lock);
//                        break;
                    // }

                } else {

//                listViewHolder.lock.setVisibility(View.VISIBLE);
//                            listViewHolder.lock.setImageResource(R.drawable.screenshot);
                }
            }

                    /*if (app_name.equals(dt)) {
                        check_app_exist = true;
                        break;
                    }*/


//                            Log.e("appp_name",""+app_name);
        } while (cursor.moveToNext());

    }
}catch (Exception e){
    System.out.println(""+e);
}

        for (int i = 0; i < itemToggled.length; i++) {
            if (itemToggled[position])
//                listViewHolder.lock.setVisibility(View.GONE);

                    listViewHolder.lock.setImageResource(R.drawable.lock);


            else {

//                listViewHolder.lock.setVisibility(View.VISIBLE);
                listViewHolder.lock.setImageResource(R.drawable.unlock);
            }
        }



        listViewHolder.main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(_ctx,"Position is: "+position , Toast.LENGTH_LONG).show();

                position_no = position;

//                Toast.makeText(_ctx, ""+getItem(position), Toast.LENGTH_SHORT).show();


//                String dt=GlobalVariables.PACKAGES_LIST.toString();

//                GlobalVariables.PACKAGES_LIST.get(position).getName();


                if (!pack.equals(null)) {
                    sqLiteDatabase = userDbHelper.getReadableDatabase();
                    cursor = userDbHelper.fetch_appname(sqLiteDatabase);
                    if (cursor.moveToFirst()) {
                        do {
                            String app_name = cursor.getString(0);

                            if (app_name.equals(pack)) {
                                check_app_exist = true;
                                break;
                            }


//                            Log.e("appp_name",""+app_name);
                        } while (cursor.moveToNext());
                    }


                }


                if (check_app_exist) {
                    check_app_exist = false;

                    sqLiteDatabase = userDbHelper.getWritableDatabase();
                    userDbHelper.delete_appname(pack, sqLiteDatabase);
//                   listViewHolder.lock.setImageResource(R.drawable.tag_lock);
//                    listViewHolder.lock.setImageResource(R.drawable.tag_lock);

                    //    listViewHolder.lock.setImageResource(itemToggled[position] ? R.drawable.tag_lock : R.drawable.screenshot);
                    Toast.makeText(_ctx, "" + dt + "  Unlock  ", Toast.LENGTH_SHORT).show();


                } else {
                    if (!Aplock_Accessibilty.isEnabled) {

                        new android.app.AlertDialog.Builder(_ctx)
                                .setMessage("We need your permission to enable the App lock feature")
                                .setPositiveButton("Enable", new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                Intent goToSettings = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                                                goToSettings.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
//                                    goToSettings.setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
                                                _ctx.startActivity(goToSettings);
                                            }
                                        }
                                ).create()
                                .show();

                    }

                    if (Aplock_Accessibilty.isEnabled) {
                        sqLiteDatabase = userDbHelper.getWritableDatabase();
                        userDbHelper.insert_app(pack, sqLiteDatabase);
                        Toast.makeText(_ctx, "" + dt + "  Locked  ", Toast.LENGTH_SHORT).show();
                    }
                }
                itemToggled[position] = !itemToggled[position];
                notifyDataSetChanged();


            }
        });


        /*sqLiteDatabase = userDbHelper.getReadableDatabase();
        cursor = userDbHelper.fetch_appname(sqLiteDatabase);
        if (cursor.moveToFirst()) {
            do {
                String app_name = cursor.getString(0);

                if (app_name.equals(pack)) {
                    check_app_exist = true;
                    break;
                }


//                            Log.e("appp_name",""+app_name);
            } while (cursor.moveToNext());
        }*/


        return convertView;
    }


    static class ViewHolder {

        TextView textInListView;
        ImageView imageInListView;
        LinearLayout main;
        ImageView lock;
    }

}
