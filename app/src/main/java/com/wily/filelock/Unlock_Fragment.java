package com.wily.filelock;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wily.R;

/**
 * Created by PANDEY on 28-06-2017.
 */

public class Unlock_Fragment extends Fragment {
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
    view=inflater.inflate(R.layout.unlock_file_fragment,container,false);




        return view;
    }
}
