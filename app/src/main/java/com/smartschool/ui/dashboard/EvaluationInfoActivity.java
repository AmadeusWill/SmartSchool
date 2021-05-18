package com.smartschool.ui.dashboard;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartschool.BaseActivity;
import com.smartschool.R;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class EvaluationInfoActivity extends AppCompatActivity {
    TextView courseTv;
    TextView teacherTv;
    ImageView imgTv;
    TextView contentTv;
    TextView suggestionTv;
    byte[] bytes;
    String teacherName="评价教师：";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation_info);

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

        courseTv=(TextView) findViewById(R.id.evaluation_info_course);
        teacherTv=(TextView) findViewById(R.id.evaluation_info_teacher);
        imgTv=(ImageView) findViewById(R.id.evaluation_info_img);
        contentTv=(TextView) findViewById(R.id.evaluation_info_eva);
        suggestionTv=(TextView) findViewById(R.id.evaluation_info_suggestion);

        final String jxb_id=getIntent().getStringExtra("jxb_id");
        final String kch_id=getIntent().getStringExtra("kch_id");
        final String xsdm=getIntent().getStringExtra("xsdm");
        final String jgh_id=getIntent().getStringExtra("jgh_id");
        final String tjzt=getIntent().getStringExtra("tjzt");
        final String pjmbmcb_id=getIntent().getStringExtra("pjmbmcb_id");
        final String sfcjlrjs=getIntent().getStringExtra("sfcjlrjs");
        teacherName+=getIntent().getStringExtra("teacher");
        initImg(jxb_id,kch_id,xsdm,jgh_id,tjzt,pjmbmcb_id,sfcjlrjs);

    }

    private void initImg(String jxb_id,String kch_id,String xsdm,String jgh_id,String tjzt,String pjmbmcb_id,String sfcjlrjs){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Connection connection= Jsoup.connect("http://jwxt.cumt.edu.cn/jwglxt/xtgl/photo_cxJzgzp2.html?jgh_id="+jgh_id);
                connection.header("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.212 Safari/537.36 Edg/90.0.818.62");
                Connection.Response response =null;
                try{
                    response = connection.cookies(BaseActivity.Cookies).ignoreContentType(true).followRedirects(true).method(Connection.Method.GET).execute();
                }    catch (IOException e){
                    e.printStackTrace();
                }
                bytes=response.bodyAsBytes();
                initData(jxb_id,kch_id,xsdm,jgh_id,tjzt,pjmbmcb_id,sfcjlrjs);
            }
        }).start();
    }

    private void initData(String jxb_id,String kch_id,String xsdm,String jgh_id,String tjzt,String pjmbmcb_id,String sfcjlrjs){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Connection connection= Jsoup.connect("http://jwxt.cumt.edu.cn/jwglxt/xspjgl/xspj_cxXspjDisplay.html?gnmkdm=N401605&su="+ BaseActivity.userId);
                connection.header("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.212 Safari/537.36 Edg/90.0.818.62");
                connection.header("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
                connection.data("jxb_id",jxb_id)
                        .data("kch_id",kch_id)
                        .data("xsdm",xsdm)
                        .data("jgh_id",jgh_id)
                        .data("tjzt",tjzt)
                        .data("pjmbmcb_id",pjmbmcb_id)
                        .data("sfcjlrjs",sfcjlrjs);
                Connection.Response response =null;
                try{
                    response = connection.cookies(BaseActivity.Cookies).ignoreContentType(true).followRedirects(true).method(Connection.Method.POST).execute();
                }    catch (IOException e){
                    e.printStackTrace();
                }
                Document document=Jsoup.parse(response.body());
                Element element=document.select("#ajaxForm1").first();
                final String courseName="当前评价课程为："+element.select(".row .col-sm-8").text();
                final String content=element.select(".tr-xspj td").get(1).text();
                final String suggestion=element.select("#pyDiv .input-xspj").text();
                initView(courseName,content,suggestion);
            }
        }).start();
    }

    private void initView(String courseName,String content,String suggestion){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                courseTv.setText(courseName);
                teacherTv.setText(teacherName);
                Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                imgTv.setImageBitmap(bitmap);
                contentTv.setText(content);
                suggestionTv.setText(suggestion);
            }
        });
    }

    public static void actionStart(Context context,String jxb_id,String kch_id,String xsdm,String jgh_id,String tjzt,String pjmbmcb_id,String sfcjlrjs,String teacherName){
        Intent intent=new Intent(context,EvaluationInfoActivity.class);
        intent.putExtra("jxb_id",jxb_id)
                .putExtra("kch_id",kch_id)
                .putExtra("xsdm",xsdm)
                .putExtra("jgh_id",jgh_id)
                .putExtra("tjzt",tjzt)
                .putExtra("pjmbmcb_id",pjmbmcb_id)
                .putExtra("sfcjlrjs",sfcjlrjs)
                .putExtra("teacher",teacherName);
        context.startActivity(intent);
    }
}