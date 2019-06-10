package com.vishnu.feedapplication.activities;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.vishnu.feedapplication.R;

public class Registration extends AppCompatActivity
{
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        context = this;
        TextView title = findViewById(R.id.title);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/keepcalm-medium.ttf");
        title.setTypeface(typeface);
    }

    public void onRegistrationClick(View view)
    {
        switch (view.getId())
        {
            case R.id.login2:
                finish();
                break;
        }
    }
}
