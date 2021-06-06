package com.smartschool.ui.dashboard;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Spinner;

import com.smartschool.BaseActivity;
import com.smartschool.R;
import com.smartschool.bean.CourseDataBean;
import com.smartschool.bean.CourseInfoDataBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CourseActivity extends BaseActivity {
    GridLayout grid;
    Spinner spinner1;
    Spinner spinner2;
    String xnm;
    String xqm;
    List<CourseInfoDataBean[][]> zcList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        View view=(View) findViewById(R.id.tb);
        Toolbar toolbar=(Toolbar) view.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setFitsSystemWindows(true);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("课程表查询");

        zcList=new ArrayList<>();
        for(int i=0;i<20;i++){
            CourseInfoDataBean[][] course=new CourseInfoDataBean[10][7];
            zcList.add(course);
        }

        grid=(GridLayout) findViewById(R.id.grid);
        Button selectBtn=(Button) findViewById(R.id.btn_select);
        spinner1=(Spinner) findViewById(R.id.sp1);
        spinner2=(Spinner) findViewById(R.id.sp2);
        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xnm=spinner1.getSelectedItem().toString();
                String text=spinner2.getSelectedItem().toString();
                if(text.equals("1")){
                    xqm="3";
                }else {
                    xqm="12";
                }
                initCourse(xnm,xqm);
            }
        });
    }

    private void initCourse(final String xnm,final String xqm){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Connection connection= Jsoup.connect("http://jwxt.cumt.edu.cn/jwglxt/kbcx/xskbcx_cxXsKb.html?gnmkdm=N253508&su="+userId);
                connection.header("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.85 Safari/537.36 Edg/90.0.818.49");
                connection.header("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
                connection.cookies(Cookies);
                connection.data("xnm",xnm)
                        .data("xqm",xqm)
                        .data("kzlx","ck");
                Connection.Response response=null;
                try {
                    response=connection.ignoreContentType(true).method(Connection.Method.POST).followRedirects(true).execute();
                }catch (IOException e){
                    e.printStackTrace();
                }
                try {
                    JSONObject jsonObject = new JSONObject(response.body());
                    JSONArray jsonArray1 = jsonObject.getJSONArray("kbList");
                    for(int i=0;i<jsonArray1.length();i++){
                        jsonObject=jsonArray1.getJSONObject(i);
                        CourseInfoDataBean courseInfoDataBean=new CourseInfoDataBean();
                        courseInfoDataBean.setCourse(jsonObject.getString("kcmc"));
                        courseInfoDataBean.setJc(jsonObject.getString("jc"));
                        courseInfoDataBean.setZc(jsonObject.getString("zcd"));
                        final String place=jsonObject.getString("xqmc")+" "+jsonObject.getString("cdmc");
                        courseInfoDataBean.setPlace(place);
                        courseInfoDataBean.setTeacher(jsonObject.getString("xm"));
                        courseInfoDataBean.setTestType(jsonObject.getString("khfsmc"));
                        courseInfoDataBean.setType(jsonObject.getString("kcxszc"));
                        courseInfoDataBean.setXf(jsonObject.getString("xf"));
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void actionStart(Context context){
        Intent intent=new Intent(context,CourseActivity.class);
        context.startActivity(intent);
    }
}