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
import com.smartschool.adapter.EvaluationAdapter;
import com.smartschool.bean.EvaluationDataBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EvaluationActivity extends BaseActivity {
    List<EvaluationDataBean> evaluationList;
    Spinner sp;
    RecyclerView evaluationRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation);

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
        actionBar.setTitle("教学评价");

        evaluationRecycler=(RecyclerView) findViewById(R.id.evaluation_recycler);

        sp=(Spinner) findViewById(R.id.evaluation_sp);
        Button selectBtn=(Button) findViewById(R.id.btn_select_evaluation);
        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sp.getSelectedItemPosition()==1){
                    initEvaluationData();
                }
            }
        });
    }

    private void initEvaluationData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Connection connection= Jsoup.connect("http://jwxt.cumt.edu.cn/jwglxt/xspjgl/xspj_cxXspjIndex.html?doType=query&gnmkdm=N401605&su="+ BaseActivity.userId);
                connection.header("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.212 Safari/537.36 Edg/90.0.818.62");
                connection.header("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
                connection.data("_search","false")
                        .data("nd",Integer.toString(new Date().getDate()))
                        .data("queryModel.showCount","30")
                        .data("queryModel.currentPage","1")
                        .data("queryModel.sortOrder","asc")
                        .data("time","0");
                Connection.Response response =null;
                try{
                    response = connection.cookies(BaseActivity.Cookies).ignoreContentType(true).followRedirects(true).method(Connection.Method.POST).execute();
                }    catch (IOException e){
                    e.printStackTrace();
                }
                try {
                    JSONObject jsonObject=new JSONObject(response.body());
                    JSONArray jsonArray=jsonObject.getJSONArray("items");
                    evaluationList=new ArrayList<>();
                    for(int i=0;i<jsonArray.length();i++){
                        jsonObject=jsonArray.getJSONObject(i);
                        EvaluationDataBean evaluationDataBean=new EvaluationDataBean();
                        evaluationDataBean.setCourseName(jsonObject.getString("kcmc"));
                        evaluationDataBean.setType(jsonObject.getString("xsmc"));
                        evaluationDataBean.setTeacher(jsonObject.getString("jzgmc"));
                        evaluationDataBean.setJxb_id(jsonObject.getString("jxb_id"));
                        evaluationDataBean.setKch_id(jsonObject.getString("kch_id"));
                        evaluationDataBean.setXsdm(jsonObject.getString("xsdm"));
                        evaluationDataBean.setJgh_id(jsonObject.getString("jgh_id"));
                        evaluationDataBean.setTjzt(jsonObject.getString("tjzt"));
                        evaluationDataBean.setPjmbmcb_id(jsonObject.getString("pjmbmcb_id"));
                        evaluationDataBean.setSfcjlrjs(jsonObject.getString("sfcjlrjs"));
                        evaluationList.add(evaluationDataBean);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
                initEvaluationRecycler();
            }
        }).start();
    }

    private void initEvaluationRecycler(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LinearLayoutManager linearLayoutManager=new LinearLayoutManager(EvaluationActivity.this);
                evaluationRecycler.setLayoutManager(linearLayoutManager);
                evaluationRecycler.setAdapter(new EvaluationAdapter(EvaluationActivity.this,evaluationList));
            }
        });
    }

    public static void actionStart(Context context){
        Intent intent=new Intent(context,EvaluationActivity.class);
        context.startActivity(intent);
    }
}