package com.movie4us;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

  private Toolbar toolbar;
  private Button buttonConnect;
  private TextInputEditText textInputUsernameToConnect;
  private Message message;
  private Gson gson;
  boolean listener;
  private Connection connection;
  private Runnable listenerThread;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_main);
    textInputUsernameToConnect = findViewById(R.id.usernameToConnect);
    buttonConnect = findViewById(R.id.buttonConnect);

    gson = new Gson();
    message = new Message();

    toolbar = findViewById(R.id.myToolBar);
    setSupportActionBar(toolbar);
    connection = Connection.getConnection();
    listener = true;

    TextView logout = findViewById(R.id.logout);
    logout.setOnClickListener(
        v -> {
          connection
              .getExecutorService()
              .execute(
                  () -> {
                    message.setUsername(connection.getUsername());
                    message.setAction("logout");
                    connection.send(gson.toJson(message));
                  });
          logout();
        });

    listenerThread =
        () -> {
          System.out.println("listenerThread  start !!!!!");
          while (listener) {
            try {
              String s = connection.getIn().readLine();
              message = gson.fromJson(s, Message.class);

              switch (message.getAction()) {
                case "accept":
                  {
                    AcceptDialog dialog = new AcceptDialog();
                    dialog.setMessage(message);
                    dialog.show(getSupportFragmentManager(), "accept_dialog");
                    break;
                  }
                case "reject":
                  {
                    ConnectDialog dialog = new ConnectDialog();
                    dialog.setMessage(message);
                    dialog.show(getSupportFragmentManager(), "info_dialog");
                    break;
                  }
                case "connect":
                  {
                    listener = false;
                    if (!message.getStatus().equals("out")) {
                      ConnectDialog dialog = new ConnectDialog();
                      dialog.setMessage(message);
                      Intent intent =
                          new Intent(getApplicationContext(), GenreSelectionActivity.class);
                      dialog.setIntent(intent);
                      dialog.show(getSupportFragmentManager(), "info_dialog");
                    } else {
                      System.out.println(message.toString());
                      Intent intent =
                          new Intent(getApplicationContext(), GenreSelectionActivity.class);
                      startActivity(intent);

                    }
                    //                    System.out.println(message.toString());
                    //                    Intent intent = new Intent(getApplicationContext(),
                    // GenreSelectionActivity.class);
                    //                    startActivity(intent);
                    //                    System.out.println("koniec watku");
                    break;
                  }
                case "match":
                  {
                    System.out.println("***************** MATCH !!!!!!!!!!!!!!!!!!!!!");
                    break;
                  }
                case "echo":
                  {
                    System.out.println("->>>>>>>> " + message.getAction());
                    break;
                  }
                case "logout":
                  {
                    listener = false;
                    System.out.println("logout server");
                    break;
                  }
              }

              System.out.println("********************************"+s);
            } catch (IOException e) {

              //              e.printStackTrace();
              return;
            }
          }
        };

    connection.getExecutorService().execute(listenerThread);

    buttonConnect.setOnClickListener(
        v ->
            connection
                .getExecutorService()
                .execute(
                    () -> {
                      //message.setAction("connect");
                      message.setAction("accept");
                      message.setConnectedUser(
                          String.valueOf(textInputUsernameToConnect.getText()));
                      message.setUsername(connection.getUsername());
                      connection.send(gson.toJson(message));
                    }));
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {

    switch (item.getItemId()) {
      case R.id.logout:
        Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
        break;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  protected void onRestart() {
    super.onRestart();
    listener = true;
    connection.getExecutorService().execute(listenerThread);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.menu, menu);
    return true;
  }

  private void logout() {
    connection.getExecutorService().shutdown();
    connection.getExecutorService().shutdownNow();

    try {
      connection.getOut().close();
      connection.getIn().close();
      connection.getSocket().close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    connection.getExecutorService().shutdownNow();
    System.out.println("logout koniec");
    Intent intent = new Intent(getApplicationContext(), Login.class);
    startActivity(intent);
    finish();
  }
}
