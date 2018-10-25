package com.practicaleducationnetwork.penapp.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.practicaleducationnetwork.penapp.R;
import com.practicaleducationnetwork.penapp.services.PenService;

/**
 * Variable names starting with 'c' denote class variables
 * while those starting with 'm' denote variables peculiar to methods
 */

public class MainActivity extends AppCompatActivity {

    View.OnClickListener cAgreeButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent mNextActivity = new Intent(MainActivity.this, SignUpActivity.class);
            startActivity(mNextActivity);
        }
    };
    View.OnClickListener cTandClistener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            RelativeLayout mWholeLayout = findViewById(R.id.wholeLayout);
            ScrollView mTermsInFull = findViewById(R.id.termsInFull);
            TextView mTandC = findViewById(R.id.termsAndConditions);

            mTandC.setVisibility(View.GONE);
            mTermsInFull.setVisibility(View.VISIBLE);
            mWholeLayout.setBackgroundColor(Color.WHITE);
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView mTandC = findViewById(R.id.termsAndConditions);
        Button mAgreeButton = findViewById(R.id.agreeAndContinue);
        mTandC.setOnClickListener(cTandClistener);
        mAgreeButton.setOnClickListener(cAgreeButtonListener);
    }

}
