package com.smartschool.ui.dashboard;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.smartschool.BaseActivity;
import com.smartschool.R;
import com.smartschool.adapter.NewsAdapter;
import com.smartschool.bean.NewsDataBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MsgActivity extends BaseActivity {
    List<NewsDataBean> msgList;
    RecyclerView msgRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg);

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
        actionBar.setTitle("个人信息");

        msgRecycler=(RecyclerView) findViewById(R.id.msg_recycler);
        initMsgData();
    }
    private void initMsgData(){
        msgList=new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Connection connection= Jsoup.connect("http://jwxt.cumt.edu.cn/jwglxt/xtgl/index_cxDbsy.html?doType=query");
                connection.header("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36 Edg/90.0.818.56");
                connection.header("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
                connection.data("sfyy","1")
                        .data("flag","1")
                        .data("_search","false")
                        .data("nd",Integer.toString(new Date().getDate()))
                        .data("queryModel.showCount","30")
                        .data("queryModel.currentPage","1")
                        .data("queryModel.sortName","cjsj")
                        .data("queryModel.sortOrder","desc")
                        .data("time","1");
                Connection.Response response =null;
                try{
                    response = connection.cookies(BaseActivity.Cookies).ignoreContentType(true).followRedirects(true).method(Connection.Method.POST).execute();
                }    catch (IOException e){
                    e.printStackTrace();
                }
                try {
                    JSONObject jsonObject=new JSONObject(response.body());
                    JSONArray jsonArray=jsonObject.getJSONArray("items");
                    for(int i=0;i<jsonArray.length();i++){
                        jsonObject=jsonArray.getJSONObject(i);
                        final String title=jsonObject.getString("xxbtjc");
                        final String time=jsonObject.getString("cjsj");
                        final String url=jsonObject.getString("xxbt");
                        NewsDataBean newsDataBean=new NewsDataBean();
                        newsDataBean.setTitle(title);
                        newsDataBean.setTime(time);
                        newsDataBean.setUrl(url);
                        msgList.add(newsDataBean);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
                initMsgRecycler();
            }
        }).start();
    }

    private void initMsgRecycler(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LinearLayoutManager linearLayoutManager=new LinearLayoutManager(MsgActivity.this);
                msgRecycler.setLayoutManager(linearLayoutManager);
                msgRecycler.setAdapter(new NewsAdapter(MsgActivity.this,msgList,2));
            }
        });
    }

    public static void actionStart(Context context){
        Intent intent=new Intent(context,MsgActivity.class);
        context.startActivity(intent);
    }
}