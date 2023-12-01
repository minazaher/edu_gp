package com.example.educationalgp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.educationalgp.Model.Teacher;
import com.example.educationalgp.ViewModel.TeacherViewModel;
import com.example.educationalgp.databinding.ActivityTeacherProfileBinding;

public class TeacherProfileActivity extends AppCompatActivity {

    ActivityTeacherProfileBinding binding;
    TeacherViewModel teacherViewModel;
    Teacher accountOwner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTeacherProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        teacherViewModel = new TeacherViewModel();

        getTeacherData();
    }



    private void getTeacherData() {
        String email = getIntent().getStringExtra("email");
        teacherViewModel.getTeacherByEmail(email, teacher -> {
            accountOwner = teacher;
            setTeacherUI();
            System.out.println(teacher.toString());
        });

    }
    private void setTeacherUI() {
        binding.tvTeacherCode.setText(accountOwner.getId());
        binding.tvTeacherNameProfile.setText(accountOwner.getUsername());
        binding.tvStudentsCount.setText(String.valueOf(accountOwner.getStudents().length));
    }
}