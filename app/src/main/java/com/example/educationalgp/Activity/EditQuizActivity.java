package com.example.educationalgp.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.educationalgp.Model.Question;
import com.example.educationalgp.Model.Quiz;
import com.example.educationalgp.Repository.QuizRepository;
import com.example.educationalgp.ViewModel.QuizViewModel;
import com.example.educationalgp.databinding.ActivityEditQuizBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;
import java.util.List;

public class EditQuizActivity extends AppCompatActivity {

    ActivityEditQuizBinding binding;
    private QuizViewModel quizViewModel;
    Question oldQuestion, newQuestion ;
    String teacherId = "";
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditQuizBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        quizViewModel = new QuizViewModel();
        initializeMisc();

        boolean comingFromQuiz = getIntent().getBooleanExtra("quiz", false);
        teacherId = getIntent().getStringExtra("teacherId");
        if (comingFromQuiz){
        oldQuestion = (Question) getIntent().getSerializableExtra("question");
        }

        binding.questionImage.setOnClickListener(v -> {
            Intent intent = new Intent(EditQuizActivity.this, QuizActivity.class);
            intent.putExtra("isTeacher", true);
            startActivity(intent);
        });

        if (oldQuestion != null) {
            setQuestion();
        }
        binding.imageSave.setOnClickListener(v -> {
            newQuestion = getNewQuestion();
            updateQuiz();
        });

    }

    private void updateQuiz(){
            quizViewModel.loadQuiz("un2less1", new QuizRepository.OnQuizFetchListener() {
                @Override
                public void onQuizFetched(Quiz quiz) {
                    quiz.getQuestionList().remove(oldQuestion);
                    quiz.getQuestionList().add(newQuestion);
                    quiz.setId(quiz.getId().concat("teacherId"));
                    System.out.println(quiz.getQuestionList().get(0));
                    quizViewModel.createQuiz(quiz);
                }

                @Override
                public void onQuizFetchFailure(String errorMessage) {
                    Toast.makeText(EditQuizActivity.this, "fml" + errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
    }
    private Question getNewQuestion() {
        String head = binding.etQuestionTitle.getText().toString();
        String ch1 = binding.layoutQuestionAnswer1.getText().toString();
        String ch2 = binding.layoutQuestionAnswer1.getText().toString();
        String ch3 = binding.layoutQuestionAnswer1.getText().toString();
        String ch4 = binding.layoutQuestionAnswer1.getText().toString();
        String rightAnswer = binding.etRightAnswer.getText().toString();

        return new Question(head,ch1, ch2,ch3,ch4,rightAnswer);
    }

    private void setQuestion() {
        binding.etQuestionTitle.setText(oldQuestion.getQuestionText());
        binding.layoutQuestionAnswer1.setText(oldQuestion.getOption_1());
        binding.layoutQuestionAnswer2.setText(oldQuestion.getOption_2());
        if (oldQuestion.isMCQ()){
            binding.layoutQuestionAnswer3.setText(oldQuestion.getOption_3());
            binding.layoutQuestionAnswer4.setText(oldQuestion.getOption_4());
        }
        else {
            binding.layoutQuestionAnswer3.setVisibility(View.GONE);
            binding.layoutQuestionAnswer4.setVisibility(View.GONE);
        }
        binding.etRightAnswer.setText(oldQuestion.getAnswer());
    }


    private void initializeMisc() {
        BottomSheetBehavior<LinearLayout> bottomSheetBehavior = BottomSheetBehavior.from(binding.misc.getRoot());
    }



}