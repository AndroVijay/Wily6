package com.wily.filelock;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.wily.R;

/**
 * Created by PANDEY on 28-06-2017.
 */

public class Lock_Fragment extends Fragment implements View.OnClickListener {

    View view;
    Button gallery,file_manager;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);

   view=inflater.inflate(R.layout.lock_filefragment,container,false);

        gallery= (Button) view.findViewById(R.id.button_gallery);
        file_manager= (Button) view.findViewById(R.id.button_filemanager);
        gallery.setOnClickListener(this);
        file_manager.setOnClickListener(this);







        return view;

    }

    @Override
    public void onClick(View v) {
        int id=v.getId();

        switch (id){


            case R.id.button_gallery:

                Intent intent = new Intent(Intent.ACTION_PICK);
                Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getAbsolutePath());
                // a directory
                intent.setDataAndType(uri, "image/*");
                startActivity(Intent.createChooser(intent, "Open folder"));


                break;
            case R.id.button_filemanager:

                Intent intent1 = new Intent(Intent.ACTION_PICK);
                Uri uri1 = Uri.parse(Environment.getExternalStorageDirectory().getAbsolutePath()); // a directory
                intent1.setDataAndType(uri1, "/*");
                startActivity(Intent.createChooser(intent1, "Open folder"));


                break;

        }


    }
}
