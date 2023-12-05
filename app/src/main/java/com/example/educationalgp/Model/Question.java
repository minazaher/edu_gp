package com.example.educationalgp.Model;

import com.google.firebase.firestore.DocumentId;

import java.io.Serializable;

public class Question implements Serializable {
    @DocumentId
    private String id;
    private String questionText;
    private String option_1, option_2, option_3, option_4;
    private String answer;
    private String imgUrl;

    public Question() {
    }

    public Question(String questionText, String option_1, String option_2, String option_3, String option_4, String answer) {
        this.questionText = questionText;
        this.option_1 = option_1;
        this.option_2 = option_2;
        this.option_3 = option_3;
        this.option_4 = option_4;
        this.answer = answer;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOption_1() {
        return option_1;
    }

    public void setOption_1(String option_1) {
        this.option_1 = option_1;
    }

    public String getOption_2() {
        return option_2;
    }

    public void setOption_2(String option_2) {
        this.option_2 = option_2;
    }

    public String getOption_3() {
        return option_3;
    }

    public void setOption_3(String option_3) {
        this.option_3 = option_3;
    }

    public String getOption_4() {
        return option_4;
    }

    public void setOption_4(String option_4) {
        this.option_4 = option_4;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public boolean isMCQ(){
        return this.option_3 != null && this.getOption_4() !=null;
    }
}
