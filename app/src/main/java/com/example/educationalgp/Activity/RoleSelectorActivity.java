package com.example.educationalgp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;

import com.example.educationalgp.databinding.ActivityRoleSelectorBinding;

public class RoleSelectorActivity extends AppCompatActivity {

    ActivityRoleSelectorBinding roleSelectorBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        roleSelectorBinding = ActivityRoleSelectorBinding.inflate(getLayoutInflater());
        setContentView(roleSelectorBinding.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);


        roleSelectorBinding.studentCard.setOnClickListener(v -> {
            Intent intent = new Intent(RoleSelectorActivity.this, LoginActivity.class);
            intent.putExtra("isStudent", true);
            startActivity(intent);
        });

        roleSelectorBinding.teacherCard.setOnClickListener(v -> {
            Intent intent = new Intent(RoleSelectorActivity.this, LoginActivity.class);
            intent.putExtra("isStudent", false);
            startActivity(intent);
        });

    }
}