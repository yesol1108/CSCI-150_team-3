package com.example.team3.csci150.loanapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class GroupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        String newString;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                newString= null;
            } else {
                newString= extras.getString("USER");
            }
        } else {
            newString= (String) savedInstanceState.getSerializable("USER");
        }
    }


}
