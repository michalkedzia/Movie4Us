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
  boolean cancelGenresSelection;
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
    cancelGenresSelection = true;

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
              System.out.println("*********** "+message.getAction());
              switch (message.getAction()) {
                case "selectedGenres":
                  {
                    listener = false;
                    cancelGenresSelection = false;
                    System.out.println("genres selected");
                    Intent intent = new Intent(getApplicationContext(), CardSwipeActivity.class);
                    startActivity(intent);
                    break;
                  }
                case "logout":
                  {
                    listener = false;
                    cancelGenresSelection = false;
                    System.out.println("logout server");
                    break;
                  }
                case "cancelGenresSelection":
                  {
                    cancelGenresSelection = false;
                    listener = false;
                    InfoDialog infoDialog =
                        new InfoDialog(
                            "Błąd", "Użytkownik po drugiej stronie wyszedł z wyboru kategori.");
                    infoDialog.show(getSupportFragmentManager(), "infoDialog");

                    break;
                  }
                case "stop":
                  {
                    System.out.println("************** GENRE SELECTED ACTIVITY    stop");
                    cancelGenresSelection = false;
                    listener = false;
                    break;
                  }
              }

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

  @Override
  protected void onPause() {
    super.onPause();
    if (cancelGenresSelection) {
      connection
          .getExecutorService()
          .execute(
              () -> {
                Message msg = new Message();
                msg.setAction("cancelGenresSelection");
                msg.setUsername(connection.getUsername());
                connection.send(gson.toJson(msg));
              });
    }
    System.err.println("onPause()");
  }

  @Override
  protected void onStart() {
    super.onStart();
    System.err.println("onStart()");
  }

  @Override
  protected void onStop() {
    super.onStop();
    System.err.println("onStop()");
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    System.err.println("onDestroy()");
  }

  @Override
  protected void onResume() {
    super.onResume();

//    listener = true;
//    cancelGenresSelection = true;
//    connection.getExecutorService().execute(listenerThread);
//    System.err.println("onResume() ");
  }

  @Override
  protected void onRestart() {
    super.onRestart();
    System.err.println("onRestart()");
    listener = true;
    cancelGenresSelection = true;
    connection.getExecutorService().execute(listenerThread);
    System.err.println("onResume() ");
  }
}
