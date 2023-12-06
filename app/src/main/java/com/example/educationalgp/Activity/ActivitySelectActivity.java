package com.example.educationalgp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.educationalgp.Model.Quiz;
import com.example.educationalgp.R;
import com.example.educationalgp.databinding.ActivitySelectActivityBinding;

public class ActivitySelectActivity extends AppCompatActivity {
    String activityId = "", teacherCode = "";
    ActivitySelectActivityBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        activityId = getIntent().getStringExtra("activityId");
        teacherCode = getIntent().getStringExtra("teacherCode");

        binding.cardGame.setOnClickListener(v -> goToGame());

        binding.cardQuiz.setOnClickListener(v -> goToQuiz());
    }

    private void goToQuiz() {
        Intent intent = new Intent(ActivitySelectActivity.this, QuizActivity.class);
        intent.putExtra("teacherId", teacherCode);
        intent.putExtra("quizId", activityId);
        startActivity(intent);
    }

    private void goToGame() {
        Intent intent = new Intent(ActivitySelectActivity.this, GameActivity.class);
        intent.putExtra("gameId", activityId);
        startActivity(intent);
    }
}