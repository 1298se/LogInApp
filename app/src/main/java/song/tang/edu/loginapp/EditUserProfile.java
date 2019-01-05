package song.tang.edu.loginapp;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class EditUserProfile extends AppCompatActivity {

    private EditText firstNameEditTextProf, lastNameEditTextProf;
    private Button updateProfileButton;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

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

        // Valid first name, last name
        updateProfileButton.setEnabled(false);
        setButtonColour();
        firstNameEditTextProf.addTextChangedListener(updateTextWatcher);
        lastNameEditTextProf.addTextChangedListener(updateTextWatcher);

        // Button onclick
        updateProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserProfile();
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
                if(task.isSuccessful()) {
                    Toast.makeText(EditUserProfile.this, "Update Successful", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else {
                    Toast.makeText(EditUserProfile.this, "Update Failed. Please Try Again", Toast.LENGTH_SHORT).show();

                }
                progressDialog.dismiss();
            }
        });
    }
    private void setButtonColour(){
        if(!updateProfileButton.isEnabled()){
            updateProfileButton.setBackground(getResources().getDrawable(R.drawable.rounded_button_inactive_gray));
            updateProfileButton.setTextColor(getResources().getColor(R.color.profileBg));
        }
        else {
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

}
