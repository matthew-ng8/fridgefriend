package com.google.example.fridgefriend;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class FirebaseData {
    static FirebaseData firebaseData = new FirebaseData();
    private DatabaseReference fridgeGroup;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    public DatabaseReference getFridgeGroup() {
        return fridgeGroup;
    }

    public FirebaseAuth getmAuth() {
        return mAuth;
    }

    public FirebaseUser getmUser() {
        return mUser;
    }

    public void setFridgeGroup(DatabaseReference fridgeGroup) {
        this.fridgeGroup = fridgeGroup;
    }

    public void setmAuth(FirebaseAuth mAuth) {
        this.mAuth = mAuth;
    }

    public void setmUser(FirebaseUser mUser) {
        this.mUser = mUser;
    }

    public FirebaseData(){}



}
