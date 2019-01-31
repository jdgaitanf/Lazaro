package com.juanito.lazaro;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CalendarPage extends AppCompatActivity {

    private RequestQueue mQueue;
    private String Token;
    private TextView TemporalInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_page);
        loadInfo();

        TemporalInfo = (TextView)findViewById(R.id.textView);
        //https://androidclarified.com/android-volley-example/
        mQueue = Volley.newRequestQueue(this);//Revisar cuál es el contexto para más actividades
        jsonParse();
    }

    public void loadInfo(){
        SharedPreferences preferences = getSharedPreferences("Credentials", Context.MODE_PRIVATE);

        Token = preferences.getString("token","");


    }

    private void jsonParse() {
        String url = "https://azrav-webapp-apl05.azurewebsites.net/api/Profile";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        String temporalString = response.toString();

                        TemporalInfo.setText(temporalString);


                        Log.d("Respuesta",temporalString);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + Token);

                return params;
            }
        };


        mQueue.add(request);

    }




}
