package com.example.tagandroid1.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.tagandroid1.R;
import com.example.tagandroid1.dao.CartProductsDao;
import com.example.tagandroid1.model.CartProduct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        setTitle("Cart");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setProductsListViewConfig();

    }


    public void setProductsListViewConfig(){
        ListView productsLv = findViewById(R.id.activity_cart_products_lv);

        productsLv.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, CartProductsDao.getProductsFromCart()));
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
