package com.vishnu.feedapplication.activities;

import android.content.Context;
import android.content.Intent;
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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PostActivity extends AppCompatActivity
{
    private Context context;
    private _Post post;
    private EditText content;
    private DatabaseReference reference;
    private int UPDATE_POST = 500, ADD_POST = 1000;
    private boolean isAnythingChanged;
    private String updatedMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = this;
        TextView title = findViewById(R.id.title);
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
        if(isAnythingChanged) {
            if(post == null)
                setResult(ADD_POST);
            else {
                Intent intent = new Intent();
                intent.putExtra("msg", updatedMsg);
                setResult(UPDATE_POST, intent);
            }
        }
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if(isAnythingChanged) {
            if(post == null)
                setResult(ADD_POST);
            else {
                Intent intent = new Intent();
                intent.putExtra("msg", updatedMsg);
                setResult(UPDATE_POST, intent);
            }
        }
        finish();
        super.onBackPressed();
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
            reference.push().setValue(new _Post(context, msg))
                    .addOnCompleteListener(task -> {
                        Helper.dismissProgressDialog();
                        if(task.isSuccessful()) {
                            isAnythingChanged = true;
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
            Map<String, Object> map = new HashMap<>();
            map.put("message", msg);
            map.put("updatedAt", Constants.format.format(new Date()));
            reference.updateChildren(map)
                    .addOnCompleteListener(task -> {
                        Helper.dismissProgressDialog();
                        if(task.isSuccessful()) {
                            isAnythingChanged = true;
                            updatedMsg = msg;
                            Helper.showSuccessDialog(context, "Updated successfully");
                        } else
                            Helper.showErrorDialog(context, "Failed to update your post. Please try again..");
                    });
        }
    }

}
