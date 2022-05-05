package com.example.trucksharingapp1.model;

public class Order {
    private int order_id;
    private String username;
    private String receivername;
    private String date;
    private String time;
    private String location;
    private String goodtype;
    private String weight;
    private String width;
    private String length;
    private String height;
    private String vehicletype;
    private byte[] image;

    public Order(String username, String receivername, String date, String time, String location, String goodtype, String weight, String width, String length, String height, String vehicletype, byte[] image) {
        this.username = username;
        this.receivername = receivername;
        this.date = date;
        this.time = time;
        this.location = location;
        this.goodtype = goodtype;
        this.weight = weight;
        this.width = width;
        this.length = length;
        this.height = height;
        this.vehicletype = vehicletype;
        this.image = image;
    }

    public Order(){

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public String getReceivername() {
        return receivername;
    }

    public void setReceivername(String receivername) {
        this.receivername = receivername;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getGoodtype() {
        return goodtype;
    }

    public void setGoodtype(String goodtype) {
        this.goodtype = goodtype;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getVehicletype() {
        return vehicletype;
    }

    public void setVehicletype(String vehicletype) {
        this.vehicletype = vehicletype;
    }
}
