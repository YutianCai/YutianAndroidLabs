package algonquin.cst2335.cai00060;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

        variableBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(variableBinding.getRoot());
        Log.w(TAG, "In onCreate() - Loading Widgets");

        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String emailAddress = prefs.getString("LoginName", "");
        float hi = prefs.getFloat("Hi", 0);
        int age = prefs.getInt("Age", 0);
        variableBinding.emailEditText.setText(emailAddress);

        variableBinding.loginButton.setOnClickListener(v -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("LoginName", variableBinding.emailEditText.getText().toString());
            editor.putFloat("Hi", 4.5f);
            editor.putInt("Age", 35);
            editor.apply();
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