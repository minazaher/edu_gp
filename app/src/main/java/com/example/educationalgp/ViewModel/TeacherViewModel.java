package com.example.educationalgp.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.educationalgp.Model.Grade;
import com.example.educationalgp.Model.Student;
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
    public void getTeacherById(String id, TeacherRepository.TeacherCallback callback){
        teacherRepository.getTeacherById(id, callback);
    }
    public void addStudentToTeacher(String code, Student student){
        teacherRepository.addStudentToTeacher(code,student);
    }

    public void getTeacherIDs(TeacherRepository.TeacherIdCallBack callBack){
        teacherRepository.getTeachersIDs(callBack);
    }
    public void updateStudentGrade(String code, String studentName, Grade grade){
        teacherRepository.updateStudentGradeForTeacher(code, studentName, grade);
    }

}
