package com.example.marketingapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void GoToAdminLogin(View view) {
        Intent GoToAdminLogin= new Intent(this, AdminLoginActivity.class);
        startActivity(GoToAdminLogin);
    }

    public void GoToUserLogin(View view) {
        Intent GoToUserLogin= new Intent(this, UserLoginActivity.class);
        startActivity(GoToUserLogin);
    }
}