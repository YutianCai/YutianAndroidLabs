package algonquin.cst2335.cai00060.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import algonquin.cst2335.cai00060.data.MainViewModel;
import algonquin.cst2335.cai00060.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private MainViewModel model;
    private ActivityMainBinding variableBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // calls parent onCreate()

        model = new ViewModelProvider(this).get(MainViewModel.class);

        variableBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(variableBinding.getRoot()); // load XML on screen

        variableBinding.myButton.setOnClickListener(click ->
                model.editString.postValue(variableBinding.myEditText.getText().toString()));

        model.editString.observe(this, s ->
                variableBinding.myText.setText("Your edit text has " + s));

    }

}
