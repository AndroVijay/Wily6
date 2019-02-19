package com.wily.utils;

import android.view.View;
import android.view.animation.AlphaAnimation;

public class AnimationUtil {

    private AnimationUtil() {
    }
    
    /**
     * 
     * @param view
     * @param flag : 1 - do visible 2 - do invisible 3 - trans - visible 'else' visible - translucent
     */

    public static void alphaAnimation(View view, int flag,long time){
//    public static void alphaAnimation(View view, int flag) {
        AlphaAnimation aa;
        if (flag == 1) {
        	aa = new AlphaAnimation(0f, 1f);
        } else if (flag == 2) {
        	aa = new AlphaAnimation(1f, 0f);
        } else if (flag == 3) {
        	aa = new AlphaAnimation(0.6f, 1f);
        } else if (flag == 4) {
        	aa = new AlphaAnimation(0f, 0.6f);
        }
        else if (flag ==5)
            aa = new AlphaAnimation(.6f, 0.3f);
            else {
        	aa = new AlphaAnimation(1f, 0.6f);
        }
        aa.setDuration(1000);

        aa.setFillAfter(true);
        view.startAnimation(aa);
    }

}
