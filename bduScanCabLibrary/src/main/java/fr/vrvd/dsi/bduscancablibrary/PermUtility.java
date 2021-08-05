package fr.vrvd.dsi.bduscancablibrary;
import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;

/**
 * Created by brunodujardin on 08/01/2017.
 */

public class PermUtility {

    public static final int MY_PERMISSIONS_REQUEST = 123;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, permission)) {
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[]{
                                        Manifest.permission.INTERNET,

                                        Manifest.permission.VIBRATE,
                                        Manifest.permission.CAMERA},
                                MY_PERMISSIONS_REQUEST);
                    } else {
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[]{
                                        Manifest.permission.INTERNET,

                                        Manifest.permission.VIBRATE,
                                        Manifest.permission.CAMERA},
                                MY_PERMISSIONS_REQUEST);
                    }
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean permissionCheck(Context ct){
        boolean res = checkPermissions(ct,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CAMERA);

        return res;
    }


    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PermUtility.MY_PERMISSIONS_REQUEST:
                boolean continuer = true;
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) continuer = false;
                }
                if (continuer) {
                    //if (userChoosenTask.equals("STORAGE"))
                    //    performTaskOperation();//this method what you need to perform

                    LibApp.toastShow("Permissions validées");
                }
                break;
        }
    }
}

