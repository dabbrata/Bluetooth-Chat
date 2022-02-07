package com.example.analogbluetoothchat;

public class CustomerModel {
    private String name;
    private int id;


    public CustomerModel(String name,Integer id) {
        this.name = name;
        this.id = id;

    }
    public CustomerModel(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
