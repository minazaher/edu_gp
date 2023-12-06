package com.example.educationalgp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.educationalgp.Adapter.StudentsAdapter;
import com.example.educationalgp.Model.Grade;
import com.example.educationalgp.Model.Student;
import com.example.educationalgp.Model.Teacher;
import com.example.educationalgp.Repository.TeacherRepository;
import com.example.educationalgp.ViewModel.TeacherViewModel;
import com.example.educationalgp.databinding.ActivityTeacherProfileBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

        binding.fabEditQuiz.setOnClickListener(v -> {
            Intent intent = new Intent(TeacherProfileActivity.this, StudentProfileActivity.class);
            intent.putExtra("isTeacher", true);
            intent.putExtra("teacherId", accountOwner.getId());
            startActivity(intent);
        });

        getTeacherData();

    }



    private void getTeacherData() {
        String email = getIntent().getStringExtra("email");
        teacherViewModel.getTeacherByEmail(email, teacher -> {
            if(teacher != null){
                accountOwner = teacher;
                setTeacherUI();
                getTeacherStudents();
            }
            else {
                String id = getIntent().getStringExtra("teacherId");
                teacherViewModel.getTeacherById(id, teacher1 -> {
                    accountOwner = teacher1;
                    setTeacherUI();
                    getTeacherStudents();
                });
            }

        });

    }
    private void setTeacherUI() {
        binding.tvTeacherCode.setText(accountOwner.getId());
        binding.tvTeacherNameProfile.setText(accountOwner.getUsername());

        if (accountOwner.getStudents() != null) {
                initializeStudentRecView();
        }

        binding.tvStudentsCount.setText(String.valueOf(accountOwner.getStudents().size()));
    }

    private List<Student> getTeacherStudents(){
        return accountOwner.getStudents();
    }
    private void initializeStudentRecView(){
        RecyclerView recyclerView = binding.studentRecyclerView;
        recyclerView.setAdapter(new StudentsAdapter(getTeacherStudents()));
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
    }

}