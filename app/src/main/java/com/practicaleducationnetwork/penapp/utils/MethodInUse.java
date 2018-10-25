package com.practicaleducationnetwork.penapp.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MethodInUse {
    //This Method is use to check if EditText when is in Focus Mode
    // which is when user is typing whether total input is below a particular string length
    // e.g  EditText<2 or EditText =-1 or EditText is Empty
    public void editLengthCheckF(EditText idEditTextt, String nameEx, int editCount){
        if(idEditTextt.getText().toString().trim()
                .length() <editCount || idEditTextt.getText().toString().trim()
                .length() >=1){
            idEditTextt.setError(nameEx+ " is required");
        }else if(idEditTextt.getText().toString().trim()
                .length() >=editCount ){
            idEditTextt.setError(null);
        }else{
            idEditTextt.setError(null);
        }

    }

    //This Method is use to check if EditText when is in Out of Focus Mode
    // which is when user is in another View .It check whether total input is below a particular string length
    // e.g  EditText<2
    public void editLengthCheck(EditText idEditText, String nameEx, int editCount){
        if(idEditText.getText().toString().trim()
                .length() < editCount){
            idEditText.setError(nameEx+ " is required");


        }
        else{
            idEditText.setError(null);
        }
    }

    //this is use to test & determine if there is no string inputted to EditText
    public boolean emptyTextEdit(EditText emptyEdit, String emptyTex){
        boolean matchText=true;
        if(TextUtils.isEmpty(emptyEdit.getText().toString().trim())){
            //emptyEdit.setError("Invalid "+emptyTex);
            //Make use of toastChangeL Method for toast messages
            //toastChangeL("Invalid "+emptyTex);
            //emptyToast.makeText(SignUpActivity.this,"Invalid "+emptyTex,Toast.LENGTH_LONG).show();


            matchText=false;
        }
        return  matchText;
    }

    //Toast Message Method
    public void toastChangeL(Context context, String changeToastMessage){
        Toast toastPrivate= null;
        //for context variable input the java file name you are currently working on
        //changeToastMessage you will input text you want it to display
        Toast.makeText(context, changeToastMessage, Toast.LENGTH_LONG).show();

    }

    //for encrypting password with salt
    public String get_SHA_512_SecurePassword(String passwordToHash, String salt){
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt.getBytes(StandardCharsets.UTF_8));
            byte[] bytes = md.digest(passwordToHash.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++){
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return generatedPassword;
    }
}
