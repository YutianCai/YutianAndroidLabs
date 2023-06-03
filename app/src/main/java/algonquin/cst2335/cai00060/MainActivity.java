package algonquin.cst2335.cai00060;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import algonquin.cst2335.cai00060.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private static String TAG = "MainActivity";
    private ActivityMainBinding variableBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_main);

        variableBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(variableBinding.getRoot());
        Log.w(TAG, "In onCreate() - Loading Widgets");

        // change here
        // Button loginButton = findViewById(R.id.loginButton);
        // EditText emailEditText = findViewById(R.id.emailEditText);

        variableBinding.loginButton.setOnClickListener(v -> {
            // Intent tells you where to go next
            // SecondActivity.class calls the constructor
            Intent nextPage = new Intent(MainActivity.this, SecondActivity.class);
            nextPage.putExtra("EmailAddress", variableBinding.emailEditText.getText().toString());
            // does the transition:
            startActivity(nextPage);
        });
    }

    @Override
    protected void onStart() { //now visible on the screen
        super.onStart();
        Log.w(TAG, "The application is now visible on screen.");
    }

    @Override
    protected void onResume() { // now responding to input touch
        super.onResume();
        Log.w(TAG, "The application is now responding to user input.");
    }

    @Override
    protected void onPause() { // not listening to clicks
        super.onPause();
        Log.w(TAG, "The application no longer responds to user input.");
    }

    @Override
    protected void onStop() { // no longer visible
        super.onStop();
        Log.w(TAG, "The application is no longer visible.");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.w(TAG, "Any memory used by the application is freed.");
    }


}