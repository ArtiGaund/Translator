package com.project.translator;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.Serializable;
import java.util.List;

public class MainListAdapter extends RecyclerView.Adapter<MainListAdapter.DataViewHolder> {

    private List<User> userList;
    Context context;

    public MainListAdapter(List<User> userList, Context context) {
        this.userList = userList;
        this.context = context;
    }

    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.usercard,parent,false);
        return  new MainListAdapter.DataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {
        User curr = userList.get(position);
        holder.textView.setText(curr.getName());
        Glide.with(context).load(Uri.parse(curr.getPhotoUrL())).into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,MainActivity.class);
                intent.putExtra("Currentuser", (Serializable) curr);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class DataViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;
        public DataViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.username);
            imageView = itemView.findViewById(R.id.userdp);
        }
    }
}
