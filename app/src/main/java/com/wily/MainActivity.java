/*
package com.wily;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.wily.utils.AppPrefs;
import com.wily.utils.Constants;
import com.wily.utils.SharedPreferencesName;
import com.wily.view.AssistiveTouch;

@SuppressLint("NewApi")
public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(info.earntalktime.at.R.layout.activity_main);

		FragmentManager fragmentManager = getFragmentManager();
		try {
			fragmentManager.popBackStack();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Fragment fragment = new UtilitiesFragment();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.replace(info.earntalktime.at.R.id.container, fragment).commit();
		boolean isATActivated = AppPrefs.getInstance(this).getBooleanValue(SharedPreferencesName.KEY_IS_AT_ACTIVATED, false);
		if (isATActivated) {
			new Thread(new Runnable() {
				public void run() {
					Intent miniModeService = new Intent(MainActivity.this, AssistiveTouch.class);
					miniModeService.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
					startService(miniModeService);
				}
			}).start();
		}
		
//		startActivity(new Intent(this, TestActivity.class));
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
*/
