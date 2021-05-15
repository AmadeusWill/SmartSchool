package com.smartschool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smartschool.R;
import com.smartschool.bean.GradeDataBean;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GradeAdapter extends RecyclerView.Adapter<GradeAdapter.ViewHolder>{
    Context context;
    List<GradeDataBean> gradeList;
    public GradeAdapter(Context context,List<GradeDataBean> gradeList){
        this.context=context;
        this.gradeList=gradeList;
    }
    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.grade_recycler_layout,parent,false);
        final ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.nameTv.setText(gradeList.get(position).getCourseName());
        final String grade="成绩："+gradeList.get(position).getGrade();
        holder.gradeTv.setText(grade);
        final String jd="绩点："+gradeList.get(position).getJd();
        holder.jdTv.setText(jd);
        final String xf="学分："+gradeList.get(position).getXf();
        holder.xfTv.setText(xf);
        if(position==gradeList.size()-1){
            holder.divider.setVisibility(View.GONE);
        }else {
            holder.divider.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return gradeList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView nameTv;
        TextView gradeTv;
        TextView jdTv;
        TextView xfTv;
        View divider;
        View gradeView;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            nameTv=(TextView) itemView.findViewById(R.id.course_name_tv);
            gradeTv=(TextView) itemView.findViewById(R.id.grade_tv);
            jdTv=(TextView) itemView.findViewById(R.id.jd_tv);
            xfTv=(TextView) itemView.findViewById(R.id.xf_tv);
            divider=(View) itemView.findViewById(R.id.divider);
            gradeView=(View) itemView.findViewById(R.id.grade_view);
        }
    }
}
