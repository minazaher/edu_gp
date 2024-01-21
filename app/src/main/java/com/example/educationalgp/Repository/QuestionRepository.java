package com.example.educationalgp.Repository;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.educationalgp.Model.Question;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class QuestionRepository {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference questionCollection = db.collection("questions");
    StorageReference storageRef;
    public QuestionRepository() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

    }

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

    public void saveImage(Uri uri, String name, onImageSaved listener){
        StorageReference imageRef = storageRef.child("images").child(name);
        imageRef.putFile(uri)
                .addOnSuccessListener(taskSnapshot -> {
                    imageRef.getDownloadUrl().addOnSuccessListener(uri1 -> {
                        String downloadUrl = uri1.toString();
                        listener.onSuccess(downloadUrl);
                    });
                }).addOnFailureListener(e -> listener.onFailure(e.getMessage()));
    }

    public interface OnQuestionOperationListener {
        void onQuestionOperationSuccess(String successMessage, String questionId);
        void onQuestionOperationFailure(String errorMessage);
    }

    public interface OnQuestionFetchListener {
        void onQuestionFetched(Question question);
        void onQuestionFetchFailure(String errorMessage);
    }
    public interface onImageSaved {
        void onSuccess(String downloadUrl);
        void onFailure(String error);

    }
}
