package com.demo.ahmed.weather.helpers;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import java.util.ArrayList;

/**
 * Created by AhmedKamal on 17/11/2018.
 */

public class PermissionHelper {
    public static boolean hasPermission(Activity context, String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    public static boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }
    public static ArrayList findUnAskedPermissions(Activity context, ArrayList<String> wanted) {
        ArrayList result = new ArrayList();
        for (String perm : wanted) {
            if (!PermissionHelper.hasPermission(context, perm)) {
                result.add(perm);
            }
        }

        return result;
    }
}
