package com.example.tagandroid1.model;

import java.io.Serializable;

public class CartProduct implements Serializable {

    private Product p;
    private int quantity;

    public CartProduct(Product p){
        this.p = p;
        quantity = 1;
    }

    public void setQuantity(int quantity){
        this.quantity = quantity;
    }

    public int getQuantity(){
        return this.quantity;
    }
}
