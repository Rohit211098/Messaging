package com.example.messaging;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.NewConversationViewHolder> {

    List<UsersModel> usersModels;
    List<ChatModel> chatModels;
    Context context;



    public MainAdapter(List<UsersModel> usersModels, Context context,List<ChatModel> chatModels) {
        this.usersModels = usersModels;
        this.context = context;
        this.chatModels =chatModels;
    }

    @NonNull
    @Override
    public NewConversationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_recycle,parent,false);


        return new NewConversationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewConversationViewHolder holder, int position) {
        if (usersModels.size() != 0){
            holder.name.setText(usersModels.get(position).getName());
            Glide.with(context).load(usersModels.get(position).getImageURL()).into(holder.profileImage);
        }

        if (chatModels.size()!=0 && chatModels.size() > position){
            holder.lastMessage.setText(chatModels.get(position).message);
            holder.time.setText(chatModels.get(position).getTime());
        }


    }

    @Override
    public int getItemCount() {
        return usersModels.size();
    }


    protected class NewConversationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private CircleImageView profileImage;
        private TextView name,time,lastMessage;


        public NewConversationViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.main_profile_image);
            name = itemView.findViewById(R.id.main_name);
            time = itemView.findViewById(R.id.main_last_message_time);
            lastMessage = itemView.findViewById(R.id.main_last_message);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent( v.getContext(),ChatActivity.class);
            Bundle bundle= new Bundle();
            bundle.putString("otherUser",usersModels.get(getAdapterPosition()).getUserId());
            bundle.putString("image",usersModels.get(getAdapterPosition()).getImageURL());
            bundle.putString("name",usersModels.get(getAdapterPosition()).getName());
            intent.putExtras(bundle);
            v.getContext().startActivity(intent);

        }
    }

}
