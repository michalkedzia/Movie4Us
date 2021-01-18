package loginRegister;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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

public class Login extends AppCompatActivity {
  TextInputEditText textInputEditTextUsername, textInputEditTextPassword;
  Button buttonLogin;
  TextView textViewSignUp;
  public static String LOGIN = "main.Login.LOGIN";


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    textInputEditTextPassword = findViewById(R.id.password);
    textInputEditTextUsername = findViewById(R.id.username);
    buttonLogin = findViewById(R.id.buttonLogin);
    textViewSignUp = findViewById(R.id.signUpText);

    textViewSignUp.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), SignUp.class);
            startActivity(intent);
            finish();
          }
        });

    buttonLogin.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {

            final String fullname, username, password, email;

            username = String.valueOf(textInputEditTextUsername.getText());
            password = String.valueOf(textInputEditTextPassword.getText());

            if (!username.equals("") && !password.equals("")) {

              // Start ProgressBar first (Set visibility VISIBLE)
              Handler handler = new Handler();
              handler.post(
                  new Runnable() {
                    @Override
                    public void run() {
                      // Starting Write and Read data with URL
                      // Creating array for parameters
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
                            String text =
                                ((EditText) findViewById(R.id.username)).getText().toString();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            Connection connection = Connection.getConnection();
                            connection.setUsername(text);
                            try {
                              connection.connect();
                            } catch (IOException e) {
                              e.printStackTrace();
                            }

                            //                                        intent.putExtra(LOGIN, text);
                            startActivity(intent);
                            finish();
                          } else {
                            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT)
                                .show();
                          }
                          // End ProgressBar (Set visibility to GONE)
                          //                        Log.i("PutData", result);
                        }
                      }
                      // End Write and Read data with URL
                    }
                  });
            } else {
              //              Toast.makeText(getApplicationContext(), "All fields required",
              // Toast.LENGTH_SHORT)
              //                  .show();
              buttonLogin.setBackgroundResource(R.drawable.buttonshapedecline);
              buttonLogin.setText("Sign in failed! Try again");
              buttonLogin.setTextSize(20);
            }
          }
        });
  }
}
