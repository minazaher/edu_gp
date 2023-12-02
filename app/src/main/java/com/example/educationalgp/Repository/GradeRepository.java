package com.example.educationalgp.Repository;

import androidx.annotation.NonNull;

import com.example.educationalgp.Model.Grade;
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

    public GradeRepository(){
        firebaseFirestore = FirebaseFirestore.getInstance();
        studentRepository = new StudentRepository();
    }

    public void addNewGrade(Grade grade){
        DocumentReference documentReference = firebaseFirestore.collection("grades").document(grade.getId());

        documentReference.set(grade)
                .addOnCompleteListener(task -> studentRepository.addStudentGrade(studentRepository.getLoggedInUser(), grade.getId()))
                .addOnFailureListener(e -> System.out.println("cannot add grade due to : " + e.getMessage()));
    }
}
