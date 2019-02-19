package com.wily;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by PANDEY on 24-04-2017.
 */

public class AlaramManagerBroadcastReceiver extends BroadcastReceiver {
    PreferenceHandler prefHandler;

    @Override
    public void onReceive(Context context, Intent intent) {
        prefHandler = new PreferenceHandler(context);
        //  Toast.makeText(context, "bootcompleted", Toast.LENGTH_SHORT).show();

        if (prefHandler.getScreenShotSwitchStatus()) {

            context.startService(new Intent(context, MyServices.class));
        }
    }
}
