package com.queens.sqliteloginandsignup.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.queens.sqliteloginandsignup.MainActivity;
import com.queens.sqliteloginandsignup.sql.DatabaseHelper;

public class SplashActivity extends AppCompatActivity {
    DatabaseHelper databaseHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        databaseHelper = new DatabaseHelper(this);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isUserLoggedIn()) {
                    Intent accountsIntent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(accountsIntent);
                    finish();
                } else {
                    Intent accountsIntent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(accountsIntent);
                    finish();
                }
            }
        }, 3000);
    }


    public Boolean isUserLoggedIn() {
        SharedPreferences user_email = getSharedPreferences("MyLoginPref", MODE_PRIVATE);
        String mail = user_email.getString("user_email", "");
        if (mail != "") {
            return true;
        }
        return false;

    }

}
