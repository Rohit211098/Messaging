package com.example.messaging;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatCoustomViewHolder> {

    List<ChatModel> chatModels;
    Context context;
    View view;
    Boolean flag = true;
    int p ;


    public ChatAdapter(List<ChatModel> chatModels, Context context) {
        this.chatModels = chatModels;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatCoustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_recycler,parent,false);

        return new ChatCoustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatCoustomViewHolder holder, int position) {

        holder.message.setText(chatModels.get(position).getMessage());

        Log.e("q","========================="+position);
        p=position;
//        if (position == 1){
//            holder.date.setText(chatModels.get(position).getDate());
//            holder.linearlayoutDate.setVisibility(View.VISIBLE);
//            Log.e("q","position 0");
//            flag =false;
//        }else {
//
//
//
//
//        }


        if(position >0)
            if (!chatModels.get(position-1).getDate().matches(chatModels.get(position).getDate())){
                holder.date.setText(chatModels.get(position).getDate());
                holder.linearlayoutDate.setVisibility(View.VISIBLE);
                Log.e("q","position1 "+chatModels.get(position-1).getDate());
                Log.e("q","position2 "+chatModels.get(position).getDate());
            }

        if (chatModels.get(position).getCurrentUser()){
            holder.linearLayout.setGravity(Gravity.END);
            holder.linearLayoutParent.setGravity(Gravity.END);
            holder.time.setTextColor(Color.parseColor("#404040"));
            holder.message.setTextColor(Color.parseColor("#404040"));
            holder.linearLayoutInside.setBackground(view.getResources().getDrawable(R.drawable.rounded_rectangle_user));
//            holder.linearLayout.setBackgroundColor(Color.parseColor("#F4F4F4"));
        }else {
            holder.linearLayout.setGravity(Gravity.START);
            holder.linearLayoutParent.setGravity(Gravity.START);
            holder.time.setGravity(Gravity.LEFT);
            holder.time.setTextColor(Color.parseColor("#FFFFFF"));
            holder.message.setTextColor(Color.parseColor("#FFFFFF"));
            holder.linearLayoutInside.setBackground(view.getResources().getDrawable(R.drawable.rounded_rectangle_another_user));
//            holder.linearLayout.setBackgroundColor(Color.parseColor("#2DB4C8"));
        }


        holder.time.setText(chatModels.get(position).getTime());


    }

    @Override
    public int getItemCount() {
        return chatModels.size();
    }

    protected class ChatCoustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView message,date,time;
        private LinearLayout linearLayout,linearLayoutParent,linearlayoutDate,linearLayoutInside;

        public ChatCoustomViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            date = itemView.findViewById(R.id.show_date);
            message = itemView.findViewById(R.id.message);
            linearLayout = itemView.findViewById(R.id.container);
            linearLayoutParent = itemView.findViewById(R.id.container_parent);
            linearlayoutDate = itemView.findViewById(R.id.date);
            linearLayoutInside = itemView.findViewById(R.id.inside_content);
            time= itemView.findViewById(R.id.time);


        }

        @Override
        public void onClick(View v) {

            Toast.makeText(context,"position "+p,Toast.LENGTH_SHORT).show();

        }
    }
}
