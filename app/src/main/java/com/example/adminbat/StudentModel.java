package com.example.adminbat;

public class StudentModel {
    private String name, roll, mac;

    public StudentModel() {

    }

    public StudentModel(String name, String roll, String mac) {
        this.name = name;
        this.roll = roll;
        this.mac = mac;
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

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }
}
