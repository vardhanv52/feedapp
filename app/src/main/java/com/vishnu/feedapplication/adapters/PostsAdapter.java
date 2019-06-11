package com.vishnu.feedapplication.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.vishnu.feedapplication.R;
import com.vishnu.feedapplication.activities.MainActivity;
import com.vishnu.feedapplication.activities.PostActivity;
import com.vishnu.feedapplication.models._Post;
import com.vishnu.feedapplication.utils.Constants;

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostsViewHolder>
{
    private Context context;
    private List<_Post> posts;
    private String Uid;
    private MainActivity mainActivity;

    public PostsAdapter(Context context, List<_Post> posts, MainActivity mainActivity) {
        this.context = context;
        this.posts = posts;
        Uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public PostsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new PostsViewHolder(LayoutInflater.from(context).inflate(R.layout.item_post, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PostsViewHolder holder, int i) {
        _Post post = posts.get(holder.getAdapterPosition());
        holder.name.setText(post.getName());
        holder.time.setText(post.getCreatedAT());
        holder.msg.setText(post.getMessage());
        if(Uid.equals(post.getId())) {
            holder.edit.setVisibility(View.VISIBLE);
            holder.delete.setVisibility(View.VISIBLE);
        } else {
            holder.edit.setVisibility(View.GONE);
            holder.delete.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    class PostsViewHolder extends RecyclerView.ViewHolder
    {
        TextView name, time, msg;
        ImageView edit, delete;
        PostsViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            time = itemView.findViewById(R.id.time);
            msg = itemView.findViewById(R.id.content);
            edit = itemView.findViewById(R.id.edit);
            delete = itemView.findViewById(R.id.delete);
            edit.setOnClickListener(v -> {
                mainActivity.onEditClicked(getAdapterPosition());
            });
            delete.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(Constants.confirm);
                builder.setMessage("Are you sure you want to delete this post?");
                builder.setCancelable(false);
                builder.setPositiveButton("YES", (dialog, which) -> {
                    mainActivity.onDeleteClicked(getAdapterPosition());
                });
                builder.setNegativeButton("NO", (dialog, which) -> { });
                builder.show();
            });
        }
    }
}
