package com.practicaleducationnetwork.penapp.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.practicaleducationnetwork.penapp.R;

public class Utils {
    public static ProgressDialog progressDialog;
//    public static SharedPreferences sharedPreferences;
//    public static final String PREFS = "PENAPP";
//
//
//    public static SharedPreferences getSharedPreferences(){
//        sharedPreferences = getSharedPreferences();
//    }

    public static void hideProgressDialog(Activity activity){
        if(progressDialog != null && !activity.isFinishing()){
            progressDialog.dismiss();
        }
        progressDialog = null;
    }

    public static void showProgressDialog(Context context,boolean cancelable){
        if(progressDialog == null){
            progressDialog = new ProgressDialog(context);
        }else{
            progressDialog.dismiss();
            progressDialog = new ProgressDialog(context);
        }
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setIndeterminateDrawable(context.getResources().getDrawable(R.drawable.custom_dialog));
        progressDialog.setCancelable(cancelable);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        progressDialog.setContentView(R.layout.layout_custom_dialog);
    }

    public static void shortToast(Context context,String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void longToast(Context context,String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }


}
