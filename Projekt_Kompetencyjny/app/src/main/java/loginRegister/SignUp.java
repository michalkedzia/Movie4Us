package loginRegister;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.movie4us.R;
import com.vishnusivadas.advanced_httpurlconnection.PutData;
import config.Configuration;

/**
 * Klasa rejestracji użytkownika do aplikacji.
 * Rejestracja w aplikacji jest możliwa po podaniu: nazwy uzytkownika, loginu, adresu email i hasła.
 * Jeśli wszystkie dane są poprawne, uzytkownik rejestruję się w aplikacji.
 * Po rejestracji ma możliwośc przejścia do panelu logowania za pomocą przycisku.
 * @author MK
 */
public class SignUp extends AppCompatActivity {
    /**
     * Pola tekstowe umożliwiające użytkownikowi wpisanie pełnej nazwy, loginu, adresu email i hasła
     */
  TextInputEditText textInputEditTextFullname,
      textInputEditTextUsername,
      textInputEditTextPassword,
      textInputEditTextEmail;
    /**
     * Przycisk rejestrujący użytkownika
     */
  Button buttonSignUp;
    /**
     * Przycisk przechodzący do panelu logowania
     */
  TextView textViewLogin;

    /**
     * Metoda tworzaca panel rejestracji i initializująca obsługę GUI
     * @param savedInstanceState
     */
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
                            "http://" + Configuration.IP + "/LoginRegister/signup.php",
                            "POST",
                            field,
                            data);
                    if (putData.startPut()) {
                      if (putData.onComplete()) {
                        String result = putData.getResult();
                        if (result.equals("Sign Up Success")) {
                          buttonSignUp.setBackgroundResource(R.drawable.buttonshapeaccept);
                          buttonSignUp.setText("Sign Up Success");
                          buttonSignUp.setTextSize(20);
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
            buttonSignUp.setBackgroundResource(R.drawable.buttonshapedecline);
            buttonSignUp.setText("Sign up Failed! Try again");
            buttonSignUp.setTextSize(20);
          }
        });
  }
}
