package algonquin.cst2335.cai00060;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;

import algonquin.cst2335.cai00060.databinding.ActivityMainBinding;

/**
 * @author Yutian Cai
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {
    // for sending network requests:
    RequestQueue queue = null;
    Bitmap image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate( getLayoutInflater() );

        //This part goes at the top of the onCreate function:
        queue = Volley.newRequestQueue(this); //like a constructor

        //loads an XML file on the page
        setContentView(  binding.getRoot()  );
        binding.getForecast.setOnClickListener(click -> {

            String cityName = binding.editText.getText().toString();

            //server name and parameters:                           name=value&name2=value2&name3=value3
            String url = "https://api.openweathermap.org/data/2.5/weather?q=" +
                    URLEncoder.encode(cityName) //replace spaces, &. = with other characters
                    + "&appid=7e943c97096a9784391a981c4d878b22&units=metric";

            JsonObjectRequest request =   new JsonObjectRequest(Request.Method.GET, url, null,
                    ( response ) -> {

                        try {
                            JSONObject mainObject = response.getJSONObject("main");
                            double temp = mainObject.getDouble("temp");
                            double min = mainObject.getDouble("temp_min");
                            double max = mainObject.getDouble("temp_max");
                            int humidity = mainObject.getInt("humidity");

                            JSONArray weatherArray = response.getJSONArray( "weather");
                            JSONObject pos0 = weatherArray.getJSONObject( 0 );
                            String description = pos0.getString("description");
                            String  iconName = pos0.getString("icon");

                            String pathname = getFilesDir() + "/" + iconName + ".png";
                            File file = new File(pathname);
                            if(file.exists()){
                                image = BitmapFactory.decodeFile(pathname);
                            }else{
                                String pictureURL = "http://openweathermap.org/img/w/" + iconName + ".png";
                                ImageRequest imgReq = new ImageRequest(pictureURL, new Response.Listener<Bitmap>() {
                                    @Override
                                    public void onResponse(Bitmap bitmap) {
                                        image = bitmap;

                                    try {
                                        FileOutputStream fOut = openFileOutput( iconName + ".png", Context.MODE_PRIVATE);
                                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                                        fOut.flush();
                                        fOut.close();
                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    }
                                }, 1024, 1024, ImageView.ScaleType.CENTER, null,
                                        (error ) -> {
                                            int i = 0;
                                        });
                                queue.add(imgReq);
                            }

                            runOnUiThread( (  )  -> {
                                binding.temp.setText("The current temperature is " + temp + " degrees");
                                binding.temp.setVisibility(View.VISIBLE);
                                binding.maxTemp.setText("The max temperature is " + max + " degrees");
                                binding.maxTemp.setVisibility(View.VISIBLE);
                                binding.minTemp.setText("The min temperature is " + min + " degrees");
                                binding.minTemp.setVisibility(View.VISIBLE);
                                binding.humitidy.setText("The humidity is " + humidity + "%");
                                binding.humitidy.setVisibility(View.VISIBLE);
                                binding.description.setText(description);
                                binding.description.setVisibility(View.VISIBLE);
                                binding.icon.setImageBitmap(image);
                                binding.icon.setVisibility(View.VISIBLE);
                            });

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }


                    }, //gets called if it is successful
                    (vError) ->   {
                        int i = 0;

                    }  ); //gets called if there is an error

            queue.add(request); //run the web query
        });
    }
}