package com.google.example.fridgefriend;

import android.net.Uri;

import java.util.List;

public class User {

    private String name;
    private List<String> allergies;
    private Uri photo;
    private String email;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getAllergies() {
        return allergies;
    }

    public void setAllergies(List<String> allergies) {
        this.allergies = allergies;
    }

    public Uri getPhoto() {
        return photo;
    }

    public void setPhoto(Uri photo) {
        this.photo = photo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void User(String name, String email, Uri photo, List<String> allergies)
    {
        this.name = name;
        this.email = email;
        this.photo = photo;
        this.allergies = allergies;
    }

    public void addAllergies(String addA)
    {
        this.getAllergies().add(addA);
    }

  
}
