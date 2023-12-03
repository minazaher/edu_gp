package com.example.educationalgp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.educationalgp.Model.Question;
import com.example.educationalgp.Model.Quiz;
import com.example.educationalgp.Repository.QuizRepository;
import com.example.educationalgp.ViewModel.QuizViewModel;
import com.example.educationalgp.databinding.ActivityQuizBinding;

import java.util.List;

public class QuizActivity extends AppCompatActivity {
    ActivityQuizBinding binding;
    String quizId;
    QuizViewModel quizViewModel;
    List<Question> questionList;
    String selectedAns = "", correctAns = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        quizViewModel = new QuizViewModel();
        quizId = getIntent().getStringExtra("quizId");
        loadQuiz(counter -> {
            checkAnswer(selectedAns, correctAns);
            counter++;
            getQuestion(counter);

        });



    }

    private void getQuestion(int i) {
        Question currentQuestion = questionList.get(i);
        correctAns = currentQuestion.getAnswer();
        binding.question.setText(currentQuestion.getQuestionText());
        binding.option1.setText(currentQuestion.getOption_1());
        binding.option2.setText(currentQuestion.getOption_2());
        binding.option3.setText(currentQuestion.getOption_3());
        binding.option4.setText(currentQuestion.getOption_4());

        binding.option1.setOnClickListener(v -> selectedAns = binding.option1.getText().toString());
        binding.option2.setOnClickListener(v -> selectedAns = binding.option2.getText().toString());
        binding.option3.setOnClickListener(v -> selectedAns = binding.option3.getText().toString());
        binding.option4.setOnClickListener(v -> selectedAns = binding.option4.getText().toString());

    }
    private void checkAnswer(String selectedAnswer, String correctAnswer) {
        boolean isCorrect = selectedAnswer.equals(correctAnswer);
        System.out.println("selected answer is : " + selectedAnswer);
        System.out.println("correctAnswer  is : " + correctAnswer);

        if (isCorrect) {
            Toast.makeText(this, "Correct Answer!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Incorrect Answer!", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadQuiz(onQuestionSolvedCallback callback) {
       quizViewModel.loadQuiz("test", new QuizRepository.OnQuizFetchListener() {
            @Override
            public void onQuizFetched(Quiz quiz) {
                if (quiz != null){
                    questionList = quiz.getQuestionList();
                    int currentQuestionNumber =0;
                    questionList.add(new Question());
                    getQuestion(currentQuestionNumber);
                    binding.submitQuestion.setOnClickListener(v -> {
                        callback.onQuestionSolved(currentQuestionNumber);
                    });
                }
            }

            @Override
            public void onQuizFetchFailure(String errorMessage) {
                Toast.makeText(QuizActivity.this, "No Quiz For this Lesson", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private interface onQuestionSolvedCallback{
        void onQuestionSolved(int counter);
    }
}
