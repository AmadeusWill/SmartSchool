package com.smartschool;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.HashMap;
import java.util.Map;

public class BaseActivity extends AppCompatActivity {
    public static Map<String,String> Cookies;
    static String username="新用户";
    static String userClass="";
    public static String userId="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
//        toolbar.setFitsSystemWindows(true);
//        toolbar.setNavigationIcon(R.drawable.ic_back);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//        setSupportActionBar(toolbar);
//        ActionBar actionBar=getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);

        fitsWindow();

    }
    private void fitsWindow(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){

            WindowManager.LayoutParams barLayoutParams=getWindow().getAttributes();

            barLayoutParams.flags=(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS|barLayoutParams.flags);
        }
    }
}
