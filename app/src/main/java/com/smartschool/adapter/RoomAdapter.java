package com.smartschool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smartschool.R;
import com.smartschool.bean.RoomDataBean;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.ViewHolder>{
    Context context;
    List<RoomDataBean> roomList;
    public RoomAdapter(Context context,List<RoomDataBean> roomList){
        this.context=context;
        this.roomList=roomList;
    }
    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.room_recycler_layout,parent,false);
        final ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        final String area=roomList.get(position).getArea();
        final String place=area+"-"+roomList.get(position).getPlace();
        holder.placeTv.setText(place);
        holder.timeTv.setText(roomList.get(position).getTime());
        holder.seatTv.setText(roomList.get(position).getSeatCount());
        holder.tipTv.setText(roomList.get(position).getTip());
        if(position==roomList.size()-1){
            holder.divider.setVisibility(View.GONE);
        }else {
            holder.divider.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView placeTv;
        TextView timeTv;
        TextView seatTv;
        TextView tipTv;
        View divider;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            placeTv=(TextView) itemView.findViewById(R.id.room_place_tv);
            timeTv=(TextView) itemView.findViewById(R.id.room_time_tv);
            seatTv=(TextView) itemView.findViewById(R.id.room_seat_tv);
            tipTv=(TextView) itemView.findViewById(R.id.room_tip_tv);
            divider=(View) itemView.findViewById(R.id.divider);
        }
    }
}
