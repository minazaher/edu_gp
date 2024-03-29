package com.example.educationalgp.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.educationalgp.Activity.EditQuizActivity;
import com.example.educationalgp.Model.Question;
import com.example.educationalgp.Model.Quiz;
import com.example.educationalgp.Repository.QuizRepository;

public class QuizViewModel extends ViewModel {

    private final QuizRepository quizRepository;

    public QuizViewModel() {
        quizRepository = new QuizRepository();
    }


    public void loadQuiz(String quizId, QuizRepository.OnQuizFetchListener listener) {
        quizRepository.getQuizById(quizId, listener);
    }

    public void getQuizForTeacher(String quizId, String teacherCode, QuizRepository.OnQuizFetchListener listener){
        quizRepository.getQuizForTeacher(quizId, teacherCode, listener);
    }
    public void createQuiz(Quiz quiz, EditQuizActivity.onQuizUpdateCompleted callback) {
        quizRepository.createQuiz(quiz, callback);
    }


    public void updateQuiz(Question oldQuestion, Question newQuestion, String id) {
        quizRepository.updateQuiz(oldQuestion, newQuestion, id);
    }
}
