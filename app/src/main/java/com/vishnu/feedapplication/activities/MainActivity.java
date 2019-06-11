package com.vishnu.feedapplication.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.vishnu.feedapplication.R;
import com.vishnu.feedapplication.adapters.PostsAdapter;
import com.vishnu.feedapplication.models._Post;
import com.vishnu.feedapplication.utils.Constants;
import com.vishnu.feedapplication.utils.Helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    private Context context;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private DatabaseReference reference;
    private RecyclerView recyclerView;
    private List<_Post> posts = new ArrayList<>();
    private List<_Post> postsTemp = new ArrayList<>();
    private PostsAdapter adapter;
    private String lastKey;
    private ProgressBar progressBar;
    private TextView noPosts, user_name, user_email;
    private boolean isLoading, noMorePosts;
    private int UPDATE_POST = 500, ADD_POST = 1000, currentEditingPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        findViewById(R.id.createPost).setOnClickListener(v -> startActivityForResult(new Intent(context, PostActivity.class), ADD_POST));
        binding();
        user_name.setText(auth.getCurrentUser().getDisplayName());
        user_email.setText(auth.getCurrentUser().getEmail());
        LinearLayoutManager manager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        adapter = new PostsAdapter(context, posts, this);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int lastItem = manager.findLastCompletelyVisibleItemPosition();
                int totalItemCount = manager.getItemCount();

                if(dy > 0) {
                    if(!isLoading) {
                        if (lastItem == totalItemCount - 1) {
                            if(noMorePosts) {
                                Helper.showToast(context, "You have reached the end...");
                                return;
                            }
                            progressBar.setVisibility(View.VISIBLE);
                            loadMoreContent();
                        }
                    }
                }
            }
        });
        getInitialData();
    }

    void binding() {
        progressBar = findViewById(R.id.extra_content);
        recyclerView = findViewById(R.id.posts);
        noPosts = findViewById(R.id.noPosts);
        user_name = findViewById(R.id.user_name);
        user_email = findViewById(R.id.user_email);
    }

    void getInitialData()
    {
        final boolean[] isExecuted = new boolean[1];
        isLoading = true;
        Helper.showProgressDialog(context);
        Query query = FirebaseDatabase.getInstance().getReference().child("posts").orderByKey().limitToLast(Constants.paginationCount + 1);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                _Post post = dataSnapshot.getValue(_Post.class);
                post.setPost_id(dataSnapshot.getKey());
                postsTemp.add(post);
                if(postsTemp.size() == Constants.paginationCount + 1) {
                    isExecuted[0] = true;
                    Helper.dismissProgressDialog();
                    noMorePosts = false;
                    lastKey = postsTemp.get(0).getPost_id();
                    postsTemp.remove(0);
                    Collections.reverse(postsTemp);
                    posts.clear();
                    posts.addAll(postsTemp);
                    postsTemp.clear();
                    isLoading = false;
                    adapter.notifyDataSetChanged();
                    query.removeEventListener(this);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                new Handler().postDelayed(() -> {
                    if(!isExecuted[0]) {
                        Helper.dismissProgressDialog();
                        if(postsTemp.size() != 0) {
                            noMorePosts = true;
                            Collections.reverse(postsTemp);
                            posts.clear();
                            posts.addAll(postsTemp);
                            postsTemp.clear();
                            isLoading = false;
                            adapter.notifyDataSetChanged();
                        }
                        if(posts.size() == 0)
                            noPosts.setVisibility(View.VISIBLE);
                        else
                            noPosts.setVisibility(View.GONE);
                    }
                }, 5000);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    void loadMoreContent()
    {
        if(!isLoading) {
            isLoading = true;
            Query query = FirebaseDatabase.getInstance().getReference().child("posts").orderByKey().endAt(lastKey).limitToLast(Constants.paginationCount + 1);
            query.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    _Post post = dataSnapshot.getValue(_Post.class);
                    post.setPost_id(dataSnapshot.getKey());
                    postsTemp.add(post);
                    if(postsTemp.size() == Constants.paginationCount + 1) {
                        progressBar.setVisibility(View.GONE);
                        lastKey = postsTemp.get(0).getPost_id();
                        postsTemp.remove(0);
                        Collections.reverse(postsTemp);
                        posts.addAll(postsTemp);
                        postsTemp.clear();
                        isLoading = false;
                        adapter.notifyDataSetChanged();
                        query.removeEventListener(this);
                        return;
                    }

                    if(lastKey.equalsIgnoreCase(post.getPost_id())) {
                        progressBar.setVisibility(View.GONE);
                        Collections.reverse(postsTemp);
                        posts.addAll(postsTemp);
                        postsTemp.clear();
                        isLoading = false;
                        noMorePosts = true;
                        adapter.notifyDataSetChanged();
                        query.removeEventListener(this);
                    }

                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menu_logout:
                logout();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(Constants.confirm);
        builder.setMessage(Constants.logout_msg);
        builder.setPositiveButton("YES", (dialog, which) -> {
            Helper.clearUserData(context);
            auth.signOut();
            startActivity(new Intent(context, LoginActivity.class));
            finish();
        });
        builder.setNegativeButton("NO", (dialog, which) -> { });
        builder.show();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(Constants.confirm);
        builder.setMessage(Constants.exit_msg);
        builder.setPositiveButton("YES", (dialog, which) -> finish());
        builder.setNegativeButton("NO", (dialog, which) -> { });
        builder.show();
    }

    public void onEditClicked(int pos) {
        currentEditingPosition = pos;
        Intent intent = new Intent(context, PostActivity.class);
        intent.putExtra("post", posts.get(pos));
        startActivityForResult(intent, UPDATE_POST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == UPDATE_POST && resultCode == UPDATE_POST && data != null) {
            String msg = data.getStringExtra("msg");
            posts.get(currentEditingPosition).setMessage(msg);
            adapter.notifyDataSetChanged();
        } else if (requestCode == ADD_POST && resultCode == ADD_POST){
            getInitialData();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onDeleteClicked(int pos)
    {
        Helper.showProgressDialog(context);
        reference = FirebaseDatabase.getInstance().getReference().child("posts").child(posts.get(pos).getPost_id());
        reference.removeValue().addOnCompleteListener(task -> {
            Helper.dismissProgressDialog();
            if(task.isSuccessful()) {
                Helper.showSuccessDialog(context, "Successfully deleted...");
                posts.remove(pos);
                adapter.notifyDataSetChanged();
            } else
                Helper.showErrorDialog(context, "Failed to delete the post...");
        });
    }

}
