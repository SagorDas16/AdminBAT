package com.example.adminbat.Courses;

public class TeacherAssignModel {
    String year,title,code,pathtocourse,pathtostudent;

    public TeacherAssignModel() {
    }

    public TeacherAssignModel(String year, String title, String code, String pathtocourse, String pathtostudent) {
        this.year = year;
        this.title = title;
        this.code = code;
        this.pathtocourse = pathtocourse;
        this.pathtostudent = pathtostudent;
    }

    public String getYear() { return year; }

    public void setYear(String year) { this.year = year; }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPathtocourse() {
        return pathtocourse;
    }

    public void setPathtocourse(String pathtocourse) {
        this.pathtocourse = pathtocourse;
    }

    public String getPathtostudent() {
        return pathtostudent;
    }

    public void setPathtostudent(String pathtostudent) {
        this.pathtostudent = pathtostudent;
    }
}
