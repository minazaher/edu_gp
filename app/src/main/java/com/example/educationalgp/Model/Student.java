package com.example.educationalgp.Model;


import com.google.firebase.firestore.PropertyName;

import java.util.HashMap;
import java.util.Map;

public class Student {

    private String id;
    private String username;
    private Map<String,Integer> quizGrades;
    public Student() {

    }

    public Student(String username) {
        this.username = username;
    }

    public Student(String id, String username) {
        this.id = id;
        this.username = username;
    }

    public Student(String id, String username, Map<String, Integer> quizGrades) {
        this.id = id;
        this.username = username;
        this.quizGrades = quizGrades;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    @PropertyName("quizGrades")

    public Map<String, Integer> getQuizGrades() {
        return quizGrades;
    }
    @PropertyName("quizGrades")
    public void setQuizGrades(Map<String, Integer> quizGrades) {
        this.quizGrades = quizGrades;
    }
}
