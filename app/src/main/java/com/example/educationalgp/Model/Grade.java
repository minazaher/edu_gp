package com.example.educationalgp.Model;

public class Grade {
    private String id;
    private String teacherCode;
    private String studentName;
    private int mark;
    private float percentage;
    public Grade(){

    }

    public Grade(String teacherCode, String studentName, int mark, float percentage) {
        this.teacherCode = teacherCode;
        this.studentName = studentName;
        this.mark = mark;
        this.percentage = percentage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTeacherCode() {
        return teacherCode;
    }

    public void setTeacherCode(String teacherCode) {
        this.teacherCode = teacherCode;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    public float getPercentage() {
        return percentage;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }
}

