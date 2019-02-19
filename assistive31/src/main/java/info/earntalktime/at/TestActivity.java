package info.earntalktime.at;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import info.earntalktime.at.utils.ATUtils;

public class TestActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		
		
	}
	
	public void clickMe(View v){
		Log.e("Screen Rotation", "" + ATUtils.getInstance(this).getScreenRotation());
		Log.e("Screen Orientation", "" + ATUtils.getInstance(this).getScreenOrientation());
		int rot = ATUtils.getInstance(this).getScreenRotation();
		if (rot == 1) {
			ATUtils.getInstance(this).setScreenRotation(0);
		} else {
			ATUtils.getInstance(this).setScreenRotation(1);
		}
	}

}
