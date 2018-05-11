package com.tunaemre.audiovisualizer;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_RECORD_AUDIO = 9998;
    private static final int PERMISSION_REQUEST_MODIFY_AUDIO_SETTINGS = 9999;

    public interface AlertCallback {
        void onPositive();
        void onNegative();
    }

    public static void ShowAlert(final Context context, String text, final AlertCallback callback)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context).setCancelable(false).setMessage(text)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callback.onPositive();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callback.onNegative();
                    }
                });

        builder.create().show();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorLightStatusBar));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        setContentView(R.layout.activity_main);

        prepareActivity();
    }

    private void prepareActivity() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSION_REQUEST_RECORD_AUDIO);
            return;
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.MODIFY_AUDIO_SETTINGS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.MODIFY_AUDIO_SETTINGS}, PERMISSION_REQUEST_MODIFY_AUDIO_SETTINGS);
            return;
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.content_main, new MainFragment()).commit();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_RECORD_AUDIO: {
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    onPermissionNotGranted();
                    return;
                }

                prepareActivity();
            }
            case PERMISSION_REQUEST_MODIFY_AUDIO_SETTINGS: {
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    onPermissionNotGranted();
                    return;
                }

                prepareActivity();
            }
        }
    }

    public void onPermissionNotGranted() {
        ShowAlert(MainActivity.this, "Please grant permissions to continue.", new AlertCallback() {
            @Override
            public void onPositive() {
                prepareActivity();
            }

            @Override
            public void onNegative() {
                finish();
            }
        });
    }
}
