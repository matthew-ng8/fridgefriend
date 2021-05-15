package com.google.example.fridgefriend;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
//TODO create static instantiaions of FridgeList and ShoppingList in Mainactivity
public class FirebaseData {
    static FirebaseData firebaseData;
    private DatabaseReference fridgeGroup;
    private String fridgeGroupName;
    private String fridgeCode;
    private DatabaseReference myUserRef;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FridgeList fridgeList;
    private ShoppingList shoppingList;
    private int groupIndexSelected;



    public FirebaseAuth getmAuth() {
        return mAuth;
    }

    public FirebaseUser getmUser() {
        return mUser;
    }

    public DatabaseReference getFridgeGroup() {
        return fridgeGroup;
    }
    public void setFridgeGroup(DatabaseReference fridgeGroup) {
        this.fridgeGroup = fridgeGroup;
    }

    public String getFridgeGroupName() {
        return fridgeGroupName;
    }
    public void setFridgeGroupName(String fridgeGroupName) {
        this.fridgeGroupName = fridgeGroupName;
    }

    public String getFridgeCode() {
        return fridgeCode;
    }

    public void setFridgeCode(String fridgeCode) {
        this.fridgeCode = fridgeCode;
    }

    public void setmAuth(FirebaseAuth mAuth) {
        this.mAuth = mAuth;
    }

    public void setmUser(FirebaseUser mUser) {
        this.mUser = mUser;
    }

    public DatabaseReference getMyUserRef() {
        return myUserRef;
    }

    public void setMyUserRef(DatabaseReference myUserRef) {
        this.myUserRef = myUserRef;
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

    public void updateFridgeGroup(boolean usePersonalList){
        String fridgeCode = FirebaseData.firebaseData.getFridgeCode();
        if(fridgeCode == null){
            FirebaseData.firebaseData.setFridgeGroup(null);
        }
        else {
            if (usePersonalList) {

                FirebaseData.firebaseData.setFridgeGroup(FirebaseDatabase.getInstance().getReference().child("groups/" + fridgeCode));
            } else {

                FirebaseData.firebaseData.setFridgeGroup(FirebaseData.firebaseData.getMyUserRef().child(fridgeCode));
            }
            if(fridgeList != null)
                fridgeList.fillListFromDatabase();
            if(shoppingList != null)
                shoppingList.fillListFromDatabase();
        }
    }

    public static FirebaseData initFirebaseData(){
        firebaseData = new FirebaseData();
        return firebaseData;
    }


    public int getGroupIndexSelected() {
        return groupIndexSelected;
    }

    public void setGroupIndexSelected(int groupIndexSelected) {
        this.groupIndexSelected = groupIndexSelected;
    }
}
