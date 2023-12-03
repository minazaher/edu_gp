package com.example.educationalgp.Model;


import com.google.firebase.firestore.PropertyName;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Student {

    private String id;
    private String username;
    List<Grade> grades;
    public Student() {

    }

    public Student(String id, String username, List<Grade> grades) {
        this.id = id;
        this.username = username;
        this.grades = grades;
    }

    public Student(String username) {
        this.username = username;
    }

    public Student(String id, String username) {
        this.id = id;
        this.username = username;
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
    @PropertyName("grades")

    public List<Grade> getGrades() {
        return grades;
    }
    @PropertyName("grades")

    public void setGrades(List<Grade> grades) {
        this.grades = grades;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", grades=" + grades +
                '}';
    }
}
