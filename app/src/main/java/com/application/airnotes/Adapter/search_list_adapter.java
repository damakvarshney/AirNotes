package com.application.airnotes.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.application.airnotes.main_screen;

import java.util.ArrayList;
import java.util.List;

public class search_list_adapter extends ArrayAdapter<String> {

    private main_screen activity;
    private List<String> friendList;
    private List<String> searchList;

    public search_list_adapter(@NonNull main_screen context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
        this.activity = context;
        this.friendList = objects;
        this.searchList = new ArrayList<>();
        this.searchList.addAll(friendList);
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return friendList.size();
    }
    public void filter(String charText){

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }
}

