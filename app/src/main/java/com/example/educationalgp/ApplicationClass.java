package com.example.educationalgp;

import android.app.Application;


import com.example.educationalgp.Model.Question;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


public class ApplicationClass extends Application {
    public static String DEFAULT_STUDENT_PASSWORD = "NO_PASSWORD";

    public static final String EMAIL_REGEX_PATTERN =
            "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    public FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    public FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    public FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    public static boolean isValidEmail(String e) {
        return Pattern.matches(EMAIL_REGEX_PATTERN, e);
    }

}
