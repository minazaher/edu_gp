package com.example.educationalgp.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.educationalgp.Model.Teacher;
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

    public void getTeacherByEmail(String email, TeacherRepository.TeacherCallback callback){
         teacherRepository.getTeacherByEmail(email,callback);
    }
    public void addStudentToTeacher(String code, String studentName){
        teacherRepository.addStudentToTeacher(code,studentName);
    }
}
