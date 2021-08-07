package fr.vrvd.dsi.bduscancablibrary;

import android.content.Context;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.Toast;

/**
 * Created by B Dujardin on 07/03/2015.
 */
public class LibApp {
    public static void toastShow(String mess) {
        Toast.makeText(AppController.getAppContext(),
                mess, Toast.LENGTH_LONG).show();
    }

    public static void vibrate(Context ct) {
        Vibrator v = (Vibrator) ct.getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(500);
        }
    }


    public static void beep(int nb) {
        ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_SYSTEM, 100);
        if (nb == 1) {
            toneGen1.startTone(ToneGenerator.TONE_CDMA_EMERGENCY_RINGBACK, 150);
        }
        if (nb == 2) {
            toneGen1.startTone(ToneGenerator.TONE_CDMA_HIGH_S_X4, 250);
        }

    }

}
