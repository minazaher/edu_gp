package com.example.educationalgp.ViewModel;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.educationalgp.Model.Question;
import com.example.educationalgp.Repository.QuestionRepository;

public class QuestionViewModel extends ViewModel {

    private final QuestionRepository questionRepository;
    private MutableLiveData<Question> questionLiveData;

    public QuestionViewModel() {
        questionRepository = new QuestionRepository();
    }

    public LiveData<Question> getQuestionById(String questionId) {
        if (questionLiveData == null) {
            questionLiveData = new MutableLiveData<>();
            loadQuestion(questionId);
        }
        return questionLiveData;
    }

    private void loadQuestion(String questionId) {
        questionRepository.getQuestionById(questionId, new QuestionRepository.OnQuestionFetchListener() {
            @Override
            public void onQuestionFetched(Question question) {
                questionLiveData.setValue(question);
            }

            @Override
            public void onQuestionFetchFailure(String errorMessage) {
                // Handle fetch failure
            }
        });
    }

    public void saveQuestionImage(Uri uri, String name,QuestionRepository.onImageSaved listener){
        questionRepository.saveImage(uri,name,listener);
    }
    public void createQuestion(Question question, QuestionRepository.OnQuestionOperationListener listener) {
        questionRepository.createQuestion(question, listener);
    }

    public void editQuestion(Question question, QuestionRepository.OnQuestionOperationListener listener) {
        questionRepository.editQuestion(question, listener);
    }

}
