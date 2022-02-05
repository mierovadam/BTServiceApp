package com.example.btserviceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.security.Policy;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    static MainActivity instance;
    PermissionManager permissionManager;

    private TextView txtView_Instruction;
    private TextInputEditText txtInput_BtName;
    private MaterialButton btnStart;
    private MaterialButton btnStop;

    public String chosenDeviceName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;

        permissionManager = new PermissionManager(this, MainActivity.this);
        permissionManager.getPermissions();
        initViews();
    }

    @Override
    protected void onDestroy() {
        //stopService(mServiceIntent);
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, MyReceiver.class);
        this.sendBroadcast(broadcastIntent);
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void initViews() {
        txtView_Instruction = findViewById(R.id.lbl_instruction);
        txtInput_BtName = findViewById(R.id.editTxt_name);
        btnStart = findViewById(R.id.btn_start);
        btnStop = findViewById(R.id.btn_stop);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chosenDeviceName = txtInput_BtName.getText().toString();
                startService( new Intent(instance, MyService.class));

            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyService.instance.setTorchMode(false);
                stopService(new Intent(instance, MyService.class));
            }
        });
    }



}