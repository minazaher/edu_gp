package com.example.educationalgp.Repository;

import androidx.annotation.NonNull;

import com.example.educationalgp.Model.Grade;
import com.example.educationalgp.ViewModel.TeacherViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.PublicKey;

public class GradeRepository {
    FirebaseFirestore firebaseFirestore;
    StudentRepository studentRepository;
    TeacherViewModel teacherViewModel ;

    public GradeRepository(){
        firebaseFirestore = FirebaseFirestore.getInstance();
        studentRepository = new StudentRepository();
        teacherViewModel = new TeacherViewModel();
    }

    public void addNewGrade(Grade grade){
        DocumentReference documentReference = firebaseFirestore.collection("grades").document(grade.getId());

        documentReference.set(grade)
                .addOnCompleteListener(task -> {
                    studentRepository.addStudentGrade(grade.getId());
                    teacherViewModel.updateStudentGrade(grade.getTeacherCode(), grade.getStudentName(), grade);
                })
                .addOnFailureListener(e -> System.out.println("cannot add grade due to : " + e.getMessage()));
    }
}
