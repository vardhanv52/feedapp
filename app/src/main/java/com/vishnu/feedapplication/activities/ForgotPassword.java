package com.vishnu.feedapplication.activities;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.vishnu.feedapplication.R;
import com.vishnu.feedapplication.utils.Constants;
import com.vishnu.feedapplication.utils.Helper;

public class ForgotPassword extends AppCompatActivity
{
    private Context context;
    private EditText email;
    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        context = this;
        TextView title = findViewById(R.id.title);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/keepcalm-medium.ttf");
        title.setTypeface(typeface);
        email = findViewById(R.id.email);
    }

    public void forgotPwdClick(View view)
    {
        switch (view.getId())
        {
            case R.id.back:
                finish();
                break;
            case R.id.reset_pwd:
                resetPassword();
                break;
        }
    }

    void resetPassword() {
        String strEmail = email.getText().toString().trim();
        if(TextUtils.isEmpty(strEmail))
            Helper.showErrorDialog(context, "Please enter your email address");
        else if(!Patterns.EMAIL_ADDRESS.matcher(strEmail).matches())
            Helper.showErrorDialog(context, "Please check your email address once");
        else if(!Helper.isNetworkAvailable(context))
            Helper.showToast(context, Constants.noNetwork);
        else {
            Helper.showProgressDialog(context);
            auth.sendPasswordResetEmail(strEmail)
                    .addOnCompleteListener(task -> {
                        Helper.dismissProgressDialog();
                        if(task.isSuccessful()) {
                            email.setText("");
                            Helper.showSuccessDialog(context, "An email has been successfully sent to your email address.");
                        } else
                            Helper.showErrorDialog(context, "This email address is not registered with us");
                    });
        }
    }

}
