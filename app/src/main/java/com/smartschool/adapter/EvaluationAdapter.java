package com.smartschool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smartschool.R;
import com.smartschool.bean.EvaluationDataBean;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EvaluationAdapter extends RecyclerView.Adapter<EvaluationAdapter.ViewHolder>{
    Context context;
    List<EvaluationDataBean> evaluationList;
    public EvaluationAdapter(Context context,List<EvaluationDataBean> evaluationList){
        this.context=context;
        this.evaluationList=evaluationList;
    }
    @NonNull
    @NotNull
    @Override
    public EvaluationAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.evaluation_recycler_layout,parent,false);
        final ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull EvaluationAdapter.ViewHolder holder, int position) {
        holder.evaluationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        holder.nameTv.setText(evaluationList.get(position).getCourseName());
        holder.typeTv.setText(evaluationList.get(position).getType());
        holder.teacherTv.setText(evaluationList.get(position).getTeacher());
        if(position==evaluationList.size()-1){
            holder.divider.setVisibility(View.GONE);
        }else {
            holder.divider.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return evaluationList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        View evaluationView;
        TextView nameTv;
        TextView typeTv;
        TextView teacherTv;
        View divider;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            evaluationView=(View) itemView.findViewById(R.id.evaluation_view);
            nameTv=(TextView) itemView.findViewById(R.id.evaluation_name_tv);
            typeTv=(TextView) itemView.findViewById(R.id.evaluation_type_tv);
            teacherTv=(TextView) itemView.findViewById(R.id.evaluation_teacher_tv);
            divider=(View) itemView.findViewById(R.id.divider);
        }
    }
}
