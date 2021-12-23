package com.satyajit.checkmyweather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * @author Satyajit Ghosh
 * @since 24-12-2021
 * */
public class ExpandedData extends AppCompatActivity {
    private TextView current_temp,area,min_temp,max_temp,humidityView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expanded_data);

        current_temp=findViewById(R.id.current_temp);
        area=findViewById(R.id.areafield);
        min_temp=findViewById(R.id.min_temp);
        max_temp=findViewById(R.id.max_temp);
        humidityView=findViewById(R.id.humidity);

        // location data received from another activity
        Intent intent = getIntent();
        String location = intent.getStringExtra("location");

        //API CALL

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "https://api.openweathermap.org/data/2.5/weather?q="+location+"&APPID=edf7b9671772635f19f881dc029fbc79";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String current_temperature=response.getJSONObject("main").getString("temp");
                            current_temperature=Double.toString(Double.parseDouble(current_temperature)-273.15);
                            current_temp.setText(current_temperature.substring(0,5)+" °C");

                            String min_temperature=response.getJSONObject("main").getString("temp_min");
                            min_temperature=Double.toString(Double.parseDouble(min_temperature)-273.15);
                            min_temp.setText("min "+min_temperature.substring(0,5)+" °C");


                            String max_temperature=response.getJSONObject("main").getString("temp_max");
                            max_temperature=Double.toString(Double.parseDouble(max_temperature)-273.15);
                            max_temp.setText("max "+max_temperature.substring(0,5)+" °C");

                            String humidity=response.getJSONObject("main").getString("humidity");
                            humidityView.setText("humidity "+humidity);


                            String desc=response.getJSONArray("weather").getJSONObject(0).getString("main");
                            area.setText(desc+" in "+location);

                        } catch (JSONException e) {
                          //  area.setText("City not Found");
                            e.printStackTrace();
                            Log.d("CityNotFound","City not found");

                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        current_temp.setText("");
                        Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_LONG).show();
                        area.setText("City not Found");

                    }
                });
        queue.add(jsonObjectRequest);

    }
}