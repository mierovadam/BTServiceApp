package com.example.btserviceapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, MyService.class));

        String action = intent.getAction();

        if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
            //discovery starts
            Log.d("ptt", "Started discovering");
        } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
            //discovery finishes, dismiss progress dialog
            Log.d("ptt", "discovery finishes");
        } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            //bluetooth device found
            BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            if(device.getName() != null) {
                Log.d("ptt", "Found device " + device.getName());
                if(device.getName().equals(MyService.nameInput) && !MyService.instance.isTorchOn) {
                    MyService.instance.setTorchMode(true);
                    Toast.makeText(context, "Found device " + device.getName(), Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

}
