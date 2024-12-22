package com.example.eventtracking_projectthree;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.*;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;
public class UserRegister extends AppCompatActivity {
    private static final String TAG = "UserRegister";
    UserDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_screen);

        Button loginBttn = (Button) findViewById(R.id.buttonLogIn);
        loginBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Clicked Login Button.");
                Intent intent = new Intent(UserRegister.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Button registerBttn = (Button) findViewById(R.id.buttonRegister);
        registerBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Clicked Sign Up Button.");
                EditText userName = (EditText) findViewById(R.id.editTextTextName);
                EditText phoneNumber = (EditText) findViewById(R.id.editTextPhoneNumber);
                EditText email = (EditText) findViewById(R.id.editTextEmail);
                EditText password = (EditText) findViewById(R.id.editTextPassword);

                String userNameValue = userName.getText().toString();
                String phoneNumberValue = phoneNumber.getText().toString();
                String emailValue = email.getText().toString();
                String passwordValue = password.getText().toString();

                createUser(userNameValue, phoneNumberValue, emailValue, passwordValue);
                Intent intent = new Intent(UserRegister.this, MainActivity.class);
                startActivity(intent);
            }
        });

        db = Room.databaseBuilder(getApplicationContext(),
                UserDatabase.class, "users").build();
    }

    private void createUser(String name, String pass, String phone, String email) {
        UserDao userDao = db.userDao();
        User user = new User();
        user.userName = name;
        user.password = pass;
        user.phoneNumber = phone;
        user.email = email;

        userDao.insertAll(user);
    }
}
