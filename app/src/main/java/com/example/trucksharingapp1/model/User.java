package com.example.trucksharingapp1.model;

public class User {

    private int user_id;
    private byte[] byteArray;
    private String fullname;
    private String username;
    private String password;
    private String phone;

    public User(String fullname, String username, String password, String phone, byte[] byteArray) {
        this.fullname = fullname;
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.byteArray = byteArray;
    }

    public User() {

    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public byte[] getByteArray() {
        return byteArray;
    }

    public void setByteArray(byte[] byteArray) {
        this.byteArray = byteArray;
    }
}
