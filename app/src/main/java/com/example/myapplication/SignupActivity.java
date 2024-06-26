package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class SignupActivity extends AppCompatActivity {
    EditText etName, etUserId, etEmail, etPassword, etPhone, etRePass;
  private  Button btnSignup, btnLogin, btnExit;
   CheckBox rememberId, rempass;
    String text = "";
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        etUserId = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etPhone = findViewById(R.id.etPhone);
        etRePass= findViewById(R.id.etRePass);
        btnSignup = findViewById(R.id.btnGo);
        btnLogin = findViewById(R.id.btnLogin);
        btnExit = findViewById(R.id.btnExit);
        rememberId = findViewById(R.id.remuserID);
        rempass = findViewById(R.id.rempass);

        sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUserId.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String pass = etPassword.getText().toString().trim();
                String rePass = etRePass.getText().toString().trim();
                String phone = etPhone.getText().toString().trim();

                StringBuilder errorMessage = new StringBuilder();

                if(username.length() < 3) {
                    errorMessage.append("Username is too short\n");
                }
                if(phone.length() < 6) {
                    errorMessage.append("Enter valid Phone number\n");
                }
                if(!isValidEmail(email)){
                    errorMessage.append("User didn't enter valid email\n");
                }
                if(pass.length() < 5){
                    errorMessage.append("Password not long enough\n");
                }
                if(!pass.equals(rePass)){
                    errorMessage.append("Password and re-enter password don't match\n");
                }

                if(errorMessage.length() > 0){
                    showErrorDialog(errorMessage.toString());
                    errorMessage.setLength(0); // Clear errorMessage

                } else {
                    // Save the login details if remember me is checked
                    if (rememberId.isChecked()) {
                        sharedPreferences.edit().putString("username", username).apply();
                    }if (rempass.isChecked()) {
                        sharedPreferences.edit().putString("password", pass).apply();
                    }
                    else if(!(rememberId.isChecked() && rempass.isChecked())) {
                        sharedPreferences.edit().clear().apply();
                    }

                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    if (rememberId.isChecked()) {

                        intent.putExtra("username", username);
                    }if (rempass.isChecked()) {
                        intent.putExtra("password", pass);
                    }

                    startActivity(intent);
                    finish();
                }
            }

        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //going to login page
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                moveTaskToBack(true);
                finish();
            }
        });
    }

    public boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }



    //error
    public void showErrorDialog(String errorMsg){
        AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
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