package com.movie4us;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Connection {

    private static Connection connection = null;
    private Connection() {}

    public String username;
    private Socket socket = null;
    private BufferedReader in;
    private PrintWriter out;
    Message message;
    Gson gson;
    ExecutorService executorService;

    public static synchronized Connection getConnection() {
        if (connection == null) {
            connection = new Connection();
        }
        return connection;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public BufferedReader getIn() {
        return in;
    }

    public PrintWriter getOut() {
        return out;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public void connect(String username){
        try{
            gson = new Gson();
            message = new Message();
            this.username = username;
            executorService = Executors.newFixedThreadPool(10);


            executorService.execute(() -> {
                try {
                    socket = new Socket("169.254.250.43", 5000);
                    out = new PrintWriter(socket.getOutputStream(), true);
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                message.setAction("login");
                message.setUsername(username);
                out.write(gson.toJson(message) + "\n");
                out.flush();
            });


        }catch (Exception exception){
            exception.printStackTrace();
        }
    }



}
