package com.juanito.lazaro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.VolleyError;

import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class MainPage extends AppCompatActivity {

    private String UserString;
    private String PasswordString;
    private String Token;
    IResult mResultCallback = null;
    VolleyService mVolleyService;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);




        loadInfo();
        Log.d("Token",Token);

        if (Token.equals("")){ //El token no existe

            Intent intent = new Intent(MainPage.this, LoginActivity.class);
            startActivity(intent);




        }else if (checkToken(Token)){ // El token responde

        }else {

            Log.d("Token guardado",Token);

            Intent intent = new Intent(MainPage.this, CalendarPage.class);
            startActivity(intent);
        }





    }

    private boolean checkToken(String Token) {

        initVolleyCallback();


        mVolleyService = new VolleyService(mResultCallback,this);
        mVolleyService.getDataVolley("GETCALL","https://azrav-webapp-apl05.azurewebsites.net/api/Profile",Token);
        Log.d("hola", mResultCallback.toString());



        return true;

    }

    void initVolleyCallback(){
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType,JSONObject response) {
                Log.d("", "Volley requester " + requestType);
                Log.d("", "Volley JSON post" + response);

            }

            @Override
            public void notifyError(String requestType,VolleyError error) {
                Log.d("", "Volley requester " + requestType);
                Log.d("", "Volley JSON post" + "That didn't work!");

            }
        };
    }

    public void loadInfo(){
        SharedPreferences preferences = getSharedPreferences("Credentials", Context.MODE_PRIVATE);

        UserString = preferences.getString("user","");
        PasswordString = preferences.getString("password","");
        Token = preferences.getString("token","");


    }
/*
    */


}
