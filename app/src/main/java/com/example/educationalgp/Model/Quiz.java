package com.example.educationalgp.Model;

import com.google.firebase.firestore.DocumentId;

import java.util.List;

public class Quiz {

    @DocumentId
    private String id;
    private int totalMarks;
    private List<Question> questionList;
    private String teacherCode;

    public Quiz(){

    }
    public Quiz(int totalMarks, List<Question> questionList, String teacherCode) {
        this.totalMarks = questionList.size();
        this.questionList = questionList;
        this.teacherCode = teacherCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(int totalMarks) {
        this.totalMarks = totalMarks;
    }

    public List<Question> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<Question> questionList) {
        this.questionList = questionList;
    }

    public String getTeacherCode() {
        return teacherCode;
    }

    public void setTeacherCode(String teacherCode) {
        this.teacherCode = teacherCode;
    }


}
