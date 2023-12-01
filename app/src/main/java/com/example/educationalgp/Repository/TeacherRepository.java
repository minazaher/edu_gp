package com.example.educationalgp.Repository;

import android.util.Log;

import com.example.educationalgp.ApplicationClass;
import com.example.educationalgp.Model.Student;
import com.example.educationalgp.Model.Teacher;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TeacherRepository {

    private static final String ALLOWED_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom secureRandom = new SecureRandom();
    private final FirebaseAuth firebaseAuth;
    private final FirebaseFirestore firebaseFirestore;

    public TeacherRepository() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    public void login(String username, String password, final StudentRepository.onAuthenticationListener listener) {
        firebaseAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listener.onSuccess();
                    } else {
                        listener.onFailure(task.getException().getMessage());
                    }
                });
    }

    public void signup(String username,String email, String password,final StudentRepository.onAuthenticationListener listener) {
        firebaseAuth.createUserWithEmailAndPassword(username, ApplicationClass.DEFAULT_STUDENT_PASSWORD)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listener.onSuccess();
                        saveToFirestore(username, email, password);
                    } else {
                        listener.onFailure(task.getException().getMessage());
                    }

                });
    }
    private void saveToFirestore(String username, String email, String password){
        Teacher teacher = createNewTeacher(username, email, password);
        DocumentReference studentsCollection = firebaseFirestore.collection("teachers").document(teacher.getId());

        studentsCollection.set(teacher)
                .addOnCompleteListener(task ->
                        Log.d("Firestore", "DocumentSnapshot added with ID " + teacher.getId()))
                .addOnFailureListener(e ->
                        Log.d("Firestore", "DocumentSnapshot not added because of : " + e.getMessage()));
    }

    private Teacher createNewTeacher(String username, String email, String password){
        String id = generateTeacherCodeCode();
        return new Teacher(id, username, email, password);
    }



    public static String generateTeacherCodeCode() {
        StringBuilder stringBuilder = new StringBuilder(6);

        for (int i = 0; i < 6; i++) {
            int randomIndex = secureRandom.nextInt(ALLOWED_CHARACTERS.length());
            char randomChar = ALLOWED_CHARACTERS.charAt(randomIndex);
            stringBuilder.append(randomChar);
        }

        return stringBuilder.toString();
    }
}