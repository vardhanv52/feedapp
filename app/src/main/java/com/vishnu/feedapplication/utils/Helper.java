package com.vishnu.feedapplication.utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.vishnu.feedapplication.R;

public class Helper
{
    private static ProgressDialog progressDialog;
    private static Toast toast;

    public static void showToast(Context context, String msg){
        try{
            if(toast != null)
                toast.cancel();
            toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
            toast.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static void showProgressDialog(Context context){
        try{
            dismissProgressDialog();
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void dismissProgressDialog(){
        if(progressDialog != null)
            progressDialog.dismiss();
    }

    public static void storeUserData(Context context, String key, String value) {
        if (key != null && value != null) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.userPreferencesName, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(key, value);
            editor.apply();
        }
    }

    public static String getUserData(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.userPreferencesName, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }

    public static void clearUserData(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.userPreferencesName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public static void storeAppData(Context context, String key, String value) {
        if (key != null && value != null) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.appPreferencesName, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(key, value);
            editor.apply();
        }
    }

    public static String getAppData(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.appPreferencesName, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }

    public static void showErrorDialog(Context context, String message){
        try{
            Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.dialog_error);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogScaling50;
            dialog.setCancelable(false);
            TextView msg = dialog.findViewById(R.id.message);
            msg.setText(message);
            dialog.findViewById(R.id.ok).setOnClickListener(view -> dialog.dismiss());
            dialog.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void showSuccessDialog(Context context, String message){
        try{
            Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.dialog_success);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogScaling50;
            dialog.setCancelable(false);
            TextView msg = dialog.findViewById(R.id.message);
            msg.setText(message);
            dialog.findViewById(R.id.ok).setOnClickListener(view -> dialog.dismiss());
            dialog.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
