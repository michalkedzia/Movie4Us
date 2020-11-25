package handlers;

import api.data.PageMovieData;
import com.google.gson.Gson;
import log.MyLOG;
import model.DataMovies;
import model.Message;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

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
      }

      MyLOG.myLOG(peek.toString());
    }
  }

  private void categorySelection(Message message) {
    ClientHandler clientHandler = clientsMap.get(message.getUsername());
    if (clientHandler.getCommonList().stream()
        .map(Message::getAction)
        .collect(Collectors.toList())
        .contains("category")) {
      Iterator<Message> iterator = clientHandler.getCommonList().iterator();
      while (iterator.hasNext()) {       Message next = iterator.next();
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

    DataMovies dataMovies = new DataMovies(genres);
    PageMovieData movies = dataMovies.getMovies();
    message.setMovies(movies);
    message.setAction("category");
    Gson gson = new Gson();

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

    Message message = new Message();
    message.setAction("connect");
    Gson gson = new Gson();

    clientHandlerFrom.getOut().write(gson.toJson(message) + "\n");
    clientHandlerFrom.getOut().flush();

    clientHandlerTo.getOut().write(gson.toJson(message) + "\n");
    clientHandlerTo.getOut().flush();

    Vector<Message> list = new Vector<>();
    clientHandlerFrom.setCommonList(list);
    clientHandlerTo.setCommonList(list);
  }

  void moviematcher(Message message) {
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
    }
  }
}
