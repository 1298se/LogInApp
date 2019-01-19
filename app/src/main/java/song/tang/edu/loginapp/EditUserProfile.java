package song.tang.edu.loginapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;


public class EditUserProfile extends AppCompatActivity {

    // Constants for camera/gallery
    private static final int REQUEST_CAMERA = 0;
    private static final int SELECT_FILE = 1;
    private Uri profilePicURI;

    private EditText firstNameEditTextProf, lastNameEditTextProf;
    private Button updateProfileButton;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private ImageView profileImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);

        // Init
        firstNameEditTextProf = (EditText) findViewById(R.id.firstNameEditTextProf);
        lastNameEditTextProf = (EditText) findViewById(R.id.lastNameEditTextProf);
        updateProfileButton = (Button) findViewById(R.id.updateProfileButton);
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        profileImageView = (ImageView) findViewById(R.id.profileImageView);

        // Valid first name, last name
        updateProfileButton.setEnabled(false);
        setButtonColour();
        firstNameEditTextProf.addTextChangedListener(updateTextWatcher);
        lastNameEditTextProf.addTextChangedListener(updateTextWatcher);

        // Button onclick to update name
        updateProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserProfile();
            }
        });

        //
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfilePic();
            }
        });
    }

    private void updateUserProfile() {
        final String firstName = firstNameEditTextProf.getText().toString().trim();
        final String lastName = lastNameEditTextProf.getText().toString().trim();

        progressDialog.setMessage("Updating Profile...");
        progressDialog.show();

        FirebaseUser user = firebaseAuth.getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(firstName + " " + lastName).build();

        user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(EditUserProfile.this, "Update Successful", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(getApplicationContext(), Profile.class));
                } else {
                    Toast.makeText(EditUserProfile.this, "Update Failed. Please Try Again", Toast.LENGTH_SHORT).show();

                }
                progressDialog.dismiss();
            }
        });
    }

    private void setButtonColour() {
        if (!updateProfileButton.isEnabled()) {
            updateProfileButton.setBackground(getResources().getDrawable(R.drawable.rounded_button_inactive_gray));
            updateProfileButton.setTextColor(getResources().getColor(R.color.profileBg));
        } else {
            updateProfileButton.setBackground(getResources().getDrawable(R.drawable.rounded_button));
            updateProfileButton.setTextColor(getResources().getColor(R.color.background));
        }
    }

    private TextWatcher updateTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String firstName = firstNameEditTextProf.getText().toString().trim();
            String lastName = lastNameEditTextProf.getText().toString().trim();


            updateProfileButton.setEnabled(!firstName.isEmpty() && !lastName.isEmpty());
            setButtonColour();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void updateProfilePic() {
        final CharSequence[] items = {"Choose From Library"};
        AlertDialog.Builder builder = new AlertDialog.Builder(EditUserProfile.this);
        builder.setTitle("Add a Profile Picture");

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Choose From Library")) {
                    openGallery();
                }
            }
        });
        builder.show();
    }

    //ToDo: Camera Function for Profile Pic
    /*private void openCamera() {
        Log.d("EDIT_PROFILE", "onClick: camera intent");
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, REQUEST_CAMERA);
    }*/
    private void openGallery() {
        Log.d("EDIT_PROFILE", "onClick: gallery intent");
        Intent storageIntent = new Intent(Intent.ACTION_PICK);
        storageIntent.setType("image/*");
        startActivityForResult(storageIntent, SELECT_FILE);
    }

    @Override
    protected void onActivityResult(int requestcode, int resultcode, Intent data) {
        super.onActivityResult(requestcode, resultcode, data);

        if (requestcode == SELECT_FILE && resultcode == RESULT_OK) {
            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(250, 175)
                    .start(this);
        }
                /*
        if (requestcode == REQUEST_CAMERA && resultcode == RESULT_OK) {

            Bitmap photo = (Bitmap) data.getExtras().get("data");
            profileImageView.setImageBitmap(photo);

    */
        if (requestcode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultcode == RESULT_OK) {

                profilePicURI = result.getUri();
                profileImageView.setImageURI(profilePicURI);

            } else if (resultcode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();

            }
        }
    }
}

