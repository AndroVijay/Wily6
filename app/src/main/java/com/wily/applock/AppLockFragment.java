package com.wily.applock;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wily.R;
import com.wily.SplashActivity;

import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.wily.SplashActivity.sqLiteDatabase;
import static com.wily.SplashActivity.userDbHelper;


/**
 * Created by BANSAL on 28-04-2017.
 */

public class AppLockFragment extends Fragment implements View.OnClickListener {

    public String pass = "";
    public String temp_1 = "";
    public String temp_2 = "";
    Button num_lock, pattern_lock;
    TextView pass_title;
    Cursor cursor;
    String user_pass, user_check;
    int pinClickCount = 0;
    View v;
    int accessibilityEnabled = 0;
    TextView textView;
    Button restet, confrime;
    Fragment fragment2;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    Button patter_text, pin_text, confirm_pass, reset, confirmPin, checkTo;
    EditText pin_pass, pin1, pin2, checkPin;
    String check_pass = "pattern";
    String detect_pattern = "";
    String numpasscheck, patternpasscheck;
    Editable num;
    // boolean check=true;
    String pin_chek_2;
    String pin_pass_check;
    String new1, new2;
    RelativeLayout newLay, checkLay, resetLay, pattern_layout;
    private MaterialLockView materialLockView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        // return super.onCreateView(inflater, container, savedInstanceState);


//            Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
//            startActivityForResult(intent, 0);



       /* Settings.Secure.putString(getActivity().getContentResolver(),
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES, "info.com.wily/Aplock_Accessibility");
        Settings.Secure.putString(getActivity().getContentResolver(),
                Settings.Secure.ACCESSIBILITY_ENABLED, "1");*/





        sqLiteDatabase = userDbHelper.getReadableDatabase();

        cursor = userDbHelper.getInformations(sqLiteDatabase);
        if (cursor.moveToFirst()) {
            do {
                user_pass = cursor.getString(0);
                user_check = cursor.getString(1);
                Log.e("userpass", "" + user_pass);
                Log.e("userpass-----", "" + user_check);
            } while (cursor.moveToNext());
        } else {

            user_pass = "";
            user_check = "";
        }
        View v = inflater.inflate(R.layout.patternlock_fragment, container, false);
        pass_title = (TextView) v.findViewById(R.id.textView2);
        materialLockView = (MaterialLockView) v.findViewById(R.id.pattern);
        patter_text = (Button) v.findViewById(R.id.pattern_text);
        pin_text = (Button) v.findViewById(R.id.pin_text);
        pin_pass = (EditText) v.findViewById(R.id.pin_pass);
        confirm_pass = (Button) v.findViewById(R.id.confirm_password);
        pin1 = (EditText) v.findViewById(R.id.pin1);
        pin2 = (EditText) v.findViewById(R.id.pin2);
        confirmPin = (Button) v.findViewById(R.id.confirmPin);
        checkTo = (Button) v.findViewById(R.id.checkTo);
        confirmPin.setOnClickListener(this);
        patter_text.setOnClickListener(this);
        pin_text.setOnClickListener(this);
        confirm_pass.setOnClickListener(this);
        reset = (Button) v.findViewById(R.id.resetButton);
        reset.setOnClickListener(this);


        newLay = (RelativeLayout) v.findViewById(R.id.newPass);
        checkLay = (RelativeLayout) v.findViewById(R.id.checkPassword);
        resetLay = (RelativeLayout) v.findViewById(R.id.resetLay);
        pattern_layout = (RelativeLayout) v.findViewById(R.id.pattern_layout);
        checkPin = (EditText) v.findViewById(R.id.checkPin);
        if (user_check.equals("")) {

            pass_title.setText(R.string.set_lock_string);

            initListner();

        } else {
            pass_title.setText(R.string.set_unlock_string);
            initListner1();
        }
//        if (user_pass.equals("pattern") || user_pass.equals("pin")) {
//            reset.setVisibility(View.VISIBLE);
//        } else if (user_pass.equals("")) {
//            reset.setVisibility(View.GONE);
//        }

