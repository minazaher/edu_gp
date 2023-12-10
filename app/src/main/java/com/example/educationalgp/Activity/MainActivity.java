package com.example.educationalgp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.educationalgp.Model.Question;
import com.example.educationalgp.R;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);


        new Handler().postDelayed(() -> {
            Intent intent = new Intent(MainActivity.this, RoleSelectorActivity.class);
            startActivity(intent);
        },3000);

    }

    private Question createTorF(String head, String ans){
        return new Question(head, "صح", "خطأ", "", "", ans);
    }
}