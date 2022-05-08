package com.laci.gazora;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private static final int SECRET_KEY = 80085;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public void goToLogin(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("SECRET_KEY", 80085);
        startActivity(intent);
    }

    public void goToSignUp(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        intent.putExtra("SECRET_KEY", 80085);
        startActivity(intent);
    }
}