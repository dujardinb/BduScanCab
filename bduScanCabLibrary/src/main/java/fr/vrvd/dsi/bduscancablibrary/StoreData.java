package fr.vrvd.dsi.bduscancablibrary;

import android.app.Activity;
import android.content.Context;

public  class StoreData {
    private static Context context;
    private static Activity  activity;

    public static void setContext(Context ct){
        context =ct;
    }
    public static Context getContext() {
        return context;
    }
}
