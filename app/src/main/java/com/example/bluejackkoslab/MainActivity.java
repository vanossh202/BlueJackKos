package com.example.bluejackkoslab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bluejackkoslab.helper.DatabaseHelperLogin;

public class MainActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private Button login;
    private Button register;
    DatabaseHelperLogin dblog;
    SharedPreferences sharedPreferences, loginPreferences;
    SharedPreferences.Editor editor, loginEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dblog = new DatabaseHelperLogin(MainActivity.this);
        username = (EditText) findViewById(R.id.et_username);
        password = (EditText) findViewById(R.id.et_password);
        login = (Button) findViewById(R.id.btn_login);
        register = (Button) findViewById(R.id.btn_register);
        sharedPreferences = getSharedPreferences("userdata", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        loginPreferences = getSharedPreferences("logindata",Context.MODE_PRIVATE);
        loginEditor = loginPreferences.edit();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Register.class);
                startActivity(intent);
            }
        });
    }

    private void validate() {
        String Username = username.getText().toString().trim();
        String Password = password.getText().toString().trim();
        Boolean isDataExist = dblog.checkUsernameAndPass(Username,Password);
        editor.putString("id", dblog.getId(Username,Password));
        editor.commit();
        loginEditor.putString("username",Username);
        loginEditor.commit();

        if(loginPreferences.getString("username",null).length() != 0) // TODO: cek validasi
        {
            Intent intent = new Intent(MainActivity.this, KosList.class);
            startActivity(intent);
            finish();
        }
         else if (Username.length() == 0 && Password.length() == 0) {
            username.requestFocus();
            username.setError("FIELD CANNOT BE EMPTY");
            password.requestFocus();
            password.setError("FIELD CANNOT BE EMPTY");
        } else {
            if (!isDataExist) {
                Toast.makeText(this, "Username or password wrong, please try again", Toast.LENGTH_LONG).show();
            } else {
                Intent intent = new Intent(MainActivity.this, KosList.class);
                startActivity(intent);
                finish();
            }
        }
    }
}
