package info.earntalktime.at.utils;

import android.app.PendingIntent;
import android.content.Intent;

/**
 * Created by PANDEY on 7/31/2017.
 */

public class Getclassname {

    static Intent intent ;
    public static void getIntent(Intent name){
        intent=name;



    }


    public static Intent setIntent(){
        return intent;
    }

}
