package com.example.educationalgp.Repository;


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.educationalgp.ApplicationClass;
import com.example.educationalgp.Model.Grade;
import com.example.educationalgp.Model.Student;
import com.example.educationalgp.ViewModel.TeacherViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class StudentRepository {
    private final FirebaseAuth firebaseAuth;
    private final FirebaseFirestore firebaseFirestore;
    TeacherViewModel teacherViewModel;

    public StudentRepository() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        teacherViewModel = new TeacherViewModel();
    }

    public LiveData<FirebaseUser> getLoggedInUser() {
        MutableLiveData<FirebaseUser> userLiveData = new MutableLiveData<>();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        userLiveData.postValue(currentUser);
        return userLiveData;
    }

    public void login(String username,String teacherCode, final onAuthenticationListener listener) {
        firebaseAuth.signInWithEmailAndPassword(username+"@gmail.com", ApplicationClass.DEFAULT_STUDENT_PASSWORD)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listener.onSuccess();
                        Student student = new Student(username);
                        student.setId(UUID.randomUUID().toString());
                        student.setGrades(new ArrayList<>());;
                        teacherViewModel.addStudentToTeacher(teacherCode, student);
                    } else {
                        listener.onFailure(task.getException().getMessage());
                    }
                });
    }

        public void signup(String username,String teacherCode, final onAuthenticationListener listener) {
        firebaseAuth.createUserWithEmailAndPassword(username+"@gmail.com", ApplicationClass.DEFAULT_STUDENT_PASSWORD)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listener.onSuccess();
                        saveToFirestore(username);
                        teacherViewModel.addStudentToTeacher(teacherCode, new Student(username));

                    } else {
                        listener.onFailure(task.getException().getMessage());
                    }

                });
    }

    private void saveToFirestore(String username){
       Student student = createNewStudent(username);
        DocumentReference studentsCollection = firebaseFirestore.collection("students").
                document(student.getUsername());

        studentsCollection.set(student)
                .addOnCompleteListener(task ->
                        Log.d("Firestore", "DocumentSnapshot added with ID " + student.getId()))
                .addOnFailureListener(e ->
                        Log.d("Firestore", "DocumentSnapshot not added because of : " + e.getMessage()));
    }

    private Student createNewStudent(String username){
        String id = UUID.randomUUID().toString();
        List<Grade> quizGrades = new ArrayList<>();
        return new Student(id, username, quizGrades);
    }

    public void addStudentGrade(String username, String gradeId) {
        DocumentReference studentsCollection = firebaseFirestore.collection("students")
                .document(username);
        studentsCollection.update("grades", FieldValue.arrayUnion(gradeId));
    }


    public interface onAuthenticationListener {
        void onSuccess();

        void onFailure(String errorMessage);
    }
}


