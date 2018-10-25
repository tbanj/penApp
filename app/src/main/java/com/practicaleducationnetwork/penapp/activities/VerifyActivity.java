package com.practicaleducationnetwork.penapp.activities;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.practicaleducationnetwork.penapp.R;
import com.practicaleducationnetwork.penapp.models.Tutor;
import com.practicaleducationnetwork.penapp.services.PenService;
import com.practicaleducationnetwork.penapp.utils.AppNavigator;
import com.practicaleducationnetwork.penapp.utils.Utils;

/**
 * Created by Ayodeji Fabusuyi on 31/05/2018.
 */


/**
 * Variable names starting with 'c' denote class variables
 * while those starting with 'm' denote variables peculiar to methods
 */

public class VerifyActivity extends AppCompatActivity {
    private static final String TAG = VerifyActivity.class.getSimpleName();
    EditText cMobileNumber;
    Spinner cCountrySpinner;
    TextView cCountryCode;
    PenService penService;
    private boolean mIsBound;
    FirebaseAuth mAuth;
    FirebaseUser dbuser;
    DatabaseReference rootRef, dbUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

        mAuth = FirebaseAuth.getInstance();
        dbuser = mAuth.getCurrentUser();

        rootRef = FirebaseDatabase.getInstance().getReference();
        dbUsers = rootRef.child("users");

        cCountrySpinner = findViewById(R.id.countriesSpinner);
        ArrayAdapter<CharSequence> mSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.countries, R.layout.country_spinner_style);
        mSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cCountrySpinner.setAdapter(mSpinnerAdapter);

        cMobileNumber = findViewById(R.id.mobileNumber);
        cMobileNumber.setOnEditorActionListener(cOkayClickedListener);
    }

    /**
     *
     */
    EditText.OnEditorActionListener cOkayClickedListener = new EditText.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                cCountryCode = findViewById(R.id.countryCode);

                String mMobileNumber = cMobileNumber.getText().toString();
                String mShortCode = cCountryCode.getText().toString();
                String mNumber;

                if (mMobileNumber.length() < 10 || mMobileNumber.length() > 10) {
                    Toast.makeText(VerifyActivity.this, R.string.mobileNumberError, Toast.LENGTH_SHORT).show();
                }else {
                    penService.tutor.setPhoneNumber(mMobileNumber);
                    showCustomConfirmDialog();
                }
            }
            return false;
        }
    };

    public void showCustomConfirmDialog() {

        LayoutInflater mLayoutInflater = getLayoutInflater();
        final View mDialogView = mLayoutInflater.inflate(R.layout.confirm_number_dialog, null);
        TextView mConfirmPrompt = mDialogView.findViewById(R.id.confirmPrompt);
        Button mEditButton = mDialogView.findViewById(R.id.editButton);
        Button mOkayButton = mDialogView.findViewById(R.id.okButton);

        String promptMessage = getString(R.string.beforeNumber) + "\n+234 - " + penService.tutor.getPhoneNumber() + "\n\n" + getString(R.string.afterNumber);

        mConfirmPrompt.setText(promptMessage);

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this,R.style.Theme_Dialog);
        mBuilder.setView(mDialogView);
        final AlertDialog mDialog = mBuilder.show();

        //Start - Attach onClickListener to OK button in Confirmation Dialog
        mOkayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, penService.tutor.getPseudoEmail());
                Log.d(TAG, penService.tutor.getPhoneNumber());
                Log.d("USER",penService.tutor.getPseudoEmail() + " : " + penService.tutor.getPhoneNumber());
                createAccount(penService.tutor.getPseudoEmail(),penService.tutor.getPhoneNumber());
                mDialog.dismiss();
            }
        });
        //End - Attach onClickListener to OK button in Confirmation Dialog

        //Attach onClickListener to Edit button in Confirmation Dialog
        mEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss(); //Assigns the Builder to an AlertDialog and dismisses it at that instance
                cMobileNumber.requestFocus();
                showKeyboard();
            }
        });
        //End - //Attach onClickListener to Edit button in Confirmation Dialog

    }

    public void showKeyboard() {
        InputMethodManager mIMM = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        mIMM.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    private void createAccount(String email, String password) {
        Utils.showProgressDialog(this,false);

        // [START create_dbuser_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in dbuser's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser dbuser = mAuth.getCurrentUser();
                            createOtherUserProperties(dbuser);
                            new AppNavigator(VerifyActivity.this).navigateToFeedActivity();
                        } else {
                            // If sign in fails, display a message to the dbuser.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Utils.longToast(VerifyActivity.this, "Authentication failed.");
                        }

                        // [START_EXCLUDE]
                        Utils.hideProgressDialog(VerifyActivity.this);
                        // [END_EXCLUDE]
                    }
                });
        // [END create_dbuser_with_email]
    }

    private void createOtherUserProperties(FirebaseUser dbuser) {
        Tutor data = penService.getTutor();
        dbUsers.child(dbuser.getUid()).setValue(data);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            penService = ((PenService.MyBinder) iBinder).getService();
            mIsBound = true;
            penService.setActivity(VerifyActivity.this);
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
    public void onStart() {
        doBindService();
        super.onStart();
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
