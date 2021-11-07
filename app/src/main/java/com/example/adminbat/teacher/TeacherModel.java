package com.example.adminbat.teacher;

public class TeacherModel {
    String name, designation,email;

    public TeacherModel() {

    }

    public TeacherModel(String name, String designation, String email) {
        this.name = name;
        this.designation = designation;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
