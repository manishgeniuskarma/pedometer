package com.queens.sqliteloginandsignup.model;

public class User {

    private int id;
    private String name;
    private String email;
    private String password;
    private String step;
    private String height;
    private String weight;


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

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {this.password = password;}

    public String getstep() {return step;}
    public void setstep(String step) {
        this.step = step;
    }

    public String getheight() {return height;}
    public void setheight(String height) {
        this.height = height;
    }

    public String getweight() {return weight;}
    public void setweight(String weight) {
        this.weight = weight;
    }
}
