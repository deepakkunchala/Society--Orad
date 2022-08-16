package com.example.societyorad;

public class volunteersmodel {

    String name,email,phone , pincode;

    public volunteersmodel(){}

    public volunteersmodel(String name, String email, String phone, String pincode) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.pincode = pincode;
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

    public String getPhone() {
        return phone;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
