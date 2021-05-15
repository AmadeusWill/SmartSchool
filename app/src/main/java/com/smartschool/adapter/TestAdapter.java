package com.smartschool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smartschool.R;
import com.smartschool.bean.TestDataBean;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.ViewHolder>{
    Context context;
    List<TestDataBean> testList;
    public TestAdapter(Context context, List<TestDataBean> testList){
        this.context=context;
        this.testList=testList;
    }
    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.test_recycler_layout,parent,false);
        final ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.courseName.setText(testList.get(position).getCourseName());
        holder.xf.setText(testList.get(position).getXf());
        holder.place.setText(testList.get(position).getPlace());
        holder.time.setText(testList.get(position).getTime());
        if(position==testList.size()-1){
            holder.divider.setVisibility(View.GONE);
        }else {
            holder.divider.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return testList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView courseName;
        TextView xf;
        TextView place;
        TextView time;
        View divider;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            courseName=(TextView) itemView.findViewById(R.id.course_name);
            xf=(TextView) itemView.findViewById(R.id.xf);
            place=(TextView) itemView.findViewById(R.id.test_place);
            time=(TextView) itemView.findViewById(R.id.test_time);
            divider=(View) itemView.findViewById(R.id.divider);
        }
    }
}
