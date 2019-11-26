package com.example.tagandroid1.dao;

import com.example.tagandroid1.model.CartProduct;
import com.example.tagandroid1.model.Product;

import java.util.ArrayList;
import java.util.List;

public class CartProductsDao {
    private static List<CartProduct> products = new ArrayList<>();

    public List<CartProduct> getProducts() {
        return  new ArrayList<>(products);
    }

    public void addToCart(Product p){
        boolean flag = false;
        for (CartProduct cp : products){
            if(cp.getProduct().getId() == p.getId()) {
                flag = true;
                cp.setQuantity(cp.getQuantity() + 1);
            }
        }

        if(!flag)
            products.add(new CartProduct(p));


    }


    public void removeFromCart(CartProduct productClicked) {
        products.remove(productClicked);
    }


}


