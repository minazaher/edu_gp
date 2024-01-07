package com.example.educationalgp.Activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.educationalgp.Model.Grade;
import com.example.educationalgp.Model.Question;
import com.example.educationalgp.Model.Quiz;
import com.example.educationalgp.R;
import com.example.educationalgp.Repository.GradeRepository;
import com.example.educationalgp.Repository.QuizRepository;
import com.example.educationalgp.ViewModel.QuizViewModel;
import com.example.educationalgp.databinding.ActivityQuizBinding;
import com.google.android.material.snackbar.Snackbar;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class QuizActivity extends AppCompatActivity {
    ActivityQuizBinding binding;
    String quizId = "", teacherId = "";
    QuizViewModel quizViewModel;
    List<Question> questionList;
    String selectedAns = "", correctAns = "";
    int correctAnswers, incorrectAnswers;
    Quiz currentQuiz;
    GradeRepository gradeRepository;
    String studentName = "";
    boolean isTeacher;
    private int currentQuestionNumber = 10;
    private AlertDialog dialogViewResult;

    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;
    private final long COUNTDOWN_INTERVAL = 1000;
    private final long SHORT_QUIZ_TIME = 1200000; // 20 minutes in milliseconds
    private final long LONG_QUIZ_TIME = 2100000 ; // 35 minutes in milliseconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        quizViewModel = new QuizViewModel();
        gradeRepository = new GradeRepository();

        quizId = getIntent().getStringExtra("quizId");
        teacherId = getIntent().getStringExtra("teacherId");
        isTeacher = getIntent().getBooleanExtra("isTeacher", false);
        studentName = getIntent().getStringExtra("studentName");

        if (isTeacher) {
            setTeacherUI();
            loadQuizForTeacher();
        } else {
            setStudentUI();
            loadQuizForStudent();
        }

    }


    private void confirmBackToProfile() {
        Snackbar snackbar = Snackbar.make(binding.getRoot(), "هل تريد العودة للحساب؟", Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        snackbar.setAction("تاكيد", v -> {
            Intent intent = new Intent(QuizActivity.this, TeacherProfileActivity.class);
            intent.putExtra("teacherId", teacherId);
            finish();
            startActivity(intent);
        });

        textView.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
        snackbar.show();
    }

    private void setTeacherUI() {
        binding.layoutTeacherButtons.setVisibility(View.VISIBLE);
        binding.studentButton.setVisibility(View.GONE);
        binding.tvNextQuestion.setOnClickListener(v -> moveNext());
        binding.tvPreviousQuestion.setOnClickListener(v -> movePrevious());
        binding.imgEdit.setVisibility(View.VISIBLE);
        binding.imgEdit.setOnClickListener(v -> selectQuestionToEdit());
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                confirmBackToProfile();
            }
        });
    }

    private void setStudentUI() {
        binding.layoutTeacherButtons.setVisibility(View.GONE);
        binding.studentButton.setVisibility(View.VISIBLE);
        binding.imgEdit.setVisibility(View.GONE);

    }

    private void startTimer(int size) {
        timeLeftInMillis = size == 15 ? SHORT_QUIZ_TIME : LONG_QUIZ_TIME;
        countDownTimer = new CountDownTimer(timeLeftInMillis, COUNTDOWN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                binding.tvSubmitQuestion.setActivated(false);
                binding.tvSubmitQuiz.setActivated(false);
                binding.tvTimerTextView.setTextColor(getResources().getColor(R.color.red));
                calculateStudentMark();
                saveGrade();
                showResult();
                new Handler().postDelayed(() -> {
                    Intent intent = new Intent(QuizActivity.this,StudentProfileActivity.class);
                    startActivity(intent);
                    finish();
                },3000);

                Toast.makeText(QuizActivity.this, "انتهى الوقت!", Toast.LENGTH_SHORT).show();
            }
        }.start();
    }

    private void updateCountDownText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format("%02d:%02d", minutes, seconds);
        binding.tvTimerTextView.setText(timeLeftFormatted);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    private void selectQuestionToEdit() {
        Question question = currentQuiz.getQuestionList().get(currentQuestionNumber);
        Intent intent = new Intent(QuizActivity.this, EditQuizActivity.class);
        intent.putExtra("question", question);
        intent.putExtra("quiz", true);
        intent.putExtra("quizId", currentQuiz.getId());
        intent.putExtra("teacherId", teacherId);

        startActivity(intent);
    }

    private void resetSelectedAnswer() {
        binding.option1.setBackgroundResource(0);
        binding.option2.setBackgroundResource(0);
        binding.option3.setBackgroundResource(0);
        binding.option4.setBackgroundResource(0);
        selectedAns = "";
    }

    private void getQuestionForStudent(int c) {
        Question currentQuestion = questionList.get(c);
        correctAns = currentQuestion.getAnswer();
        binding.question.setText(currentQuestion.getQuestionText());
        binding.option1.setText(currentQuestion.getOption_1());
        binding.option2.setText(currentQuestion.getOption_2());

        if (isMCQ(currentQuestion)) {
            binding.option3.setText(currentQuestion.getOption_3());
            binding.option4.setText(currentQuestion.getOption_4());
            binding.layoutOption3.setVisibility(View.VISIBLE);
            binding.layoutOption4.setVisibility(View.VISIBLE);
        } else {
            binding.layoutOption3.setVisibility(View.GONE);
            binding.layoutOption4.setVisibility(View.GONE);
        }

        if (currentQuestion.getImgUrl() == null) {
            binding.imgQuestion.setVisibility(View.GONE);
            binding.questionImage.setVisibility(View.GONE);
        } else {
            binding.imgQuestion.setVisibility(View.VISIBLE);
            binding.questionImage.setVisibility(View.VISIBLE);
            Glide.with(this).asBitmap().load(currentQuestion.getImgUrl()).into(binding.imgQuestion);
        }
        if (c + 1 == questionList.size()) {
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

    private void getQuestionForTeacher(int c) {
        Question currentQuestion = questionList.get(c);
        correctAns = currentQuestion.getAnswer();
        binding.question.setText(currentQuestion.getQuestionText());
        binding.option1.setText(currentQuestion.getOption_1());
        binding.option2.setText(currentQuestion.getOption_2());

        if (isMCQ(currentQuestion)) {
            binding.option3.setText(currentQuestion.getOption_3());
            binding.option4.setText(currentQuestion.getOption_4());
            binding.layoutOption3.setVisibility(View.VISIBLE);
            binding.layoutOption4.setVisibility(View.VISIBLE);
        } else {
            binding.layoutOption3.setVisibility(View.GONE);
            binding.layoutOption4.setVisibility(View.GONE);
        }

        if (currentQuestion.getImgUrl() == null) {
            binding.imgQuestion.setVisibility(View.GONE);
            binding.questionImage.setVisibility(View.GONE);
        } else {
            binding.imgQuestion.setVisibility(View.VISIBLE);
            binding.questionImage.setVisibility(View.VISIBLE);
            Glide.with(this).asBitmap().load(currentQuestion.getImgUrl()).into(binding.imgQuestion);
        }
        if (c + 1 == questionList.size()) {
            binding.tvSubmitQuestion.setVisibility(View.GONE);
            binding.tvSubmitQuiz.setVisibility(View.VISIBLE);
        }

    }

    private boolean isMCQ(Question currentQuestion) {
        return currentQuestion.getOption_3() != null && currentQuestion.getOption_4() != null;
    }

    private void setCounter(int position, int total) {
        String pos = String.valueOf(position);
        String tot = String.valueOf(total);
        String count = MessageFormat.format("{0}/{1}", pos, tot);
        binding.tvQuizCounter.setText(count);
    }

    private void checkAnswer(String selectedAnswer, String correctAnswer) {
        boolean isCorrect = selectedAnswer.equals(correctAnswer);
        if (isCorrect) {
            Toast.makeText(this, "اجابة صحيحة!", Toast.LENGTH_SHORT).show();
            highlightCorrectAnswer();
            correctAnswers++;
        } else {
            Toast.makeText(this, "اجابة خاطئة!", Toast.LENGTH_SHORT).show();
            highlightCorrectAnswer();
            highlightIncorrectAnswer();
            incorrectAnswers++;
        }

    }

    private void highlightIncorrectAnswer() {
        TextView[] optionViews = {binding.option1, binding.option2, binding.option3, binding.option4};
        for (TextView v : optionViews) {
            if (v.getText().toString().equals(selectedAns)) {
                v.setBackgroundResource(R.color.red);
            }
        }
    }

    private void highlightCorrectAnswer() {
        TextView[] optionViews = {binding.option1, binding.option2, binding.option3, binding.option4};
        for (TextView v : optionViews) {
            if (v.getText().toString().equals(correctAns)) {
                v.setBackgroundResource(R.color.green);
            }
        }
    }

    private void loadQuizForStudent() {
        quizViewModel.getQuizForTeacher(quizId, teacherId, new QuizRepository.OnQuizFetchListener() {
            @Override
            public void onQuizFetched(Quiz quiz) {
                currentQuiz = quiz;
                if (quiz != null) {
                    questionList = quiz.getQuestionList();
                    Collections.shuffle(questionList);
                    getQuestionForStudent(currentQuestionNumber);
                    setCounter(currentQuestionNumber + 1, questionList.size());
                    startTimer(questionList.size());
                    setupButtonListeners();
                } else {
                    loadQuizById(true);
                }
            }

            @Override
            public void onQuizFetchFailure(String errorMessage) {
                Toast.makeText(QuizActivity.this, "لا يوجد اختبار لهذا الدرس", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadQuizById(boolean isStudent) {
        quizViewModel.loadQuiz(quizId, new QuizRepository.OnQuizFetchListener() {
            @Override
            public void onQuizFetched(Quiz quiz) {
                currentQuiz = quiz;
                if (quiz != null) {
                    questionList = quiz.getQuestionList();
                    Collections.shuffle(questionList);
                    if (isStudent){
                        startTimer(questionList.size());
                        getQuestionForStudent(currentQuestionNumber);
                    }
                    else
                        getQuestionForTeacher(currentQuestionNumber);
                    setCounter(currentQuestionNumber + 1, questionList.size());
                    setupButtonListeners();
                }
            }

            @Override
            public void onQuizFetchFailure(String errorMessage) {
                Toast.makeText(QuizActivity.this, "لا يوجد اختبار لهذا الدرس", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void loadQuizForTeacher() {
        quizViewModel.getQuizForTeacher(quizId, teacherId, new QuizRepository.OnQuizFetchListener() {
            @Override
            public void onQuizFetched(Quiz quiz) {
                currentQuiz = quiz;

                if (quiz != null) {
                    questionList = quiz.getQuestionList();
                    Collections.shuffle(questionList);
                    getQuestionForTeacher(currentQuestionNumber);
                    setCounter(currentQuestionNumber + 1, questionList.size());
                    moveNextOrFinish();
                } else {
                    loadQuizById(false);
                }
            }

            @Override
            public void onQuizFetchFailure(String errorMessage) {
                Toast.makeText(QuizActivity.this, "لا يوجد اختبار لهذا الدرس", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupButtonListeners() {
        binding.tvSubmitQuestion.setOnClickListener(v -> {
            checkAnswer(selectedAns, correctAns);
            new Handler().postDelayed(() -> {
                resetSelectedAnswer();
                moveNextOrFinish();
            }, 2000);

        });

        binding.tvSubmitQuiz.setOnClickListener(v -> {
            checkAnswer(selectedAns, correctAns);
            calculateStudentMark();
            saveGrade();
            showResult();
        });
    }

    private void moveNextOrFinish() {
        if (currentQuestionNumber < questionList.size() - 1) {
            currentQuestionNumber++; // Move to the next question
            getQuestionForStudent(currentQuestionNumber);
            setCounter(currentQuestionNumber + 1, questionList.size());
        } else {
            binding.tvSubmitQuestion.setEnabled(false); // Disable the question submission button
            binding.tvSubmitQuiz.callOnClick(); // Automatically trigger quiz submission
        }
    }

    private void movePrevious() {
        if (currentQuestionNumber > 0) {
            currentQuestionNumber--; // Move to the previous question
            getQuestionForTeacher(currentQuestionNumber);
            setCounter(currentQuestionNumber + 1, questionList.size());
        } else {
            Toast.makeText(this, "هذا اول سؤال في الاختبار", Toast.LENGTH_SHORT).show();
        }
    }

    private void moveNext() {
        if (currentQuestionNumber < questionList.size() - 1) {
            currentQuestionNumber++; // Move to the next question
            getQuestionForTeacher(currentQuestionNumber);
            setCounter(currentQuestionNumber + 1, questionList.size());
        } else {
            Toast.makeText(this, "هذا اخر سؤال في الاختبار", Toast.LENGTH_SHORT).show();
        }
    }


    private void calculateStudentMark() {
        System.out.println("Correct Answers : " + correctAnswers);
        System.out.println("Incorrect Answers : " + incorrectAnswers);
        System.out.println("percentage is : " + correctAnswers / questionList.size());
    }

    private void saveGrade() {
        System.out.println("Grade Student Name is :" + studentName);
        Grade grade = new Grade(teacherId, studentName, correctAnswers,
                (float) correctAnswers / currentQuiz.getTotalMarks());
        grade.setId(UUID.randomUUID().toString());
        gradeRepository.addNewGrade(studentName, grade);
    }

    private void showResult() {
        if (dialogViewResult == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View view = LayoutInflater.from(this).inflate(R.layout.layout_view_result,
                    (ViewGroup) findViewById(R.id.layout_viewResultContainer));
            builder.setView(view);
            dialogViewResult = builder.create();
            if (dialogViewResult.getWindow() != null) {
                dialogViewResult.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }
            TextView tv_reseult = view.findViewById(R.id.tv_Result);
            String res = correctAnswers + "/" + questionList.size();
            tv_reseult.setText(String.format("درجة الاختبار هي : %s", res));
            view.findViewById(R.id.textBack).setOnClickListener(view1 -> {
                Intent intent = new Intent(QuizActivity.this, StudentProfileActivity.class);
                intent.putExtra("teacherId", teacherId);
                intent.putExtra("studentName", studentName);
                startActivity(intent);
                finish();
            });
        }
        dialogViewResult.show();
    }

}

