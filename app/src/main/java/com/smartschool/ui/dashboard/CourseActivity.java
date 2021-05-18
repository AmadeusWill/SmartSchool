package com.smartschool.ui.dashboard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.smartschool.R;

public class CourseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
    }

    public static void actionStart(Context context){
        Intent intent=new Intent(context,CourseActivity.class);
        context.startActivity(intent);
    }
}