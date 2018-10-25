package com.practicaleducationnetwork.penapp.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.practicaleducationnetwork.penapp.R;
import com.practicaleducationnetwork.penapp.services.PenService;
import com.practicaleducationnetwork.penapp.utils.AppNavigator;
import com.practicaleducationnetwork.penapp.utils.Utils;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;

    private static final String TAG = SignUpActivity.class.getSimpleName();

    private EditText mPhoneNumberField,mPasswordField;
    private Button mSignIn;
    private TextView mSignUp;
    PenService penService;
    private boolean mIsBound;
    DatabaseReference rootRef,dbUsers;
    SharedPreferences sharedPreferences;
    public static final String PREFS = "PENAPP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        sharedPreferences = getSharedPreferences(PREFS, 0);

        mPhoneNumberField = (EditText) findViewById(R.id.phone_number);
        mPasswordField = (EditText) findViewById(R.id.password);

        mSignIn = findViewById(R.id.sign_in);
        mSignIn.setOnClickListener(this);
        mSignUp = findViewById(R.id.sign_up);
        mSignUp.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        rootRef = FirebaseDatabase.getInstance().getReference();
        dbUsers = rootRef.child("users");
    }

    @Override
    protected void onStart() {
        doBindService();
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent i = new Intent(SignInActivity.this,FeedActivity.class);
            startActivity(i);
        }
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        Utils.showProgressDialog(this,false);

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            new AppNavigator(SignInActivity.this).navigateToFeedActivity();
                            interceptSignIn(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(SignInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        if (!task.isSuccessful()) {
                            Toast.makeText(SignInActivity.this, R.string.auth_failed, Toast.LENGTH_SHORT).show();
//                            mStatusTextView.setText(R.string.auth_failed);
                        }
                        Utils.hideProgressDialog(SignInActivity.this);
                    }
                });
    }

    private void interceptSignIn(FirebaseUser user) {
        dbUsers.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Toast.makeText(SignInActivity.this, "Sign In intercepted.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mPhoneNumberField.getText().toString();

        if (TextUtils.isEmpty(email)) {
            mPhoneNumberField.setError("Required.");
            valid = false;
        }else if (email.length() < 9){
            mPhoneNumberField.setError("Incomplete.");
            valid = false;
        }else if (mPhoneNumberField.getText().toString().length() >  9){
            mPhoneNumberField.setError("Too many numbers.");
            valid = false;
        } else {
            mPhoneNumberField.setError(null);
            penService.tutor.setPhoneNumber(email);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        switch (i){
            case R.id.sign_in:
                if(validateForm()){
                    signIn(penService.tutor.getPseudoEmail(), mPasswordField.getText().toString());
                    Log.d("USER",penService.tutor.getPseudoEmail() + " : " + mPasswordField.getText().toString());
                }
                break;
            case R.id.sign_up:
                new AppNavigator(this).navigateToSignUpFlow();
                break;
        }
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            penService = ((PenService.MyBinder) iBinder).getService();
            mIsBound = true;
            penService.setActivity(SignInActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            penService = null;
        }
    };

    void doBindService()
    {
        Intent intent = new Intent(this, PenService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    void doUnbindService() {
        if (mIsBound) {
            // Detach our existing connection.
            unbindService(serviceConnection);
            mIsBound = false;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("server", "Destroy");
        if(mIsBound)
        {
            doUnbindService();
        }
    }

    public void onStop()
    {
        if(mIsBound)
        {
            doUnbindService();
        }
        super.onStop();
    }
}
