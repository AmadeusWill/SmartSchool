package com.smartschool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smartschool.LoginActivity;
import com.smartschool.R;
import com.smartschool.bean.BtnDataBean;
import com.smartschool.ui.dashboard.MsgActivity;
import com.smartschool.ui.dashboard.SelectActivity;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BtnAdapter extends RecyclerView.Adapter<BtnAdapter.ViewHolder>{
    Context context;
    List<BtnDataBean> btnList;
    public BtnAdapter(Context context,List<BtnDataBean> btnList){
        this.context=context;
        this.btnList=btnList;
    }
    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.btn_recycler_layout,parent,false);
        final ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.btnImg.setImageResource(btnList.get(position).getImgId());
        holder.btnText.setText(btnList.get(position).getBtnText());
        holder.btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(LoginActivity.loginSuccess){
                    switch (position){
                        case 0:
                            SelectActivity.actionStart(context,position);
                            break;
                        case 1:
                            SelectActivity.actionStart(context,position);
                            break;
                        case 2:
                            MsgActivity.actionStart(context);
                            break;
                        default:break;
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return btnList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        View btnView;
        ImageView btnImg;
        TextView btnText;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            btnView=(View) itemView.findViewById(R.id.btn_view);
            btnImg=(ImageView) itemView.findViewById(R.id.btn_iv);
            btnText=(TextView) itemView.findViewById(R.id.btn_tv);
        }
    }
}
