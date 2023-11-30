package com.example.educationalgp.Repository;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.educationalgp.ApplicationClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StudentRepository {
    private FirebaseAuth firebaseAuth;

    public StudentRepository() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public LiveData<FirebaseUser> getLoggedInUser() {
        MutableLiveData<FirebaseUser> userLiveData = new MutableLiveData<>();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        userLiveData.postValue(currentUser);
        return userLiveData;
    }

    public void login(String username, final onAuthenticationListener listener) {
        firebaseAuth.signInWithEmailAndPassword(username, ApplicationClass.DEFAULT_STUDENT_PASSWORD)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listener.onSuccess();
                    } else {
                        listener.onFailure(task.getException().getMessage());
                    }
                });
    }

    public void signup(String username, final onAuthenticationListener listener) {
        firebaseAuth.createUserWithEmailAndPassword(username, ApplicationClass.DEFAULT_STUDENT_PASSWORD)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listener.onSuccess();
                    } else {
                        listener.onFailure(task.getException().getMessage());
                    }

                });
    }
    public interface onAuthenticationListener {
        void onSuccess();

        void onFailure(String errorMessage);
    }
}


