package com.project.translator;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.DataViewHolder> {
    private Context context;
    private List<String> chats,chattimes;
    private List<Boolean> chatside;

    public ChatMessageAdapter(Context context, List<String> chats, List<String> chattimes, List<Boolean> chatside) {
        this.context = context;
        this.chats = chats;
        this.chattimes = chattimes;
        this.chatside = chatside;
    }

    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.sendermessage,parent,false);
        return  new DataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {
        if(chatside.get(position)==true)
            holder.layout.setGravity(Gravity.RIGHT);
        else
            holder.layout.setGravity(Gravity.LEFT);
        final String singleChat = chats.get(position);
        final String singleChatTime = chattimes.get(position);
        holder.textView.setText(singleChat);
        holder.timeView.setText(singleChatTime);
//        Toast.makeText(context,"http://springspree.in/media/"+dataCard.getFields().getLogo() , Toast.LENGTH_SHORT).show();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context, dataCard.getFields().getName()+" is clicked", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(context,DetailsActivity.class).putExtra("Data",dataCard.getFields());
//                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public class DataViewHolder extends RecyclerView.ViewHolder
    {
        TextView textView,timeView;
        LinearLayout layout;
        public DataViewHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.textMessage);
            timeView=itemView.findViewById(R.id.timestamp);
            layout = itemView.findViewById(R.id.layout);
        }
    }
}
