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
import java.util.zip.Inflater;

import de.hdodenhof.circleimageview.CircleImageView;

public class NewConversationAdapter extends RecyclerView.Adapter<NewConversationAdapter.NewConversationViewHolder> {

    List<UsersModel> usersModels;
    Context context;



    public NewConversationAdapter(List<UsersModel> usersModels, Context context) {
        this.usersModels = usersModels;
        this.context = context;
    }

    @NonNull
    @Override
    public NewConversationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_conversation_recycle,parent,false);


        return new NewConversationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewConversationViewHolder holder, int position) {
        holder.name.setText(usersModels.get(position).getName());
        Glide.with(context).load(usersModels.get(position).getImageURL()).into(holder.profileImage);
    }

    @Override
    public int getItemCount() {
        return usersModels.size();
    }


    protected class NewConversationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private CircleImageView profileImage;
        private TextView name;


        public NewConversationViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.new_conversation_profile_image);
            name = itemView.findViewById(R.id.new_conversation_name);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent( v.getContext(),ChatActivity.class);
            Bundle bundle= new Bundle();
            bundle.putString("otherUser",usersModels.get(getAdapterPosition()).getUserId());
            intent.putExtras(bundle);
            v.getContext().startActivity(intent);

        }
    }

}
