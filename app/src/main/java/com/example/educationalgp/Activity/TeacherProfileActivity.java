package com.example.educationalgp.Activity;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.educationalgp.Adapter.StudentsAdapter;
import com.example.educationalgp.Model.Grade;
import com.example.educationalgp.Model.Student;
import com.example.educationalgp.Model.Teacher;
import com.example.educationalgp.Repository.TeacherRepository;
import com.example.educationalgp.ViewModel.TeacherViewModel;
import com.example.educationalgp.databinding.ActivityTeacherProfileBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TeacherProfileActivity extends AppCompatActivity {

    ActivityTeacherProfileBinding binding;
    TeacherViewModel teacherViewModel;
    Teacher accountOwner;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTeacherProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        teacherViewModel = new TeacherViewModel();
        id = getIntent().getStringExtra("teacherId");
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        if (id != null){
            teacherViewModel.getTeacherById(id, teacher1 -> {
                accountOwner = teacher1;
                setTeacherUI();
                getTeacherStudents();
            });
        }
        else {
            getTeacherData();
        }

        binding.fabEditQuiz.setOnClickListener(v -> {
            Intent intent = new Intent(TeacherProfileActivity.this, StudentProfileActivity.class);
            intent.putExtra("isTeacher", true);
            intent.putExtra("teacherId", accountOwner.getId());
            startActivity(intent);
        });


        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                confirmSignout();
            }
        });
    }

    private void confirmSignout() {
        Snackbar snackbar = Snackbar.make(binding.getRoot(), "هل تريد تسجيل الخروج؟", Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        snackbar.setAction("تاكيد", v -> {
            Intent intent = new Intent(TeacherProfileActivity.this, RoleSelectorActivity.class);
            FirebaseAuth.getInstance().signOut();
            finish();
            startActivity(intent);
        });

        textView.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
        snackbar.show();
    }


    private void getTeacherData() {
        String email = getIntent().getStringExtra("email");
        teacherViewModel.getTeacherByEmail(email, teacher -> {
            if(teacher != null){
                accountOwner = teacher;
                setTeacherUI();
                getTeacherStudents();
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
        recyclerView.setAdapter(new StudentsAdapter(
                getTeacherStudents().stream()
                        .filter(x -> !x.getGrades().isEmpty())
                        .collect(Collectors.toList())
        ));
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
    }

}