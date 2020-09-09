package com.application.airnotes;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.application.airnotes.Data.UserNotes;

import java.util.ArrayList;

public class main_screen_adapter extends RecyclerView.Adapter<main_screen_adapter.myholder>{
    Context context;
    ArrayList<UserNotes> arrayList = new ArrayList<>();

    public main_screen_adapter(Context context, ArrayList<UserNotes> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(context).inflate(R.layout.adapter_for_recyclerview, parent, false);
        myholder holder = new myholder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull myholder holder, int position) {
        UserNotes userNotes = arrayList.get(position);
        holder.title.setText(userNotes.getNoteTitle());
        holder.desc.setText(userNotes.getNoteDesc());
        holder.date.setText(userNotes.getNoteDate());
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class myholder extends ViewHolder {

        TextView title,desc,date;
        ImageView edit,delete;
        public myholder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title_received);
            desc = itemView.findViewById(R.id.desc_received);
            date = itemView.findViewById(R.id.date_received);
            edit = itemView.findViewById(R.id.edit_image);
            delete = itemView.findViewById(R.id.delete_image);
        }
    }
}
