package com.example.educationalgp;

import android.app.Application;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;


public class ApplicationClass extends Application {
    public static String DEFAULT_STUDENT_PASSWORD = "NO_PASSWORD";
    public FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    public FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    public FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();


}
