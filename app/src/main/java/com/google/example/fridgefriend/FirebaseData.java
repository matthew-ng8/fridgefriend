package com.google.example.fridgefriend;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class FirebaseData {
    static FirebaseData firebaseData = new FirebaseData();
    private DatabaseReference fridgeGroup;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FridgeList fridgeList;



    private ShoppingList shoppingList;

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

    public void setFridgeList(FridgeList f){
        this.fridgeList = f;
    }

    public FridgeList getFridgeList(){
        return  this.fridgeList;
    }

    public ShoppingList getShoppingList() {
        return shoppingList;
    }

    public void setShoppingList(ShoppingList shoppingList) {
        this.shoppingList = shoppingList;
    }

    public FirebaseData(){}



}
