package vi.itplus.sonhv.dubaothoitiet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    TextView txtTemperState, txtTemper, txtHumidity, txtWind, txtCloud, txtCurentTemper, txtProvince, txtNational;
    ImageView imgWeatherIcon;
    Button btnCity;
    EditText edtCity;
    private android.widget.Toast Toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapping();
        addAction();
//        getJsonWeather("London");
    }

    private void addAction() {
        btnCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = edtCity.getText().toString();
                getJsonWeather(city);
            }
        });
    }

    private void mapping() {
        txtTemperState = findViewById(R.id.txtTemperState);
        txtTemper = findViewById(R.id.txtTemper);
        txtHumidity = findViewById(R.id.txtHumidity);
        txtWind = findViewById(R.id.txtWind);
        txtCloud = findViewById(R.id.txtCloud);
        txtCurentTemper = findViewById(R.id.txtCurentTemper);
        txtProvince = findViewById(R.id.txtProvince);
        txtNational = findViewById(R.id.txtNational);
        imgWeatherIcon = findViewById(R.id.imgWeatherIcon);
        btnCity= findViewById(R.id.btnOK);
        edtCity = findViewById(R.id.edtProvince);
    }

    public void getJsonWeather(final String city){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String url = "http://api.openweathermap.org/data/2.5/weather?q="+city+"&appid="+"2f0e82a18b29ed0ebbe7750ddd76f9b9"+"&units=metric";
//        String url = "http://api.openweathermap.org/data/2.5/weather?q=hanoi&appid=2f0e82a18b29ed0ebbe7750ddd76f9b9&units=metric";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray weatherArray = response.getJSONArray("weather");
                            JSONObject weatherObj = weatherArray.getJSONObject(0);
                            String icon = weatherObj.getString("icon");//icon thời tiết
                            String urlIcon = "http://openweathermap.org/img/wn/"+icon+".png";
                            Picasso.get().load(urlIcon).into(imgWeatherIcon);
                            String temperState = weatherObj.getString("main");//trạng thái
                            txtTemperState.setText(temperState);
                            JSONObject main = response.getJSONObject("main");
                            String temp = main.getString("temp");//nhiệt độ
                            txtTemper.setText(temp+"ºC");
                            String humidity = main.getString("humidity");//độ ẩm
                            txtHumidity.setText(humidity+"%");
                            JSONObject wind = response.getJSONObject("wind");
                            String speed = wind.getString("speed");//tóc độ gió
                            txtWind.setText(speed+"m/s");
                            JSONObject clouds = response.getJSONObject("clouds");
                            String all = clouds.getString("all");//phần trăm mây
                            txtCloud.setText(all+"%");
                            String sNgay = response.getString("dt");
                            long lNgay = Long.parseLong(sNgay);
                            SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, yyyy-MM-dd HH:mm:ss");
                            Date date = new Date(lNgay*1000);
                            String currentTime = dateFormat.format(date);//ngày giờ hiện tại
                            txtCurentTemper.setText(currentTime);
                            String name = response.getString("name");// thành phố
                            txtProvince.setText("Thành phố "+name);
                            JSONObject sys = response.getJSONObject("sys");
                            String country = sys.getString("country");//quốc gia
                            txtNational.setText("Quốc gia "+country);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("MyError1:",response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Không có dữ liệu cho thành phố "+city, Toast.LENGTH_LONG).show();
                        Log.d("MyError:",error.toString());
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }

}