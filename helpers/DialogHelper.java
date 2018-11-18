package com.demo.ahmed.weather.helpers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

/**
 * Created by AhmedKamal on 17/11/2018.
 */

public class DialogHelper {
    public static void showMessageOKCancel(Activity context, String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
}
