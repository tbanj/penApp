package com.practicaleducationnetwork.penapp.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.practicaleducationnetwork.penapp.services.PenService;
import com.practicaleducationnetwork.penapp.utils.MethodInUse;
import com.practicaleducationnetwork.penapp.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = SignUpActivity.class.getName();
    // Flag to indicate the request of the next task to be perf
    private static final int REQUEST_SELECT_IMAGE = 1;
    private static final int REQUEST_TAKE_PHOTO = 0;
    private static final String TEMP_IMAGE_NAME = "tempImage";
    MethodInUse methodInUse = new MethodInUse();
    String firstName;
    String lastName;
    String nameOfSchool;
    String districtOfSchool;
    String cityOfSchool;
    private EditText firstNameText;
    private EditText lastNameText;
    private EditText nameOfSchoolText;
    private EditText districtOfSchoolText;
    private EditText cityOfSchoolText;
    private Button submitButton;
    private ImageView bFromGallery;
    private ImageView bSetImageCenter;
    private TextView profileHeading;
    // The URI of photo taken with camera
    private Uri uriPhoto;
    private Toast validateToast;
    // Flag to indicate which task is to be performed.
    //private static final int REQUEST_SELECT_IMAGE = 0;
    PenService penService;
    private boolean mIsBound;

    private static List<Intent> addIntentsToList(Context context, List<Intent> list, Intent intent) {
        List<ResolveInfo> resInfo = context.getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resInfo) {
            String packageName = resolveInfo.activityInfo.packageName;
            Intent targetedIntent = new Intent(intent);
            targetedIntent.setPackage(packageName);
            list.add(targetedIntent);
            Log.d(TAG, "Intent: " + intent.getAction() + " package: " + packageName);
        }
        return list;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        profileHeading = findViewById(R.id.signUp_profile);
        profileHeading.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });


        firstNameText = findViewById(R.id.firstNameEdit);
        lastNameText = findViewById(R.id.lastNameEdit);
        nameOfSchoolText = findViewById(R.id.school_name_Edit);
        districtOfSchoolText = findViewById(R.id.district_school_Edit);
        cityOfSchoolText = findViewById(R.id.city_of_school_Edit);
        submitButton = findViewById(R.id.submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
            }
        });


        bSetImageCenter = findViewById(R.id.image_center);
        bFromGallery = findViewById(R.id.image_base);

        bFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chooseImageIntent = getPickImageIntent(SignUpActivity.this);
                startActivityForResult(chooseImageIntent, REQUEST_SELECT_IMAGE);
            }
        });


    }

    // Deal with the result of selection of the photos and faces.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_TAKE_PHOTO:
            case REQUEST_SELECT_IMAGE:
                Uri imageUri;
                try {
                    if (resultCode == RESULT_OK) {

                        if (data == null || data.getData() == null) {
                            imageUri = uriPhoto;
                        } else {
                            imageUri = data.getData();
                        }
                        bSetImageCenter.setVisibility(View.GONE);
                        bFromGallery.setImageURI(imageUri);
                    }
                } catch (Exception error) {
                    Log.d(TAG, " onActivityResult(): " + " Select Image= 1  Camera=0: ");
                    Log.d(TAG, " onActivityResult(): " + " Image upload type that is selected is: " + requestCode);

                }

                break;
            default:
                break;
        }
    }

    public void submitForm() {

        if (validate()) {
            penService.tutor.setFirstName(firstName);
            penService.tutor.setLastName(lastName);
            penService.tutor.setNameOfSchool(nameOfSchool);
            penService.tutor.setDistrictOfSchool(districtOfSchool);
            penService.tutor.setCityOfSchool(cityOfSchool);
            Intent nextActivity = new Intent(SignUpActivity.this, VerifyActivity.class);
            startActivity(nextActivity);
        }
    }

    private boolean validate() {
        boolean valid = true;

        firstName = firstNameText.getText().toString();
        lastName = lastNameText.getText().toString();
        nameOfSchool = nameOfSchoolText.getText().toString();
        districtOfSchool = districtOfSchoolText.getText().toString();
        cityOfSchool = cityOfSchoolText.getText().toString();

            if (firstName.isEmpty()) {
                firstNameText.setError("First name is required.");
                valid = false;
            }

            if (lastName.isEmpty()) {
                lastNameText.setError("Last name is required.");
                valid = false;
            }

            if (nameOfSchool.isEmpty()) {
                nameOfSchoolText.setError("Name of School is required.");
                valid = false;
            }

            if (cityOfSchool.isEmpty()) {
                cityOfSchoolText.setError("City of school is required.");
                valid = false;
            }

            if (districtOfSchool.isEmpty()) {
                districtOfSchoolText.setError("District of School is Required.");
                valid = false;
            }

        return valid;
    }

    public Intent takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Save the photo taken to a temporary file.
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            try {
                File file = File.createTempFile("IMG_", ".jpg", storageDir);
                uriPhoto = Uri.fromFile(file);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uriPhoto);

            } catch (IOException e) {
            }
        }
        return intent.putExtra(MediaStore.EXTRA_OUTPUT, uriPhoto);
    }

    public Intent getPickImageIntent(Context context) {
        Intent chooserIntent = null;

        List<Intent> intentList = new ArrayList<>();

        Intent pickIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        intentList = addIntentsToList(context, intentList, pickIntent);
        intentList = addIntentsToList(context, intentList, takePhoto());

        if (intentList.size() > 0) {
            chooserIntent = Intent.createChooser(intentList.remove(intentList.size() - 1),
                    context.getString(R.string.pick_image_intent_text));
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentList.toArray(new Parcelable[]{}));
        }

        return chooserIntent;
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            penService = ((PenService.MyBinder) iBinder).getService();
            mIsBound = true;
            penService.setActivity(SignUpActivity.this);
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
