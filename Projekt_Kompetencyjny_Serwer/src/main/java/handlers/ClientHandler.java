package handlers;

import com.google.gson.Gson;
import log.MyLOG;
import model.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;

/**
 * Klasa przechowująca informację klienta i obsługująca połączenie z klientem. Odbiera żądania
 * wysłania przez klienta, następnie umieszcza je w messageQueue.
 */
public class ClientHandler implements Runnable {
  /** Socket przydzielony przez główna klasę serwera. */
  private Socket socket;
  /** Strumien wyjścia danych danego klienta. */
  private PrintWriter out;
  /** Strumień wejścia danego klienta. */
  private BufferedReader in;
  /**
   * Kolejka wiadomości globalana dla całego serwera. Przechowuję żądania wysłane przez wszystkich
   * klientów.
   */
  private BlockingQueue<Message> messageQueue;
  /**
   * Vector przechowujący Messages. Jest to vector wspólny dla danego użytkownika i użytkownka z nim
   * połączonego. Vector przechowuje Messages wyłącznie z informacją o wybranym filmie przez dwóch
   * użytkowników. Jest on initializowany przez serwer.
   */
  private Vector<Message> commonList = null;
  /** Nazwa użytkownika połączonego z serwerem. */
  private String username = null;
  /** Nazwa użytkownika, który jest aktualnie połączony z danym klientem. */
  private String connectedUser = null;

  /**
   * Konstruktor klasy, parametry otrzymywane są od serwera.
   *
   * @param messageQueue globalna kolejka serwra
   */
  public ClientHandler(
      Socket socket, PrintWriter out, BufferedReader in, BlockingQueue<Message> messageQueue) {
    this.socket = socket;
    this.out = out;
    this.in = in;
    this.messageQueue = messageQueue;
  }

  public Vector<Message> getCommonList() {
    return commonList;
  }

  public void setCommonList(Vector<Message> commonList) {
    this.commonList = commonList;
  }

  public Socket getSocket() {
    return socket;
  }

  public void setSocket(Socket socket) {
    this.socket = socket;
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

  public BlockingQueue<Message> getMessageQueue() {
    return messageQueue;
  }

  public void setMessageQueue(BlockingQueue<Message> messageQueue) {
    this.messageQueue = messageQueue;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getConnectedUser() {
    return connectedUser;
  }

  public void setConnectedUser(String connectedUser) {
    this.connectedUser = connectedUser;
  }

  /**
   * Watek odbierający żądania od klienta i umieszczający je w kolejsce. Wątek kończy sie gdy,
   * użytkownik wylogował się, lub został napotkany błąd, którego nie jest w stanie obsłużyć.
   */
  @Override
  public void run() {
    String json = null;
    Gson gson = new Gson();
    Message message;

    while (true) {
      try {
        json = in.readLine();
      } catch (IOException e) {
        e.printStackTrace();
      }
      MyLOG.myLOG(json);
      message = gson.fromJson(json, Message.class);
      messageQueue.add(message);
      if (message.getAction().equals("logout")) {
        out.write(json + "\n");
        out.flush();
        try {
          in.close();
          out.close();
          socket.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
        return;
      }
    }
  }
}
