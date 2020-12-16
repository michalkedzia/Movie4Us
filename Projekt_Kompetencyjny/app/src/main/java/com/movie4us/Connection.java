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

  private String username;
  private ExecutorService executorService;
  private Socket socket;
  private PrintWriter out;
  private BufferedReader in;
  private Message message;
  private Gson gson;

  public static synchronized Connection getConnection() {
    if (connection == null) {
      connection = new Connection();
    }
    return connection;
  }

  public void connect() throws IOException {
    executorService = Executors.newFixedThreadPool(20);
    message = new Message();
    gson = new Gson();
    executorService.execute(
        () -> {
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
  }

  public synchronized void send(String message) {
    out.write(message + "\n");
    out.flush();
  }

  public String getUsername() {
    return username;
  }

  public Socket getSocket() {
    return socket;
  }

  public void setSocket(Socket socket) {
    this.socket = socket;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public ExecutorService getExecutorService() {
    return executorService;
  }

  public void setExecutorService(ExecutorService executorService) {
    this.executorService = executorService;
  }

  public PrintWriter getOut() {
    return out;
  }

  public void setOut(PrintWriter out) {
    this.out = out;
  }

  public BufferedReader getIn() {
    return in;
  }

  public void setIn(BufferedReader in) {
    this.in = in;
  }

  public Message getMessage() {
    return message;
  }

  public void setMessage(Message message) {
    this.message = message;
  }
}
