package com.example.educationalgp.Model;

public class Course {
    private String name;
    private String imgUrl;

    public Course(String name, String imgUrl) {
        this.name = name;
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Override
    public String toString() {
        return "Course{" +
                "name='" + name + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                '}';
    }
}
