package com.example.educationalgp.Model;


public class Student {

    private int id;
    private String username;

    public Student() {
    }

    public Student(String username) {
        this.username = username;
    }

    public Student(int id, String username) {
        this.id = id;
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
