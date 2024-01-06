package com.example.educationalgp.Repository;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

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
                            List<HashMap<String, Object>> studentsData = (List<HashMap<String, Object>>) document.get("students");
                            List<Student> students = new ArrayList<>();
                            if (studentsData != null) {
                                for (HashMap<String, Object> studentData : studentsData) {
                                    String studentId = (String) studentData.get("id");
                                    String studentUsername = (String) studentData.get("username");
                                    List <HashMap<String, Object>> grades = (List<HashMap<String, Object>>) studentData.get("grades");
                                   List<Grade> gradeList = new ArrayList<>();
                                    if (grades != null){
                                        for (HashMap<String, Object> grade : grades) {
                                            String gradeId = (String) grade.get("id");
                                            long mark = (long) grade.get("mark");
                                            double percent = (double) grade.get("percentage");
                                            String name = (String) grade.get("studentName");
                                            String c = (String) grade.get("teacherCode");
                                            Grade grade1 = new Grade(c, name, (int) mark, (float) percent);
                                            grade1.setId(gradeId);
                                            gradeList.add(grade1);
                                        }
                                    }

                                    Student student = new Student(studentId, studentUsername , gradeList);
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

    public void getTeacherById(String id, TeacherCallback callback) {
        CollectionReference teachersCollection = firebaseFirestore.collection("teachers");
        teachersCollection.whereEqualTo("id", id).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String email = document.getString("email");
                            String username = document.getString("username");
                            String password = document.getString("password");
                            List<HashMap<String, Object>> studentsData = (List<HashMap<String, Object>>) document.get("students");
                            List<Student> students = new ArrayList<>();
                            if (studentsData != null) {
                                for (HashMap<String, Object> studentData : studentsData) {
                                    String studentId = (String) studentData.get("id");
                                    String studentUsername = (String) studentData.get("username");
                                    List <HashMap<String, Object>> grades = (List<HashMap<String, Object>>) studentData.get("grades");
                                    List<Grade> gradeList = new ArrayList<>();
                                    assert grades != null;
                                    for (HashMap<String, Object> grade : grades) {
                                        String gradeId = (String) grade.get("id");
                                        long mark = (long) grade.get("mark");
                                        double percent = (double) grade.get("percentage");
                                        String name = (String) grade.get("studentName");
                                        String c = (String) grade.get("teacherCode");
                                        Grade grade1 = new Grade(c, name, (int) mark, (float) percent);
                                        grade1.setId(gradeId);
                                        gradeList.add(grade1);
                                    }
                                    Student student = new Student(studentId, studentUsername , gradeList);
                                    students.add(student);
                                }
                            }
                            Teacher teacher = new Teacher(id, username, email, password, students);
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
        teachersCollection.document(code).get().addOnCompleteListener(task -> {
                DocumentSnapshot documentSnapshot = task.getResult();
                if (documentSnapshot.exists()){
                   Teacher teacher =  documentSnapshot.toObject(Teacher.class);
                   callback.onTeacherLoaded(teacher);
                }
        });
    }

    public void getTeachersIDs(TeacherIdCallBack callBack){
        firebaseFirestore.collection("teachers")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<String> ids = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            ids.add(document.getId());
                        }
                        callBack.onTeachersIdsLoaded(ids);
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
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

    public void addStudentToTeacher(String code, Student student) {
        checkIfTeacherContainsStudent(code, student, contains -> {
            if (!contains) {
                addStudentToTeacherArray(code, student);
                System.out.println("The teacher array contains the student name");
            }
        });
    }

    private void addStudentToTeacherArray(String code, Student student){
        List<Grade> grades = new ArrayList<>();
        firebaseFirestore.collection("teachers")
                .document(code).update("students", FieldValue.arrayUnion(new Student(UUID.randomUUID().toString(), student.getUsername(), grades)))
                .addOnSuccessListener(unused -> System.out.println("Student Added!"))
                .addOnFailureListener(e -> System.out.println("Failed Due To:" + e.getMessage()));
    }

    private void checkIfTeacherContainsStudent(String code, Student student, OnContainsStudentListener listener) {
        CollectionReference teachersCollection = firebaseFirestore.collection("teachers");
        teachersCollection.whereEqualTo("id", code).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        boolean isContaining = false;
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            List<Student> students = document.toObject(Teacher.class).getStudents();
                            boolean isUsernameAbsent = students.stream()
                                    .noneMatch(s -> s.getUsername().equals(student.getUsername()));
                            if (!isUsernameAbsent) {
                                isContaining = true;
                                break;
                            }
                        }
                        listener.onContainsStudent(isContaining);
                    } else {
                        Log.w("Firestore", "Error getting documents: ", task.getException());
                        listener.onContainsStudent(false); // Assuming not found in case of an error
                    }
                });
    }

    private interface OnContainsStudentListener {
        void onContainsStudent(boolean contains);
    }


    public interface TeacherCallback {
        void onTeacherLoaded(Teacher teacher);
    }

    public interface TeacherIdCallBack {
        void onTeachersIdsLoaded(List<String> IDs);
    }

}
