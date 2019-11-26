package com.example.tagandroid1.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.example.tagandroid1.R;
import com.example.tagandroid1.dao.CartProductsDao;
import com.example.tagandroid1.model.Product;
import com.example.tagandroid1.utils.AnalyticsEvents;
import com.example.tagandroid1.utils.ProductsUtils;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.List;

public class ProductsActivity extends AppCompatActivity {
    final CartProductsDao cpDao = new CartProductsDao();
    private ArrayAdapter<Product> adapter;
    private AnalyticsEvents analyticsEvents;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        setTitle("Products");
        analyticsEvents = new AnalyticsEvents(this);
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
        Intent intent = new Intent(this, CartActivity.class);
        startActivity(intent);

    }

    private void productListConfig() {

        ListView listView = findViewById(R.id.activity_products_lv);

        setProductListAdapterConfig(listView);

        sendProductImpressionsEventToFirebase();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Product clickedProduct = (Product) parent.getItemAtPosition(position);

                cpDao.addToCart(clickedProduct);

                sendAddToCartEventToFirebase(clickedProduct);

                Toast.makeText(ProductsActivity.this, clickedProduct.getName() + " added to cart!", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void sendAddToCartEventToFirebase(Product clickedProduct) {

        Bundle product = prepareBundleItem(clickedProduct);

        analyticsEvents.addToCart(product);

    }

    private Bundle prepareBundleItem(Product p){
        Bundle product = new Bundle();

        product.putString(FirebaseAnalytics.Param.ITEM_ID, p.getId());
        product.putString(FirebaseAnalytics.Param.ITEM_NAME, p.getName());
        product.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, p.getCategory());
        product.putString(FirebaseAnalytics.Param.ITEM_VARIANT, p.getVariant());
        product.putDouble(FirebaseAnalytics.Param.PRICE, p.getPrice() );
        product.putString(FirebaseAnalytics.Param.CURRENCY, "BRL" );

        return  product;

    }
    private void sendProductImpressionsEventToFirebase() {

        ArrayList<Bundle> products = new ArrayList<>();

        for ( int i = 0; i < adapter.getCount(); i++) {
            Product p = adapter.getItem(i);

            Bundle product = prepareBundleItem(p);

            products.add(product);

        }

        analyticsEvents.productImpressions(products);

    }

    private void setProductListAdapterConfig(ListView listView) {
        List<Product> products = new ProductsUtils().products();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, products);
        listView.setAdapter(adapter);

    }
}
