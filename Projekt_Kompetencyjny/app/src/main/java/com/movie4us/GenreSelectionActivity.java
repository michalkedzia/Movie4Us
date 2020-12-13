package com.movie4us;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.gson.Gson;

import java.io.IOException;

public class GenreSelectionActivity extends AppCompatActivity {
  private Toolbar toolbar;
  private Spinner spinnerCategories;
  private Button buttonCategories;
  private Message message;
  private Gson gson;
  boolean listener;
  private Connection connection;
  private Runnable listenerThread;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.genre_selection);
    spinnerCategories = findViewById(R.id.spinnerCategories);
    buttonCategories = findViewById(R.id.buttonCategories);

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
          System.out.println("listenerThread - genreActivity start !!!!!");
          while (listener) {
            try {
              String s = connection.getIn().readLine();
              message = gson.fromJson(s, Message.class);

              switch (message.getAction()) {
                case "selectedGenres":
                  {
                    listener = false;
                    System.out.println("genres selected");
                    Intent intent = new Intent(getApplicationContext(), CardSwipeActivity.class);
                    startActivity(intent);

                    break;
                  }
                case "logout":
                  {
                    listener = false;
                    System.out.println("logout server");
                    break;
                  }
              }

              System.out.println(s);
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
        };

    connection.getExecutorService().execute(listenerThread);

    buttonCategories.setOnClickListener(
        v ->
            connection
                .getExecutorService()
                .execute(
                    () -> {
                      message.setAction("category");
                      message.setUsername(connection.getUsername());
                      message.setSelectedCategory(spinnerCategories.getSelectedItem().toString());
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
