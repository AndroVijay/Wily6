package com.wily;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;

import com.wily.recievers.AdminReceiver;

public class DeviceAdminActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ComponentName cn = new ComponentName(this, AdminReceiver.class);
		Intent intentLock = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
		intentLock.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, cn);
		intentLock.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, this.getResources().getString(com.wily.R.string.device_admin_explanation));
		startActivityForResult(intentLock, 1000);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1000) {
			finish();
		}
	}
}
