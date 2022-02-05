package com.example.btserviceapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.Calendar;


public class MyService extends Service {
    static MyService instance;

    BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
    MyReceiver myReceiver;
    private Thread thread;
    private Boolean isRunning = false;
    public Boolean isTorchOn = false;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        instance = this;

        IntentFilter filter = new IntentFilter();

        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        myReceiver = new MyReceiver();
        isRunning = true;

        thread = new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        while (isRunning) {

                                registerReceiver(myReceiver, filter);
                                adapter.startDiscovery();

                            try {
                                Thread.sleep(10000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
        );
        thread.start();
//        return super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent){
        Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
        restartServiceIntent.setPackage(getPackageName());

        PendingIntent restartServicePendingIntent = PendingIntent.getService(getApplicationContext(), 1, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.set(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 1000,
                restartServicePendingIntent);

        super.onTaskRemoved(rootIntent);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Log.d("ptt", "onDestroy ");
        stopSelf();
        thread.interrupt();
        isRunning = false;
        unregisterReceiver(myReceiver);
        sendBroadcast(new Intent());
    }

    public boolean checkFlashlightAvailability() {
        boolean flashlight = this.getBaseContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        Log.d("ptt", "Started discovering" + flashlight);

        return flashlight;
    }

    public void setTorchMode(Boolean bool) {
        if(checkFlashlightAvailability() && isEvening() && !isTorchOn) {
            CameraManager camManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
            try {
                String cameraId = camManager.getCameraIdList()[0];
                camManager.setTorchMode(cameraId, bool);
                isTorchOn = true;
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isEvening() {
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if(timeOfDay >= 17){
            return true;
        }else {
            return false;
        }
    }
}


