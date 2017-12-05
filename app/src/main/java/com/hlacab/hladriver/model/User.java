package com.hlacab.hladriver.model;

/**
 * Created by gopinath on 02/12/17.
 */

public class User {

    private String email,password,name,phoneno;


    public User()
    {

    }

    public User(String name, String email, String phoneno, String password) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phoneno = phoneno;
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

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }
}
