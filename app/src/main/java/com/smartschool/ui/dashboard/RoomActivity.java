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
import android.widget.Button;
import android.widget.Spinner;

import com.smartschool.BaseActivity;
import com.smartschool.R;
import com.smartschool.adapter.RoomAdapter;
import com.smartschool.bean.RoomDataBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RoomActivity extends BaseActivity {
    Spinner spArea;
    Spinner spWeek;
    Spinner spDay;
    Spinner spJC;
    List<RoomDataBean> roomList;
    RecyclerView roomRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

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

        spArea=(Spinner) findViewById(R.id.sp_area);
        spWeek=(Spinner) findViewById(R.id.sp_week);
        spDay=(Spinner) findViewById(R.id.sp_day);
        spJC=(Spinner) findViewById(R.id.sp_jc);
        Button btnSelect=(Button) findViewById(R.id.btn_select_room);
        roomRecycler=(RecyclerView) findViewById(R.id.room_recycler);
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String xqh_id=Integer.toString(spArea.getSelectedItemPosition()+1);
                String xnm="2020";
                String xqm="12";
                String zcd=Integer.toString((int)Math.pow(2,spWeek.getSelectedItemPosition()));
                String xqj=Integer.toString(spDay.getSelectedItemPosition()+1);
                String jcd=Integer.toString((int)Math.pow(2,spJC.getSelectedItemPosition()));
                if(spWeek.getSelectedItemPosition()>=10&&spWeek.getSelectedItemPosition()+spDay.getSelectedItemPosition()>=16){
                    initRoomData(xqh_id,xnm,xqm,zcd,xqj,jcd);
                }
            }
        });
    }

    private void initRoomData(final String xqh_id,final String xnm,final String xqm,final String zcd,final String xqj,final String jcd){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Connection connection= Jsoup.connect("http://jwxt.cumt.edu.cn/jwglxt/cdjy/cdjy_cxKxcdlb.html?doType=query&gnmkdm=N2155");
                connection.header("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36 Edg/90.0.818.56");
                connection.header("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
                connection.data("fwzt","cx")
                        .data("xqh_id",xqh_id)
                        .data("xnm",xnm)
                        .data("xqm",xqm)
                        .data("jyfs","0")
                        .data("zcd",zcd)
                        .data("xqj",xqj)
                        .data("jcd",jcd)
                        .data("_search","false")
                        .data("nd",Integer.toString(new Date().getDate()))
                        .data("queryModel.showCount","30")
                        .data("queryModel.currentPage","1")
                        .data("queryModel.sortName","cdbh")
                        .data("queryModel.sortOrder","asc")
                        .data("time","1");
                Connection.Response response =null;
                try{
                    response = connection.cookies(BaseActivity.Cookies).ignoreContentType(true).followRedirects(true).method(Connection.Method.POST).execute();
                }    catch (IOException e){
                    e.printStackTrace();
                }
                roomList=new ArrayList<>();
                try {
                    JSONObject jsonObject=new JSONObject(response.body());
                    JSONArray jsonArray=jsonObject.getJSONArray("items");
                    for(int i=0;i<jsonArray.length();i++){
                        jsonObject=jsonArray.getJSONObject(i);
                        RoomDataBean roomDataBean=new RoomDataBean();
                        roomDataBean.setArea(jsonObject.getString("xqmc"));
                        roomDataBean.setPlace(jsonObject.getString("cdmc"));
                        final String time="时间："+jsonObject.getString("dateDigitSeparator");
                        roomDataBean.setTime(time);
                        final String count="座位数："+jsonObject.getString("zws");
                        roomDataBean.setSeatCount(count);
                        final String tip="备注："+jsonObject.getString("bz");
                        roomDataBean.setTip(tip);
                        roomList.add(roomDataBean);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
                initRoomRecycler();
            }
        }).start();
    }

    private void initRoomRecycler(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LinearLayoutManager linearLayoutManager=new LinearLayoutManager(RoomActivity.this);
                roomRecycler.setLayoutManager(linearLayoutManager);
                roomRecycler.setAdapter(new RoomAdapter(RoomActivity.this,roomList));
            }
        });
    }

    public static void actionStart(Context context){
        Intent intent=new Intent(context,RoomActivity.class);
        context.startActivity(intent);
    }
}