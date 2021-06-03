package com.smartschool.ui.dashboard;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.smartschool.BaseActivity;
import com.smartschool.MainActivity;
import com.smartschool.R;

import java.io.InputStream;

public class ImgActivity extends BaseActivity {
    ImageView imageView;
    Dialog dialog;
    ImageView mImgView;
    int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img);

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

        type=getIntent().getIntExtra("type",0);

        imageView=(ImageView) findViewById(R.id.img_iv);
        if(type==0){
            actionBar.setTitle("校园班车");
            imageView.setImageResource(R.mipmap.bus);
//            Glide.with(this).load(R.mipmap.bus).into(imageView);
        }else {
            actionBar.setTitle("校历");
            imageView.setImageResource(R.mipmap.calendar);
//            Glide.with(this).load(R.mipmap.calendar).into(imageView);
        }
        init();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
    }

    private void init(){
        dialog=new Dialog(ImgActivity.this,R.style.Theme_AppCompat_Light_Dialog_Alert);
        mImgView=getImageView();
        dialog.setContentView(mImgView);

        mImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

//        mImgView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                AlertDialog.Builder builder=new AlertDialog.Builder(ImgActivity.this);
//                builder.setItems(new String[]{getResources().getString(R.string.save_picture)}, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                });
//                builder.show();
//                return true;
//            }
//        });
    }

    private ImageView getImageView(){
        ImageView iv=new ImageView(this);
        iv.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
        iv.setPadding(20,20,20,20);
//        InputStream is;
        if(type==0){
//            is=getResources().openRawResource(R.raw.bus_raw);
            iv.setImageResource(R.mipmap.bus);
        }else {
//            is=getResources().openRawResource(R.raw.calendar_raw);
            iv.setImageResource(R.mipmap.calendar);
        }
//        Drawable drawable= BitmapDrawable.createFromStream(is,null);
//        iv.setImageDrawable(drawable);
        return iv;
    }

    public static void actionStart(Context context,int type){
        Intent intent=new Intent(context,ImgActivity.class);
        intent.putExtra("type",type);
        context.startActivity(intent);
    }
}