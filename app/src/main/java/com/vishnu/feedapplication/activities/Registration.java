package com.vishnu.feedapplication.activities;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.vishnu.feedapplication.R;
import com.vishnu.feedapplication.utils.Constants;
import com.vishnu.feedapplication.utils.Helper;

public class Registration extends AppCompatActivity
{
    private Context context;
    private EditText name, email, pwd, pwd2;
    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        context = this;
        TextView title = findViewById(R.id.title);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/keepcalm-medium.ttf");
        title.setTypeface(typeface);
        binding();
    }

    void binding() {
        name = findViewById(R.id.full_name);
        email = findViewById(R.id.email);
        pwd = findViewById(R.id.password1);
        pwd2 = findViewById(R.id.password2);
    }

    public void onRegistrationClick(View view)
    {
        switch (view.getId())
        {
            case R.id.login2:
                finish();
                break;
            case R.id.register:
                register();
                break;
        }
    }

    public void register()
    {
        String strName, strEmail, strPwd, strPwd2;
        strName = name.getText().toString().trim();
        strEmail = email.getText().toString().trim();
        strPwd = pwd.getText().toString().trim();
        strPwd2 = pwd2.getText().toString().trim();
        if(TextUtils.isEmpty(strName))
            Helper.showErrorDialog(context, "Please enter your full name");
        else if(TextUtils.isEmpty(strEmail))
            Helper.showErrorDialog(context, "Please enter your email address");
        else if(!Patterns.EMAIL_ADDRESS.matcher(strEmail).matches())
            Helper.showErrorDialog(context, "Please check your email address");
        else if(TextUtils.isEmpty(strPwd))
            Helper.showErrorDialog(context, "Please enter you password");
        else if(TextUtils.isEmpty(strPwd2))
            Helper.showErrorDialog(context, "Please enter your confirm password");
        else if(!strPwd.equals(strPwd2))
            Helper.showErrorDialog(context, "Passwords are not identical");
        else if(!Helper.isNetworkAvailable(context))
            Helper.showToast(context, Constants.noNetwork);
        else {
            Helper.showProgressDialog(context);
            try{
                auth.createUserWithEmailAndPassword(strEmail, strPwd)
                        .addOnCompleteListener(task -> {
                            if(task.isSuccessful()) {
                                FirebaseUser user = auth.getCurrentUser();
                                UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(strName)
                                        .build();
                                user.updateProfile(request)
                                        .addOnCompleteListener(task1 ->
                                        {
                                            Helper.dismissProgressDialog();
                                            name.setText("");
                                            email.setText("");
                                            pwd.setText("");
                                            pwd2.setText("");
                                            showSuccessDialog("Successfully registered");
                                        });
                            } else {
                                Helper.dismissProgressDialog();
                                try{
                                    throw task.getException();
                                }catch (FirebaseAuthUserCollisionException e) {
                                    Helper.showErrorDialog(context, "This Email address is already in use...");
                                }catch (Exception e){
                                    Helper.showErrorDialog(context, "Registration failed. Please try again...");
                                }
                            }
                        });
            }catch (Exception e){
                e.printStackTrace();
                Helper.showErrorDialog(context, "Registration failed. Please try again...");
            }
        }
    }

    public void showSuccessDialog(String message) {
        try {
            Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.dialog_success);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogScaling50;
            dialog.setCancelable(false);
            TextView msg = dialog.findViewById(R.id.message);
            msg.setText(message);
            dialog.findViewById(R.id.ok).setOnClickListener(view -> {
                dialog.dismiss();
                auth.signOut();
                new Handler().postDelayed(this::finish, 400);
            });
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
