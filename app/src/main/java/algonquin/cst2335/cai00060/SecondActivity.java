package algonquin.cst2335.cai00060;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import algonquin.cst2335.cai00060.databinding.ActivityMainBinding;
import algonquin.cst2335.cai00060.databinding.ActivitySecondBinding;

public class SecondActivity extends AppCompatActivity {

    private ActivitySecondBinding variableBinding;
    private ActivityResultLauncher<Intent> cameraResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        variableBinding = ActivitySecondBinding.inflate(getLayoutInflater());
        setContentView(variableBinding.getRoot());

        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String emailAddress = prefs.getString("LoginName", "");
        // String emailAddress = fromPrevious.getStringExtra("EmailAddress");
        variableBinding.textView.setText("Welcome back " + emailAddress);
        String phoneNumber = prefs.getString("phoneNumber", "");
        variableBinding.editTextPhone.setText(phoneNumber);

        // Intent fromPrevious = getIntent();

        File file = new File(getFilesDir(), "Picture.png");
        if (file.exists()) {
            Bitmap theImage = BitmapFactory.decodeFile(file.getAbsolutePath());
            variableBinding.profileImage.setImageBitmap(theImage);
        }

        // move cameraResult from "inside setOnClickListener method" to "inside onCreate method"
        cameraResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            Bitmap thumbnail = data.getParcelableExtra("data");
                            variableBinding.profileImage.setImageBitmap(thumbnail);
                            FileOutputStream fOut = null;
                            try {
                                fOut = openFileOutput("Picture.png", Context.MODE_PRIVATE);
                                thumbnail.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                                fOut.flush();
                                fOut.close();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

        variableBinding.buttonChange.setOnClickListener(v -> {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
                    cameraResult.launch(cameraIntent);
                else
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 20);
            }
        });

        variableBinding.buttonCall.setOnClickListener(v -> {
            Intent call = new Intent(Intent.ACTION_DIAL);
            String editPhoneNumber = variableBinding.editTextPhone.getText().toString();

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("phoneNumber", editPhoneNumber);
            editor.apply();

            call.setData(Uri.parse("tel:" + editPhoneNumber));
            startActivity(call);
        });

        variableBinding.backButton.setOnClickListener(v -> {
            // go back to the last page where you are from
            finish();
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("phoneNumber", variableBinding.editTextPhone.getText().toString());
    }
}