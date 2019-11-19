package com.example.tagandroid1.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tagandroid1.R;
import com.google.firebase.analytics.FirebaseAnalytics;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    private static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Sign in");
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        setLoginButtonConfig();
    }


    private void setLoginButtonConfig() {
        Button loginButton = findViewById(R.id.activity_login_signin_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValidUser()){
                    sendLoginEventToFirebase();
                    openProductsActivity();
                    Toast.makeText(LoginActivity.this, "Welcome back!", Toast.LENGTH_LONG).show();

                }
                else {
                    Toast.makeText(LoginActivity.this, "User not valid!", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private void openProductsActivity() {
        startActivity(new Intent(this, ProductsActivity.class));
    }

    public boolean emailValidator(String email) {

        Matcher emailMatcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);

        return emailMatcher.find();
    }

    public boolean passwordValidator(String password) {
        return password.length() > 3 ? true : false;
    }

    public boolean isValidUser() {
        EditText userEmail = findViewById(R.id.activity_login_email_et);
        EditText userPassword = findViewById(R.id.activity_login_senha_et);

        if(emailValidator(userEmail.getText().toString()) && passwordValidator(userPassword.getText().toString()))
            return true;

        return false;
    }

    public void sendLoginEventToFirebase(){
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.METHOD, "Login");
            mFirebaseAnalytics.setUserId("u3sad341qfdasf");
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, bundle);
    }


}
