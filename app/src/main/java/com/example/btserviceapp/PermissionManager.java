package com.example.btserviceapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;


import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionManager {
    static final Integer ACCESS_COARSE_LOCATION = 0x1;
    static final Integer BLUETOOTH = 0x2;
    static final Integer ACCESS_FINE_LOCATION = 0x3;
    static final Integer BLUETOOTH_ADMIN = 0x4;
    static final Integer BLUETOOTH_CONNECT = 0x5;
    static final Integer BLUETOOTH_SCAN = 0x6;

    private Context context;
    private Activity activity;

    public PermissionManager(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }


    public void getPermissions() {
        askForPermission(Manifest.permission.ACCESS_COARSE_LOCATION, ACCESS_COARSE_LOCATION);
        askForPermission((Manifest.permission.BLUETOOTH), BLUETOOTH);
        askForPermission((Manifest.permission.BLUETOOTH_ADMIN), BLUETOOTH_ADMIN);
        askForPermission((Manifest.permission.ACCESS_FINE_LOCATION), ACCESS_FINE_LOCATION);
        askForPermission((Manifest.permission.BLUETOOTH_CONNECT), BLUETOOTH_CONNECT);
        askForPermission((Manifest.permission.BLUETOOTH_SCAN), BLUETOOTH_SCAN);
    }

    public void askForPermission(String permission, Integer requestCode) {
        //if permission isnt granted already then ask.
        if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
            }
        }
    }
}
