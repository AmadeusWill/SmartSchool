package com.smartschool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smartschool.R;
import com.smartschool.bean.CourseDataBean;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder>{
    Context context;
    List<CourseDataBean> courseList;
    int type;
    public CourseAdapter(Context context,List<CourseDataBean> courseList,int type){
        this.context=context;
        this.courseList=courseList;
        this.type=type;
    }
    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.course_recycler_layout,parent,false);
        final ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        if(type==0){
            holder.divider.setVisibility(View.VISIBLE);
            holder.courseTv.setText(courseList.get(position).getCourse());
            holder.xfTv.setText(courseList.get(position).getXf());
            final String jczc="("+courseList.get(position).getJc()+")"+courseList.get(position).getZc();
            holder.jczcTv.setText(jczc);
            holder.placeTv.setText(courseList.get(position).getPlace());
            holder.teacherTv.setText(courseList.get(position).getTeacher());
            holder.testTypeTv.setText(courseList.get(position).getTestType());
            holder.typeTv.setText(courseList.get(position).getType());
            if(position==courseList.size()-1){
                holder.divider.setVisibility(View.GONE);
            }else {
                holder.divider.setVisibility(View.VISIBLE);
            }
        }else {
            if(position!=courseList.size()-1){
                holder.divider.setVisibility(View.VISIBLE);
                holder.courseTv.setText(courseList.get(position).getCourse());
                holder.xfTv.setText(courseList.get(position).getXf());
                final String jczc="上课时间："+"("+courseList.get(position).getJc()+")"+courseList.get(position).getZc();
                holder.jczcTv.setText(jczc);
                holder.placeTv.setText(courseList.get(position).getPlace());
                String teacher="任课教师："+courseList.get(position).getTeacher();
                holder.teacherTv.setText(teacher);
                String testType="考核方式："+courseList.get(position).getTestType();
                holder.testTypeTv.setText(testType);
                String type="学分组成："+courseList.get(position).getType();
                holder.typeTv.setText(type);
            }else {
                holder.divider.setVisibility(View.GONE);
                holder.courseTv.setText(courseList.get(position).getCourse());
            }
        }
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView courseTv;
        TextView xfTv;
        TextView jczcTv;
        TextView placeTv;
        TextView teacherTv;
        TextView testTypeTv;
        TextView typeTv;
        View divider;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            courseTv=(TextView) itemView.findViewById(R.id.course_tv);
            xfTv=(TextView) itemView.findViewById(R.id.course_xf);
            jczcTv=(TextView) itemView.findViewById(R.id.course_jc_zc);
            placeTv=(TextView) itemView.findViewById(R.id.course_place);
            teacherTv=(TextView) itemView.findViewById(R.id.course_teacher);
            testTypeTv=(TextView) itemView.findViewById(R.id.course_testType);
            typeTv=(TextView) itemView.findViewById(R.id.course_type);
            divider=(View) itemView.findViewById(R.id.divider);
        }
    }
}
