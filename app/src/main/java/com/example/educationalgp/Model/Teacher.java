package com.example.educationalgp.Model;

import java.util.Arrays;
import java.util.List;

public class Teacher {

    private String id;
    private String username;
    private String email;
    private String password;
    private List<Student> students;

    public Teacher() {
    }

    public Teacher(String id, String username, String email, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public Teacher(String id, String username, String email, String password, List<Student> students) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.students = students;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", students=" + students.toString() +
                '}';
    }
}
