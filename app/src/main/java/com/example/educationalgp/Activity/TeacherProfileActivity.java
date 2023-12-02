package com.example.educationalgp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.educationalgp.Model.Teacher;
import com.example.educationalgp.ViewModel.TeacherViewModel;
import com.example.educationalgp.databinding.ActivityTeacherProfileBinding;

import java.util.Arrays;

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
        binding.fabEditQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeacherProfileActivity.this, EditQuizActivity.class);
                startActivity(intent);
            }
        });
        getTeacherData();
    }



    private void getTeacherData() {
        String email = getIntent().getStringExtra("email");
        teacherViewModel.getTeacherByEmail(email, teacher -> {
            accountOwner = teacher;
            setTeacherUI();
            System.out.println(teacher.toString());
            getTeacherStudents();
        });

    }
    private void setTeacherUI() {
        binding.tvTeacherCode.setText(accountOwner.getId());
        binding.tvTeacherNameProfile.setText(accountOwner.getUsername());
        binding.tvStudentsCount.setText(String.valueOf(accountOwner.getStudents().length));
    }

    private void getTeacherStudents(){
        String email = getIntent().getStringExtra("email");
        System.out.println(Arrays.deepToString(new String[]{Arrays.toString(accountOwner.getStudents())}));
    }
}