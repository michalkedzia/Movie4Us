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
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

//  private ExecutorService executorService;
//  public String username;
  private Toolbar toolbar;
//  private Socket socket = null;
//  private BufferedReader in;
//  private PrintWriter out;
  private Button buttonConnect;
  private TextInputEditText textInputUsernameToConnect;
  private Spinner spinnerCategories;
  private Button buttonCategories;
  private Connection connection;
  private Message message;
  private Gson gson;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    textInputUsernameToConnect = findViewById(R.id.usernameToConnect);
    buttonConnect = findViewById(R.id.buttonConnect);
    spinnerCategories = findViewById(R.id.spinnerCategories);
    buttonCategories = findViewById(R.id.buttonCategories);

    gson = new Gson();
    message = new Message();

    connection = Connection.getConnection();

    toolbar = findViewById(R.id.myToolBar);
    setSupportActionBar(toolbar);



    System.out.println("on create main");



//    connection.getExecutorService().execute(
//        () -> {
//
//
//          while (true) {
//            try {
//              String s = connection.getIn().readLine();
//              message = gson.fromJson(s, Message.class);
//
//              switch (message.getAction()) {
//                case "connect":
//                  {
//                    System.out.println(message.toString());
//                    break;
//                  }
//                case "category":
//                  {
//                    int i = 0;
//                    for (MovieData movieData : message.getMovies().movieDataArray) {
//                      System.out.println(i);
//                      i++;
//                      movieData.toString();
//                    }
//                    break;
//                  }
//                case "match":
//                  {
//                    System.out.println("***************** MATCH !!!!!!!!!!!!!!!!!!!!!");
//                    break;
//                  }
//              }
//
//              System.out.println(s);
//            } catch (IOException e) {
//              e.printStackTrace();
//            }
//          }
//        });

    buttonConnect.setOnClickListener(
        v ->
            connection.getExecutorService().execute(
                () -> {
                  Intent intent1 = new Intent(getApplicationContext(), MovieListActivity.class);
                  startActivity(intent1);
                  System.out.println("------------ Main");
//                  message.setAction("connect");
//                  message.setConnectedUser(String.valueOf(textInputUsernameToConnect.getText()));
//                  message.setUsername(connection.getUsername());
//                  connection.getOut().write(gson.toJson(message) + "\n");
//                  connection.getOut().flush();
////                  runOnUiThread(() -> buttonConnect.setVisibility(View.GONE)); // ukruwanie button
                }));

    buttonCategories.setOnClickListener(
        v ->
                connection.getExecutorService().execute(
                () -> {
                  message.setAction("category");
                  message.setUsername(connection.getUsername());
                  message.setSelectedCategory(spinnerCategories.getSelectedItem().toString());
                  connection.getOut().write(gson.toJson(message) + "\n");
                  connection.getOut().flush();
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



    @Override
    protected void onDestroy() {
        System.out.println("Destroy main");
        super.onDestroy();
    }
}
