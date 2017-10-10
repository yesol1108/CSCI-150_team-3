package com.example.team3.csci150.loanapp;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class CreateAccountFragment extends TopLevelFragment {
    TextView result,link_login;
    EditText input_name,input_email,input_pwd,input_pwdck;
    String name,email,pwd,pwdck;

    public static CreateAccountFragment newInstance() {
        return new CreateAccountFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_create_account, container, false);
        result = (TextView) view.findViewById(R.id.result);

        input_name = (EditText) view.findViewById(R.id.input_name);
        input_email = (EditText) view.findViewById(R.id.input_email);
        input_pwd = (EditText) view.findViewById(R.id.input_pwd);
        input_pwdck = (EditText) view.findViewById(R.id.input_pwdck);
        link_login = (TextView) view.findViewById(R.id.link_login);

        view.findViewById(R.id.link_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMainActivity().resetToplevel(LoginFragment.newInstance());
            }
        });

        view.findViewById(R.id.create_account).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = input_name.getText().toString();
                email = input_email.getText().toString();
                pwd = input_pwd.getText().toString();
                pwdck = input_pwdck.getText().toString();

                if(pwd.equals(pwdck))
                {
                    CreatingAccount ca = new CreatingAccount();
                    ca.execute(name,email,pwd);

                }else {
                    Toast.makeText(getActivity(), "\"Password and Password check are not equal!\"", Toast.LENGTH_SHORT).show();
                    input_pwd.setText("");
                    input_pwd.isFocused();
                    input_pwdck.setText("");
                }
            }
        });

        return view;
    }

    class CreatingAccount extends AsyncTask<String, String, String> {
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
                getMainActivity().resetToplevel(LoginFragment.newInstance());
            }
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
