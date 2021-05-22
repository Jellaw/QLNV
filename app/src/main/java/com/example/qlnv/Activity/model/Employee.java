package com.example.qlnv.Activity.model;


public class Employee {
    private int id;
    private String name;
    private String dob;
    private int age;
    private String gender;
    private String email;
    private String address;
    private String phone;
    private String position;
    private Double salary;
    private int AccountID;

    public Employee(String name, String dob, int age, String gender, String email, String address, String phone, String position,double salary, int AccountID) {
        this.name = name;
        this.dob = dob;
        this.age = age;
        this.gender = gender;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.position = position;
        this.salary = salary;
        this.AccountID = AccountID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public int getAccountID() {
        return AccountID;
    }

    public void setAccountID(int accountID) {
        AccountID = accountID;
    }
}
