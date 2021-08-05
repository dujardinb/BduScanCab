package fr.vrvd.dsi.bduscancablibrary;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ASUS on 07/03/2015.
 */
public class Prefs extends AppController {

    public static final String PARAM_PREF = "PARAM_PREF";
    public static final String FORMAT_DATA = "FORMAT_DATA";
    public static final String PARAM_VIBRATE = "PARAM_VIBRATE";

    public static void setFormatCab( int value) {
        SharedPreferences pref = getAppContext().getSharedPreferences(PARAM_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        if (pref.contains(FORMAT_DATA)) editor.remove(FORMAT_DATA);
        editor.putInt(FORMAT_DATA, value);
        editor.commit();
    }

    public static int getFormatCab(){
        SharedPreferences pref = getAppContext().getSharedPreferences(PARAM_PREF, Context.MODE_PRIVATE);
        return pref.getInt(FORMAT_DATA,256);
    }


    public static void setVibrate( int value) {
        SharedPreferences pref = getAppContext().getSharedPreferences(PARAM_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        if (pref.contains(PARAM_VIBRATE)) editor.remove(PARAM_VIBRATE);
        editor.putInt(PARAM_VIBRATE, value);
        editor.commit();
    }

    public static int getVibrate(){
        SharedPreferences pref = getAppContext().getSharedPreferences(PARAM_PREF, Context.MODE_PRIVATE);
        return pref.getInt(PARAM_VIBRATE,0);
    }

}
