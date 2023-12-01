package com.example.educationalgp.Activity;

import static com.example.educationalgp.ApplicationClass.isValidEmail;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.educationalgp.R;
import com.example.educationalgp.Repository.StudentRepository;
import com.example.educationalgp.ViewModel.TeacherViewModel;
import com.example.educationalgp.databinding.ActivitySignUpBinding;

public class SignUpActivity extends AppCompatActivity {
    ActivitySignUpBinding binding;
    String username = "", email = "", password = "", confirmPassword = "";
    TeacherViewModel teacherViewModel ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        teacherViewModel = new TeacherViewModel();

        binding.btnSignup.setOnClickListener(v -> signup());
        binding.btnLoginNow.setOnClickListener(v -> goToLoginPage());
    }

    private void signup() {
        getTeacherData();
        teacherViewModel.signupTeacher(username, email, password, new StudentRepository.onAuthenticationListener() {
            @Override
            public void onSuccess() {
                goToLoginPage();
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(SignUpActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void goToLoginPage() {
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        intent.putExtra("isStudent", false);
        startActivity(intent);
    }

    private void getTeacherData() {
        getUsername();
        getEmail();
        getPassword();
    }

    private void getUsername() {
        String n = binding.etTeacherNameSignup.getText().toString();
        if (TextUtils.isEmpty(n)){
            binding.etTeacherNameSignup.setError("من فضلك ادخل اسمك هنا");
        }
        else {
            username = n;
        }
    }

    private void getEmail(){
        String e = binding.etTeacherEmailSignup.getText().toString();
        if(TextUtils.isEmpty(e)){
            binding.etTeacherEmailSignup.setError("من فضلك ادخل البريد الالكتروني هنا");
        }
        else if(!isValidEmail(e)){
            binding.etTeacherEmailSignup.setError("من فضلك تأكد من صيغة البريد الالكتروني");
        }
        else
            email = e;
    }

    private void getPassword(){
        String p = binding.etTeacherPasswordSignup.getText().toString();
        String confirm = binding.etTeacherConfirmPasswordSignup.getText().toString();
        if(TextUtils.isEmpty(p)){
            binding.etTeacherPasswordSignup.setError("من فضلك ادخل كلمة المرور هنا");
        }
        if(TextUtils.isEmpty(confirm)){
            binding.etTeacherConfirmPasswordSignup.setError("من فضلك ادخل كلمة المرور مرة اخرى هنا");
        }
        if(!confirm.equals(p)){
            binding.etTeacherPasswordSignup.setError("من فضلك تحقق من تطابق كلمتي المرور");
        }
        else{
            password = p;
        }
    }



}