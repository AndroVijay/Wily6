package com.wily.recievers;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.wily.R;
import com.wily.utils.ATUtils;



public class AdminReceiver extends DeviceAdminReceiver {

	@Override
	public void onEnabled(Context context, Intent intent) {
		ATUtils.getInstance(context).showToast(context.getString(R.string.admin_receiver_status_enabled), Toast.LENGTH_SHORT);
	}

}