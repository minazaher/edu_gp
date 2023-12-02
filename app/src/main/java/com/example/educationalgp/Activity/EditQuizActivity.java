package com.example.educationalgp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.example.educationalgp.Model.Question;
import com.example.educationalgp.Model.Quiz;
import com.example.educationalgp.ViewModel.QuizViewModel;
import com.example.educationalgp.databinding.ActivityEditQuizBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;
import java.util.List;

public class EditQuizActivity extends AppCompatActivity {

    ActivityEditQuizBinding binding;
    private QuizViewModel quizViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditQuizBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        quizViewModel = new QuizViewModel();
        initializeMisc();
    }


    private void initializeMisc() {
        BottomSheetBehavior<LinearLayout> bottomSheetBehavior = BottomSheetBehavior.from(binding.misc.getRoot());

    }



}