package com.vishnu.feedapplication.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.vishnu.feedapplication.R;
import com.vishnu.feedapplication.utils.Constants;
import com.vishnu.feedapplication.utils.Helper;

public class MainActivity extends AppCompatActivity
{
    private Context context;
    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        findViewById(R.id.createPost).setOnClickListener(v -> {
            startActivity(new Intent(context, PostActivity.class));
        });
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

}
