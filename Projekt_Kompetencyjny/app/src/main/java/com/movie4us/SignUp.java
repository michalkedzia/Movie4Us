package com.movie4us;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputEditText;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class SignUp extends AppCompatActivity {

    TextInputEditText textInputEditTextFullname,
            textInputEditTextUsername,
            textInputEditTextPassword,
            textInputEditTextEmail;
    Button buttonSignUp;
    TextView textViewLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        textInputEditTextEmail = findViewById(R.id.email);
        textInputEditTextFullname = findViewById(R.id.fullname);
        textInputEditTextPassword = findViewById(R.id.password);
        textInputEditTextUsername = findViewById(R.id.username);
        buttonSignUp = findViewById(R.id.buttonSignUp);
        textViewLogin = findViewById(R.id.loginText);

        textViewLogin.setOnClickListener(
                v -> {
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    startActivity(intent);
                    finish();
                });

        buttonSignUp.setOnClickListener(
                v -> {
                    final String fullname, username, password, email;
                    fullname = String.valueOf(textInputEditTextFullname.getText());
                    username = String.valueOf(textInputEditTextUsername.getText());
                    password = String.valueOf(textInputEditTextPassword.getText());
                    email = String.valueOf(textInputEditTextEmail.getText());

                    if (!fullname.equals("")
                            && !username.equals("")
                            && !email.equals("")
                            && !password.equals("")) {

                        Handler handler = new Handler();
                        handler.post(
                                new Runnable() {
                                    @Override
                                    public void run() {

                                        String[] field = new String[4];
                                        field[0] = "fullname";
                                        field[1] = "username";
                                        field[2] = "password";
                                        field[3] = "email";

                                        String[] data = new String[4];
                                        data[0] = fullname;
                                        data[1] = username;
                                        data[2] = password;
                                        data[3] = email;

                                        PutData putData =
                                                new PutData(
                                                        "http://192.168.1.32/LoginRegister/signup.php", "POST", field, data);
                                        if (putData.startPut()) {
                                            if (putData.onComplete()) {
                                                String result = putData.getResult();
                                                if (result.equals("Sign Up Success")) {
                                                    ////////TODO: Dodane
                                                    buttonSignUp.setBackgroundResource(R.drawable.buttonshapeaccept);
                                                    buttonSignUp.setText("Sign Up Success");
                                                    buttonSignUp.setTextSize(16);
                                                    ////////
                                                    Intent intent = new Intent(getApplicationContext(), Login.class);
                                                    startActivity(intent);
                                                    finish();
                                                } else {
                                                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT)
                                                            .show();
                                                }
                                            }
                                        }
                                    }
                                });
                    } else {
//                        Toast.makeText(getApplicationContext(), "All fields required", Toast.LENGTH_SHORT)
//                                .show();
                        //////////////TODO: Dodane
                        buttonSignUp.setBackgroundResource(R.drawable.buttonshapedecline);
                        buttonSignUp.setText("Register Failed! Try again");
                        buttonSignUp.setTextSize(16);
                        //////////////
                    }
                });
    }
}