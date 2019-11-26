package com.example.tagandroid1.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import com.example.tagandroid1.R;
import com.example.tagandroid1.dao.CartProductsDao;
import com.example.tagandroid1.model.CartProduct;
import com.example.tagandroid1.utils.AnalyticsEvents;
import com.google.firebase.analytics.FirebaseAnalytics;
import java.util.ArrayList;

public class CheckoutActivity extends AppCompatActivity {
    private CartProductsDao cartDao = new CartProductsDao();
    private AnalyticsEvents analyticsEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Checkout");
        analyticsEvents = new AnalyticsEvents(this);
        setTotalTextViewConfig();
        setPaymentConfirmButonConfg();
        sendBeginCheckoutStep1EventToFirebase();

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

    private void sendBeginCheckoutStep1EventToFirebase() {
        ArrayList<Bundle> items = prepareEcommerceItemsBundle();

        analyticsEvents.beginCheckoutStep1(items);

    }


    private void setPaymentConfirmButonConfg() {
        Button btn = findViewById(R.id.activity_checkout_payment_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!paymentMethodChoosed().equals("")){

                    sendBeginCheckoutStep2EventToFirebase(paymentMethodChoosed());
                    goToPurchaseActivity();
                }
            }
        });
    }

    private void sendBeginCheckoutStep2EventToFirebase(String paymentMethod) {
        ArrayList<Bundle> items = prepareEcommerceItemsBundle();

        analyticsEvents.beginCheckoutStep2(items, paymentMethod);

    }
    
    private void goToPurchaseActivity() {
        Intent intent = new Intent(this, PurchaseActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private String paymentMethodChoosed(){
        RadioButton boletoCheck = findViewById(R.id.activity_checkout_rb_boleto);
        RadioButton cartaoCheck = findViewById(R.id.activity_checkout_rb_cartao);

        if(boletoCheck.isChecked()) return boletoCheck.getText().toString();
        if(cartaoCheck.isChecked()) return cartaoCheck.getText().toString();

        return "";

    }

    private void setTotalTextViewConfig() {
        TextView totalTextView = findViewById(R.id.activity_checkout_payment_tv);
        double totalPrice = 0;
        for (CartProduct cp : cartDao.getProducts())
            totalPrice += cp.getProduct().getPrice() * cp.getQuantity();
        totalTextView.setText("Total: R$" + String.format("%.2f", totalPrice));

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
