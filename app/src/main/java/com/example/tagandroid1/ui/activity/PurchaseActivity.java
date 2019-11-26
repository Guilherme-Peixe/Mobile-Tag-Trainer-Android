package com.example.tagandroid1.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tagandroid1.R;
import com.example.tagandroid1.dao.CartProductsDao;
import com.example.tagandroid1.model.CartProduct;
import com.example.tagandroid1.utils.AnalyticsEvents;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;

public class PurchaseActivity extends AppCompatActivity {
    private CartProductsDao cartDao = new CartProductsDao();
    private AnalyticsEvents analyticsEvents;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);
        analyticsEvents = new AnalyticsEvents(this);
        setTitle("Purchase");
        setProductsListViewConfig();
        setTotalPriceTextViewConfgi();
        sendPurchaseEventToFirebase();
        setHomeButtonConfig();
    }

    private void setHomeButtonConfig() {
        Button btn = findViewById(R.id.activity_purchase_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PurchaseActivity.this, ProductsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

    }

    private Bundle prepareBundleItem(CartProduct p){
        Bundle product = new Bundle();

        product.putString( FirebaseAnalytics.Param.ITEM_ID, p.getProduct().getId());  // ITEM_ID or ITEM_NAME is required
        product.putString( FirebaseAnalytics.Param.ITEM_NAME, p.getProduct().getName());
        product.putString( FirebaseAnalytics.Param.ITEM_CATEGORY, p.getProduct().getCategory());
        product.putString( FirebaseAnalytics.Param.ITEM_VARIANT, p.getProduct().getVariant());
        product.putDouble( FirebaseAnalytics.Param.PRICE, p.getProduct().getPrice() );
        product.putString( FirebaseAnalytics.Param.CURRENCY, "BRL" );
        product.putLong( FirebaseAnalytics.Param.QUANTITY,  p.getQuantity());


        return  product;

    }

    private ArrayList<Bundle> prepareEcommerceItemsBundle() {

        ArrayList<Bundle> items = new ArrayList<>();

        for (CartProduct cp : cartDao.getProducts()){

            Bundle item = prepareBundleItem(cp);
            items.add(item);

        }

        return items;

    }


    private void sendPurchaseEventToFirebase() {
        ArrayList<Bundle> items =prepareEcommerceItemsBundle();

        analyticsEvents.purchase(items);
    }

    private void setProductsListViewConfig() {
        ListView lv = findViewById(R.id.activity_purchase_products_lv);
        setProductsListAdapterConfig(lv);
    }

    private void setProductsListAdapterConfig(ListView lv) {
        lv.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, cartDao.getProducts()));
    }

    private void setTotalPriceTextViewConfgi() {
        TextView totalTextView = findViewById(R.id.activity_purchase_total_tv);

        totalTextView.setText("Total Purchase: R$" + String.format("%.2f",getTotalPriceFromItems()));
    }

    private double getTotalPriceFromItems() {
        double total = 0;
        for (CartProduct cp : cartDao.getProducts())
            total += cp.getProduct().getPrice() * cp.getQuantity();
        return total;
    }


}
