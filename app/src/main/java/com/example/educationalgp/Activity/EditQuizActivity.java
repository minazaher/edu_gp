package com.example.educationalgp.Activity;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.educationalgp.Model.Question;
import com.example.educationalgp.Model.Quiz;
import com.example.educationalgp.Repository.QuestionRepository;
import com.example.educationalgp.Repository.QuizRepository;
import com.example.educationalgp.ViewModel.QuestionViewModel;
import com.example.educationalgp.ViewModel.QuizViewModel;
import com.example.educationalgp.databinding.ActivityEditQuizBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.UUID;


public class EditQuizActivity extends AppCompatActivity {

    ActivityEditQuizBinding binding;
    private QuizViewModel quizViewModel;
    QuestionViewModel questionViewModel;
    Question oldQuestion, newQuestion ;
    String teacherId = "";
    String imgUrlBeforeSaving = "";
    String quizId;
    private static final int REQUEST_CODE_STORAGE_PERMISSION = 1;
    public ActivityResultLauncher<String> selectPhoto;

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditQuizBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        questionViewModel = new QuestionViewModel();
        quizViewModel = new QuizViewModel();
        initializeMisc();

        selectPhoto = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            binding.imageDeleteImage.setVisibility(View.VISIBLE);
            binding.questionImage.setImageURI(result);
            binding.questionImage.setVisibility(View.VISIBLE);
            binding.questionImageCard.setVisibility(View.VISIBLE);
            saveQuestionImage(result);
        });

        boolean comingFromQuiz = getIntent().getBooleanExtra("quiz", false);
        teacherId = getIntent().getStringExtra("teacherId");

        if (comingFromQuiz){
             oldQuestion = (Question) getIntent().getSerializableExtra("question");
             quizId = getIntent().getStringExtra("quizId");
        }


        if (oldQuestion != null) {
            setQuestion();
        }
        binding.imageSave.setOnClickListener(v -> {
            newQuestion = getNewQuestion();
            updateQuiz(new onQuizUpdateCompleted() {
                @Override
                public void onUpdateCompleted() {
                    Toast.makeText(EditQuizActivity.this, "تم تعديل الاختبار", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditQuizActivity.this, QuizActivity.class);
                    intent.putExtra("isTeacher", true);
                    intent.putExtra("teacherId", teacherId);
                    intent.putExtra("quizId", quizId);
                    startActivity(intent);
                }
            });
        });
        binding.imageDeleteImage.setOnClickListener(v -> {
            imgUrlBeforeSaving = "";
            binding.questionImage.setImageDrawable(null);
        });

        binding.imageBack.setOnClickListener(v ->
                getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(EditQuizActivity.this, TeacherProfileActivity.class);
                intent.putExtra("teacherId", teacherId);
                startActivity(intent);
            }
        })
        );

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(EditQuizActivity.this, TeacherProfileActivity.class);
                intent.putExtra("teacherId", teacherId);
                startActivity(intent);
            }
        });

    }


    private void updateQuiz(onQuizUpdateCompleted callback){
            quizViewModel.loadQuiz(quizId, new QuizRepository.OnQuizFetchListener() {
                @Override
                public void onQuizFetched(Quiz quiz) {
                    quiz.getQuestionList().removeIf(question -> question.getQuestionText().equals(oldQuestion.getQuestionText()));
                    quiz.getQuestionList().add(newQuestion);
                    if (!editedBySameTeacher(quiz.getId(),teacherId)){
                        quiz.setId(quiz.getId().concat(teacherId));
                        quizId = quiz.getId().concat(teacherId);
                    }
                    quizViewModel.createQuiz(quiz);
                    callback.onUpdateCompleted();
                }
                @Override
                public void onQuizFetchFailure(String errorMessage) {
                    Toast.makeText(EditQuizActivity.this, "لم يتم تعديل الاختبار بسبب : " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
    }

    interface onQuizUpdateCompleted{
        void onUpdateCompleted();
    }
    private boolean editedBySameTeacher(String quizId, String teacherId){
        return quizId.contains(teacherId);
    }
    private Question getNewQuestion() {
        String head = binding.etQuestionTitle.getText().toString();
        String ch1 = binding.layoutQuestionAnswer1.getText().toString();
        String ch2 = binding.layoutQuestionAnswer2.getText().toString();
        String ch3 = binding.layoutQuestionAnswer3.getText().toString();
        String ch4 = binding.layoutQuestionAnswer4.getText().toString();
        String rightAnswer = binding.etRightAnswer.getText().toString();
        String url = imgUrlBeforeSaving;

        Question question = new Question(head,ch1, ch2,ch3,ch4,rightAnswer);
        if (!url.isEmpty()){
            question.setImgUrl(url);
        }
        return question;
    }

    private void setQuestion() {
        binding.etQuestionTitle.setText(oldQuestion.getQuestionText());
        binding.layoutQuestionAnswer1.setText(oldQuestion.getOption_1());
        binding.layoutQuestionAnswer2.setText(oldQuestion.getOption_2());
        setImage();
        binding.etRightAnswer.setText(oldQuestion.getAnswer());

        if (oldQuestion.isMCQ())
            setAsMCQ();
        else
            setAsTorF();

    }

    private void setAsMCQ(){
        binding.layoutQuestionAnswer3.setText(oldQuestion.getOption_3());
        binding.layoutQuestionAnswer4.setText(oldQuestion.getOption_4());
    }
    private void setAsTorF(){
        binding.layoutQuestionAnswer3.setVisibility(View.GONE);
        binding.layoutQuestionAnswer4.setVisibility(View.GONE);
    }
    private void setImage(){
        if (oldQuestion.getImgUrl() == null){
            binding.questionImage.setVisibility(View.GONE);
            binding.imageDeleteImage.setVisibility(View.GONE);
            binding.questionImageCard.setVisibility(View.GONE);
        }
        else {
            binding.questionImage.setVisibility(View.VISIBLE);
            binding.imageDeleteImage.setVisibility(View.VISIBLE);
            binding.questionImageCard.setVisibility(View.VISIBLE);
            Glide.with(this).asBitmap().load(oldQuestion.getImgUrl()).into(binding.questionImage);
        }
    }


    private void initializeMisc() {
        BottomSheetBehavior<LinearLayout> bottomSheetBehavior = BottomSheetBehavior.from(binding.misc.getRoot());
        binding.misc.layoutClearFields.setOnClickListener(v -> clearAllFields());
        binding.misc.layoutAddImage.setOnClickListener(view -> {
            if (!isPermissionGranted())
                grantPermission();
            else
                selectImage();
        });

    }

    private void clearAllFields() {
        binding.etQuestionTitle.setText("");
        binding.layoutQuestionAnswer1.setText("");
        binding.layoutQuestionAnswer2.setText("");
        binding.layoutQuestionAnswer3.setText("");
        binding.layoutQuestionAnswer4.setText("");
        binding.etRightAnswer.setText("");
    }


    public void selectImage() {
        selectPhoto.launch("image/*");
    }
    private boolean isPermissionGranted() {
        return ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }
    private void grantPermission() {
        ActivityCompat.requestPermissions(EditQuizActivity.this, new String[]
                {Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_STORAGE_PERMISSION);

    }

    private void saveQuestionImage(Uri result ){
        questionViewModel.saveQuestionImage(result, UUID.randomUUID().toString(), new QuestionRepository.onImageSaved() {
            @Override
            public void onSuccess(String downloadUrl) {
                imgUrlBeforeSaving = downloadUrl;
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(EditQuizActivity.this, "لا يمكن حفظ الصورة بسبب : " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                selectImage();
            else {
                Toast.makeText(this, "تم رفض الاذن!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}