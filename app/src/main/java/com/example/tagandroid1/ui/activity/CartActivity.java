package com.example.tagandroid1.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import com.example.tagandroid1.R;
import com.example.tagandroid1.dao.CartProductsDao;
import com.example.tagandroid1.model.CartProduct;
import com.example.tagandroid1.utils.AnalyticsEvents;
import com.google.firebase.analytics.FirebaseAnalytics;


public class CartActivity extends AppCompatActivity {
    private ArrayAdapter<CartProduct> adapter;
    private CartProductsDao cartDao = new CartProductsDao();
    private AnalyticsEvents analyticsEvents;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        setTitle("Cart");
        analyticsEvents = new AnalyticsEvents(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setProductsListViewConfig();
        setCheckoutButtonConfig();

    }

    private void setCheckoutButtonConfig() {
        Button bnt = findViewById(R.id.activity_cart_goToCheckout_button);
        bnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cartDao.getProducts().size() > 0)
                    startCheckoutActivity();
            }
        });
    }
    public void startCheckoutActivity (){
        Intent intent = new Intent(this, CheckoutActivity.class);
        startActivity(intent);;
    }
    public void setProductsListViewConfig(){

        ListView productsLv = findViewById(R.id.activity_cart_products_lv);
        setAdapterConfig(productsLv);
        registerForContextMenu(productsLv);

    }

    public void refreshProductsList(){
        adapter.clear();
        adapter.addAll(cartDao.getProducts());
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.activity_cart_products_lv_menu, menu);
    }

    @Override
    protected void onResume() {
        refreshProductsList();
        super.onResume();
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        CartProduct productClicked = adapter.getItem(menuInfo.position);

        if(itemId == R.id.activity_cart_products_lv_menu_add){

            cartDao.addToCart(productClicked.getProduct());


            sendAddToCartEventToFirebase(productClicked);

            refreshProductsList();

        }

        else {

            cartDao.removeFromCart(productClicked);

            sendRemoveFromCartEventToFirebase(productClicked);

            adapter.remove(productClicked);

        }

        return super.onContextItemSelected(item);

    }

    private void sendRemoveFromCartEventToFirebase(CartProduct p) {
        Bundle product = prepareBundleItem(p);

        analyticsEvents.removeFromCart(product);
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

    private void sendAddToCartEventToFirebase(CartProduct clickedProduct) {

        Bundle product = prepareBundleItem(clickedProduct);

        analyticsEvents.addToCart(product);

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

    public void setAdapterConfig(ListView lv) {
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
            lv.setAdapter(adapter);
    }

}
