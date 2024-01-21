package com.example.educationalgp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.educationalgp.Model.Quiz;
import com.example.educationalgp.R;
import com.example.educationalgp.databinding.ActivitySelectActivityBinding;

public class ActivitySelectActivity extends AppCompatActivity {
    String activityId = "", teacherCode = "";
    ActivitySelectActivityBinding binding;
    String studentName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        activityId = getIntent().getStringExtra("activityId");
        teacherCode = getIntent().getStringExtra("teacherCode");
        studentName = getIntent().getStringExtra("studentName");

        binding.cardGame.setOnClickListener(v -> goToGame());

        binding.cardQuiz.setOnClickListener(v -> goToQuiz());
    }

    private void goToQuiz() {
        Intent intent = new Intent(ActivitySelectActivity.this, QuizActivity.class);
        intent.putExtra("teacherId", teacherCode);
        intent.putExtra("quizId", activityId);
        intent.putExtra("studentName", studentName);

        startActivity(intent);
    }

    private void goToGame() {
        Intent intent = new Intent(ActivitySelectActivity.this, GameActivity.class);
        intent.putExtra("gameId", activityId);
        startActivity(intent);
    }
}