package com.vishnu.feedapplication.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.vishnu.feedapplication.R;

public class LoginActivity extends AppCompatActivity
{
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;
        TextView title = findViewById(R.id.title);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/keepcalm-medium.ttf");
        title.setTypeface(typeface);
    }

    public void onLoginClick(View view)
    {
        switch (view.getId())
        {
            case R.id.frgt2:
                startActivity(new Intent(context, ForgotPassword.class));
                break;
            case R.id.signup2:
                startActivity(new Intent(context, Registration.class));
                break;
        }
    }

}
