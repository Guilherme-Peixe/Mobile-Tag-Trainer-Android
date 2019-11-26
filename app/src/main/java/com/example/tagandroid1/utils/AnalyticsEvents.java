package com.example.tagandroid1.utils;

import android.content.Context;
import android.os.Bundle;
import com.google.firebase.analytics.FirebaseAnalytics;
import java.util.ArrayList;


public class AnalyticsEvents {

    public FirebaseAnalytics mFirebaseAnalytics;

    public AnalyticsEvents(Context context) {

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);

    }


    public void login(String method) {

        Bundle bundle = new Bundle();

        bundle.putString(FirebaseAnalytics.Param.METHOD, method);

        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, bundle);

    }

    public void productImpressions(ArrayList<Bundle> items) {

        Bundle ecommerceBundle = new Bundle();

        ecommerceBundle.putParcelableArrayList("items", items);

        ecommerceBundle.putString(FirebaseAnalytics.Param.ITEM_LIST, "Item list");

        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM_LIST, ecommerceBundle);

    }



    public void addToCart(Bundle items) {

        Bundle ecommerce = new Bundle();

        ecommerce.putBundle("items", items);

        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.ADD_TO_CART, items);

    }

    public void removeFromCart(Bundle items) {

        Bundle removeFromCartBundle = new Bundle();

        removeFromCartBundle.putBundle("items", items);

        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.REMOVE_FROM_CART, removeFromCartBundle);

    }

    public void beginCheckoutStep1(ArrayList<Bundle> items) {

        Bundle ecommerceBundle = new Bundle();

        ecommerceBundle.putLong(FirebaseAnalytics.Param.CHECKOUT_STEP, 1);

        ecommerceBundle.putParcelableArrayList("items", items);

        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.BEGIN_CHECKOUT, ecommerceBundle);

    }

    public void beginCheckoutStep2(ArrayList<Bundle> items, String paymentMethod) {

        Bundle ecommerceBundle = new Bundle();

        ecommerceBundle.putParcelableArrayList("items", items);

        ecommerceBundle.putLong(FirebaseAnalytics.Param.CHECKOUT_STEP, 2);

        ecommerceBundle.putString(FirebaseAnalytics.Param.CHECKOUT_OPTION, paymentMethod);

        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.BEGIN_CHECKOUT, ecommerceBundle);

    }

    public void purchase(ArrayList<Bundle> items) {

        Bundle ecommerceBundle = new Bundle();

        ecommerceBundle.putParcelableArrayList("items", items);

        ecommerceBundle.putString( FirebaseAnalytics.Param.TRANSACTION_ID, "T12345" );
        ecommerceBundle.putString( FirebaseAnalytics.Param.AFFILIATION, "Tag Trainer Store - Online" );
        //ecommerceBundle.putDouble( FirebaseAnalytics.Param.VALUE, getTotalPriceFromItems() );
        ecommerceBundle.putDouble( FirebaseAnalytics.Param.SHIPPING, 5.34 );
        ecommerceBundle.putString( FirebaseAnalytics.Param.CURRENCY, "BRL" );
        ecommerceBundle.putString( FirebaseAnalytics.Param.COUPON, "Mobile Tag Trainer" );

        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.ECOMMERCE_PURCHASE, ecommerceBundle);

    }



}
