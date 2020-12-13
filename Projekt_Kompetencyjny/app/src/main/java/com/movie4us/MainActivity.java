package com.movie4us;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import data.MovieData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

  private Toolbar toolbar;
  private Button buttonConnect;
  private TextInputEditText textInputUsernameToConnect;
  private Message message;
  private Gson gson;
  boolean listener;

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
    Connection connection = Connection.getConnection();
    listener = true;

    connection
        .getExecutorService()
        .execute(
            () -> {
              while (listener) {
                try {
                  String s = connection.getIn().readLine();
                  message = gson.fromJson(s, Message.class);

                  switch (message.getAction()) {
                    case "connect":
                      {
                        System.out.println(message.toString());
                        listener = false;

                        Intent intent =
                            new Intent(getApplicationContext(), GenreSelectionActivity.class);
                        startActivity(intent);

                        break;
                      }
                      //                    case "category":
                      //                      {
                      //                        int i = 0;
                      //                        for (MovieData movieData :
                      // message.getMovies().movieDataArray) {
                      //                          System.out.println(i);
                      //                          i++;
                      //                          movieData.toString();
                      //                        }
                      //                        break;
                      //                      }
                    case "selectedGenres":
                      {
                        listener = false;

                        Intent intent =
                            new Intent(getApplicationContext(), CardSwipeActivity.class);
                        startActivity(intent);

                        break;
                      }
                  }

                  System.out.println(s);
                } catch (IOException e) {
                  e.printStackTrace();
                }
              }
            });

    buttonConnect.setOnClickListener(
        v ->
            connection
                .getExecutorService()
                .execute(
                    () -> {
                      message.setAction("connect");
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
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.menu, menu);
    return true;
  }
}
