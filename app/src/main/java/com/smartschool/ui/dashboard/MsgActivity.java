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

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
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

        msgRecycler=(RecyclerView) findViewById(R.id.msg_recycler);
        initMsgData();
    }
    private void initMsgData(){
        msgList=new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Connection connection= Jsoup.connect("http://jwxt.cumt.edu.cn/jwglxt/xtgl/index_cxAreaThree.html?localeKey=zh_CN&gnmkdm=index&su="+BaseActivity.userId);
                connection.header("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36 Edg/90.0.818.56");
                Connection.Response response =null;
                try{
                    response = connection.cookies(BaseActivity.Cookies).ignoreContentType(false).followRedirects(true).execute();
                }    catch (IOException e){
                    e.printStackTrace();
                }
                Document document=Jsoup.parse(response.body());
                Elements elements=document.select("#home a");
                for(int i=0;i<elements.size()&&i<7;i++){
                    Element element=elements.get(i);
                    String title=element.select(".title").text();
                    String url=element.select(".title").attr("title");
                    String time=element.select(".fraction").text();
                    NewsDataBean newsDataBean=new NewsDataBean();
                    newsDataBean.setTitle(title);
                    newsDataBean.setTime(time);
                    newsDataBean.setUrl(url);
                    msgList.add(newsDataBean);
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