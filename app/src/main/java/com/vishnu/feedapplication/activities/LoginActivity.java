package com.vishnu.feedapplication.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.vishnu.feedapplication.R;
import com.vishnu.feedapplication.utils.Constants;
import com.vishnu.feedapplication.utils.Helper;

public class LoginActivity extends AppCompatActivity
{
    private Context context;
    private EditText email, pwd;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    private CheckBox rememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;
        TextView title = findViewById(R.id.title);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/keepcalm-medium.ttf");
        title.setTypeface(typeface);
        binding();
        if(Helper.getAppData(context, Constants.keyCredentials).equals(Constants.valTrue)) {
            rememberMe.setChecked(true);
            email.setText(Helper.getAppData(context, Constants.keyCredEmail));
            pwd.setText(Helper.getAppData(context,Constants.keyCredPwd));
        }
    }

    void binding() {
        email = findViewById(R.id.email);
        pwd = findViewById(R.id.password);
        rememberMe = findViewById(R.id.remember_me);
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
            case R.id.login_btn:
                login();
                break;
        }
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

    void login() {
        String strEmail, strPassword;
        strEmail = email.getText().toString().trim();
        strPassword = pwd.getText().toString().trim();
        if(TextUtils.isEmpty(strEmail))
            Helper.showErrorDialog(context, "Please enter your email address");
        else if(TextUtils.isEmpty(strPassword))
            Helper.showErrorDialog(context, "Please enter your password");
        else if(!Helper.isNetworkAvailable(context))
            Helper.showToast(context, Constants.noNetwork);
        else {
            Helper.showProgressDialog(context);
            auth.signInWithEmailAndPassword(strEmail, strPassword)
                    .addOnCompleteListener(task -> {
                        Helper.dismissProgressDialog();
                        if(task.isSuccessful()) {
                            String val = auth.getCurrentUser().getDisplayName();
                            if(val == null)
                                val = strEmail;
                            Helper.storeUserData(context, Constants.keyDisplayName, val);
                            Helper.storeUserData(context, Constants.keyEmail, strEmail);

                            if(rememberMe.isChecked()) {
                                Helper.storeAppData(context, Constants.keyCredentials, Constants.valTrue);
                                Helper.storeAppData(context, Constants.keyCredEmail, strEmail);
                                Helper.storeAppData(context, Constants.keyCredPwd, strPassword);
                            } else
                                Helper.storeAppData(context, Constants.keyCredentials, Constants.valFalse);

                            startActivity(new Intent(context, MainActivity.class));
                            finish();
                        } else {
                            Helper.showErrorDialog(context, "Incorrect Credentials");
                        }
                    });
        }
    }

}
