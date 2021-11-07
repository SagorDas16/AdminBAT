package com.example.adminbat.TeacherSide;

public class AttendanceModel {
    private String name, roll, status;

    public AttendanceModel() {
    }

    public AttendanceModel(String name, String roll, String status) {
        this.name = name;
        this.roll = roll;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoll() {
        return roll;
    }

    public void setRoll(String roll) {
        this.roll = roll;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
