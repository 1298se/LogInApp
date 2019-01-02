package song.tang.edu.loginapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (getIntent().hasExtra("song.tang.edu.region")) {
            TextView display = (TextView) findViewById(R.id.displayTextView);
            String text = getIntent().getExtras().getString("song.tang.edu.region");
            display.setText(text);
        }
    }
}
