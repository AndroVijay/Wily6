package info.earntalktime.at;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification.BigPictureStyle;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.Image.Plane;
import android.media.ImageReader;
import android.media.ImageReader.OnImageAvailableListener;
import android.media.MediaPlayer;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.OrientationEventListener;
import info.earntalktime.at.utils.Constants;
import info.earntalktime.at.view.AssistiveTouch;

@TargetApi(21)
public class ScreenShotActivity extends Activity {
	private static boolean check;
	private String path;
	private int num;
	private final String f797c;
	private int cont;
	private MediaProjection mProjection;
	private MediaProjectionManager mProjectionManager;
	private ImageReader imageReader;
	private Display display;
	private VirtualDisplay virtualDisplay;
	private int desity;
	private int point_x;
	private int pont_y;
	private int rotation_;
	private Orient orient;
	private DisplayMetrics displayMetrics;
	private int num_;
	private Intent intent;
	private NotificationManager notificationManager;

	/* renamed from: com.easytouch.activity.ScreenShotActivity.1 */
	class Run_class implements Runnable {
		final /* synthetic */ ScreenShotActivity act;

		Run_class(ScreenShotActivity screenShotActivity) {
			this.act = screenShotActivity;
		}

		public void run() {
			this.act.m1445c();
		}
	}

	/* renamed from: com.easytouch.activity.ScreenShotActivity.a */
	private class C0068a implements OnImageAvailableListener {
		final /* synthetic */ ScreenShotActivity f792a;

		private C0068a(ScreenShotActivity screenShotActivity) {
			this.f792a = screenShotActivity;
		}

		public void onImageAvailable(ImageReader imageReader) {
			Exception e;
			OutputStream outputStream;
			Throwable th;
			Log.i("TEST", "in OnImageAvailable");
			FileOutputStream fileOutputStream = null;
			Bitmap bitmap = null;
			Image image = null;
			try {
				image = this.f792a.imageReader.acquireLatestImage();
				if (image != null) {
					Plane[] planes = image.getPlanes();
					if (planes[0].getBuffer() == null) {
						if (fileOutputStream != null) {
							try {
								fileOutputStream.close();
							} catch (IOException e2) {
								e2.printStackTrace();
							}
						}
						if (bitmap != null) {
							bitmap.recycle();
						}
						if (image != null) {
							image.close();
							return;
						}
						return;
					}
					int width = image.getWidth();
					int height = image.getHeight();
					int pixelStride = planes[0].getPixelStride();
					int rowStride = planes[0].getRowStride() - (pixelStride * width);
					byte[] bArr = new byte[((width * height) * 4)];
					int i = 0;
					bitmap = Bitmap.createBitmap(this.f792a.displayMetrics, width, height, Config.ARGB_8888);
					ByteBuffer buffer = planes[0].getBuffer();

//
					for (int i2 = 0; i2 < height; i2++) {
						for (int i3 = 0; i3 < width; i3++) {
							bitmap.setPixel(i3, i2, (((0 | ((buffer.get(i) & 255) << 16)) | ((buffer.get(i + 1) & 255) << 8)) | (buffer.get(i + 2) & 255)) | ((buffer.get(i + 3) & 255) << 24));
							i += pixelStride;
						}
						i += rowStride;
					}
					if (this.f792a.num == 0) {
						String stringBuilder = new StringBuilder(String.valueOf(this.f792a.path)).append("/Screenshot_").append(this.f792a.m1444b()).append(".png").toString();
						OutputStream fileOutputStream2 = new FileOutputStream(stringBuilder);
						try {
							bitmap.compress(CompressFormat.JPEG, 100, fileOutputStream2);
							image.close();
							ScreenShotActivity screenShotActivity = this.f792a;
							screenShotActivity.num = screenShotActivity.num + 1;

							this.f792a.m1429a(stringBuilder);
							this.f792a.m1438f();
							this.f792a.m1440g();
							this.f792a.sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.parse("file://" + stringBuilder)));
							this.f792a.num = 0;
							fileOutputStream = (FileOutputStream) fileOutputStream2;
						} catch (Exception e3) {
							e = e3;
							outputStream = fileOutputStream2;
							try {
								e.printStackTrace();
								if (fileOutputStream != null) {
									try {
										fileOutputStream.close();
									} catch (IOException e22) {
										e22.printStackTrace();
									}
								}
								if (bitmap != null) {
									bitmap.recycle();
								}
								if (image != null) {
									image.close();
									return;
								}
								return;
							} catch (Throwable th2) {
								th = th2;
								if (fileOutputStream != null) {
									try {
										fileOutputStream.close();
									} catch (IOException e4) {
										e4.printStackTrace();
									}
								}
								if (bitmap != null) {
									bitmap.recycle();
								}
								if (image != null) {
									image.close();
								}
								try {
									throw th;
								} catch (Throwable e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							}
						} catch (Throwable th3) {
							th = th3;
							outputStream = fileOutputStream2;
							if (fileOutputStream != null) {
								fileOutputStream.close();
							}
							if (bitmap != null) {
								bitmap.recycle();
							}
							if (image != null) {
								image.close();
							}
							try {
								throw th;
							} catch (Throwable e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}
					ScreenShotActivity screenShotActivity2 = this.f792a;
					screenShotActivity2.num = screenShotActivity2.num + 1;
				}
				if (fileOutputStream != null) {
					try {
						fileOutputStream.close();
					} catch (IOException e222) {
						e222.printStackTrace();
					}
				}
				if (bitmap != null) {
					bitmap.recycle();
				}
				if (image != null) {
					image.close();
				}
			} catch (Exception e5) {
				e = e5;
			}
		}
	}


