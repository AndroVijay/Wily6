package info.earntalktime.at;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import info.earntalktime.at.utils.AppPrefs;
import info.earntalktime.at.utils.Constants;
import info.earntalktime.at.utils.SharedPreferencesName;
import info.earntalktime.at.view.AssistiveTouch;

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