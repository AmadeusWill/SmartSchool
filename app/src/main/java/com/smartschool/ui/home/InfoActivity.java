package com.smartschool.ui.home;

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
import com.smartschool.util.HttpUtil;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class InfoActivity extends AppCompatActivity {
    ImageView img;
    TextView baseInfo;
    TextView xhTv;
    TextView xmTv;
    TextView sfzTv;
    TextView csMzTv;
    TextView zzTv;
    TextView rxTv;
    TextView jgTv;
    TextView lxTv;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

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
        actionBar.setTitle("个人学籍信息");
        img=(ImageView) findViewById(R.id.person_img);
        baseInfo=(TextView) findViewById(R.id.person_base_tv);
        xhTv=(TextView) findViewById(R.id.xh_tv);
        xmTv=(TextView) findViewById(R.id.xm_xmpy_tv);
        sfzTv=(TextView) findViewById(R.id.sfz_tv);
        csMzTv=(TextView) findViewById(R.id.csrq_mz_tv);
        zzTv=(TextView) findViewById(R.id.zzmm_tv);
        rxTv=(TextView) findViewById(R.id.rxrq_xslx_tv);
        jgTv=(TextView) findViewById(R.id.jg_tv);
        lxTv=(TextView) findViewById(R.id.sfzlx_tv);

        initInfo();
    }

    private void initInfo(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Connection connection= Jsoup.connect("http://jwxt.cumt.edu.cn/jwglxt/xsxxxggl/xsgrxxwh_cxXsgrxx.html?gnmkdm=N100801&layout=default&su="+ BaseActivity.userId);
                connection.header("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.212 Safari/537.36 Edg/90.0.818.62");
                Connection.Response response =null;
                try {
                    response=connection.followRedirects(true).method(Connection.Method.GET).cookies(BaseActivity.Cookies).ignoreContentType(false).execute();
                }catch (IOException e){
                    e.printStackTrace();
                }
                Document document=Jsoup.parse(response.body());
                final String imgUrl=document.select("#xsrxhZp").first().attr("src");
                final String xh=document.select("#col_xh").text();
                final String xm=document.select("#col_xm").text();
                final String xmpy=document.select("#col_xmpy").text();
                final String xb=document.select("#col_xbm").text();
                final String sfz=document.select("#col_zjhm").text();
                final String sfzlx=document.select("#col_zjlxm").text();
                final String cs=document.select("#col_csrq").text();
                final String mz=document.select("#col_mzm").text();
                final String zz=document.select("#col_zzmmm").text();
                final String rx=document.select("#col_rxrq").text();
                final String jg=document.select("#col_jg").text();
                final String lx=document.select("#col_xslxdm").text();
                initImg(imgUrl,xh,xm,xmpy,xb,sfz,sfzlx,cs,mz,zz,rx,jg,lx);
            }
        }).start();
    }

    private void initImg(final String url,final String xh,final String xm,final String xmpy,final String xb,final String sfz,final String sfzlx,final String cs,final String mz,final String zz,final String rx,final String jg,final String lx){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Connection connection= Jsoup.connect("http://jwxt.cumt.edu.cn"+url);
                connection.header("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36 Edg/91.0.864.37");
                Connection.Response response=null;
                try {
                    response=connection.followRedirects(true).method(Connection.Method.GET).cookies(BaseActivity.Cookies).ignoreContentType(true).execute();
                }catch (IOException e){
                    e.printStackTrace();
                }
                byte[] bytes=new byte[1024];
                bytes=response.bodyAsBytes();
                bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                initView(xh,xm,xmpy,xb,sfz,sfzlx,cs,mz,zz,rx,jg,lx);
            }
        }).start();
    }

    private void initView(final String xh,final String xm,final String xmpy,final String xb,final String sfz,final String sfzlx,final String cs,final String mz,final String zz,final String rx,final String jg,final String lx){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                img.setImageBitmap(bitmap);
                String baseStr=xh+"\n"+xm;
                baseInfo.setText(baseStr);
                String xhStr="学生学号："+xh+"  性别："+xb;
                xhTv.setText(xhStr);
                String xmStr="姓名："+xm+"  姓名拼音："+xmpy;
                xmTv.setText(xmStr);
                String sfzStr="身份证号码："+sfz;
                sfzTv.setText(sfzStr);
                sfzStr="身份证类型："+sfzlx;
                lxTv.setText(sfzStr);
                String csMzStr="出生日期："+cs+"  民族："+mz;
                csMzTv.setText(csMzStr);
                String zzStr="政治面貌："+zz;
                zzTv.setText(zzStr);
                String rxStr="入学日期："+rx+"  学生类型："+lx;
                rxTv.setText(rxStr);
                String jgStr="籍贯："+jg;
                jgTv.setText(jgStr);
            }
        });
    }

    public static void actionStart(Context context){
        Intent intent=new Intent(context,InfoActivity.class);
        context.startActivity(intent);
    }
}