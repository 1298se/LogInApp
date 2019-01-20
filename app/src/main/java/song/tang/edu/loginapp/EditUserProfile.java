package song.tang.edu.loginapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class EditUserProfile extends AppCompatActivity {

    // Constants for camera/gallery
    private static final int SELECT_FILE = 1;
    private Uri profilePicURI;
    private Boolean pictureSelected;
    private Boolean profilePicExists;

    private EditText firstNameEditTextProf, lastNameEditTextProf;
    private Button updateProfileButton;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private ImageView profileImageView;

    // Firebase Storage
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);

        // Check if user already has profile
        profilePicExists = true;


        // Init
        firstNameEditTextProf = (EditText) findViewById(R.id.firstNameEditTextProf);
        lastNameEditTextProf = (EditText) findViewById(R.id.lastNameEditTextProf);
        updateProfileButton = (Button) findViewById(R.id.updateProfileButton);
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        profileImageView = (ImageView) findViewById(R.id.profileImageView);
        // Valid first name, last name
        setButtonColour();
        firstNameEditTextProf.addTextChangedListener(updateTextWatcher);
        lastNameEditTextProf.addTextChangedListener(updateTextWatcher);

        String[] fullNameArr = firebaseAuth.getCurrentUser().getDisplayName().split("\\s+");
        firstNameEditTextProf.setText(fullNameArr[0]);
        lastNameEditTextProf.setText(fullNameArr[1]);

        // Firebase Storage Reference
        String uID= firebaseAuth.getCurrentUser().getUid();
        mStorageRef = FirebaseStorage.getInstance().getReference("users/" + uID);

        // Set profile picture (Default picture if user has not uploaded any)
        mStorageRef.child("profile_img.null").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                profilePicExists = false;
            }
        });

        // Button onclick to update name, profile picture
        pictureSelected = false;
        updateProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pictureSelected) {
                    updateUserProfile();
                    updateProfilePic();
                } else {
                    updateUserProfile();
                }
            }
        });

        // Choose Profile Image
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectProfilePic();

            }
        });
    }
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void updateProfilePic() {
        if(profilePicExists) {
            mStorageRef.child("profile_img.null").delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    uploadProfilePic();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditUserProfile.this, "Profile Picture Update Failed",
                            Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            uploadProfilePic();
        }
    }

    private void uploadProfilePic() {

        progressDialog.setMessage("Updating Profile...");
        progressDialog.show();

            final StorageReference fileReference = mStorageRef.child("profile_img" + "." + getFileExtension(profilePicURI));
            fileReference.putFile(profilePicURI)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            // Unneeded code for profile pic(used to getDownloadUrl)
                        /*Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                        while(!urlTask.isSuccessful());*/

                            progressDialog.dismiss();
                            startActivity(new Intent(getApplicationContext(), Profile.class));
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(EditUserProfile.this, "Picture Upload Failed",
                                    Toast.LENGTH_SHORT).show();
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
                    if(!pictureSelected) {
                        finish();
                        startActivity(new Intent(getApplicationContext(), Profile.class));
                    }
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

    private void selectProfilePic() {
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

                Picasso.get().load(profilePicURI).into(profileImageView);
                pictureSelected = true;

            } else if (resultcode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();

            }
        }
    }
}

