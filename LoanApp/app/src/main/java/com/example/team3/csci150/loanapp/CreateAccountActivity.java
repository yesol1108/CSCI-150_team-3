package com.example.team3.csci150.loanapp;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Debug;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class CreateAccountActivity extends AppCompatActivity {

    TextView result,link_login;
    EditText input_name,input_email,input_pwd,input_pwdck;
    String name,email,pwd,pwdck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        result = (TextView) findViewById(R.id.result);

        input_name = (EditText) findViewById(R.id.input_name);
        input_email = (EditText) findViewById(R.id.input_email);
        input_pwd = (EditText) findViewById(R.id.input_pwd);
        input_pwdck = (EditText) findViewById(R.id.input_pwdck);
        link_login = (TextView) findViewById(R.id.link_login);
    }

    public void create_account(View view) {
        name = input_name.getText().toString();
        email = input_email.getText().toString();
        pwd = input_pwd.getText().toString();
        pwdck = input_pwdck.getText().toString();

        if(pwd.equals(pwdck))
        {
            CreatingAccount ca = new CreatingAccount();
            ca.execute(name,email,pwd);

        }else {

        }
    }

    class CreatingAccount extends AsyncTask<String, String, String> {
        ProgressDialog createProgress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            createProgress = ProgressDialog.show(CreateAccountActivity.this, "Loading...", null, true, true);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            createProgress.dismiss();
            result.setText(s);
            Log.d("RESPONSE", "POST response - " + result);
        }

        @Override
        protected String doInBackground(String... params) {

            String name = (String)params[0];
            String email = (String)params[1];
            String pswd = (String)params[2];

            String server = "http://52.53.157.82/csci150/php/appdata.php";
            String postParams = "name=" + name + "&email=" + email + "&pswd=" + pswd;

            try {
                java.net.URL url = new URL(server);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(4000);
                httpURLConnection.setConnectTimeout(4000);
                httpURLConnection.setRequestMethod("POST");
                //httpURLConnection.setRequestProperty("content-type", "application/json");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParams.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                int responCode = httpURLConnection.getResponseCode();
                Log.d("RESPONSE", "POST response code - " + responCode);
                InputStream inputStream;
                if(responCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }else {
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }

                bufferedReader.close();

                return sb.toString();

            } catch (Exception e) {
                Log.d("RESPONSE", "InsertData: Error ", e);

                return new String("Error: " + e.getMessage());
            }

        }
    }
}
