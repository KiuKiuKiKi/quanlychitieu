package com.example.qlct.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qlct.R;
import com.example.qlct.model.Item;

import java.util.List;

public class SubAdapter extends RecyclerView.Adapter<SubAdapter.ViewHolder> {

    private List<Item> subList;
    private Context context;

    private SubAdapter.OnItemClick onItemClick;

    public void setOnItemClick(SubAdapter.OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public SubAdapter(Context context, List<Item> subList) {
        this.subList = subList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_add_sub, viewGroup, false);
        return new ViewHolder(itemView, 0);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        Item item = subList.get(i);
        //todo set Image Bill
        viewHolder.txtTopic.setText(item.getTopic());
        viewHolder.txtName.setText(item.getName());
        viewHolder.txtAmount.setText(item.getAmount()+"");
        viewHolder.txtTime.setText(item.getTime());

        if(item.getUrl() != "" && item.getUrl() != null){
            Uri uri = Uri.parse(item.getUrl());
            viewHolder.imgBill.setImageURI(uri);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClick.onItemClick(i);
            }
        });

        viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClick.onDelete(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return subList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgBill;
        TextView txtTopic;
        TextView txtName;
        TextView txtAmount;
        TextView txtTime;
        Button btnDelete;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            imgBill = itemView.findViewById(R.id.img_bill);
            txtTopic = itemView.findViewById(R.id.txt_topic);
            txtName = itemView.findViewById(R.id.txt_name);
            txtAmount = itemView.findViewById(R.id.txt_amount);
            txtTime = itemView.findViewById(R.id.txt_time);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }

    public interface OnItemClick{
        void onItemClick(int pos);
        void onDelete(int pos);
    }

}
