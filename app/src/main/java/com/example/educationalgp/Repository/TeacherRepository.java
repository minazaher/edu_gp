package com.example.educationalgp.Repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.educationalgp.Model.Grade;
import com.example.educationalgp.Model.Student;
import com.example.educationalgp.Model.Teacher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

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
        DocumentReference teachersDocumentRef = firebaseFirestore.collection("teachers").document(teacher.getId());

        teachersDocumentRef.set(teacher)
                .addOnCompleteListener(task ->
                        Log.d("Firestore", "DocumentSnapshot added with ID " + teacher.getId()))
                .addOnFailureListener(e ->
                        Log.d("Firestore", "DocumentSnapshot not added because of : " + e.getMessage()));
    }
    private Teacher createNewTeacher(String username, String email, String password) {
        String id = generateTeacherCode();
        return new Teacher(id, username, email, password);
    }

    public static String generateTeacherCode() {
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
                            List<HashMap<String, Object>> studentsData = (List<HashMap<String, Object>>)
                                    document.get("students");
                            List<Student> students = new ArrayList<>();
                            if (studentsData != null) {
                                for (HashMap<String, Object> studentData : studentsData) {
                                    String studentId = (String) studentData.get("id");
                                    String studentUsername = (String) studentData.get("username");
                                    List<Grade> grades = (List<Grade>) studentData.get("grades");
                                    Student student = new Student(studentId, studentUsername , grades);
                                    students.add(student);
                                }
                            }
                            Teacher teacher = new Teacher(code, username, email, password, students);
                            callback.onTeacherLoaded(teacher);
                            return;
                        }
                    } else {
                        Log.w("Firestore", "Error getting documents: ", task.getException());
                    }
                });
    }
    private void getTeacherByCode(String code, TeacherCallback callback) {
        CollectionReference teachersCollection = firebaseFirestore.collection("teachers");
        teachersCollection.document(code).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()){
                       Teacher teacher =  documentSnapshot.toObject(Teacher.class);
                       callback.onTeacherLoaded(teacher);
                    }
            }
        });
    }

    public void updateStudentGradeForTeacher(String code, String studentName, Grade grade){
        getTeacherByCode(code, teacher -> {
            System.out.println(teacher.getStudents().toString());
            Optional<Student> student = teacher.getStudents().stream()
                    .filter(s -> s.getUsername().equals(studentName))
                    .findFirst();
            student.ifPresent(value -> value.getGrades().add(grade));
            FirebaseFirestore.getInstance().collection("teachers").document(teacher.getId())
                    .set(teacher);
        });
    }
    private List<Student> getTeacherStudents(Object studentNamesObj) {
        List<Student> students = new ArrayList<>();
        if (studentNamesObj instanceof List<?>) {
            List<?> studentNamesList = (List<?>) studentNamesObj;

            for (Object obj : studentNamesList) {
                if (obj instanceof String) {
                    students.add((Student) obj);
                }
            }
        }
        return students;
    }

    public void addStudentToTeacher(String code, Student student){
        if(!isTeacherArrayContainStudent(code, student)){
            addStudentToTeacherArray(code, student);
        }

    }

    private void addStudentToTeacherArray(String code, Student student){
        firebaseFirestore.collection("teachers")
                .document(code).update("students", FieldValue.arrayUnion(student))
                .addOnSuccessListener(unused -> System.out.println("Student Added!"))
                .addOnFailureListener(e -> System.out.println("Failed Due To:" + e.getMessage()));
    }
    private boolean isTeacherArrayContainStudent(String code, Student student) {
        final boolean[] isContaining = {false};
        CollectionReference teachersCollection = firebaseFirestore.collection("teachers");
        teachersCollection.whereEqualTo("id", code).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            List<Student> students = document.toObject(Teacher.class).getStudents();
                            isContaining[0] = students.contains(student);
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