	MediaPlayer mp;
	private void playSound() {


		mp = MediaPlayer.create(this, R.raw.camera);

		mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				// TODO Auto-generated method stub
				mp.release();
			}
		});
		mp.start();
	}
	/* renamed from: com.easytouch.activity.ScreenShotActivity.b */
	private class Orient extends OrientationEventListener {
		final /* synthetic */ ScreenShotActivity f793a;

		public Orient(ScreenShotActivity screenShotActivity, Context context) {
			super(context);
			this.f793a = screenShotActivity;
		}

		public void onOrientationChanged(int i) {
			synchronized (this) {
				int rotation = this.f793a.display.getRotation();
				if (rotation != this.f793a.rotation_) {
					this.f793a.rotation_ = rotation;
					try {
						if (this.f793a.virtualDisplay != null) {
							this.f793a.virtualDisplay.release();
						}
						if (this.f793a.imageReader != null) {
							this.f793a.imageReader.setOnImageAvailableListener(null, null);
						}
						this.f793a.m1443a();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public ScreenShotActivity() {
		this.num = 0;
		this.f797c = "screencap";
		this.cont = 9;
		this.num_ = 0;
		this.intent = null;
	}



	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		this.displayMetrics = getResources().getDisplayMetrics();
		this.desity = this.displayMetrics.densityDpi;
		this.display = getWindowManager().getDefaultDisplay();
		this.orient = new Orient(this, this);

		this.notificationManager = (NotificationManager) getSystemService("notification");
		if (VERSION.SDK_INT >= 21) {
			check = true;
		} else {
			check = false;
		}
		if (check) {
			this.mProjectionManager = (MediaProjectionManager) getSystemService("media_projection");
			m1436e();
		}
	}

	private void m1434d() {
		if (this.mProjection == null) {
			this.mProjection = this.mProjectionManager.getMediaProjection(this.num_, this.intent);
		}
	}

	private void m1436e() {
		Log.i("TEST", "Requesting confirmation");
		startActivityForResult(this.mProjectionManager.createScreenCaptureIntent(), 100);
	}

	public void m1443a() {
		Point point = new Point();
		this.display.getSize(point);
		this.point_x = point.x;
		this.pont_y = point.y;
		Log.d("TEST", "Size: " + this.point_x + " " + this.pont_y);
		this.imageReader = ImageReader.newInstance(this.point_x, this.pont_y, 1, 2);
		this.virtualDisplay = this.mProjection.createVirtualDisplay("screencap", this.point_x, this.pont_y, this.desity, this.cont, this.imageReader.getSurface(), null, null);
		this.num = 0;
		this.imageReader.setOnImageAvailableListener(new C0068a(ScreenShotActivity.this), null);
	}

	public String m1444b() {
		return new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(Calendar.getInstance().getTime());
	}

	private void m1429a(String str) {
		Bitmap decodeFile = BitmapFactory.decodeFile(str);
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.setDataAndType(Uri.fromFile(new File(str)), "image/*");
		PendingIntent activity = PendingIntent.getActivity(this, 0, intent, 0);
		Builder builder = new Builder(this);
		builder.setSmallIcon(R.drawable.ic_launcher);
		builder.setTicker("Screenshot captured");
		builder.setContentTitle("Screenshot captured");
		builder.setContentText("Tab to view screenshot.");
		builder.setLargeIcon(decodeFile);
		builder.setContentIntent(activity);
		builder.setAutoCancel(true);
		BigPictureStyle bigPictureStyle = new BigPictureStyle(builder);
		bigPictureStyle.bigLargeIcon(decodeFile);
		bigPictureStyle.bigPicture(decodeFile);
		bigPictureStyle.setBigContentTitle("Screenshot captured");
		this.notificationManager.cancel(123);
		this.notificationManager.notify(123, bigPictureStyle.build());
	}

	protected void onActivityResult(int i, int i2, Intent intent) {
		boolean z = true;
		if (i == 100) {
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					Intent startServiceIntent = new Intent(ScreenShotActivity.this, AssistiveTouch.class);
					startServiceIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
					ScreenShotActivity.this.startService(startServiceIntent);				}
			}, 1000);
			Log.e("TEST", "onActivityResult " + i2 + " " + (intent != null));
			if (i2 != -1) {
				// ToastUtils.m1594a(this, "Permission Denied", 0);
				m1438f();
				m1440g();
				finish();
				return;
			}
			this.intent = intent;
			this.num_ = i2;
			new Handler().postDelayed(new Run_class(this), 500);
			finish();
			String str = "TEST";
			StringBuilder append = new StringBuilder("onActivityResult ").append(this.num_).append(" ");
			if (this.intent == null) {
				z = false;
			}
			Log.e(str, append.append(z).toString());
		}
	}

	public void m1445c() {
		Log.e("TEST", "saveAndExData " + this.num_ + " " + (this.intent != null));
		if (this.num_ != 0 && this.intent != null) {
			if (this.mProjection != null) {
				m1440g();
			}
			m1434d();
			if (this.mProjection != null) {
				this.path = new StringBuilder(String.valueOf(Environment.getExternalStorageDirectory().getAbsolutePath())).append("/Wily_Screenshot/").toString();
				File file = new File(this.path);
				if (file.exists() || file.mkdirs()) {
					playSound();
					m1443a();
					if (this.orient.canDetectOrientation()) {
						this.orient.enable();
						return;
					}
					return;
				}
				Log.e("TEST", "failed to create file storage directory.");
				m1438f();
				m1440g();
			}
		}
	}

	private void m1438f() {
		if (this.virtualDisplay != null) {
			this.virtualDisplay.release();
			this.virtualDisplay = null;
			if (this.imageReader != null) {
				this.imageReader.setOnImageAvailableListener(null, null);
			}
		}
	}

	private void m1440g() {
		if (this.mProjection != null) {
			this.mProjection.stop();
			this.mProjection = null;
		}
	}
}
