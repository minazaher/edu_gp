package com.example.educationalgp.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.educationalgp.Model.Quiz;
import com.example.educationalgp.Repository.QuizRepository;

public class QuizViewModel extends ViewModel {

        private final QuizRepository quizRepository;
        private MutableLiveData<Quiz> quizLiveData;

        public QuizViewModel() {
            quizRepository = new QuizRepository();
        }

        public LiveData<Quiz> getQuizById(String quizId) {
            if (quizLiveData == null) {
                quizLiveData = new MutableLiveData<>();
                loadQuiz(quizId);
            }
            return quizLiveData;
        }

        private void loadQuiz(String quizId) {
            quizRepository.getQuizById(quizId, new QuizRepository.OnQuizFetchListener() {
                @Override
                public void onQuizFetched(Quiz quiz) {
                    quizLiveData.setValue(quiz);
                }

                @Override
                public void onQuizFetchFailure(String errorMessage) {
                    // Handle fetch failure
                }
            });
        }

        public void createQuiz(Quiz quiz){
            quizRepository.createQuiz(quiz);
        }


}
