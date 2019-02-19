package com.wily;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.wily.utils.AppPrefs;
import com.wily.utils.Constants;
import com.wily.utils.SharedPreferencesName;
import com.wily.view.AssistiveTouch;

public class BootBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
			boolean isATActivated = AppPrefs.getInstance(context).getBooleanValue(SharedPreferencesName.KEY_IS_AT_ACTIVATED, false);
			if (isATActivated) {
				Intent startServiceIntent = new Intent(context, AssistiveTouch.class);
				startServiceIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
				context.startService(startServiceIntent);
			}
		}
	}
}