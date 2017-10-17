package com.example.team3.csci150.loanapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.AsynchronousCloseException;

public class LoginFragment extends TopLevelFragment {
    EditText input_email, input_pwd;
    String email,pwd;
    TextView result;
    Boolean loginChecked;
    CheckBox checkBox;
    SharedPreferences pref;
    SharedPreferences.Editor editor;


    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        result = (TextView) view.findViewById(R.id.result);
        input_email = (EditText) view.findViewById(R.id.input_email);
        input_pwd = (EditText) view.findViewById(R.id.input_pwd);
        checkBox = (CheckBox) view.findViewById(R.id.checkBox);

        pref = getMainActivity().getSharedPreferences("login", 0);
        editor = pref.edit();
        loginChecked = pref.getBoolean("autoLogin", false);

        //if autologin checked, get input
        if(pref.getBoolean("autoLogin", false)) {
            input_email.setText(pref.getString("id", ""));
            input_pwd.setText(pref.getString("pw", ""));
            checkBox.setChecked(true);
        }else {
            email = input_email.getText().toString();
            pwd = input_pwd.getText().toString();
            LoginProcess lp = new LoginProcess();
            lp.execute("http://52.53.157.82/csci150/php/login.php",email,pwd);
        }

        view.findViewById(R.id.login_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = input_email.getText().toString();
                pwd = input_pwd.getText().toString();

                if(email.equals("") && pwd.equals("")) {
                    Toast.makeText(getActivity(), "Please enter email and password", Toast.LENGTH_SHORT).show();
                }else {
                    LoginProcess lp = new LoginProcess();
                    lp.execute("http://52.53.157.82/csci150/php/login.php",email,pwd);
                }
            }
        });

        ((CheckBox) view.findViewById(R.id.checkBox)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    loginChecked = true;
                } else {
                    loginChecked = false;
                    editor.clear();
                    editor.commit();
                }
            }
        });

        return view;
    }

    class LoginProcess extends AsyncTask<String, String, String> {
        ProgressDialog createProgress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            createProgress = ProgressDialog.show(getActivity(), "Loading...", null, true, true);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            createProgress.dismiss();
//            result.setText(s);
            if(s.equals("SUCCESS")) {
                if (loginChecked) {
                    editor.putString("id",email);
                    editor.putString("pw", pwd);
                    editor.putBoolean("autoLogin", true);
                    editor.commit();
                }
                Intent i = new Intent(getMainActivity(), GroupActivity.class);
                editor.putString("name", s);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                getMainActivity().startActivity(i);
            }else{
                result.setText(s);
            }
            Log.d("RESPONSE", "POST response - " + result);
        }
        @Override
        protected String doInBackground(String... params) {

            String server = (String)params[0];
            String email = (String)params[1];
            String pswd = (String)params[2];

//            String server = "http://52.53.157.82/csci150/php/login.php";
            String postParams = "email=" + email + "&pswd=" + pswd;

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
