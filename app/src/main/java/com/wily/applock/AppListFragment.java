package com.wily.applock;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.wily.utilss.GlobalVariables;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.wily.R;

/**
 * Created by PANDEY on 28-04-2017.
 */

public class AppListFragment extends Fragment {


    PackageManager packageManager;

    @SuppressWarnings("ResourceType")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.applist_fragment, container, false);
        ArrayList<String> list;
                setHasOptionsMenu(true);
        ListView userInstalledApps = (ListView) view.findViewById(R.id.installed_app_list);


        if (GlobalVariables.PACKAGES_LIST != null && GlobalVariables.PACKAGES_LIST.size() > 0) {

            Collections.sort(GlobalVariables.PACKAGES_LIST, new Comparator<AppList>() {
                @Override
                public int compare(AppList o1, AppList o2) {

                    return (o1.getName().toString()).compareToIgnoreCase(o2.getName().toString());

                }

            });



            AppAdapter installedAppAdapter = new AppAdapter(getActivity(), GlobalVariables.PACKAGES_LIST);
            userInstalledApps.setAdapter(installedAppAdapter);
        } else {
            Toast.makeText(getActivity(), "No data found! ", Toast.LENGTH_LONG).show();
        }
        return view;
    }

}

