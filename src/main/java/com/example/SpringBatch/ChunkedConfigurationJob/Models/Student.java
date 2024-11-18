package com.example.SpringBatch.ChunkedConfigurationJob.Models;

public class Student {
    private int id;
    private String name;
    private String gender;
    private int age;
    private String date;
    private String country;

    public Student() {
    }

    public String toString() {
        return "Student{id=" + this.id + ", name='" + this.name + "', gender='" + this.gender + "', age=" + this.age + ", date=" + this.date + ", country='" + this.country + "'}";
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return this.gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
