package com.example.tagandroid1.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.example.tagandroid1.R;
import com.example.tagandroid1.dao.CartProductsDao;

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
        CartProductsDao cartDao = new CartProductsDao();
        productsLv.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, cartDao.getProducts()));
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
