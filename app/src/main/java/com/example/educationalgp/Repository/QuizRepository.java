package com.example.educationalgp.Repository;

import androidx.annotation.NonNull;

import com.example.educationalgp.Model.Quiz;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class QuizRepository {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference quizCollection = db.collection("quizzes");

    public void createQuiz(Quiz quiz) {
        quizCollection.document(quiz.getId()).set(quiz)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        System.out.println("quiz added");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("quiz not added cus" +e.getMessage());
                    }
                });

    }

    public void getQuizById(String quizId, OnQuizFetchListener listener) {
        quizCollection.document(quizId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Quiz quiz = documentSnapshot.toObject(Quiz.class);
                        listener.onQuizFetched(quiz);
                    } else {
                        listener.onQuizFetchFailure("Quiz not found");
                    }
                })
                .addOnFailureListener(e -> listener.onQuizFetchFailure(e.getMessage()));
    }


    public interface OnQuizFetchListener {
        void onQuizFetched(Quiz quiz);

        void onQuizFetchFailure(String errorMessage);
    }


}
