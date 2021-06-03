package com.smartschool;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;

import com.smartschool.util.HttpUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoginActivity extends BaseActivity {
    public static boolean loginSuccess=false;
    ImageView codeImg;
    EditText nameEt;
    EditText pwdEt;
    EditText codeEt;
    final String baseUrl="http://holer50743.restclient.cn/crawler_server_war_exploded/";
    ProgressDialog waitingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        codeImg=(ImageView) findViewById(R.id.code_img);
        nameEt=(EditText) findViewById(R.id.name_et);
        pwdEt=(EditText) findViewById(R.id.pwd_et);
        codeEt=(EditText) findViewById(R.id.code_et);
        CheckBox displayPwd=(CheckBox) findViewById(R.id.display_pwd);
        displayPwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    pwdEt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }else {
                    pwdEt.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });

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
        actionBar.setTitle("用户登录");
//        actionBar.setDisplayHomeAsUpEnabled(true);

        initImg();

        Button loginBtn=(Button) findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initWaiting();
                final String name=nameEt.getText().toString();
                final String pwd=pwdEt.getText().toString();
                final String code=codeEt.getText().toString();
                userId=name;

                final String loginUrl=baseUrl+"getCookies?"
                        +"username="+name
                        +"&password="+pwd
                        +"&code="+code;

                HttpUtil.sendOkHttpRequest(loginUrl, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        waitingDialog.dismiss();
                        if(e instanceof SocketTimeoutException){
                            initDialog("连接超时，请重试！");
                        } else if(e instanceof ConnectException){
                            initDialog("连接出错，请检查网络并重试！");
                        }else {
                            initDialog("失败，请重试！");
                        }
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try {
                            JSONObject jsonObject=new JSONObject(response.body().string());
                            JSONArray jsonArray=jsonObject.getJSONArray("rows");
                            Cookies=new HashMap<>();
                            for(int i=0;i<jsonArray.length();i++){
                                jsonObject=jsonArray.getJSONObject(i);
                                final String key=jsonObject.getString("key");
                                final String value=jsonObject.getString("value");
                                Cookies.put(key,value);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        initStandardInfo();
                    }
                });
            }
        });
    }

    private void initStandardInfo(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Connection connection= Jsoup.connect("http://jwxt.cumt.edu.cn/jwglxt/xsxxxggl/xsgrxxwh_cxXsgrxx.html?gnmkdm=N100801&layout=default&su="+userId);
                connection.header("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.85 Safari/537.36 Edg/90.0.818.49");
                Connection.Response response =null;
                try{
                    response = connection.cookies(Cookies).ignoreContentType(false).followRedirects(true).execute();
                }    catch (IOException e){
                    e.printStackTrace();
                }
                Document document =Jsoup.parse(response.body());
                System.out.println("学生信息：");
                Element element=document.select("#col_xm p").first();
                if(element==null||element.text().length()<=0){
                    waitingDialog.dismiss();
                    initDialog("数据请求错误，请重试！");
                }else {
                    username=element.text();

                    element=document.select("#col_xbm p").first();

                    element=document.select("#col_njdm_id p").first();

                    element=document.select("#col_jg_id p").first();

                    element=document.select("#col_zyh_id p").first();

                    element=document.select("#col_bh_id p").first();
                    userClass=element.text();
                    waitingDialog.dismiss();
                    loginSuccess=true;
                    finish();
                }
            }
        }).start();
    }

    private void initImg(){
        final String imgUrl=baseUrl+"Image";
        HttpUtil.sendOkHttpRequest(imgUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if(e instanceof SocketTimeoutException){
                    initDialog("连接超时，请重试！");
                } else if(e instanceof ConnectException){
                    initDialog("连接出错，请检查网络并重试！");
                }else {
                    initDialog("失败，请重试！");
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                byte[] bytes=new byte[1024];
                bytes=response.body().bytes();
                Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        codeImg.setImageBitmap(bitmap);
                    }
                });
            }
        });
    }

    private void initDialog(String msg){
        AlertDialog.Builder builder=new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("访问失败！");
        builder.setMessage(msg);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        builder.create().show();
    }

    private void initWaiting(){
        waitingDialog=new ProgressDialog(LoginActivity.this);
        waitingDialog.setTitle("登陆中");
        waitingDialog.setMessage("请等待……");
        waitingDialog.setCancelable(false);
        waitingDialog.show();
    }

    public static void actionStart(Context context){
        Intent intent=new Intent(context,LoginActivity.class);
        context.startActivity(intent);
    }
}