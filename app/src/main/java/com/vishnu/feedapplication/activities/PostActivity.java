package com.vishnu.feedapplication.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vishnu.feedapplication.models._Post;
import com.vishnu.feedapplication.R;
import com.vishnu.feedapplication.utils.Constants;
import com.vishnu.feedapplication.utils.Helper;

public class PostActivity extends AppCompatActivity
{
    private Context context;
    private _Post post;
    private TextView title;
    private EditText content;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = this;
        title = findViewById(R.id.title);
        content = findViewById(R.id.content);
        post = (_Post) getIntent().getSerializableExtra("post");
        if(post != null) {
            title.setText("Update Post");
            content.setText(post.getMessage());
        } else
            title.setText("Create Post");
        findViewById(R.id.send).setOnClickListener(v -> {
            if(post != null)
                updatePost();
            else
                createPost();
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    public void createPost() {
        String msg = content.getText().toString().trim();
        if(TextUtils.isEmpty(msg))
            Helper.showErrorDialog(context, "Content cannot be empty");
        else if(!Helper.isNetworkAvailable(context))
            Helper.showToast(context, Constants.noNetwork);
        else {
            Helper.showProgressDialog(context);
            reference = FirebaseDatabase.getInstance().getReference().child("posts");
            reference.push().setValue(new _Post(context, msg, false))
                    .addOnCompleteListener(task -> {
                        Helper.dismissProgressDialog();
                        if(task.isSuccessful()) {
                            content.setText("");
                            Helper.showSuccessDialog(context, "Posted successfully");
                        } else
                            Helper.showErrorDialog(context, "Failed to post your content. Please try again..");
                    });
        }
    }

    public void updatePost() {
        String msg = content.getText().toString().trim();
        if(TextUtils.isEmpty(msg))
            Helper.showErrorDialog(context, "Content cannot be empty");
        else if(!Helper.isNetworkAvailable(context))
            Helper.showToast(context, Constants.noNetwork);
        else {
            Helper.showProgressDialog(context);
            reference = FirebaseDatabase.getInstance().getReference().child("posts").child(post.getPost_id());
            reference.setValue(new _Post(context, msg, true))
                    .addOnCompleteListener(task -> {
                        Helper.dismissProgressDialog();
                        if(task.isSuccessful()) {
                            Helper.showSuccessDialog(context, "Updated successfully");
                        } else
                            Helper.showErrorDialog(context, "Failed to update your post. Please try again..");
                    });
        }
    }

}
