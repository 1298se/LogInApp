package song.tang.edu.loginapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

// PseudoCode
// Title
// Dropdown Menu for Region
// Popup Information
// Log In Button
// Sign Up
// Text Fields for Registration

public class MainActivity extends AppCompatActivity{

    String region = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Region Drop Down Menu
        Spinner regionSpinner = (Spinner) findViewById(R.id.regionSpinner);

        ArrayAdapter<String> regionAdapter = new ArrayAdapter<String>(MainActivity.this,
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
        Button logIn = (Button) findViewById(R.id.loginButton);
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(getApplicationContext(), Login.class);
                login.putExtra("song.tang.edu.region", region);
                startActivity(login);

                // URI
                /*
                String loginWeb = "https://sso.core.opentext.com";
                Uri webAddress = Uri.parse(loginWeb);

                Intent login = new Intent(Intent.ACTION_VIEW, webAddress);
                if (login.resolveActivity(getPackageManager()) != null) {
                    startActivity(login);
                } */
            }
        });

        // Registration
        Button signUp = (Button) findViewById(R.id.signupButton);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signup = new Intent(getApplicationContext(), Signup.class);
                startActivity(signup);
            }
        });
    }
}