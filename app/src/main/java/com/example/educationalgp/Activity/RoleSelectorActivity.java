package com.example.educationalgp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.educationalgp.databinding.ActivityRoleSelectorBinding;

public class RoleSelectorActivity extends AppCompatActivity {

    ActivityRoleSelectorBinding roleSelectorBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        roleSelectorBinding = ActivityRoleSelectorBinding.inflate(getLayoutInflater());
        setContentView(roleSelectorBinding.getRoot());


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