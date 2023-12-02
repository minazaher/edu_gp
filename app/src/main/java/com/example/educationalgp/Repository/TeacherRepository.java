package com.example.educationalgp.Repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.educationalgp.Model.Teacher;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

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

    public void signup(String username, String email, String password, final StudentRepository.onAuthenticationListener listener) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listener.onSuccess();
                        saveToFirestore(username, email, password);
                    } else {
                        listener.onFailure(task.getException().getMessage());
                    }

                });
    }

    private void saveToFirestore(String username, String email, String password) {
        Teacher teacher = createNewTeacher(username, email, password);
        DocumentReference teacherCollection = firebaseFirestore.collection("teachers").document(teacher.getId());

        teacherCollection.set(teacher)
                .addOnCompleteListener(task ->
                        Log.d("Firestore", "DocumentSnapshot added with ID " + teacher.getId()))
                .addOnFailureListener(e ->
                        Log.d("Firestore", "DocumentSnapshot not added because of : " + e.getMessage()));
    }

    private Teacher createNewTeacher(String username, String email, String password) {
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

    public void getTeacherByEmail(String email, TeacherCallback callback) {
        CollectionReference teachersCollection = firebaseFirestore.collection("teachers");
        teachersCollection.whereEqualTo("email", email).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String code = document.getId();
                            String username = document.getString("username");
                            String password = document.getString("password");
                            Object studentNamesObject = document.get("students");
                            String[] students = getStudentNames(studentNamesObject);
                            Teacher teacher = new Teacher(code, username, email, password, students);
                            callback.onTeacherLoaded(teacher);
                            return;
                        }
                    } else {
                        Log.w("Firestore", "Error getting documents: ", task.getException());
                    }
                });
    }

    private String[] getStudentNames(Object studentNamesObj) {
        ArrayList<String> students = new ArrayList<>();
        if (studentNamesObj instanceof List<?>) {
            List<?> studentNamesList = (List<?>) studentNamesObj;

            for (Object obj : studentNamesList) {
                if (obj instanceof String) {
                    students.add((String) obj);
                }
            }
        }
        return students.toArray(new String[students.size()]);
    }

    public void addStudentToTeacher(String code, String name){
        //If teacher array of student doesn't contain student name
        if(!isTeacherArrayContainStudent(code, name)){
            //Add student
            addStudentToTeacherArray(code, name);
        }

    }

    private void addStudentToTeacherArray(String code,String name){
        firebaseFirestore.collection("teachers")
                .document(code).update("students", FieldValue.arrayUnion(name))
                .addOnSuccessListener(unused -> System.out.println("Student Added!"))
                .addOnFailureListener(e -> System.out.println("Failed Due To:" + e.getMessage()));
    }
    private boolean isTeacherArrayContainStudent(String code, String studentName) {
        final boolean[] isContaining = {false};
        CollectionReference teachersCollection = firebaseFirestore.collection("teachers");
        teachersCollection.whereEqualTo("id", code).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Object studentNamesObject = document.get("students");
                            String[] students = getStudentNames(studentNamesObject);
                            isContaining[0] = Arrays.asList(students).contains(studentName);
                        }
                    } else {
                        Log.w("Firestore", "Error getting documents: ", task.getException());
                    }
                });
        return isContaining[0];
    }

    public interface TeacherCallback {
        void onTeacherLoaded(Teacher teacher);
    }

}
