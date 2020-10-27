package com.example.buscandohogar.classes;

import android.content.ContentValues;

public class User {

    private int id;
    private String name;
    private String lastname;
    private String password;
    private String email;
    private String address;
    private int phone;
    private String city;

    //Default constructor
    public User(){
    }

    //Full constructor
        public User(String name, String password, String lastname, String email, String address, int phone, String city){
            this.name = name;
            this.password = password;
            this.lastname = lastname;
            this.email = email;
            this.address = address;
            this.phone = phone;
            this.city = city;
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

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public ContentValues getContentValues(User user){
        ContentValues values = new ContentValues();
        values.put("name", user.getName());
        values.put("password", user.getPassword());
        values.put("lastname", user.getLastname());
        values.put("email", user.getEmail());
        values.put("address", user.getAddress());
        values.put("phone", user.getPhone());
        values.put("city", user.getCity());

        return values;
    }


}
