package com.example.tagandroid1.dao;

import com.example.tagandroid1.model.CartProduct;
import com.example.tagandroid1.model.Product;

import java.util.HashMap;
import java.util.List;

public class CartProductsDao {
    public static final HashMap<String, CartProduct> cartProducts = new HashMap<>();

    public void addToCart(Product p){

        if(cartProducts.containsKey(p.getId())) {

            CartProduct cp = cartProducts.get(p.getId());
            cp.setQuantity(cp.getQuantity() + 1);
            cartProducts.remove(p.getId());
            cartProducts.put(p.getId(), cp);

        }

        else {

            cartProducts.put(p.getId() ,new CartProduct(p));

        }
    }

    public static List<Product> getProductsFromCart() {
        return (List) cartProducts.values();
    }

    public HashMap<String, CartProduct> cartProductHashMap(){
        return (HashMap) cartProducts.clone();
    }

}
