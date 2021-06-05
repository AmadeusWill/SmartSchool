package com.smartschool.ui.dashboard;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.smartschool.BaseActivity;
import com.smartschool.LoginActivity;
import com.smartschool.R;
import com.smartschool.adapter.BtnAdapter;
import com.smartschool.adapter.NewsAdapter;
import com.smartschool.bean.BtnDataBean;
import com.smartschool.bean.NewsDataBean;
import com.smartschool.databinding.FragmentDashboardBinding;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DashboardFragment extends Fragment {
    RecyclerView btnRecycler;
    RecyclerView notiRecycler;
    List<NewsDataBean> notiLsit;
    ProgressDialog dialog;

    final int[] imgIds={R.drawable.ic_select_test,R.drawable.ic_select_point,R.drawable.ic_select_msg,R.drawable.ic_evaluation,R.drawable.ic_empty_room,R.drawable.ic_bus,R.drawable.ic_calendar,R.drawable.ic_course_form,R.drawable.ic_ele,R.drawable.ic_more};
    final String[] btnText={"考试查询","成绩查询","消息","教学评价","空闲教室","班车时刻表","校历","课程表","电量查询","更多功能"};

//    private DashboardViewModel dashboardViewModel;
    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        dashboardViewModel =
//                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        final TextView textView = binding.textDashboard;
//        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        btnRecycler=(RecyclerView) root.findViewById(R.id.btn_recycler);
        notiRecycler=(RecyclerView) root.findViewById(R.id.noti_recycler);
        initBtnRecycler();
        if(LoginActivity.loginSuccess){
            dialog=new ProgressDialog(getContext());
            dialog.setTitle("请等待!");
            dialog.setMessage("数据加载中…………");
            dialog.setCancelable(false);
            dialog.show();
            if(notiLsit==null||notiLsit.size()==0){
                notiLsit=new ArrayList<>();
                initNotiData();
            }else{
                initNotiRecycler();
            }
        }

        return root;
    }

    private void initBtnRecycler(){
        List<BtnDataBean> btnList=new ArrayList<>();
        final int n=imgIds.length;
        for(int i=0;i<n;i++){
            BtnDataBean btnDataBean=new BtnDataBean();
            btnDataBean.setImgId(imgIds[i]);
            btnDataBean.setBtnText(btnText[i]);
            btnList.add(btnDataBean);
        }
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        btnRecycler.setLayoutManager(linearLayoutManager);
        btnRecycler.setAdapter(new BtnAdapter(getContext(),btnList));
    }

    private void initNotiData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Connection connection= Jsoup.connect("http://jwxt.cumt.edu.cn/jwglxt/xtgl/index_cxNews.html?localeKey=zh_CN&gnmkdm=index&su="+BaseActivity.userId);
                connection.header("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36 Edg/90.0.818.56");
                Connection.Response response =null;
                try{
                    response = connection.cookies(BaseActivity.Cookies).ignoreContentType(true).followRedirects(true).method(Connection.Method.POST).execute();
                }    catch (IOException e){
                    e.printStackTrace();
                }
                Document document=Jsoup.parse(response.body());
                Elements elements=document.select(".list-group a");
                System.out.println(response.body());
                for(int i=0;i<elements.size()&&i<7;i++){
                    Element element=elements.get(i);
                    String title=element.attr("title");
                    String url=element.attr("href");
                    String time=element.select(".time").first().text();
                    NewsDataBean newsDataBean=new NewsDataBean();
                    newsDataBean.setTitle(title);
                    newsDataBean.setTime(time);
                    newsDataBean.setUrl(url);
                    notiLsit.add(newsDataBean);
                }
                initNotiRecycler();
            }
        }).start();
    }

    private void initNotiRecycler(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
                notiRecycler.setLayoutManager(linearLayoutManager);
                notiRecycler.setAdapter(new NewsAdapter(getContext(),notiLsit,1));
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}