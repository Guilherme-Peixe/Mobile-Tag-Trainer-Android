package com.example.tagandroid1.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tagandroid1.R;
import com.example.tagandroid1.dao.CartProductsDao;
import com.google.firebase.analytics.FirebaseAnalytics;

import com.example.tagandroid1.model.Product;


public class MobileTagTrainerActivity extends AppCompatActivity {
    private FirebaseAnalytics mFirebaseAnalytics;
    private static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private static final String USER_ID = "a28891a6e12b995c44f1969aace3c374";
    private Product product1 = new Product("Tenis DC", "sku_1234", "Shoes", "42", 199.95);
    private Product product2 =new Product("Camiseta Nike", "sku_3234", "T-SHIRT", "M", 85.00);
    private Product product3 =new Product("Mi Band 4", "sku_4321", "Smartbands", "Black", 159.99);


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_trainer);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        setTitle("Mobile Ecommerce Tag Trainer");
        setLoginButtonConfig();
        setAddToCartButtonConfig();
        setRadioGroupConfig();
        setPurchaseButtonConfig();

    }

    private void setLoginButtonConfig() {

        Button loginButton =  findViewById(R.id.activity_mobile_trainer_login_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendLoginEventToFirebase();
            }
        });

    }


    private void setRadioGroupConfig() {

        RadioGroup paymentRadioGroup = findViewById(R.id.activity_mobile_trainer_radio_group);

        paymentRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                 sendBeginCheckoutEventToFirebase(checkedId);

            }
        });

    }

    private void setAddToCartButtonConfig() {

        Button addToCartButton = findViewById(R.id.activity_mobile_trainer_add_button);

        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendAddToCartEventToFirebase();
            }
        });

    }

    private void setPurchaseButtonConfig() {
        Button purchaseButton = findViewById(R.id.activity_mobile_trainer_purchase_button);

        purchaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPurchaseEventToFirebase();
            }
        });
    }



    private List<CheckBox> getAllProductsCheckBoxes() {

        CheckBox product1 = findViewById(R.id.activity_mobile_trainer_product1);
        CheckBox product2 = findViewById(R.id.activity_mobile_trainer_product2);
        CheckBox product3 = findViewById(R.id.activity_mobile_trainer_product3);

        List<CheckBox> checkBoxes = new ArrayList<>(Arrays.asList(product1, product2, product3));

        return checkBoxes;
    }

    private List<CheckBox> getAllProductsCheckedBoxes(List<CheckBox> allProductsCheckBoxes) {

        List<CheckBox> products = new ArrayList<>();

        for (CheckBox product:
                allProductsCheckBoxes) {

            if(product.isChecked()) {
                products.add(product);
            }
        }

        return products;
    }

    private ArrayList getAllProductsFromCart() {

        List<CheckBox> products = getAllProductsCheckedBoxes(getAllProductsCheckBoxes());

        ArrayList allItems = new ArrayList<>();

        if(!products.isEmpty()) {


            for (CheckBox ck : products) {
                Bundle bundle = new Bundle();

                Product product;

                if (ck.getText().toString().equals("Product 1")) {
                    product = product1;

                }

                else if (ck.getText().toString().equals("Product 2")) {
                    product = product2;
                }

                else {
                    product = product3;
                }

                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, product.getId());
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, product.getName());
                bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, product.getCategory());
                bundle.putString(FirebaseAnalytics.Param.ITEM_VARIANT, product.getVariant());
                bundle.putDouble(FirebaseAnalytics.Param.PRICE, product.getPrice());
                bundle.putString(FirebaseAnalytics.Param.CURRENCY, "BRL");
                bundle.putLong(FirebaseAnalytics.Param.QUANTITY, 1);

                allItems.add(bundle);

            }
        }

        return allItems;
    }

    private double getCartTotalValue() {

        double total = 0;

        ArrayList cartProducts = getAllProductsFromCart();

        for( Object product : cartProducts) {

           Bundle item= (Bundle) product;

           total += item.getDouble("price");

        }

        return total;

    }

    public String getEmailTextField() {

        EditText emailField = findViewById(R.id.activity_mobile_trainer_email);

        return emailField.getText().toString();
    }

    public boolean emailValidator(String email) {

        Matcher emailMatcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);

        return emailMatcher.find();
    }

    private boolean isEmptyCart() {

        return getAllProductsFromCart().isEmpty();

    }

    private Bundle prepareEcommerceItemsBundle() {

        ArrayList productsFromCart = getAllProductsFromCart();

        Bundle ecommerceBundle = new Bundle();

        ecommerceBundle.putParcelableArrayList("items", productsFromCart);

        return ecommerceBundle;
    }

    public void sendLoginEventToFirebase() {

        String email = getEmailTextField();

        if(emailValidator(email)) {
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.METHOD, "Login");

            mFirebaseAnalytics.setUserId(USER_ID);
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, bundle);
        }
    }

    private void sendAddToCartEventToFirebase() {

        if(!isEmptyCart()) {

            Bundle ecommerceBundle = prepareEcommerceItemsBundle();

            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.ADD_TO_CART, ecommerceBundle);

        }
    }


    private void sendBeginCheckoutEventToFirebase(int radioButtonId) {

        RadioButton paymentRadioButtonSelected = findViewById(radioButtonId);

        String paymentMethod = paymentRadioButtonSelected.getText().toString();

        if(!isEmptyCart()) {

            Bundle ecommerceBundle = prepareEcommerceItemsBundle();

            ecommerceBundle.putLong(FirebaseAnalytics.Param.CHECKOUT_STEP, 1);
            ecommerceBundle.putString(FirebaseAnalytics.Param.CHECKOUT_OPTION, paymentMethod);

            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.BEGIN_CHECKOUT, ecommerceBundle);
        }

    }

    private void sendPurchaseEventToFirebase() {

        if(!isEmptyCart()) {

            Bundle ecommerceBundle = prepareEcommerceItemsBundle();

            ecommerceBundle.putString(FirebaseAnalytics.Param.TRANSACTION_ID, "T6636");
            ecommerceBundle.putString(FirebaseAnalytics.Param.AFFILIATION, "Mobile Tag Trainer");
            ecommerceBundle.putDouble(FirebaseAnalytics.Param.VALUE, getCartTotalValue());
            ecommerceBundle.putString(FirebaseAnalytics.Param.CURRENCY, "BRL");
            ecommerceBundle.putDouble(FirebaseAnalytics.Param.SHIPPING, 5.34);

            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.ECOMMERCE_PURCHASE, ecommerceBundle);

        }
    }
 }



