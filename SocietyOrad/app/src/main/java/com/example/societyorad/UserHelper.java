package com.example.societyorad;

public class UserHelper {
    private String username;
    private String Phone;
    private String Email;
    private String Password;

    public UserHelper()
    {

    }

    public UserHelper(String username, String phone, String email, String password) {
        this.username = username;
        Phone = phone;
        Email = email;
        Password = password;
    }

    public String getName() {
        return username;
    }

    public void setName(String name) {
        this.username = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
