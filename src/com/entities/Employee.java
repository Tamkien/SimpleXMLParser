package com.entities;

import java.util.regex.Pattern;

public class Employee {
    private String ID;
    private String Name;
    private String DOB;
    private String Address;
    private String Email;
    private String phone;
    private String placeOfWork;

    public Employee() {
    }

    @Override
    public String toString() {
        return "Employee{" +
                "ID='" + ID + '\'' +
                ", Name='" + Name + '\'' +
                ", DOB='" + DOB + '\'' +
                ", Address='" + Address + '\'' +
                ", Email='" + Email + '\'' +
                ", phone='" + phone + '\'' +
                ", placeOfWork='" + placeOfWork + '\'' +
                '}';
    }

    public Employee(String ID, String name, String DOB, String address, String email, String phone, String placeOfWork) {
        this.ID = ID;
        Name = name;
        this.DOB = DOB;
        Address = address;
        Email = email;
        this.phone = phone;
        this.placeOfWork = placeOfWork;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPlaceOfWork() {
        return placeOfWork;
    }

    public void setPlaceOfWork(String placeOfWork) {
        this.placeOfWork = placeOfWork;
    }

    private boolean validate(String txt, String regex) {
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(txt).find();
    }

    public boolean validID() {
        return validate(getID(), "^[a-zA-Z0-9]*$") && !getID().isEmpty();
    }

    public boolean validName() {
        return validate(getName(), "^[a-zA-Z ]*$") && !getName().isEmpty();
    }

    public boolean validDOB() {
        return validate(getDOB(), "^[0-9]{4}-(((0[13578]|(10|12))-(0[1-9]|[1-2][0-9]|3[0-1]))|(02-(0[1-9]|[1-2][0-9]))|(" +
                "(0[469]|11)-(0[1-9]|[1-2][0-9]|30)))$");
    }

    public boolean validAddress() {
        return validate(getAddress(), "^[a-zA-Z0-9, ]*$") && !getAddress().isEmpty();
    }

    public boolean validEmail() {
        return validate(getEmail(), "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+.[a-zA-Z0-9-.]+$");
    }

    public boolean validPhone() {
        return validate(getPhone(), "^[0-9()-]+$");
    }

    public boolean validPlaceOfWork() {
        return validate(getPlaceOfWork(), "^[a-zA-Z0-9, ]*$") && !getPlaceOfWork().isEmpty();
    }
}
