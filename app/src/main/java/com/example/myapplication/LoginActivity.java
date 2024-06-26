package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

        EditText etUsername, etPassword;
       private Button btnGo, btnSignup, btnExit;     String text = "";
  CheckBox  rememberId, rempass;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_login);
                etUsername =  findViewById(R.id.etUsername);
                etPassword = findViewById(R.id.etPassword);
                btnGo =  findViewById(R.id.btnGo);
                btnSignup =  findViewById(R.id.btnSignup);
                btnExit = findViewById(R.id.btnExit);
                rememberId = findViewById(R.id.remuserID);
                rempass = findViewById(R.id.rempass);


                Intent intent = getIntent();
                if (intent.hasExtra("username") && intent.hasExtra("password")) {
                        String username = intent.getStringExtra("username");
                        String password = intent.getStringExtra("password");
                        etUsername.setText(username);
                        etPassword.setText(password);
                        // Both username and password are present
                } else if (intent.hasExtra("username")) {
                        String username = intent.getStringExtra("username");
                        etUsername.setText(username);
                        // Only username is present
                } else if (intent.hasExtra("password")) {
                        String password = intent.getStringExtra("password");
                        etPassword.setText(password);
                        // Only password is present
                } else {
                        etUsername.setText("");
                        etPassword.setText("");
                }




                SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);

                if (sharedPreferences.contains("username")) {
                      String  username = sharedPreferences.getString("username", "");
                        if (!username.isEmpty()) {
                                etUsername.setText(username);
                        }
                }

                if (sharedPreferences.contains("password")) {
                        String pass = sharedPreferences.getString("password", "");
                        if (!pass.isEmpty()) {
                                etPassword.setText(pass);
                        }
                }
                if (sharedPreferences.contains("username") && sharedPreferences.contains("password")) {
                    String   username = sharedPreferences.getString("username", "");
                        String pass = sharedPreferences.getString("password", "");

                        if (!username.isEmpty() &&!pass.isEmpty()) {
                                etUsername.setText(username);
                                etPassword.setText(pass);
                        }
                }



                btnGo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                String username = etUsername.getText().toString();
                                String pass = etPassword.getText().toString();

                                if(username.length() < 3) {
                                        String  text1 = "Username is too short";
                                        text = text + "\n" + text1;

                                }
                                if(pass.length() < 5){
                                        // password too short
                                        String  text1 = "password not long enough";
                                        text = text + "\n" + text1;
                                }
                                //errormsg dialog
                                if(text.length() >0){
                                        showErrorDialog(text);
                                        text= "";
                                }
                                else {
                                        String success = " SIGNUP SUCCESSFUL" ;
                                        showErrorDialog(success);
// if remember me is checked
                                        if (rememberId.isChecked()) {
                                                sharedPreferences.edit().putString("username", username).apply();
                                        } else {
                                                sharedPreferences.edit().remove("username").apply();
                                        }
                                        if (rempass.isChecked()) {
                                                sharedPreferences.edit().putString("password", pass).apply();
                                        } else {
                                                sharedPreferences.edit().remove("password").apply();
                                        }
                                        Intent intent = new Intent(LoginActivity.this, NewActivity.class);
                                        startActivity(intent);
                                }












                        }
                });
                btnSignup.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                Intent intent2 = new Intent(getApplicationContext(), SignupActivity.class);
                                startActivity(intent2);
                        }
                });
                btnExit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                moveTaskToBack(true);
                        }
                });

        }


        public void showErrorDialog(String errorMsg){
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setMessage(errorMsg);
                builder.setTitle("Status");
                builder.setCancelable(true);

                builder.setPositiveButton("Back", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                        }
                });
                AlertDialog alert = builder.create();
                alert.show();
        }
}

