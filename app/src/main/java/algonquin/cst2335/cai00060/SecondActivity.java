package algonquin.cst2335.cai00060;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;

import algonquin.cst2335.cai00060.databinding.ActivityMainBinding;
import algonquin.cst2335.cai00060.databinding.ActivitySecondBinding;

public class SecondActivity extends AppCompatActivity {

    private ActivitySecondBinding variableBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_second);
        variableBinding = ActivitySecondBinding.inflate(getLayoutInflater());
        setContentView(variableBinding.getRoot());

        Intent fromPrevious = getIntent();
        String emailAddress = fromPrevious.getStringExtra("EmailAddress");

        variableBinding.textView.setText("Welcome back " + emailAddress);

        variableBinding.buttonChange.setOnClickListener(v -> {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            ActivityResultLauncher<Intent> cameraResult = registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
//                                if (data != null && data.getExtras() != null) {
                            Bitmap thumbnail = data.getParcelableExtra("data");
                            variableBinding.profileImage.setImageBitmap(thumbnail);
//                                }
//                            }else if(result.getResultCode() == Activity.RESULT_CANCELED && data!=null) {
//                                Toast.makeText(SecondActivity.this, "No Picture Taken", Toast.LENGTH_SHORT).show();
                        }
                    });
            cameraResult.launch(cameraIntent);
        });

        variableBinding.buttonCall.setOnClickListener(v -> {
            Intent call = new Intent(Intent.ACTION_DIAL);
            call.setData(Uri.parse("tel:" + variableBinding.editTextPhone.getText().toString()));
            startActivity(call);
        });

        variableBinding.backButton.setOnClickListener(v -> {
            // go back to the last page where you are from
            finish();
        });

    }
}