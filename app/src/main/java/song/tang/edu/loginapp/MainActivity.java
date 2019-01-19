package song.tang.edu.loginapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;

// PseudoCode
// Title
// Dropdown Menu for Region
// Popup Information
// Log In Button
// Sign Up
// Text Fields for Registration

public class MainActivity extends AppCompatActivity{

    private String region;
    private Spinner regionSpinner;
    private ArrayAdapter<String> regionAdapter;
    private Button logIn, signUp;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Init
        regionSpinner = (Spinner) findViewById(R.id.regionSpinnerMain);
        logIn = (Button) findViewById(R.id.loginButton);
        signUp = (Button) findViewById(R.id.signUpButtonMain);
        firebaseAuth = FirebaseAuth.getInstance();


        // Region Drop Down Menu
        regionAdapter = new ArrayAdapter<String>(MainActivity.this,
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

        // Log In
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If User is already signed in
                if(firebaseAuth.getCurrentUser() != null) {
                    startActivity(new Intent(getApplicationContext(), Profile.class));
                } else if(firebaseAuth.getCurrentUser() == null) {
                    Intent login = new Intent(getApplicationContext(), Login.class);
                    login.putExtra("song.tang.edu.region", region);
                    startActivity(login);
                }

                // Using website
                /*
                String loginWeb = "https://sso.core.opentext.com";
                Uri webAddress = Uri.parse(loginWeb);

                Intent login = new Intent(Intent.ACTION_VIEW, webAddress);
                if (login.resolveActivity(getPackageManager()) != null) {
                    startActivity(login);
                } */
            }
        });

        // Sign Up
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signup = new Intent(getApplicationContext(), Signup.class);
                startActivity(signup);
            }
        });
    }
    public void onBackPressed() {
        finish();
        System.exit(0);
    }
}