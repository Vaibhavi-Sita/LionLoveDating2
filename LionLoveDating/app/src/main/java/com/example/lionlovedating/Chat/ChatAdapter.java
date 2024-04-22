package com.example.lionlovedating.Chat;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.lionlovedating.R;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatViewHolder> {

    private List<ChatObject> chatList;
    private Context context;


    public ChatAdapter(List<ChatObject> matchesList, Context context){
        this.chatList = matchesList;
        this.context = context;
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        ChatViewHolder rcv = new ChatViewHolder(layoutView);

        return rcv;
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {
        holder.mMessage.setText(chatList.get(position).getMessage());

        if(chatList.get(position).getCurrentUser()){
            holder.mMessage.setGravity(Gravity.END);
            holder.mMessage.setTypeface(Typeface.DEFAULT_BOLD);
            holder.mMessage.setTextColor(Color.parseColor("#FFFFFF"));
            holder.mContainer.setBackgroundColor(Color.parseColor("#1F417C"));
            holder.mContainer.setPadding(30,30,30,30);


        }else{
            holder.mMessage.setGravity(Gravity.START);
            holder.mMessage.setTextColor(Color.parseColor("#1F417C"));
            holder.mMessage.setTypeface(Typeface.DEFAULT_BOLD);
            holder.mContainer.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }

    }

    @Override
    public int getItemCount() {
        return this.chatList.size();
    }
}
