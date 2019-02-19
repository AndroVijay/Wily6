package com.wily.applock;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.wily.R;


/**
 * Created by PANDEY on 28-04-2017.
 */

public class NumLockFragment extends Fragment implements View.OnClickListener {


    Fragment fragment2;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    EditText password,confirm_password;
    Button cancel,setpassword;
    public static String pass="";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.numlock_frgment,container,false);

            password= (EditText) view.findViewById(R.id.enter_password);
            confirm_password= (EditText) view.findViewById(R.id.confirm_password);
            cancel= (Button) view.findViewById(R.id.cancel);
            setpassword= (Button) view.findViewById(R.id.set_password);

           setpassword.setOnClickListener(this);
            cancel.setOnClickListener(this);

        pass=password.getText().toString();



        return   view;
    }


    @Override
    public void onClick(View v) {

int id=v.getId();
        switch (id) {
            case R.id.set_password:
                if (password.getText().toString().equals(confirm_password.getText().toString())) {
                    Toast.makeText(getActivity(), "sucsessfully set password", Toast.LENGTH_SHORT).show();
                    fragment2 = new AppListFragment();
                    fragmentManager = getFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, fragment2);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                } else {
                    confirm_password.setText("");
                }

                break;

            case R.id.cancel:
                password.setText("");
                confirm_password.setText("");
                break;

        }
    }
}
