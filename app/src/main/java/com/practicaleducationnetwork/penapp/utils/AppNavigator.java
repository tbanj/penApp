package com.practicaleducationnetwork.penapp.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.practicaleducationnetwork.penapp.activities.FeedActivity;
import com.practicaleducationnetwork.penapp.activities.MainActivity;
import com.practicaleducationnetwork.penapp.activities.SignInActivity;

public class AppNavigator {
    private final Activity activity;
    private Bundle bundle;

    public AppNavigator(Activity activity) {
        this.activity = activity;
    }

    public AppNavigator(Activity activity, Bundle bundle) {
        this.activity = activity;
        this.bundle = bundle;
    }

    public void navigateToSignUpFlow(){
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(0,0);
    }

    public void navigateToSignInActivity(){
        Intent intent = new Intent(activity, SignInActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(0,0);
    }

    public void navigateToFeedActivity(){
        Intent intent = new Intent(activity, FeedActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(0,0);
    }
}
