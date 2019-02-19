package com.wily;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by neha on 06-04-2017.
 */

public class PreferenceHandler {

    SharedPreferences pref;
    SharedPreferences.Editor edit;


    public PreferenceHandler(Context ctx) {
        pref = ctx.getSharedPreferences("com.wily", Context.MODE_PRIVATE);
        edit = pref.edit();
    }


    public boolean getScreenShotSwitchStatus() {
        return pref.getBoolean("status", false);
    }

    public void setScreenShotSwitchStatus(boolean status) {
        edit.putBoolean("status", status).commit();
    }
    public boolean SplaceStatus() {

        return pref.getBoolean("access_status", false);
    }

    public void SplaceStatusData(boolean status) {
        edit.putBoolean("access_status", status).commit();
    }




    public boolean SplaceStatusData() {

        return pref.getBoolean("access_status", false);
    }

    public void SplaceStatus(boolean status) {
        edit.putBoolean("access_status", status).commit();
    }

}
