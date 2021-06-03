package com.smartschool.ui.notifications;

import androidx.appcompat.app.ActionBar;
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
import com.smartschool.util.HttpUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class NewsActivity extends BaseActivity {
    final String url="http://www.cumt.edu.cn/";
    List<NewsDataBean> newsList;
    RecyclerView newsRecycler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        final int type=getIntent().getIntExtra("newsType",0);

        View view=(View) findViewById(R.id.tb);
        Toolbar toolbar=(Toolbar) view.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if(type==0){
            actionBar.setTitle("视点新闻");
        }else {
            actionBar.setTitle("学术聚焦");
        }
        toolbar.setFitsSystemWindows(true);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        newsRecycler=(RecyclerView) findViewById(R.id.news_recycler);
        if(type==0){
            initNewsData1();
        }else {
            initNewsData2();
        }
    }

    private void initNewsData1(){
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Document document= Jsoup.parse(response.body().string());
                Elements elements=document.select("#wp_news_w111 .mox");
                newsList=new ArrayList<>();
                for(Element element:elements){
                    Element element1=element.select(".news_title a").first();
                    final String title=element1.text();
                    final String time=element.select(".news_time").first().text();
                    final String url=element1.attr("href");
                    NewsDataBean newsDataBean=new NewsDataBean();
                    newsDataBean.setTitle(title);
                    newsDataBean.setTime(time);
                    newsDataBean.setUrl(url);
                    newsList.add(newsDataBean);
                }
                initList();
            }
        });
    }

    private void initNewsData2(){
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Document document=Jsoup.parse(response.body().string());
                Elements elements=document.select("#wp_news_w12 .news");
                newsList=new ArrayList<>();
                for(Element element:elements){
                    final String date=element.select(".news_date .news_year").first().text();
                    final String month=element.select(".news_date .news_days").first().text();
                    final String time=month+"月-"+date;
                    Element element1=element.select(".news_wz a").first();
                    final String title=element1.text();
                    final String url=element1.attr("href");
                    NewsDataBean newsDataBean=new NewsDataBean();
                    newsDataBean.setTitle(title);
                    newsDataBean.setTime(time);
                    newsDataBean.setUrl(url);
                    newsList.add(newsDataBean);
                }
                initList();
            }
        });
    }

    private void initList(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LinearLayoutManager linearLayoutManager=new LinearLayoutManager(NewsActivity.this);
                newsRecycler.setLayoutManager(linearLayoutManager);
                newsRecycler.setAdapter(new NewsAdapter(NewsActivity.this,newsList,0));
            }
        });
    }

    public static void actionStart(Context context,int type){
        Intent intent=new Intent(context,NewsActivity.class);
        intent.putExtra("newsType",type);
        context.startActivity(intent);
    }
}