        if (user_check.equals("pin")) {
            patter_text.setVisibility(View.GONE);
            reset.setVisibility(View.VISIBLE);
            pattern_layout.setVisibility(View.GONE);
            newLay.setVisibility(View.VISIBLE);
            pass_title.setText("Enter Pin to Unlock");

        } else if (user_check.equals("pattern")) {
            pin_text.setVisibility(View.GONE);
            reset.setVisibility(View.VISIBLE);
            newLay.setVisibility(View.GONE);
            pattern_layout.setVisibility(View.VISIBLE);
            pass_title.setText("Enter Pattern to Unlock");

        } else if (user_check.equals("")) {
            reset.setVisibility(View.GONE);
            patter_text.setVisibility(View.VISIBLE);
            pin_text.setVisibility(View.VISIBLE);
        }


        return v;

    }


    private boolean isAccessibilityEnabled(String id) {
        AccessibilityManager am = (AccessibilityManager) getApplicationContext().getSystemService(Context.ACCESSIBILITY_SERVICE);
        List<AccessibilityServiceInfo> enabledServices = am.getEnabledAccessibilityServiceList(AccessibilityEvent.TYPES_ALL_MASK);
        for (AccessibilityServiceInfo service : enabledServices) {
            if (id.equals(service.getId())) {
                return true;
            }
        }
        return false;
    }

    private void initListner1() {
        pattern_layout.setVisibility(View.VISIBLE);

        materialLockView.setOnPatternListener(new MaterialLockView.OnPatternListener() {
            @Override
            public void onPatternDetected(List<MaterialLockView.Cell> pattern, String SimplePattern) {

                Log.d("userpass", "" + user_pass);
                if (!SimplePattern.equals(user_pass)) {

                    materialLockView.setDisplayMode(MaterialLockView.DisplayMode.Wrong);
                    materialLockView.clearPattern();

                    Toast.makeText(getApplicationContext(), R.string.wrong_pattern, Toast.LENGTH_SHORT).show();


                } else if (SimplePattern.equals(user_pass)) {


                    fragment2 = new AppListFragment();
                    fragmentManager = getFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, fragment2);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
                super.onPatternDetected(pattern, SimplePattern);
            }
        });

    }

    private void initListner() {
        pattern_layout.setVisibility(View.VISIBLE);
        materialLockView.setOnPatternListener(new MaterialLockView.OnPatternListener() {

            @Override
            public void onPatternDetected(List<MaterialLockView.Cell> pattern, String SimplePattern) {

            if(SimplePattern.length()>=2) {
                Log.e("SimplePattern", SimplePattern);
                if (pass.equals("") && temp_1.equals("") && temp_2.equals("")) {
                    temp_1 = SimplePattern;
                    materialLockView.clearPattern();
                    pass_title.setText(R.string.pass_conform_string);
                } else if (!temp_1.equals("") && temp_2.equals("") && pass.equals("")) {
                    if (temp_1.equals(SimplePattern)) {
                        temp_2 = SimplePattern;
                        pass = SimplePattern;
                        materialLockView.clearPattern();
                        sqLiteDatabase = userDbHelper.getWritableDatabase();
                        userDbHelper.insert_data(pass, check_pass, sqLiteDatabase);
                        sqLiteDatabase = userDbHelper.getReadableDatabase();
                        cursor = userDbHelper.getInformations(sqLiteDatabase);
                        if (cursor.moveToFirst()) {
                            do {
                                user_pass = cursor.getString(0);
                                user_check = cursor.getString(1);
                                Log.e("userpass", "" + user_pass);
                                Log.e("userpass-----", "" + user_check);
                            } while (cursor.moveToNext());
                        }
                        reset.setVisibility(View.VISIBLE);
                        pin_text.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Pattern set successfully ", Toast.LENGTH_SHORT).show();
                        if (SimplePattern.equals(user_pass)) {


                            fragment2 = new AppListFragment();
                            fragmentManager = getFragmentManager();
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.content_frame, fragment2);

                            fragmentTransaction.addToBackStack(null);


                            fragmentTransaction.commit();
//                    getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.content_frame)).commit();


                        }
                       // pass_title.setText("DRAW PATTERN TO UNLOCK");
                    } else {
                        materialLockView.setDisplayMode(MaterialLockView.DisplayMode.Wrong);
                        materialLockView.clearPattern();
                        temp_1 = "";
                        temp_2 = "";
                        pass_title.setText(R.string.set_lock_string);
                    }
                } else if (!SimplePattern.equals(user_pass)) {

                    materialLockView.setDisplayMode(MaterialLockView.DisplayMode.Wrong);
                    materialLockView.clearPattern();

                    Toast.makeText(getApplicationContext(), R.string.wrong_pattern, Toast.LENGTH_SHORT).show();
                } /*else if (SimplePattern.equals(user_pass)) {


                    fragment2 = new AppListFragment();
                    fragmentManager = getFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, fragment2);

                    fragmentTransaction.addToBackStack(null);


                    fragmentTransaction.commit();
//                    getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.content_frame)).commit();


                }*/
            }else {

                Toast.makeText(getApplicationContext(), "connect max 2 dot", Toast.LENGTH_SHORT).show();
                materialLockView.clearPattern();
            }

                detect_pattern = SimplePattern;
                super.onPatternDetected(pattern, SimplePattern);
            }

        });


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id) {

            case R.id.pattern_text:

                check_pass = "pattern";
                pin_text.setTextColor(Color.GRAY);
                patter_text.setTextColor(Color.WHITE);
                pattern_layout.setVisibility(View.VISIBLE);
                newLay.setVisibility(View.GONE);
                SplashActivity.closeKeyboard(getApplicationContext(), pin1.getWindowToken());
                SplashActivity.closeKeyboard(getApplicationContext(), pin2.getWindowToken());
                pinClickCount = 0;
                if (user_check.equals("pattern"))
                    pass_title.setText("Draw Pattern to Unlock");

                else
                    pass_title.setText(R.string.set_lock_string);


                break;
            case R.id.pin_text:
                check_pass = "pin";

                pinClickCount = 0;
                pin_text.setTextColor(Color.WHITE);
                patter_text.setTextColor(Color.GRAY);
                pattern_layout.setVisibility(View.GONE);
                if (user_check.equals("pin"))
                    pass_title.setText("Enter Password to Unlock");
                else
                    pass_title.setText("Set Pattern or Pin");

                pin_pass.setFocusableInTouchMode(true);
                pin_pass.setFocusable(true);
                pin_pass.requestFocus();
                reset.setVisibility(View.GONE);

//                InputMethodManager imm1 = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm1.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                newLay.setVisibility(View.VISIBLE);
                break;

            case R.id.confirm_password:
                method();
                if (user_check.equals("")) {
                    if (pinClickCount == 0 && !pin_pass.getText().toString().equals("")) {
                        pin_pass_check = pin_pass.getText().toString();
                        // check=!check;
                        pass_title.setText("Confirm Pin");
                        patter_text.setVisibility(View.GONE);
                        pinClickCount = 1;
                        pin_pass.setText("");
//                        confirm_pass.setText("OK");
                    } else if (pinClickCount == 1) {
                        pass_title.setText("Enter Pin to Unlock");
                        if (pin_pass_check.equals(pin_pass.getText().toString()) && !pin_pass.getText().toString().equals("")) {
                            pass = pin_pass_check;
                            sqLiteDatabase = userDbHelper.getWritableDatabase();
                            userDbHelper.insert_data(pass, check_pass, sqLiteDatabase);

                            Toast.makeText(getApplicationContext(), "set password succesfully ", Toast.LENGTH_SHORT).show();
                            pin_pass.setText("");
                            pinClickCount = 2;

                        } else {
                            pinClickCount = 0;
                            pass_title.setText("Enter New Pin");
                            pin_pass.setText("");
                            Toast.makeText(getApplicationContext(), R.string.password_mismatch, Toast.LENGTH_SHORT).show();
                            //    check=!check;
                        }

                    } else if (pin_pass.getText().toString().equals(""))
                        Toast.makeText(getActivity(), "Pin cannot be empty", Toast.LENGTH_SHORT).show();
                    else {
                        pass_title.setText("Enter Pin to Unlock");
//                        confirm_pass.setText("OK");
                        sqLiteDatabase = userDbHelper.getReadableDatabase();
                        cursor = userDbHelper.getInformations(sqLiteDatabase);
                        pattern_layout.setVisibility(View.GONE);
                        if (cursor.moveToFirst()) {
                            do {
                                user_pass = cursor.getString(0);
                                user_check = cursor.getString(1);
                                Log.e("userpass", "" + user_pass);
                                Log.e("userpass-----", "" + user_check);
                            } while (cursor.moveToNext());
                        }
                        if (!user_pass.equals(pin_pass.getText().toString())) {
                            checkLay.setVisibility(View.GONE);

//                        materialLockView.setDisplayMode(MaterialLockView.DisplayMode.Wrong);
//                            pin_text.setText("");
                            pattern_layout.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), R.string.wrong_password, Toast.LENGTH_SHORT).show();
                        } else if (pin_pass.getText().toString().equals(user_pass) && !pin_pass.getText().toString().equals("")) {
                            SplashActivity.closeKeyboard(getApplicationContext(), pin_pass.getWindowToken());


                            checkLay.setVisibility(View.GONE);

                            fragment2 = new AppListFragment();
                            fragmentManager = getFragmentManager();
                            fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.content_frame, fragment2);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();

                        }

                    }


                } else {
//                    pass_title.setText(R.string.set_unlock_string);
//                    initListner1();

//                    confirm_pass.setText("OK");
                    sqLiteDatabase = userDbHelper.getReadableDatabase();
                    cursor = userDbHelper.getInformations(sqLiteDatabase);
                    if (cursor.moveToFirst()) {
                        do {
                            user_pass = cursor.getString(0);
                            user_check = cursor.getString(1);
                            Log.e("userpass", "" + user_pass);
                            Log.e("userpass-----", "" + user_check);
                        } while (cursor.moveToNext());
                    }
                    if (!pin_pass.getText().toString().equals(user_pass)) {
                        pattern_layout.setVisibility(View.GONE);
                        pin_pass.setText("");
                        Toast.makeText(getApplicationContext(), R.string.wrong_password, Toast.LENGTH_SHORT).show();

//                        materialLockView.setDisplayMode(MaterialLockView.DisplayMode.Wrong);
                        pin_text.setText("PIN");
                    } else if (pin_pass.getText().toString().equals(user_pass) && !pin_pass.getText().toString().equals("")) {
                        SplashActivity.closeKeyboard(getApplicationContext(), pin_pass.getWindowToken());

                        fragment2 = new AppListFragment();
                        fragmentManager = getFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.content_frame, fragment2);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();

                    } else if (pin_pass.getText().toString().equals("")) {
                        Toast.makeText(getApplicationContext(), "Field can not be blank", Toast.LENGTH_SHORT).show();
                    }

                }
                break;
            case R.id.resetButton:
                checkPin.setText("");
                newLay.setVisibility(View.GONE);
                sqLiteDatabase = userDbHelper.getReadableDatabase();
                sqLiteDatabase = userDbHelper.getWritableDatabase();
                cursor = userDbHelper.getInformations(sqLiteDatabase);
                if (cursor.moveToFirst()) {
                    do {
                        user_pass = cursor.getString(0);
                        user_check = cursor.getString(1);
                        if (user_check.equals("pattern")) {
                            pass_title.setText("Enter Saved Pattern");
                            patter_text.setVisibility(View.GONE);
                            pin_text.setVisibility(View.GONE);
                            checkLay.setVisibility(View.GONE);
                            pattern_layout.setVisibility(View.VISIBLE);
                            resetLay.setVisibility(View.GONE);
                            materialLockView.setOnPatternListener(new MaterialLockView.OnPatternListener() {
                                public void onPatternDetected(List<MaterialLockView.Cell> pattern, String SimplePattern) {
                                    super.onPatternDetected(pattern, SimplePattern);
                                    if (user_pass.equals(SimplePattern)) {
                                        temp_1 = "";
                                        temp_2 = "";
                                        pass = "";
                                        materialLockView.clearPattern();
                                        pass_title.setText("Enter New Pattern or Pin");
                                        initListner();

                                        user_check = "";
                                        patter_text.setVisibility(View.VISIBLE);
                                        pin_text.setVisibility(View.VISIBLE);
                                        pin_text.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                resetLay.setVisibility(View.VISIBLE);
                                                pass_title.setText("Enter New Pin");
                                                patter_text.setTextColor(Color.GRAY);

                                                pin_text.setTextColor(Color.WHITE);

                                                pattern_layout.setVisibility(View.GONE);
                                            }
                                        });
                                        patter_text.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                resetLay.setVisibility(View.GONE);
                                                pattern_layout.setVisibility(View.VISIBLE);
                                                pass_title.setText("Enter New Pattern");
                                                pin_text.setTextColor(Color.GRAY);
                                                SplashActivity.closeKeyboard(getApplicationContext(), pin1.getWindowToken());
                                                SplashActivity.closeKeyboard(getApplicationContext(), pin2.getWindowToken());

                                                patter_text.setTextColor(Color.WHITE);
                                            }
                                        });
                                    } else {
                                        materialLockView.clearPattern();
                                        Toast.makeText(getApplicationContext(), R.string.wrong_pattern, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                        if (user_check.equals("pin")) {
                            pass_title.setText("Enter Saved Pin");
                            patter_text.setVisibility(View.GONE);
                            pin_text.setVisibility(View.GONE);

                            checkLay.setVisibility(View.VISIBLE);
                            reset.setVisibility(View.GONE);
                            pattern_layout.setVisibility(View.GONE);
                            checkTo.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    reset.setVisibility(View.GONE);
                                    if (checkPin.getText().toString().equals(""))
                                        Toast.makeText(getApplicationContext(), "Field can not be null", Toast.LENGTH_SHORT).show();
                                    if (user_pass.equals(checkPin.getText().toString()) && !checkPin.getText().toString().equals("")) {
                                        pin_text.setVisibility(View.VISIBLE);
                                        patter_text.setVisibility(View.VISIBLE);
                                        checkLay.setVisibility(View.GONE);
                                        pass_title.setText("Enter New Pin");
                                        resetLay.setVisibility(View.VISIBLE);
                                        SplashActivity.closeKeyboard(getApplicationContext(), checkPin.getWindowToken());
                                        pin_text.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                resetLay.setVisibility(View.VISIBLE);
                                                pattern_layout.setVisibility(View.GONE);
                                                pass_title.setText("Set Pin");
                                                pin1.setText("");
                                                patter_text.setTextColor(Color.GRAY);
                                                pin_text.setTextColor(Color.WHITE);
                                                pin2.setText("");
                                            }
                                        });

                                        patter_text.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                resetLay.setVisibility(View.GONE);
                                                pattern_layout.setVisibility(View.VISIBLE);
                                                pass_title.setText("Set Pattern");
                                                initListner();
                                                pin_text.setTextColor(Color.GRAY);
                                                patter_text.setTextColor(Color.WHITE);
                                                SplashActivity.closeKeyboard(getApplicationContext(), pin1.getWindowToken());
                                                SplashActivity.closeKeyboard(getApplicationContext(), pin2.getWindowToken());


                                            }
                                        });

                                    } else {
                                        checkPin.setText("");
                                        Toast.makeText(getApplicationContext(), R.string.password_mismatch, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    } while (cursor.moveToNext());
                } else {

                    user_pass = "";
                    user_check = "";
                }
                break;
            case R.id.confirmPin:
                if (pin1.getText().toString().equals("") && pin2.getText().toString().equals(""))
                    Toast.makeText(getApplicationContext(), "Pin can not be blank", Toast.LENGTH_SHORT).show();
                else if (!pin1.getText().toString().equals("") && !pin2.getText().toString().equals("") && pin2.getText().toString().equals(pin1.getText().toString())) {
                    checkPin.setText("");
                    check_pass = "pin";
                    pass = pin2.getText().toString();
                    sqLiteDatabase = userDbHelper.getWritableDatabase();
                    userDbHelper.resetPassword(pass, check_pass);
                    SplashActivity.closeKeyboard(getApplicationContext(), pin2.getWindowToken());
                    patter_text.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Password Changed", Toast.LENGTH_SHORT).show();
                    fragment2 = new AppListFragment();
                    fragmentManager = getFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, fragment2);
                    fragmentTransaction.commit();

                } else {
                    pin1.setText("");
                    pin2.setText("");
                    pin_pass.setText("");
                    Toast.makeText(getApplicationContext(), R.string.password_mismatch, Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

    private void method() {

        sqLiteDatabase = userDbHelper.getReadableDatabase();

        cursor = userDbHelper.getInformations(sqLiteDatabase);
        if (cursor.moveToFirst()) {
            do {
                user_pass = cursor.getString(0);
                user_check = cursor.getString(1);
                Log.e("userpass", "" + user_pass);
                Log.e("userpass-----", "" + user_check);
            } while (cursor.moveToNext());
        } else {

            user_pass = "";
            user_check = "";
        }

    }
}
