package com.wily.utilss;

import android.content.Context;
import android.provider.Settings;

import com.wily.applock.AppList;

import java.util.ArrayList;



/**
 * Created by PANDEY on 08-05-2017.
 */

public class GlobalVariables {
    public static ArrayList<AppList> PACKAGES_LIST;

    public static boolean check_open_app = true;

    public static boolean isAccessibiltyEnable(Context ctx) {
        boolean status = false;
        try {
            int i = Settings.Secure.getInt(ctx.getContentResolver(), Settings.Secure.ACCESSIBILITY_ENABLED);
            status = (i == 0) ? false : true;
            //   Log.d(LOGTAG, "ACCESSIBILITY: " + accessibilityEnabled);
        } catch (Settings.SettingNotFoundException e) {
            // Log.d(LOGTAG, "Error finding setting, default accessibility to not found: " + e.getMessage());
        }
        return status;
    }
}
