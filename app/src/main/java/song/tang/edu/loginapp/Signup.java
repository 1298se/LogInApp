package song.tang.edu.loginapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.oob.SignUp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.regex.Pattern;


public class Signup extends AppCompatActivity {

    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=\\S+$).{8,}$");

    private String region;
    private Spinner regionSpinner;
    private EditText firstNameEditText, lastNameEditText, emailEditText, passwordEditText;
    private Button signUpButton;
    private TextView signInTextView;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        regionSpinner = (Spinner)findViewById(R.id.regionSpinnerSign);
        firstNameEditText = (EditText)findViewById(R.id.firstNameEditText);
        lastNameEditText = (EditText)findViewById(R.id.lastNameEditText);
        emailEditText = (EditText)findViewById(R.id.emailEditText);
        passwordEditText = (EditText)findViewById(R.id.passwordEditText);
        signUpButton = (Button)findViewById(R.id.signUpButtonSign);
        signInTextView = (TextView)findViewById(R.id.signInTextView);
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();


        // Valid First Name, Last Name, Email, Password
        signUpButton.setEnabled(false);
        setButtonColour();
        firstNameEditText.addTextChangedListener(signupTextWatcher);
        lastNameEditText.addTextChangedListener(signupTextWatcher);
        emailEditText.addTextChangedListener(signupTextWatcher);
        passwordEditText.addTextChangedListener(signupTextWatcher);


        // Drop Down Menu
        ArrayAdapter<String> regionAdapter = new ArrayAdapter<String>(Signup.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.regions));
        regionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        regionSpinner.setAdapter(regionAdapter);

        regionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position)
                {
                    case 0:
                        region = "North America";
                        break;
                    case 1:
                        region = "Europe";
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Register Button
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        //Already have an account
        signInTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(login);
            }
        });

    }

    private void registerUser(){
        final String firstName = firstNameEditText.getText().toString().trim();
        final String lastName = lastNameEditText.getText().toString().trim();
        final String email = emailEditText.getText().toString().trim();
        final String password = passwordEditText.getText().toString();


        progressDialog.setMessage("Registering User...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Signup.this, "Successfully Registered!", Toast.LENGTH_SHORT).show();

                            FirebaseUser user = firebaseAuth.getCurrentUser();

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(firstName + " " + lastName).build();

                            user.updateProfile(profileUpdates);
                            finish();
                            startActivity(new Intent(getApplicationContext(), Login.class));
                        }
                        else {
                            Toast.makeText(Signup.this, "Unsuccessful Registration. Please Try Again",
                                    Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });
    }
    private boolean validateEmail() {
        String email = emailEditText.getText().toString().trim();

        if (email.isEmpty()){
            passwordEditText.setError("Please Enter your Email");
            return false;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Please Enter a Valid Email Address");
            return false;
        }
        else {
            emailEditText.setError(null);
            return true;
        }
    }
    private boolean validatePassword() {
        String password = passwordEditText.getText().toString();

         if (password.isEmpty()){
             passwordEditText.setError("Please Enter a Password");
             return false;

         }else if(!PASSWORD_PATTERN.matcher(password).matches()){
            passwordEditText.setError("Invalid Password");
            return false;
        }
        else {
            passwordEditText.setError(null);
            return true;
        }
    }
    private void setButtonColour(){
        if(!signUpButton.isEnabled()){
            signUpButton.setBackground(getResources().getDrawable(R.drawable.rounded_button_inactive));
        }
        else {
            signUpButton.setBackground(getResources().getDrawable(R.drawable.rounded_button));
        }
    }
    private TextWatcher signupTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String firstName = firstNameEditText.getText().toString().trim();
            String lastName = lastNameEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString();

            validateEmail();
            validatePassword();

            signUpButton.setEnabled(!email.isEmpty() && !password.isEmpty() && !firstName.isEmpty() && !lastName.isEmpty() &&
                    validateEmail() && validatePassword());
            setButtonColour();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}