package info.earntalktime.at.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayOutputStream;

public class Constants {
	public interface ACTION {
		public static String MAIN_ACTION = "com.truiton.foregroundservice.action.main";
		public static String STARTFOREGROUND_ACTION = "com.truiton.foregroundservice.action.startforeground";
		public static String STOPFOREGROUND_ACTION = "com.truiton.foregroundservice.action.stopforeground";
	}

	public interface NOTIFICATION_ID {
		public static int FOREGROUND_SERVICE = 101;
	}

	public static byte[] getByteFromDrawable(Drawable d)
	{
		//Drawable d; // the drawable (Captain Obvious, to the rescue!!!)
		Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
		byte[] bitmapdata = stream.toByteArray();

		return  bitmapdata;
	}
	public static Drawable getDrawableFromByte(byte[] b)
	{
		Drawable image = new BitmapDrawable(BitmapFactory.decodeByteArray(b, 0, b.length));

		return  image;
	}
}