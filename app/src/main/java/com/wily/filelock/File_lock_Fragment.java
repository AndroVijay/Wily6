package com.wily.filelock;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Build;
import android.os.Bundle;
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

public class File_lock_Fragment extends Fragment implements View.OnClickListener {
    Button lock_file,unlock_file;
    View view;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);

            view=inflater.inflate(R.layout.fragment_file_lock,container,false);
                    init();
        setInitFragment();

                lock_file.setOnClickListener(this);
                unlock_file.setOnClickListener(this);



            return view;
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void setInitFragment() {


        Fragment fragment = new Lock_Fragment();
        FragmentManager fragmentManager = getChildFragmentManager();
        fragmentManager.beginTransaction().addToBackStack(null);
        fragmentManager.beginTransaction().replace(R.id.content_frame1, fragment).commit();


    }

    private void init() {
        lock_file= (Button) view.findViewById(R.id.lock_file);
        unlock_file= (Button) view.findViewById(R.id.unlock_file);




    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onClick(View v) {

        int id=v.getId();
        switch (id){

            case R.id.lock_file:

                Fragment fragment = new Lock_Fragment();
                FragmentManager fragmentManager = getChildFragmentManager();
                fragmentManager.beginTransaction().addToBackStack(null);
                fragmentManager.beginTransaction().replace(R.id.content_frame1, fragment).commit();

                break;
            case R.id.unlock_file:
                Fragment fragment1 = new Unlock_Fragment();
                FragmentManager fragmentManager1 = getChildFragmentManager();
                fragmentManager1.beginTransaction().addToBackStack(null);
                fragmentManager1.beginTransaction().replace(R.id.content_frame1, fragment1).commit();



                break;

        }

    }
}
