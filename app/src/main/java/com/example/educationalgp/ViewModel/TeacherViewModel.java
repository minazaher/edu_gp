package com.example.educationalgp.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.educationalgp.Repository.StudentRepository;
import com.example.educationalgp.Repository.TeacherRepository;
import com.google.firebase.auth.FirebaseUser;

public class TeacherViewModel extends ViewModel {
    private final TeacherRepository teacherRepository;
    public TeacherViewModel() {
        teacherRepository = new TeacherRepository();
    }


    public void loginTeacher(String email,String password, StudentRepository.onAuthenticationListener listener) {
        teacherRepository.login(email,password,listener);
    }

    public void signupTeacher(String username, String email,String password,StudentRepository.onAuthenticationListener listener) {
        teacherRepository.signup(username,email, password,listener);
    }
}
