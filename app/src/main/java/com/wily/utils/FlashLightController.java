package com.wily.utils;

import android.hardware.Camera;
import android.os.AsyncTask;
import android.util.Log;

@SuppressWarnings("deprecation")
public class FlashLightController {

	private boolean a = false;
	private Camera b;
	private Camera.Parameters c;

	private boolean startFlashPreview() {
		Camera localCamera = this.b;
		boolean bool = false;
		if (localCamera == null) {
		}
		try {
			this.b = Camera.open();
			this.c = this.b.getParameters();
			this.b.startPreview();
			bool = true;
			return bool;
		} catch (RuntimeException localRuntimeException) {
		}
		return false;
	}

	public void a(boolean paramBoolean) {
		this.a = paramBoolean;
	}

	public boolean isFlashOn() {
		return this.a;
	}

	public void b() {
		if ((this.b == null) || (this.c == null)) {
			return;
		}
		try {
			this.c.setFlashMode("torch");
			this.b.setParameters(this.c);
			return;
		} catch (RuntimeException localRuntimeException) {
		}
	}

	public void turnFlash_ON_OFF(boolean paramBoolean) {
		if (paramBoolean) {
			a(true);
			new b().execute(new Void[0]);
			return;
		}
		a(false);
		new a().execute(new Void[0]);
	}

	public void c() {
		if ((this.b == null) || (this.c == null)) {
			return;
		}
		try {
			this.c.setFlashMode("off");
			this.b.setParameters(this.c);
			return;
		} catch (RuntimeException localRuntimeException) {
		}
	}

	public void d() {
		if (this.b != null) {
		}
		try {
			this.b.stopPreview();
			this.b.release();
			this.b = null;
			return;
		} catch (Exception localException) {
		}
	}

	private class a extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			try {
				FlashLightController.this.c();
				FlashLightController.this.d();
			} catch (Exception e) {
			}
		}
	}

	private class b extends AsyncTask<Void, Void, Boolean> {
		private b() {
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			Log.d("TEST", "Task ON Flashlight");
			return Boolean.valueOf(FlashLightController.this.startFlashPreview());
		}

		protected void onPostExecute(Boolean result) {
			if (result.booleanValue()) {
				FlashLightController.this.b();
			}
		}
	}

}
