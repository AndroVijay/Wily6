package info.earntalktime.at;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import info.earntalktime.at.utils.AppPrefs;
import info.earntalktime.at.utils.Constants;
import info.earntalktime.at.utils.SharedPreferencesName;
import info.earntalktime.at.view.AssistiveTouch;

@SuppressLint("NewApi")
public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		FragmentManager fragmentManager = getFragmentManager();
		try {
			fragmentManager.popBackStack();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Fragment fragment = new UtilitiesFragment();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.replace(R.id.container, fragment).commit();
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
