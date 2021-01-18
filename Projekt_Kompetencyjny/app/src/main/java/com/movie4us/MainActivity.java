package com.movie4us;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import dialog.AcceptDialog;
import loginRegister.Login;
import model.Message;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    System.out.println(getRequestedOrientation());
    toolbar = findViewById(R.id.myToolBar);
    setSupportActionBar(toolbar);
    connection = Connection.getConnection();
    listener = true;

    connection
        .getExecutorService()
        .execute(
            () -> {
              message.setUsername(connection.getUsername());
              message.setAction("getFriendsList");
              connection.send(gson.toJson(message));
            });

    ListView listView = findViewById(R.id.firendsList);
    List<String> firends = new ArrayList<>();

    ArrayAdapter arrayAdapter =
        new ArrayAdapter(this, android.R.layout.simple_list_item_1, firends);
    listView.setAdapter(arrayAdapter);
    listView.setOnItemClickListener(
        (parent, view, position, id) -> textInputUsernameToConnect.setText(firends.get(position)));

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
                      Intent intent =
                          new Intent(getApplicationContext(), GenreSelectionActivity.class);
                      startActivity(intent);
                    }
                    break;
                  }
                case "match":
                  {
                    break;
                  }
                case "echo":
                  {
                    break;
                  }
                case "logout":
                  {
                    listener = false;
                    break;
                  }
                case "getFriendsList":
                  {
                    runOnUiThread(
                        () -> {
                          for (String frnd : message.getFriendsList()) {
                            if (!firends.contains(frnd)) {
                              firends.add(frnd);
                            }
                          }
                          arrayAdapter.notifyDataSetChanged();
                        });
                    break;
                  }
                case "error":
                  {
                    runOnUiThread(
                        () -> {
                          Toast.makeText(this, message.getError(), Toast.LENGTH_SHORT).show();
                        });
                    break;
                  }
              }

            } catch (IOException e) {
              e.printStackTrace();
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
                      // message.setAction("connect");
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
    message.setUsername(connection.getUsername());
    message.setAction("getFriendsList");
    connection.getExecutorService().execute(() -> connection.send(gson.toJson(message)));
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
    Intent intent = new Intent(getApplicationContext(), Login.class);
    startActivity(intent);
    finish();
  }
}
