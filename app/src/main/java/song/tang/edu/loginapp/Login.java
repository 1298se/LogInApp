package song.tang.edu.loginapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    private TextView regionTextView;
    private EditText emailEditText, passwordEditText;
    private Button signIn, backButton;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Init
        regionTextView = (TextView) findViewById(R.id.regionTextView);
        emailEditText = (EditText) findViewById(R.id.emailEditTextLog);
        passwordEditText = (EditText) findViewById(R.id.passwordEditTextLog);
        signIn = (Button) findViewById(R.id.signInButtonLog);
        backButton = (Button) findViewById(R.id.backButton);
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();

        if (getIntent().hasExtra("song.tang.edu.region")) {
            String text = getIntent().getExtras().getString("song.tang.edu.region");
            regionTextView.setText(text);
        }


        // If user is already logged in
        if(firebaseAuth.getCurrentUser() != null) {
            // Profile
            startActivity(new Intent(getApplicationContext(), Profile.class));
        }

        // Sign In button
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });

        // Back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(back); }
        });
    }

    private void userLogin(){
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if(TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please Enter Your Email", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please Enter A Password", Toast.LENGTH_LONG).show();
            return;
        }

        progressDialog.setMessage("Logging In...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            // Profile
                            finish();
                            startActivity(new Intent(getApplicationContext(), Profile.class));
                        }
                        else {
                            Toast.makeText(Login.this, "Unsuccessful Login. Please Try Again",
                                    Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });
    }
}
