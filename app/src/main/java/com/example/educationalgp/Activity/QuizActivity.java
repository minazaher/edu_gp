package com.example.educationalgp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.educationalgp.Model.Grade;
import com.example.educationalgp.Model.Question;
import com.example.educationalgp.Model.Quiz;
import com.example.educationalgp.R;
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
    private int currentQuestionNumber = 0;

    GradeRepository gradeRepository;
    String unit = "", lesson = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        quizViewModel = new QuizViewModel();
        gradeRepository = new GradeRepository();
        unit = getIntent().getStringExtra("unit");
        lesson = getIntent().getStringExtra("lesson");
        quizId = unit.concat(lesson);
        loadQuiz();
    }
    private void resetSelectedAnswer(){
        binding.option1.setBackgroundResource(0);
        binding.option2.setBackgroundResource(0);
        binding.option3.setBackgroundResource(0);
        binding.option4.setBackgroundResource(0);
        selectedAns = "";
    }

    private void getQuestion(int c) {
        Question currentQuestion = questionList.get(c);
        correctAns = currentQuestion.getAnswer();
        binding.question.setText(currentQuestion.getQuestionText());
        binding.option1.setText(currentQuestion.getOption_1());
        binding.option2.setText(currentQuestion.getOption_2());

        if (isMCQ(currentQuestion)){
            binding.option3.setText(currentQuestion.getOption_3());
            binding.option4.setText(currentQuestion.getOption_4());
        }
        else {
            binding.layoutOption3.setVisibility(View.GONE);
            binding.layoutOption4.setVisibility(View.GONE);
        }

        if (c+1 == questionList.size()){
            binding.tvSubmitQuestion.setVisibility(View.GONE);
            binding.tvSubmitQuiz.setVisibility(View.VISIBLE);
        }

        View[] optionViews = {binding.option1, binding.option2, binding.option3, binding.option4};
        String[] optionTexts = {
                binding.option1.getText().toString(),
                binding.option2.getText().toString(),
                binding.option3.getText().toString(),
                binding.option4.getText().toString()
        };

        for (int i = 0; i < optionViews.length; i++) {
            int finalI = i;
            optionViews[i].setOnClickListener(v -> {
                selectedAns = optionTexts[finalI];
                optionViews[finalI].setBackgroundResource(R.drawable.selected_answer_bg);
                for (int j = 0; j < optionViews.length; j++) {
                    if (j != finalI) {
                        optionViews[j].setBackgroundResource(0);
                    }
                }
            });
        }

    }

    private boolean isMCQ(Question currentQuestion) {
        return currentQuestion.getOption_3() !=null && currentQuestion.getOption_4() !=null;
    }

    private void setCounter(int position, int total){
        String pos = String.valueOf(position);
        String tot = String.valueOf(total);
        String count = MessageFormat.format("{0}/{1}", pos, tot);
        binding.tvQuizCounter.setText(count);
    }
    private void checkAnswer(String selectedAnswer, String correctAnswer) {
        boolean isCorrect = selectedAnswer.equals(correctAnswer);

        if (isCorrect) {
            Toast.makeText(this, "Correct Answer!", Toast.LENGTH_SHORT).show();
            correctAnswers++;
        } else {
            Toast.makeText(this, "Incorrect Answer!", Toast.LENGTH_SHORT).show();
            incorrectAnswers++;
        }

    }

    private void loadQuiz() {
        quizViewModel.loadQuiz("un2less1", new QuizRepository.OnQuizFetchListener() {
            @Override
            public void onQuizFetched(Quiz quiz) {
                currentQuiz = quiz;
                if (quiz != null) {
                    questionList = quiz.getQuestionList();
                    getQuestion(currentQuestionNumber);
                    setCounter(currentQuestionNumber + 1, questionList.size());
                    setupButtonListeners();
                }
            }

            @Override
            public void onQuizFetchFailure(String errorMessage) {
                Toast.makeText(QuizActivity.this, "No Quiz For this Lesson", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupButtonListeners() {
        binding.tvSubmitQuestion.setOnClickListener(v -> {
            checkAnswer(selectedAns, correctAns);
            resetSelectedAnswer();
            moveNextOrFinish();
        });

        binding.tvSubmitQuiz.setOnClickListener(v -> {
            calculateStudentMark();
            saveGrade();
        });
    }

    private void moveNextOrFinish() {
        if (currentQuestionNumber < questionList.size() - 1) {
            currentQuestionNumber++; // Move to the next question
            getQuestion(currentQuestionNumber);
            setCounter(currentQuestionNumber + 1, questionList.size());
        } else {
            binding.tvSubmitQuestion.setEnabled(false); // Disable the question submission button
            binding.tvSubmitQuiz.callOnClick(); // Automatically trigger quiz submission
        }
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
}
