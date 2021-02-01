package loginRegister;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.movie4us.Connection;
import com.movie4us.MainActivity;
import com.movie4us.R;
import com.vishnusivadas.advanced_httpurlconnection.PutData;
import config.Configuration;

import java.io.IOException;

/**
 * Klasa logowania użytkownika do aplikacji.
 * Autoryzacja użytkownika następuje poprzez zapytanie do bazy danych.
 * Jeżeli użytkownik uzyskła autoaryzację ma dostep do aplikacji.
 * Jeśli użytkownik nie posiada konta, może wybrac opcję stworzenia konta.
 * @author MK
 */
public class Login extends AppCompatActivity {

  /**
   * Pola tesktowe umożliwiające wprowadzenie nazwy loginu i hasła.
   */
  TextInputEditText textInputEditTextUsername, textInputEditTextPassword;
  /**
   * Przycisk logowania.
   */
  Button buttonLogin;
  /**
   * Przycisk umożliwiający przejście do panelu rejestracji.
   */
  TextView textViewSignUp;
  /**
   * Nazwa użytkownika, kóra poda klient
   */
  public static String LOGIN = "main.Login.LOGIN";

  /**
   * Metoda tworzaca panel logowania i initializująca obsługę GUI
   * @param savedInstanceState
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    textInputEditTextPassword = findViewById(R.id.password);
    textInputEditTextUsername = findViewById(R.id.username);
    buttonLogin = findViewById(R.id.buttonLogin);
    textViewSignUp = findViewById(R.id.signUpText);

    textViewSignUp.setOnClickListener(
        v -> {
          Intent intent = new Intent(getApplicationContext(), SignUp.class);
          startActivity(intent);
          finish();
        });

    buttonLogin.setOnClickListener(
        v -> {
          final String fullname, username, password, email;

          username = String.valueOf(textInputEditTextUsername.getText());
          password = String.valueOf(textInputEditTextPassword.getText());

          if (!username.equals("") && !password.equals("")) {

            Handler handler = new Handler();
            handler.post(
                () -> {
                  String[] field = new String[2];

                  field[0] = "username";
                  field[1] = "password";

                  // Creating array for data
                  String[] data = new String[2];

                  data[0] = username;
                  data[1] = password;

                  PutData putData =
                      new PutData(
                          "http://" + Configuration.IP + "/LoginRegister/login.php",
                          "POST",
                          field,
                          data);
                  if (putData.startPut()) {
                    if (putData.onComplete()) {
                      String result = putData.getResult();
                      if (result.equals("Login Success")) {
                        buttonLogin.setBackgroundResource(R.drawable.buttonshapeaccept);
                        buttonLogin.setText("Sign in success");
                        buttonLogin.setTextSize(20);
                        String text = ((EditText) findViewById(R.id.username)).getText().toString();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        Connection connection = Connection.getConnection();
                        connection.setUsername(text);
                        try {
                          connection.connect();
                        } catch (IOException e) {
                          e.printStackTrace();
                        }

                        startActivity(intent);
                        finish();
                      } else {
                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                      }
                    }
                  }
                });
          } else {

            buttonLogin.setBackgroundResource(R.drawable.buttonshapedecline);
            buttonLogin.setText("Sign in failed! Try again");
            buttonLogin.setTextSize(20);
          }
        });
  }
}
