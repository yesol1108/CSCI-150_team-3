package com.example.yesol.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.net.HttpURLConnection;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    EditText input_name,input_email,input_pwd,input_pwdck;
    String name,email,pwd,pwdck;
    String URL = "ec2-52-53-157-82.us-west-1.compute.amazonaws.com/CSCI150/appdata.php";
    HashMap<String, String> hashMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        input_name = (EditText) findViewById(R.id.input_name);
        input_email = (EditText) findViewById(R.id.input_email);
        input_pwd = (EditText) findViewById(R.id.input_pwd);
        input_pwdck = (EditText) findViewById(R.id.input_pwdck);
    }

    public void create_account(View view) {
        name = input_name.getText().toString();
        email = input_email.getText().toString();
        pwd = input_pwd.getText().toString();
        pwdck = input_pwdck.getText().toString();

        if(pwd.equals(pwdck))
        {

        }else {

        }
    }



}
