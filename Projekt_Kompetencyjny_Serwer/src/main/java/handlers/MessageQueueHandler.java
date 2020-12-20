package handlers;

import api.data.PageMovieData;
import com.google.gson.Gson;
import model.DataMovies;
import model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
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
  private Logger LOG = LoggerFactory.getLogger(MessageQueueHandler.class);

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
            matchStop(peek);
            break;
          }
        case "cancelGenresSelection":
          {
            cancelGenresSelection(peek);
            break;
          }
      }
      LOG.info(peek.toString());
    }
  }

  private String toGson(Message message) {
    Gson gson = new Gson();
    return gson.toJson(message) + "\n";
  }

  private void send(PrintWriter printWriter, Message msg) {
    printWriter.write(toGson(msg));
    printWriter.flush();
  }

  private void sendInfo(Message message, String action) {
    ClientHandler clientHandlerTo = clientsMap.get(message.getConnectedUser());
    Message acceptMessage = new Message();
    acceptMessage.setAction(action);
    acceptMessage.setConnectedUser(message.getUsername());
    send(clientHandlerTo.getOut(), acceptMessage);
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
    Message genresSelection = new Message();
    genresSelection.setUsername(message.getUsername());
    genresSelection.setAction("selectedGenres");
    ClientHandler client = clientsMap.get(message.getUsername());
    send(client.getOut(), genresSelection);

    genresSelection.setUsername(client.getConnectedUser());
    client = clientsMap.get(client.getConnectedUser());
    send(client.getOut(), genresSelection);

    DataMovies dataMovies = new DataMovies(genres);
    PageMovieData movies = dataMovies.getMovies();
    message.setMovies(movies);
    message.setAction("category");

    ClientHandler clientHandler = clientsMap.get(message.getUsername());
    send(clientHandler.getOut(), message);

    clientHandler = clientsMap.get(clientHandler.getConnectedUser());
    send(clientHandler.getOut(), message);
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

    send(clientHandlerFrom.getOut(), messageFrom);
    send(clientHandlerTo.getOut(), messageTo);

    Vector<Message> list = new Vector<>();
    clientHandlerFrom.setCommonList(list);
    clientHandlerTo.setCommonList(list);
  }

  private void moviematcher(Message message) {
    ClientHandler clientHandler = clientsMap.get(message.getUsername());
    if (clientHandler.getCommonList().stream()
        .anyMatch(
            m ->
                (m.getAction().equals("selectedMovie")
                    && m.getMovieId() == message.getMovieId()))) {
      Message match = new Message();
      match.setAction("match");
      match.setMovieId(message.getMovieId());
      send(clientHandler.getOut(), match);

      clientHandler = clientsMap.get(clientHandler.getConnectedUser());
      send(clientHandler.getOut(), match);

    } else {
      clientHandler.getCommonList().add(message);
    }
  }

  private void echo(Message message) {
    ClientHandler clientHandler = clientsMap.get(message.getUsername());
    send(clientHandler.getOut(), message);
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

  private void cancelGenresSelection(Message message) {
    ClientHandler clientHandler = clientsMap.get(message.getUsername());
    Message msg = new Message();
    msg.setAction("stop");
    msg.setConnectedUser(clientHandler.getConnectedUser());
    send(clientHandler.getOut(), msg);

    clientHandler = clientsMap.get(clientHandler.getConnectedUser());
    msg.setAction("cancelGenresSelection");
    msg.setConnectedUser(message.getUsername());
    send(clientHandler.getOut(), msg);
  }

  private void matchStop(Message message) {
    ClientHandler clientHandler = clientsMap.get(message.getUsername());
    if (clientHandler.getCommonList() != null) {
      clientHandler.getCommonList().clear();
    }
    send(clientHandler.getOut(), message);

    clientHandler = clientsMap.get(clientHandler.getConnectedUser());
    if (clientHandler.getCommonList() != null) {
      clientHandler.getCommonList().clear();
    }
    send(clientHandler.getOut(), message);
  }
}
