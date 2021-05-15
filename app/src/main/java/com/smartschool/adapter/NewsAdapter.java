package com.smartschool.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smartschool.BaseActivity;
import com.smartschool.R;
import com.smartschool.bean.NewsDataBean;
import com.smartschool.ui.notifications.NewsInfoActivity;
import com.smartschool.util.HttpUtil;

import org.jetbrains.annotations.NotNull;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder>{
    Context context;
    List<NewsDataBean> newsList;
    int type;

    public NewsAdapter(Context context,List<NewsDataBean> newsList,int type){
        this.context=context;
        this.newsList=newsList;
        this.type=type;
    }
    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.news_recycler_layout,parent,false);
        final ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        switch (type){
            case 0:
                holder.newsView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String newsUrl=newsList.get(position).getUrl();
                        NewsInfoActivity.actionStart(context,newsUrl);
                    }
                });
                break;
            case 1:
                holder.newsView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url=newsList.get(position).getUrl();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Connection connection= Jsoup.connect("http://jwxt.cumt.edu.cn"+url);
                                connection.header("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36 Edg/90.0.818.56");
                                Connection.Response response =null;
                                try{
                                    response = connection.cookies(BaseActivity.Cookies).ignoreContentType(false).followRedirects(true).execute();
                                }    catch (IOException e){
                                    e.printStackTrace();
                                }
                                Document document=Jsoup.parse(response.body());
                                Element element=document.select(".news_con a").first();
                                String newsUrl=element.attr("href");
                                NewsInfoActivity.actionStart(context,newsUrl);
                            }
                        }).start();
                    }
                });
                break;
            case 2:
                holder.newsView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String msgContent=newsList.get(position).getUrl();
                        System.out.println(msgContent);
                        final AlertDialog.Builder builder=new AlertDialog.Builder(context);
                        builder.setTitle("详细信息");
                        builder.setMessage(msgContent);
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.create().show();
                    }
                });
                break;
            default:break;
        }

        holder.titleText.setText(newsList.get(position).getTitle());
        holder.timeText.setText(newsList.get(position).getTime());
        if(position==newsList.size()-1){
            holder.divider.setVisibility(View.GONE);
        }else{
            holder.divider.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        View newsView;
        TextView titleText;
        TextView timeText;
        View divider;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            newsView=(View) itemView.findViewById(R.id.news_recycler_layout);
            titleText=(TextView) itemView.findViewById(R.id.news_title_tv);
            timeText=(TextView) itemView.findViewById(R.id.news_time_tv);
            divider=(View) itemView.findViewById(R.id.divider);
        }
    }
}
