package com.wily.applock;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



import java.util.List;

import com.wily.R;

/**
 * Created by PANDEY on 28-04-2017.
 */

public class PatternlockFragment extends Fragment {
    private String CorrectPattern = "123";
    private MaterialLockView materialLockView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.patternlock_fragment,container,false);

        materialLockView = (MaterialLockView)view.findViewById(R.id.pattern);

        materialLockView.setOnPatternListener(new MaterialLockView.OnPatternListener() {
            @Override
            public void onPatternDetected(List<MaterialLockView.Cell> pattern, String SimplePattern) {
                Log.e("SimplePattern", SimplePattern);
                if (!SimplePattern.equals(CorrectPattern)) {

                    materialLockView.setDisplayMode(MaterialLockView.DisplayMode.Wrong);
                     materialLockView.clearPattern();

                } else {

                    materialLockView.setDisplayMode(MaterialLockView.DisplayMode.Correct);

                }
                super.onPatternDetected(pattern, SimplePattern);
            }
        });

       /* ((CheckBox) view.findViewById(R.id.stealthmode)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.e("checked", isChecked+"");
                materialLockView.setInStealthMode(isChecked);
            }
        });

        ((EditText) view.findViewById(R.id.correct_pattern_edittext)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                CorrectPattern = "" + s;
            }

            @Override
            public void afterTextChanged(Editable s) {




            }
        });*/

        return  view;
    }




    }





