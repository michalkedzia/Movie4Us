import com.google.gson.Gson;
import handlers.ClientHandler;
import handlers.MessageQueueHandler;
import log.MyLOG;
import model.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * Klasa serwera rozpoczynająca pracę serwera
 *
 * @author MK
 */
public class MainServer {
  public static void main(String[] args) throws IOException {
    ExecutorService executorService = Executors.newFixedThreadPool(10);
    ServerSocket serverSocket = new ServerSocket(5000);
    Gson gson = new Gson();
    Message message;
    ConcurrentMap<String, ClientHandler> clientsMap = new ConcurrentHashMap<>();
    BlockingQueue<Message> messageQueue = new LinkedBlockingDeque<>();
    executorService.execute(new MessageQueueHandler(clientsMap, messageQueue));
    System.out.println("************--- SERVER START ---************");
    while (true) {
      Socket socket = serverSocket.accept();
      System.out.println(socket.toString());
      PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
      BufferedReader bufferedReader =
          new BufferedReader(new InputStreamReader(socket.getInputStream()));

      ClientHandler clientHandler;
      String json = bufferedReader.readLine();
      message = gson.fromJson(json, Message.class);
      if (message.getAction().equals("login")) {
        clientHandler = new ClientHandler(socket, printWriter, bufferedReader, messageQueue);
        clientHandler.setUsername(message.getUsername());
        clientsMap.put(message.getUsername(), clientHandler);
        executorService.execute(clientHandler);
      } else {
        MyLOG.myLOG("login error " + socket);
      }
    }
  }
}
