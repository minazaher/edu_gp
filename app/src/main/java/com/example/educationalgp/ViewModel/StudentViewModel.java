package com.example.educationalgp.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.educationalgp.Repository.StudentRepository;
import com.google.firebase.auth.FirebaseUser;

public class StudentViewModel extends ViewModel {
    private final StudentRepository studentRepository;
    private final LiveData<FirebaseUser> loggedInStudent;

    public StudentViewModel() {
        studentRepository = new StudentRepository();
        loggedInStudent = studentRepository.getLoggedInUser();
    }

    public LiveData<FirebaseUser> getLoggedInUser() {
        return loggedInStudent;
    }

    public void loginUser(String username, String code, StudentRepository.onAuthenticationListener listener) {
        studentRepository.login(username, code, listener);
    }

    public void signupUser(String username, String code,StudentRepository.onAuthenticationListener listener) {
        studentRepository.signup(username, code,listener);
    }
}


