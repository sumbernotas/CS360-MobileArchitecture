package com.example.eventtracking_projectthree;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.*;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;

import java.util.List;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    UserDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        Log.d(TAG, "onCreate: Starting.");

        Button registerBttn = (Button) findViewById(R.id.buttonRegister);
        registerBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Clicked Register Button.");
                Intent intent = new Intent(MainActivity.this, UserRegister.class);
                startActivity(intent);
            }
        });

        Button loginBttn = (Button) findViewById(R.id.buttonSignIn);
        loginBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Sign in Button.");
                //boolean approved = login(loginBttn);

               // if(approved) {
                    //Intent intent = new Intent(MainActivity.this, DatabaseHome.class);
                   // startActivity(intent);
                //}

                Intent intent = new Intent(MainActivity.this, DatabaseHome.class);
                startActivity(intent);
            }
        });

        db = Room.databaseBuilder(getApplicationContext(),
                UserDatabase.class, "users").build();
    }

    private boolean login(View v) {
        Log.d(TAG, "onClick: Sign in Button.");
        EditText userName = (EditText) findViewById(R.id.editTextTextUsername);
        EditText password = (EditText) findViewById(R.id.editTextTextPassword);

        String userNameValue = userName.getText().toString();
        String passwordValue = password.getText().toString();

        if(checkLogin(userNameValue, passwordValue)) {
            return true;
        }
        else {
            return false;
        }
    }
    private boolean checkLogin(String username, String password) {
        UserDao userDao = db.userDao();
        User user = userDao.findByName(username, password);
        return user != null;
    }
}
