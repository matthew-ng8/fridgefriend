package com.google.example.fridgefriend.dummy;

public class EdibleItem {

    private String name;

    /**
     * TODO MAYBE add a quantity number
     *
     */

    public EdibleItem(String name){
        this.name = name;
    }

    public  EdibleItem(EdibleItem e){
        this.name = e.name;
    }

    public String getName(){
        return name;
    }

    public void setName(String s){
        name = s;
    }


    public String toString(){
        return name;
    }

}
