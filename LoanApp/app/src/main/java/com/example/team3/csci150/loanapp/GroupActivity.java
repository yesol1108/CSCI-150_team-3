package com.example.team3.csci150.loanapp;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class GroupActivity extends AppCompatActivity {
    EditText et;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        et = (EditText) findViewById(R.id.editText2);
        pref = this.getSharedPreferences("login", 0);
        editor = pref.edit();
//        et.setText(pref.getString("name",""));
//        if (savedInstanceState == null) {
//            Bundle extras = getIntent().getExtras();
//            if(extras == null) {
//                tv= null;
//            } else {
//                tv.setText(extras.getString("USER"));
//            }
//        } else {
//            tv.setText((String) savedInstanceState.getSerializable("USER"));
//        }
    }


}
