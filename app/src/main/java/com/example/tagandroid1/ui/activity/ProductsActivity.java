package com.example.tagandroid1.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.tagandroid1.R;
import com.example.tagandroid1.dao.CartProductsDao;
import com.example.tagandroid1.dao.ProductsDao;
import com.example.tagandroid1.model.CartProduct;
import com.example.tagandroid1.model.Product;
import com.example.tagandroid1.utils.ProductsUtils;

import java.io.Serializable;
import java.util.List;

public class ProductsActivity extends AppCompatActivity {
    final CartProductsDao cpDao = new CartProductsDao();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        setTitle("Products");
        productListConfig();
        goToCartButtonConfig();

    }

    private void goToCartButtonConfig() {
        Button goToCart = findViewById(R.id.activity_products_goToCart_button);

        goToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToCartActivity();
            }
        });
    }

    private void goToCartActivity() {
        Intent goToCartIntent = new Intent(ProductsActivity.this, CartActivity.class);
        goToCartIntent.putExtra("cartProducts",  cpDao.cartProductHashMap());
        startActivity(goToCartIntent);
    }

    private void productListConfig() {
        ListView listView = findViewById(R.id.activity_products_lv);
        setProductListAdapterConfig(listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Product clickedProduct = (Product) parent.getItemAtPosition(position);
                cpDao.addToCart(clickedProduct);
                Toast.makeText(ProductsActivity.this, clickedProduct.getName() + " added to cart!", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void setProductListAdapterConfig(ListView listView) {
        ProductsUtils productsUtils = new ProductsUtils();
        final List<Product> productList = productsUtils.products();
        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, productList));
    }
}
