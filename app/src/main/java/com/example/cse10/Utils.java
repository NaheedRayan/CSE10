package com.example.cse10;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;

import androidx.core.content.ContextCompat;

public class Utils {

    //this class is for changing the color of status bar from any activity
    // The public static function which can be called from other classes
    public static void darkenStatusBar(Activity activity, int color) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            activity.getWindow().setStatusBarColor(
                    darkenColor(
                            ContextCompat.getColor(activity, color)));
        }

    }


    // Code to darken the color supplied (mostly color of toolbar)
    private static int darkenColor(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 0.8f;
        return Color.HSVToColor(hsv);
    }


}
