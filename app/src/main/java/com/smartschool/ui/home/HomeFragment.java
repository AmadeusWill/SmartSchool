package com.smartschool.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.smartschool.LoginActivity;
import com.smartschool.R;
import com.smartschool.databinding.FragmentHomeBinding;
import com.smartschool.ui.notifications.NewsInfoActivity;

public class HomeFragment extends Fragment implements View.OnClickListener{

//    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        homeViewModel =
//                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        final TextView textView = binding.textHome;
//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        Button infoBtn=(Button) root.findViewById(R.id.home_person_btn);
        Button studyBtn=(Button) root.findViewById(R.id.home_online_study_btn);
        Button moreBtn=(Button) root.findViewById(R.id.home_more_btn);
        infoBtn.setOnClickListener(this);
        studyBtn.setOnClickListener(this);
        moreBtn.setOnClickListener(this);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.home_person_btn:
                if(LoginActivity.loginSuccess){
                    InfoActivity.actionStart(getContext());
                }else {
                    final AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                    builder.setTitle("学籍信息查询失败");
                    builder.setMessage("请先进行登陆！");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.create().show();
                }
                break;
            case R.id.home_online_study_btn:
                StudyActivity.actionStart(getContext(),"http://cumt.fy.chaoxing.com/portal");
                break;
            case R.id.home_more_btn:
                NewsInfoActivity.actionStart(getContext(),"http://yx.houqinbao.com/index.php?m=Wechat&c=Wechat&a=index&token=gh_8cbd49d3fd1d&openid=oUiRowS0zsDmGwLjfjnf1T2_n0TM");
                break;
            default:break;
        }
    }
}