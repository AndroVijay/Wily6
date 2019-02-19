package com.wily.applock;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wily.R;
import com.wily.utilss.GlobalVariables;

import java.util.List;
import java.util.TimerTask;

import static com.wily.SplashActivity.sqLiteDatabase;
import static com.wily.SplashActivity.userDbHelper;

public class Unlock_Activity extends AppCompatActivity {
    EditText password;
    Button ok;
    Handler handler;
    ImageView imageView;
    //private String CorrectPattern =materialLockView.correct_pass ;
    Button okk;
    EditText unlock_pin;
    TextView unlock_text, timerSet;

    int count = 0;
    CountDownTimer timer;
    TimerTask task;
    private MaterialLockView materialLockView;
    private String CorrectPattern = "42685";
    private String user_pass, user_check;
    //String p_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patter_unlock_layout);
        String p_name;
        Bundle bundle = getIntent().getExtras();
        p_name = bundle.getString("packagename");
        Drawable icon = null;
        try {
            icon = getPackageManager().getApplicationIcon(p_name);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        materialLockView = (MaterialLockView) findViewById(R.id.pattern);
        imageView = (ImageView) findViewById(R.id.lock_icon);
        okk = (Button) findViewById(R.id.submit_pass);
        unlock_pin = (EditText) findViewById(R.id.pin_pass_unlock);
        unlock_text = (TextView) findViewById(R.id.unlock_text);
        timerSet = (TextView) findViewById(R.id.timerSet);
        imageView.setImageDrawable(icon);

        sqLiteDatabase = userDbHelper.getReadableDatabase();

        Cursor cursor = userDbHelper.getInformations(sqLiteDatabase);

        if (cursor.moveToFirst()) {
            do {
//                String user_pass, user_check;
                user_pass = cursor.getString(0);
                user_check = cursor.getString(1);
//
//                DataProvider dataProvider = new DataProvider(name, mob, email);
//                dataListAdapter.add(dataProvider);
//                Log.e("userpass",""+user_pass);
//                Log.e("userpass-----",""+user_check);
            } while (cursor.moveToNext());
        }


        if (user_check.equals("pin")) {
            materialLockView.setVisibility(View.GONE);
            okk.setVisibility(View.VISIBLE);
            unlock_pin.setVisibility(View.VISIBLE);
            unlock_text.setText("Enter Pin to Unlock");
            okk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!unlock_pin.getText().toString().equals(user_pass)) {


                        if (count < 3) {
                            count++;
                            unlock_pin.setText("");
                            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(500);
                        } else {
                            unlock_pin.setText("");
                            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(800);

                            Toast.makeText(Unlock_Activity.this, "over attempt", Toast.LENGTH_SHORT).show();
                            timer = new CountDownTimer(10000, 1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {
                                    timerSet.setVisibility(View.VISIBLE);
                                    timerSet.setText("Unlock in : " + millisUntilFinished / 1000 + " seconds");
                                }

                                @Override
                                public void onFinish() {
                                    unlock_pin.setEnabled(true);
                                    okk.setEnabled(true);
                                    timerSet.setVisibility(View.GONE);
                                    count = 0;
                                }
                            }.start();
                            unlock_pin.setEnabled(false);
                            okk.setEnabled(false);
                        }

                    } else  if (
                            unlock_pin.getText().toString().equals(user_pass)){

                        GlobalVariables.check_open_app = !GlobalVariables.check_open_app;
                            finish();
                        }



                }
            });


        } else {
            materialLockView.setVisibility(View.VISIBLE);
            unlock_text.setText("Draw Pattern to Unlock");
            okk.setVisibility(View.GONE);
            unlock_pin.setVisibility(View.GONE);


        }


        materialLockView.setOnPatternListener(new MaterialLockView.OnPatternListener() {
            @Override
            public void onPatternDetected(List<MaterialLockView.Cell> pattern, String SimplePattern) {
                Log.e("SimplePattern", SimplePattern);

                if (!SimplePattern.equals(user_pass)) {
                    if (count < 3) {
                        count++;
                        materialLockView.setDisplayMode(MaterialLockView.DisplayMode.Wrong);
                        materialLockView.clearPattern();
                        ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(500);
                    } else {
                        materialLockView.clearPattern();
                        ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(800);

                        Toast.makeText(Unlock_Activity.this, "over attempt", Toast.LENGTH_SHORT).show();
                        timer = new CountDownTimer(10000, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                timerSet.setVisibility(View.VISIBLE);
                                timerSet.setText("Unlock in : " + millisUntilFinished / 1000 + " seconds");
                            }

                            @Override
                            public void onFinish() {
                                materialLockView.enableInput();
                                timerSet.setVisibility(View.GONE);
                                count = 0;
                            }
                        }.start();
                        materialLockView.disableInput();
                    }
                } else if(SimplePattern.equals(user_pass)) {
                    GlobalVariables.check_open_app = !GlobalVariables.check_open_app;

                    finish();
//                    Aplock_Accessibilty.check=false;


//                    materialLockView.setDisplayMode(MaterialLockView.DisplayMode.Correct);

                }
                super.onPatternDetected(pattern, SimplePattern);
            }
        });


//******************
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
//        Toast.makeText(this, "ondestroy", Toast.LENGTH_SHORT).show();
        Aplock_Accessibilty.check = true;


    }

    @Override
    protected void onPause() {
        super.onPause();
        Aplock_Accessibilty.check = true;
       this.finish();
//        Toast.makeText(this, "onpause", Toast.LENGTH_SHORT).show();


    }



    @Override
    protected void onStop() {
        super.onStop();
//        Toast.makeText(this, "onstop", Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(this, Unlock_Activity.class);
//        startActivity(intent);
//        finish();

        Aplock_Accessibilty.check = true;


    }
   /* @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (!hasFocus) {
            Intent i = new Intent(getBaseContext(), Unlock_Activity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            startActivity(i);
        }
    }*/
}
