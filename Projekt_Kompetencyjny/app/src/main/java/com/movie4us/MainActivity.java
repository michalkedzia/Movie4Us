package com.movie4us;

import android.content.Intent;
import android.location.GpsSatellite;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


import android.widget.Spinner;
import android.widget.ThemedSpinnerAdapter;
import android.widget.Toast;
import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.channels.GatheringByteChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MainActivity extends AppCompatActivity {

    private ExecutorService executorService;
    public String username;
    private Toolbar toolbar;
    private Socket socket = null;
    private BufferedReader in;
    private PrintWriter out;
    private Button buttonConnect;
    private TextInputEditText textInputUsernameToConnect;
    private Spinner spinnerCategories;
    private Button buttonCategories;

    private Message message;
    private Gson gson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();

        username = intent.getStringExtra(Login.LOGIN);
        textInputUsernameToConnect = findViewById(R.id.usernameToConnect);
        buttonConnect = findViewById(R.id.buttonConnect);
        spinnerCategories = findViewById(R.id.spinnerCategories);
        buttonCategories =  findViewById(R.id.buttonCategories);

        gson = new Gson();
        message =  new Message();



        toolbar = findViewById(R.id.myToolBar);
        setSupportActionBar(toolbar);
        executorService = Executors.newFixedThreadPool(10);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket("169.254.250.43", 5000);
                    out = new PrintWriter(socket.getOutputStream(), true);
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                message.setAction("login");
                message.setUsername(username);
                out.write(gson.toJson(message)+ "\n");
                out.flush();

                while (true) {
                    try {
                        String s = in.readLine();
                        System.out.println(s);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });


        buttonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        message.setAction("connect");
                        message.setConnectedUser(String.valueOf(textInputUsernameToConnect.getText()));
                        out.write(gson.toJson(message)+ "\n");
                        out.flush();
                        runOnUiThread(() -> buttonConnect.setVisibility(View.GONE));
                    }
                });
            }
        });














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