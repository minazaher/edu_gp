package com.example.educationalgp.Repository;


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.educationalgp.ApplicationClass;
import com.example.educationalgp.Model.Student;
import com.example.educationalgp.ViewModel.TeacherViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
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
        firebaseAuth.signInWithEmailAndPassword(username, ApplicationClass.DEFAULT_STUDENT_PASSWORD)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listener.onSuccess();
                        teacherViewModel.addStudentToTeacher(teacherCode, username);
                    } else {
                        listener.onFailure(task.getException().getMessage());
                    }
                });
    }

        public void signup(String username,String teacherCode, final onAuthenticationListener listener) {
        firebaseAuth.createUserWithEmailAndPassword(username, ApplicationClass.DEFAULT_STUDENT_PASSWORD)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listener.onSuccess();
                        saveToFirestore(username);
                        teacherViewModel.addStudentToTeacher(teacherCode, username);

                    } else {
                        listener.onFailure(task.getException().getMessage());
                    }

                });
    }

    private void saveToFirestore(String username){
       Student student = createNewStudent(username);
        DocumentReference studentsCollection = firebaseFirestore.collection("students").document(student.getId());

        studentsCollection.set(student)
                .addOnCompleteListener(task ->
                        Log.d("Firestore", "DocumentSnapshot added with ID " + student.getId()))
                .addOnFailureListener(e ->
                        Log.d("Firestore", "DocumentSnapshot not added because of : " + e.getMessage()));
    }

    private Student createNewStudent(String username){
        String id = UUID.randomUUID().toString();
        Map<String, Integer> quizGrades = new HashMap<>();
        quizGrades.put("Un1gen", 0);
        quizGrades.put("Un1less1", 0);
        quizGrades.put("Un1less2", 0);
        quizGrades.put("Un1less3", 0);
        return new Student(id, username, quizGrades);
    }
    public interface onAuthenticationListener {
        void onSuccess();

        void onFailure(String errorMessage);
    }
}


