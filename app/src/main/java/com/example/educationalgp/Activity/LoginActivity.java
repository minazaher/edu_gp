package com.example.educationalgp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.educationalgp.R;
import com.example.educationalgp.Fragment.StudentLoginFragment;
import com.example.educationalgp.Fragment.TeacherLoginFragment;
import com.example.educationalgp.databinding.ActivityLoginBinding;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding activityLoginBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityLoginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(activityLoginBinding.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        boolean isStudent = getIntent().getBooleanExtra("isStudent", false);
        setSuitableInterface(isStudent);

    }

    private void setSuitableInterface(boolean isStudent){
        if(isStudent)
            setStudentInterface();
        else
            setTeacherInterface();
    }


    private void setTeacherInterface(){
        Fragment fragment = new TeacherLoginFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_login, fragment).commit();
    }

    private void setStudentInterface(){
        Fragment fragment = new StudentLoginFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_login, fragment).commit();
    }
}