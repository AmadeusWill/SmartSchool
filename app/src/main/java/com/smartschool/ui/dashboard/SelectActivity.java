package com.smartschool.ui.dashboard;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import com.smartschool.BaseActivity;
import com.smartschool.R;
import com.smartschool.adapter.CourseAdapter;
import com.smartschool.adapter.GradeAdapter;
import com.smartschool.adapter.TestAdapter;
import com.smartschool.bean.CourseDataBean;
import com.smartschool.bean.GradeDataBean;
import com.smartschool.bean.TestDataBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SelectActivity extends BaseActivity {
    List<TestDataBean> testList;
    List<GradeDataBean> gradeList;
    List<CourseDataBean> courseList;
    String xnm;
    String xqm;
    Spinner spinner1;
    Spinner spinner2;
    RecyclerView selectRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
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

        int type=getIntent().getIntExtra("type",0);
        selectRecycler=(RecyclerView) findViewById(R.id.select_recycler);
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
                switch (type){
                    case 0:
                        actionBar.setTitle("考试信息查询");
                        testList=new ArrayList<>();
                        initTestInfo(xnm,xqm);
                        break;
                    case 1:
                        actionBar.setTitle("课程成绩查询");
                        gradeList=new ArrayList<>();
                        initGradeInfo(xnm,xqm);
                        break;
                    case 2:
                        actionBar.setTitle("课程表查询");
                        courseList=new ArrayList<>();
                        initCourseData(xnm,xqm);
                        break;
                    default:break;
                }
            }
        });
    }

    private void initTestInfo(final String xnm,final String xqm){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Connection connection= Jsoup.connect("http://jwxt.cumt.edu.cn/jwglxt/kwgl/kscx_cxXsksxxIndex.html?doType=query&gnmkdm=N358105&su="+userId);
                connection.header("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.85 Safari/537.36 Edg/90.0.818.49");
                connection.header("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
                connection.data("xnm",xnm)
                        .data("xqm",xqm)
                        .data("_search","false")
                        .data("nd",Integer.toString(new Date().getDate()))
                        .data("queryModel.showCount",Integer.toString(30))
                        .data("queryModel.currentPage",Integer.toString(1))
                        .data("queryModel.sortOrder","asc")
                        .data("time",Integer.toString(1));
                Connection.Response response=null;
                try{
                    response=connection.cookies(Cookies).ignoreContentType(true).method(Connection.Method.POST).followRedirects(true).execute();
                }catch (IOException e){
                    e.printStackTrace();
                }
                try {
                    JSONObject jsonObject=new JSONObject(response.body());
                    JSONArray jsonArray=jsonObject.getJSONArray("items");
                    System.out.println("考试信息：");
                    for(int i=0;i<jsonArray.length();i++){
                        jsonObject=jsonArray.getJSONObject(i);
                        TestDataBean testDataBean=new TestDataBean();
                        testDataBean.setCourseName(jsonObject.getString("kcmc"));
                        final String area=jsonObject.getString("cdxqmc");
                        final String place=jsonObject.getString("cdmc");
                        testDataBean.setPlace(area+" "+place);
                        testDataBean.setXf(jsonObject.getString("xf"));
                        testDataBean.setTime(jsonObject.getString("kssj"));
                        testList.add(testDataBean);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
                initTestRecycler();
            }
        }).start();
    }

    private void initTestRecycler(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LinearLayoutManager linearLayoutManager=new LinearLayoutManager(SelectActivity.this);
                selectRecycler.setLayoutManager(linearLayoutManager);
                selectRecycler.setAdapter(new TestAdapter(SelectActivity.this,testList));
            }
        });
    }

    private void initGradeInfo(final String xnm,final String xqm){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Connection connection=Jsoup.connect("http://jwxt.cumt.edu.cn/jwglxt/cjcx/cjcx_cxDgXscj.html?doType=query&gnmkdm=N305005&su="+userId);
                connection.header("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.85 Safari/537.36 Edg/90.0.818.49");
                connection.header("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
                connection.cookies(Cookies);
                connection.data("xnm",xnm)
                        .data("xqm",xqm)
                        .data("_search","false")
                        .data("nd",Integer.toString(new Date().getDate()))
                        .data("queryModel.showCount",Integer.toString(30))
                        .data("queryModel.currentPage",Integer.toString(1))
                        .data("queryModel.sortOrder","asc")
                        .data("time",Integer.toString(1));
                Connection.Response response=null;
                try {
                    response=connection.ignoreContentType(true).method(Connection.Method.POST).execute();
                }catch (IOException e){
                    e.printStackTrace();
                }
                try {
                    JSONObject jsonObject=new JSONObject(response.body());
                    JSONArray jsonArray=jsonObject.getJSONArray("items");
                    System.out.println("成绩信息：");
                    for(int i=0;i<jsonArray.length();i++){
                        jsonObject=jsonArray.getJSONObject(i);
                        GradeDataBean gradeDataBean=new GradeDataBean();
                        gradeDataBean.setCourseName(jsonObject.getString("kcmc"));
                        gradeDataBean.setGrade(jsonObject.getString("cj"));
                        gradeDataBean.setJd(jsonObject.getString("jd"));
                        gradeDataBean.setXf(jsonObject.getString("xf"));
                        gradeList.add(gradeDataBean);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
                initGradeRecycler();
            }
        }).start();
    }

    private void initGradeRecycler(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LinearLayoutManager linearLayoutManager=new LinearLayoutManager(SelectActivity.this);
                selectRecycler.setLayoutManager(linearLayoutManager);
                selectRecycler.setAdapter(new GradeAdapter(SelectActivity.this,gradeList));
            }
        });
    }

    private void initCourseData(final String xnm,final String xqm){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Connection connection=Jsoup.connect("http://jwxt.cumt.edu.cn/jwglxt/kbcx/xskbcx_cxXsKb.html?gnmkdm=N253508&su="+userId);
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
                    JSONObject jsonObject=new JSONObject(response.body());
                    JSONArray jsonArray1=jsonObject.getJSONArray("kbList");
                    JSONArray jsonArray2=jsonObject.getJSONArray("sjkList");
                    for(int i=0;i<jsonArray1.length();i++){
                        jsonObject=jsonArray1.getJSONObject(i);
                        CourseDataBean courseDataBean=new CourseDataBean();
                        courseDataBean.setCourse(jsonObject.getString("kcmc"));
                        courseDataBean.setJc(jsonObject.getString("jc"));
                        courseDataBean.setZc(jsonObject.getString("zcd"));
                        final String place=jsonObject.getString("xqmc")+" "+jsonObject.getString("cdmc");
                        courseDataBean.setPlace(place);
                        courseDataBean.setTeacher(jsonObject.getString("xm"));
                        courseDataBean.setTestType(jsonObject.getString("khfsmc"));
                        courseDataBean.setType(jsonObject.getString("kcxszc"));
                        courseDataBean.setXf(jsonObject.getString("xf"));
                        courseList.add(courseDataBean);
                    }
                    String content="";
                    for(int i=0;i<jsonArray2.length();i++){
                        jsonObject=jsonArray2.getJSONObject(i);
                        content+=jsonObject.getString("sjkcgs");
                    }
                    CourseDataBean courseDataBean=new CourseDataBean();
                    courseDataBean.setCourse(content);
                    courseDataBean.setJc("");
                    courseDataBean.setZc("");
                    courseDataBean.setPlace("");
                    courseDataBean.setTeacher("");
                    courseDataBean.setTestType("");
                    courseDataBean.setType("");
                    courseDataBean.setXf("");
                    if(content.length()>1){
                        courseList.add(courseDataBean);
                        initCourseRecycler(1);
                    }else {
                        initCourseRecycler(0);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void initCourseRecycler(int type){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LinearLayoutManager linearLayoutManager=new LinearLayoutManager(SelectActivity.this);
                selectRecycler.setLayoutManager(linearLayoutManager);
                selectRecycler.setAdapter(new CourseAdapter(SelectActivity.this,courseList,type));
            }
        });
    }

    public static void actionStart(Context context,int type){
        Intent intent=new Intent(context,SelectActivity.class);
        intent.putExtra("type",type);
        context.startActivity(intent);
    }
}