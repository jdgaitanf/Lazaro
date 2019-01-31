package com.juanito.lazaro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import android.os.AsyncTask;

public class LoginActivity extends AppCompatActivity {

    private EditText UserField;
    private EditText PasswordField;
    private TextView ShowInfo;
    private Button LoginBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        UserField = (EditText)findViewById(R.id.username_id);
        PasswordField = (EditText)findViewById(R.id.password_id);
        ShowInfo = (TextView)findViewById(R.id.info);
        LoginBtn = (Button)findViewById(R.id.login_button);

        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginBtn.setEnabled(false);
                new validate().execute();
            }
        });
    }

    public class validate extends AsyncTask<Void,Void,Void>{
        String response_url = "";
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Connection.Response res = Jsoup.connect("https://azrav-webapp-apl05.azurewebsites.net/Account/Login?ReturnUrl=https://mycrew.avianca.com/callback&client_id=angularclient")
                        .userAgent("Mozila")
                        .timeout(0)
                        .method(Connection.Method.GET)
                        .execute();

                Document docu = res.parse();
                String csrf = docu.select("input[name=__RequestVerificationToken]").val();

                Connection.Response response = Jsoup.connect("https://azrav-webapp-apl05.azurewebsites.net/Account/Login?returnurl=https%3A%2F%2Fmycrew.avianca.com%2Fcallback")
                        .userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/71.0.3578.80 Chrome/71.0.3578.80 Safari/537.36")
                        .data("Email", UserField.getText().toString())
                        .data("Password", PasswordField.getText().toString())
                        .data("__RequestVerificationToken", csrf)
                        .cookies(res.cookies())
                        .method(Connection.Method.POST)
                        .execute();

                response_url = response.url().toString();






                // words = doc.text();

                Log.d("Textoenpag",(String.valueOf(response.statusCode())));
                Log.d("User",UserField.getText().toString());
                Log.d("Pass",PasswordField.getText().toString());
                Log.d("Url",response.url().toString());

            }catch(Exception e){
                Log.d("Error","--");
                e.printStackTrace();


            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d("substring",response_url);
            if (response_url.substring(0,35).equals("https://mycrew.avianca.com/callback")){


                ShowInfo.setText("Entrando");

                SharedPreferences preferences = getSharedPreferences("Credentials", Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("user",UserField.getText().toString());
                editor.putString("password",PasswordField.getText().toString());
                editor.putString("token",response_url.substring(49));
                editor.commit();

                Intent intent = new Intent(LoginActivity.this, CalendarPage.class);
                startActivity(intent);






            }else{
                LoginBtn.setEnabled(true);
                ShowInfo.setText("Email o contraseña inválidos");
            }
        }
    }

}
