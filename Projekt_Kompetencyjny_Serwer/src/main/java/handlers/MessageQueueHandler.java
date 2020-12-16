package handlers;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import api.data.PageMovieData;
import com.google.gson.Gson;
import log.*;
import model.*;

import javax.swing.*;

public class MessageQueueHandler implements Runnable {
  private ConcurrentMap<String, ClientHandler> clientsMap;
  private BlockingQueue<Message> messageQueue;

  public MessageQueueHandler(
      ConcurrentMap<String, ClientHandler> clientsMap, BlockingQueue<Message> messageQueue) {
    this.clientsMap = clientsMap;
    this.messageQueue = messageQueue;
  }

  @Override
  public void run() {
    while (true) {
      Message peek = null;
      try {
        peek = messageQueue.take();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

      switch (peek.getAction()) {
        case "accept":
        {
          sendInfo(peek, "accept");
          break;
        }
        case "reject":
        {
          sendInfo(peek, "reject");
          break;
        }
        case "connect":
          {
            connectTwoUser(peek.getUsername(), peek.getConnectedUser());
            break;
          }
        case "category":
          {
            categorySelection(peek);
            break;
          }
        case "selectedMovie":
          {
            moviematcher(peek);
            break;
          }
        case "echo":
          {
            echo(peek);
            break;
          }
        case "logout":
          {
            logout(peek);
            break;
          }
        case "matchStop":
          {
            echo(peek);
            break;
          }
      }
      MyLOG.myLOG(peek.toString());
    }
  }

  private void sendInfo(Message message, String action){
    ClientHandler clientHandlerTo = clientsMap.get(message.getConnectedUser());

    Message acceptMessage = new Message();
    acceptMessage.setAction(action);
    acceptMessage.setConnectedUser(message.getUsername());
    Gson gson = new Gson();

    clientHandlerTo.getOut().write(gson.toJson(acceptMessage) + "\n");
    clientHandlerTo.getOut().flush();
  }

  private void categorySelection(Message message) {
    ClientHandler clientHandler = clientsMap.get(message.getUsername());
    if (clientHandler.getCommonList().stream()
        .map(Message::getAction)
        .collect(Collectors.toList())
        .contains("category")) {
      Iterator<Message> iterator = clientHandler.getCommonList().iterator();
      while (iterator.hasNext()) {
        Message next = iterator.next();
        if (next.getAction().equals("category")) {

          List<String> genres = new LinkedList<>();
          genres.add(next.getSelectedCategory());
          genres.add(message.getSelectedCategory());
          sendMoviesToBothUsers(message, genres);
          iterator.remove();
          break;
        }
      }
    } else {
      clientHandler.getCommonList().add(message);
    }
  }

  private void sendMoviesToBothUsers(Message message, List<String> genres) {

    // ** Dodatkowa wiadomosc od serwera do klienta- wybarnie kategorii
    Message genresSelection = new Message();
    Gson gson = new Gson();
    genresSelection.setUsername(message.getUsername());
    genresSelection.setAction("selectedGenres");
    ClientHandler client = clientsMap.get(message.getUsername());
    client.getOut().write(gson.toJson(genresSelection) + "\n");
    client.getOut().flush();

    genresSelection.setUsername(client.getConnectedUser());
    client = clientsMap.get(client.getConnectedUser());
    client.getOut().write(gson.toJson(genresSelection) + "\n");
    client.getOut().flush();
    // **

    DataMovies dataMovies = new DataMovies(genres);
    PageMovieData movies = dataMovies.getMovies();
    message.setMovies(movies);
    message.setAction("category");

    ClientHandler clientHandler = clientsMap.get(message.getUsername());

    clientHandler.getOut().write(gson.toJson(message) + "\n");
    clientHandler.getOut().flush();

    clientHandler = clientsMap.get(clientHandler.getConnectedUser());

    clientHandler.getOut().write(gson.toJson(message) + "\n");
    clientHandler.getOut().flush();
  }

  // TODO nowy wyjatek do obsługi albo nie
  private void connectTwoUser(String from, String to) {
    ClientHandler clientHandlerFrom = clientsMap.get(from);
    ClientHandler clientHandlerTo = clientsMap.get(to);
    clientHandlerFrom.setConnectedUser(to);
    clientHandlerTo.setConnectedUser(from);

    Message messageFrom = new Message();
    messageFrom.setAction("connect");
    messageFrom.setUsername(from);
    messageFrom.setConnectedUser(to);
    messageFrom.setStatus("out");

    Message messageTo = new Message();
    messageTo.setAction("connect");
    messageTo.setUsername(to);
    messageTo.setConnectedUser(from);
    messageTo.setStatus("in");

//    Message message = new Message();
//    message.setAction("connect");
//    message.setUsername(to);
//    message.setConnectedUser(from);

    Gson gson = new Gson();

    clientHandlerFrom.getOut().write(gson.toJson(messageFrom) + "\n");
    clientHandlerFrom.getOut().flush();

    clientHandlerTo.getOut().write(gson.toJson(messageTo) + "\n");
    clientHandlerTo.getOut().flush();

    Vector<Message> list = new Vector<>();
    clientHandlerFrom.setCommonList(list);
    clientHandlerTo.setCommonList(list);
  }

  private void moviematcher(Message message) {

    ClientHandler clientHandler = clientsMap.get(message.getUsername());
    String connectedUser = clientHandler.getConnectedUser();

    if (clientHandler.getCommonList().stream()
        .anyMatch(
            m ->
                (m.getAction().equals("selectedMovie")
                    && m.getMovieId() == message.getMovieId()))) {
      Message match = new Message();
      match.setAction("match");
      Gson gson = new Gson();

      clientHandler.getOut().write(gson.toJson(match) + "\n");
      clientHandler.getOut().flush();

      clientHandler = clientsMap.get(clientHandler.getConnectedUser());

      clientHandler.getOut().write(gson.toJson(match) + "\n");
      clientHandler.getOut().flush();

    } else {
      clientHandler.getCommonList().add(message);
      System.out.println("nie znaleziono");
    }
  }

  private void echo(Message message) {
    ClientHandler clientHandler = clientsMap.get(message.getUsername());
    Gson gson = new Gson();
    clientHandler.getOut().write(gson.toJson(message) + "\n");
    clientHandler.getOut().flush();
  }

  // TODO rozłaczenie z drugim klientem,usuwanie z listy etc.
  private void logout(Message message) {
    ClientHandler user = clientsMap.get(message.getUsername());

    //   ClientHandler connectedUser = clientsMap.get(user.getConnectedUser());
    //   user.setCommonList(null);
    //   connectedUser.setConnectedUser(null);
    //   connectedUser.setCommonList(null);
    clientsMap.remove(message.getUsername());
  }
}
