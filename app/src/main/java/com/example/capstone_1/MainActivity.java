package com.example.capstone_1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class MainActivity  extends AppCompatActivity {
    final static int PERMISSION_REQUEST_CODE = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        permissionCheck();
        Intent intent = new Intent(getApplicationContext(), SensorService.class);
        startService(intent);
    }
    private void permissionCheck(){
        if(Build.VERSION.SDK_INT >= 23){
            int permissionCheck1 = ContextCompat. checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
            int permissionCheck2 = ContextCompat. checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
            //int permissionCheck3 = ContextCompat. checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION);
            ArrayList<String> arrayPermission = new ArrayList<String>();
            if(permissionCheck1 != PackageManager.PERMISSION_GRANTED){
                arrayPermission.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if(permissionCheck2 != PackageManager.PERMISSION_GRANTED){
                arrayPermission.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
            //if(permissionCheck3 != PackageManager.PERMISSION_GRANTED){
            //    arrayPermission.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION);
            //}
            if(arrayPermission.size() > 0 ){
                String strArray[] = new String[arrayPermission.size()];
                strArray = arrayPermission.toArray(strArray);
                ActivityCompat.requestPermissions(this, strArray, PERMISSION_REQUEST_CODE);
            } else {

            }
        }
    }
}
