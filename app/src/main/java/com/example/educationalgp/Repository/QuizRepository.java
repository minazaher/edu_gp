package com.example.educationalgp.Repository;

import androidx.annotation.NonNull;

import com.example.educationalgp.Activity.EditQuizActivity;
import com.example.educationalgp.Model.Question;
import com.example.educationalgp.Model.Quiz;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class QuizRepository {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference quizCollection = db.collection("quizzes");

    public void createQuiz(Quiz quiz, EditQuizActivity.onQuizUpdateCompleted callback) {
        quizCollection.document(quiz.getId()).set(quiz)
                .addOnCompleteListener(task -> {
                    System.out.println("quiz added");
                    callback.onUpdateCompleted(quiz);
                })
                .addOnFailureListener(e -> System.out.println("quiz not added cus" +e.getMessage()));

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

    public void updateQuiz(Question oldQuestion, Question newQuestion, String id) {
        deleteQuestion(oldQuestion, id);
        addQuestion(newQuestion, id);
    }

    private void addQuestion(Question newQuestion, String id) {
        quizCollection.document(id).update("questions", FieldValue.arrayUnion(newQuestion));
    }

    private void deleteQuestion(Question oldQuestion, String id) {
        quizCollection.document(id).update("questions", FieldValue.arrayRemove(oldQuestion));
    }

    public void getQuizForTeacher(String quizId, String teacherCode, OnQuizFetchListener listener){
        quizCollection.document(quizId.concat(teacherCode)).get().addOnSuccessListener(documentSnapshot -> {
            Quiz quiz = documentSnapshot.toObject(Quiz.class);
            listener.onQuizFetched(quiz);
        }).addOnFailureListener(e -> getQuizById(quizId, listener));
    }


    public interface OnQuizFetchListener {
        void onQuizFetched(Quiz quiz);

        void onQuizFetchFailure(String errorMessage);
    }


}
