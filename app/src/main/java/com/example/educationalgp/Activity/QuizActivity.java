package com.example.educationalgp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.educationalgp.Model.Grade;
import com.example.educationalgp.Model.Question;
import com.example.educationalgp.Model.Quiz;
import com.example.educationalgp.Repository.GradeRepository;
import com.example.educationalgp.Repository.QuizRepository;
import com.example.educationalgp.ViewModel.GradeViewModel;
import com.example.educationalgp.ViewModel.QuizViewModel;
import com.example.educationalgp.databinding.ActivityQuizBinding;

import java.text.MessageFormat;
import java.util.List;

public class QuizActivity extends AppCompatActivity {
    ActivityQuizBinding binding;
    String quizId;
    QuizViewModel quizViewModel;
    List<Question> questionList;
    String selectedAns = "", correctAns = "";
    int correctAnswers, incorrectAnswers;
    Quiz currentQuiz;
    GradeRepository gradeRepository;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        quizViewModel = new QuizViewModel();
        gradeRepository = new GradeRepository();
        quizId = getIntent().getStringExtra("quizId");
        loadQuiz(counter -> {
            checkAnswer(selectedAns, correctAns);
            counter++;
            getQuestion(counter);
            setCounter(counter+1, currentQuiz.getQuestionList().size());

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
        if(questionList.get(i).getImgUrl() != null){
//            Glide.with(this).asDrawable().into(binding.imgQuestion);
        }
        if (i+1 == questionList.size()){
            binding.tvSubmitQuestion.setVisibility(View.GONE);
            binding.tvSubmitQuiz.setVisibility(View.VISIBLE);
        }

        binding.option1.setOnClickListener(v -> selectedAns = binding.option1.getText().toString());
        binding.option2.setOnClickListener(v -> selectedAns = binding.option2.getText().toString());
        binding.option3.setOnClickListener(v -> selectedAns = binding.option3.getText().toString());
        binding.option4.setOnClickListener(v -> selectedAns = binding.option4.getText().toString());

    }
    private void setCounter(int position, int total){
        String pos = String.valueOf(position);
        String tot = String.valueOf(total);
        String count = MessageFormat.format("{0}/{1}", pos, tot);
        binding.tvQuizCounter.setText(count);
    }
    private void checkAnswer(String selectedAnswer, String correctAnswer) {
        boolean isCorrect = selectedAnswer.equals(correctAnswer);
        System.out.println("selected answer is : " + selectedAnswer);
        System.out.println("correctAnswer  is : " + correctAnswer);

        if (isCorrect) {
            Toast.makeText(this, "Correct Answer!", Toast.LENGTH_SHORT).show();
            correctAnswers++;
        } else {
            Toast.makeText(this, "Incorrect Answer!", Toast.LENGTH_SHORT).show();
            incorrectAnswers++;
        }

    }

    private void loadQuiz(onQuestionSolvedCallback callback) {
       quizViewModel.loadQuiz("test", new QuizRepository.OnQuizFetchListener() {
            @Override
            public void onQuizFetched(Quiz quiz) {
                currentQuiz = quiz;
                if (quiz != null){
                    questionList = quiz.getQuestionList();
                    int currentQuestionNumber =0;
                    questionList.add(new Question());
                    getQuestion(currentQuestionNumber);
                    setCounter(currentQuestionNumber+1, questionList.size());
                    binding.tvSubmitQuestion.setOnClickListener(v -> {
                        callback.onQuestionSolved(currentQuestionNumber);
                    });
                    binding.tvSubmitQuiz.setOnClickListener(v -> {
                        if (currentQuestionNumber+1 == quiz.getTotalMarks()){{
                            calculateStudentMark();
                            saveGrade();
                        }}
                    });
                }
            }

            @Override
            public void onQuizFetchFailure(String errorMessage) {
                Toast.makeText(QuizActivity.this, "No Quiz For this Lesson", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void calculateStudentMark() {
        System.out.println("Correct Answers : " + correctAnswers);
        System.out.println("Incorrect Answers : " + incorrectAnswers);
        System.out.println("percentage is : " + correctAnswers/questionList.size());
    }

    private void saveGrade(){
        Grade grade = new Grade("tu2wdF", "student@gmail.com", correctAnswers,
                (float)correctAnswers/currentQuiz.getTotalMarks());
        grade.setId("NOT NULL");
        gradeRepository.addNewGrade(grade);
    }

    private interface onQuestionSolvedCallback{
        void onQuestionSolved(int counter);
    }
}
