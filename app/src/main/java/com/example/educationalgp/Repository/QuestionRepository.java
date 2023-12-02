package com.example.educationalgp.Repository;

import com.example.educationalgp.Model.Question;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class QuestionRepository {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference questionCollection = db.collection("questions");

    public void createQuestion(Question question, OnQuestionOperationListener listener) {
        questionCollection.add(question)
                .addOnSuccessListener(documentReference -> {
                    question.setId(documentReference.getId());
                    listener.onQuestionOperationSuccess("Question created successfully", question.getId());
                })
                .addOnFailureListener(e -> listener.onQuestionOperationFailure("Failed to create question: " + e.getMessage()));
    }

       public void getQuestionById(String questionId, OnQuestionFetchListener listener) {
        questionCollection.document(questionId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Question question = documentSnapshot.toObject(Question.class);
                        listener.onQuestionFetched(question);
                    } else {
                        listener.onQuestionFetchFailure("Question not found");
                    }
                })
                .addOnFailureListener(e -> listener.onQuestionFetchFailure(e.getMessage()));
    }

    // Edit an existing question
    public void editQuestion(Question question, OnQuestionOperationListener listener) {
        questionCollection.document(question.getId()).set(question)
                .addOnSuccessListener(aVoid -> listener.onQuestionOperationSuccess("Question edited successfully", question.getId()))
                .addOnFailureListener(e -> listener.onQuestionOperationFailure("Failed to edit question: " + e.getMessage()));
    }


    public interface OnQuestionOperationListener {
        void onQuestionOperationSuccess(String successMessage, String questionId);
        void onQuestionOperationFailure(String errorMessage);
    }

    public interface OnQuestionFetchListener {
        void onQuestionFetched(Question question);
        void onQuestionFetchFailure(String errorMessage);
    }
}
