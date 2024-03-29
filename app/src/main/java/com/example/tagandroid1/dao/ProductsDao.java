package com.example.tagandroid1.dao;

import com.example.tagandroid1.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductsDao {

    private static final List<Product> productsList = new ArrayList<>();

    public void saveProduct(Product p){
        productsList.add(p);
    }

    public void saveProduct(List<Product> pl){
        productsList.addAll(pl);
    }

    public List<Product> getProductsList() {
        return  new ArrayList<>(productsList);
    }
}
