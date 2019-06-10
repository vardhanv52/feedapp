package com.vishnu.feedapplication.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.vishnu.feedapplication.R;

public class SplashScreen extends AppCompatActivity
{
    private Context context;
    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        context = this;
        TextView textView = findViewById(R.id.app_name);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/keepcalm-medium.ttf");
        textView.setTypeface(typeface);
        if(auth.getCurrentUser() != null)
            startTransition(true);
        else
            startTransition(false);
    }

    void startTransition(boolean isLoggedIn) {
        new Handler().postDelayed(() -> {
            if(isLoggedIn)
                startActivity(new Intent(context, MainActivity.class));
            else
                startActivity(new Intent(context, LoginActivity.class));
            finish();
        }, 1500);
    }

}
