package algonquin.cst2335.cai00060.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.Toast;

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

        model.editString.observe(this, s ->
                variableBinding.myText.setText("Your edit text has " + s));

        variableBinding.myButton.setOnClickListener(click ->
                model.editString.postValue(variableBinding.myEditText.getText().toString()));

        model.isSelected.observe(this, selected -> {
            variableBinding.myCheckBox.setChecked(selected);
            variableBinding.myRadioButton.setChecked(selected);
            variableBinding.mySwitch.setChecked(selected);
        });

        variableBinding.myCheckBox.setOnCheckedChangeListener((btn, isChecked) -> {
            model.isSelected.postValue(isChecked);
            Toast.makeText(getApplicationContext(), "The value is now: " + isChecked, Toast.LENGTH_SHORT).show();
        });

        variableBinding.myRadioButton.setOnCheckedChangeListener((btn, isChecked) -> {
            model.isSelected.postValue(isChecked);
        });

        variableBinding.mySwitch.setOnCheckedChangeListener((btn, isChecked) -> {
            model.isSelected.postValue(isChecked);
        });

        variableBinding.myImageView.setOnClickListener(click -> {
            variableBinding.myText.setText("You clicked the picture.");
        });

        variableBinding.myImageButton.setOnClickListener(click -> {
            Toast.makeText(getApplicationContext(),
                            "The width = " + variableBinding.myImageButton.getWidth() + " and height = " + variableBinding.myImageButton.getHeight(),
                            Toast.LENGTH_SHORT)
                    .show();
        });

    }

}
