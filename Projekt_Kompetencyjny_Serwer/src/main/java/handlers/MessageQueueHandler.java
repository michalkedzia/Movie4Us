package handlers;

import api.data.PageMovieData;
import com.google.gson.Gson;
import db.DBQueries;
import model.DataMovies;
import model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * Główna klasa serwera obługująca wszystkie żądania klientów.
 *
 * @author MK
 */
public class MessageQueueHandler implements Runnable {
  /** Mapa użytkowników przechowująca nazwę użytkownika oraz klase obsługującą danego klienta. */
  private ConcurrentMap<String, ClientHandler> clientsMap;
  /** Globalna kolejka cąłego serwera przechowująca wszystkie żądania klientów. */
  private BlockingQueue<Message> messageQueue;

  private Logger LOG = LoggerFactory.getLogger(MessageQueueHandler.class);

  /**
   * Konstrukotr klasy.
   *
   * @param clientsMap mapa klientów połączonych z serwerem.
   * @param messageQueue globalan kolejka serwra
   */
  public MessageQueueHandler(
      ConcurrentMap<String, ClientHandler> clientsMap, BlockingQueue<Message> messageQueue) {
    this.clientsMap = clientsMap;
    this.messageQueue = messageQueue;
  }

  /**
   * Głowny wątek serwera pobierający żadania klientów z kolejki i obsługuje je za pomocą
   * oddelegowanych do tego celu metod.
   */
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
            if (checkIfUserIsLoggedIn(peek.getConnectedUser())) {
              sendInfo(peek, "accept");
            } else {
              sendError(peek, "The selected user is not available.");
            }
            break;
          }
        case "reject":
          {
            sendInfo(peek, "reject");
            break;
          }
        case "connect":
          {
            if (checkIfUserIsLoggedIn(peek.getConnectedUser())) {
              connectTwoUser(peek.getUsername(), peek.getConnectedUser());
            }
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
        case "getFriendsList":
          {
            getFriendsList(peek);
            break;
          }
      }
      LOG.info(peek.toString());
    }
  }

  /**
   * Metoda konwertująca klasę Message do postaci JSON
   *
   * @param message Wiadomośc klent-serwer
   * @return
   */
  private String toGson(Message message) {
    Gson gson = new Gson();
    return gson.toJson(message) + "\n";
  }

  /**
   * Metoda dodaje użytkownika z który chcemy się połączyc do bazy danych. Wszystkich użytkowników
   * tak dodanych do bazy danych, możena póżniej wyświetlic na liście znajomych.
   *
   * @param username nazwa użytkonika
   * @param friendName nazwa uzytkownika z którym chcemy się połączyć
   * @return true - w przypadku udanego wstawienia do bazy danych, false - nie udało wstawić się do
   *     bazy danych (użytkownik jest już dodany)
   */
  private boolean addFriendToDataBase(String username, String friendName) {
    List<String> allUsers = DBQueries.getAllUsers();
    if (DBQueries.getFriendsList(username).contains(friendName)) return false;
    if (!allUsers.contains(friendName)) return false;
    DBQueries.insertFriend(friendName, username);
    return true;
  }

  /**
   * @param printWriter strumień klienta do którego ma zostac wysłana wiadomość
   * @param msg wiadomośc która ma zostać wysłana
   */
  private void send(PrintWriter printWriter, Message msg) {
    printWriter.write(toGson(msg));
    printWriter.flush();
  }

  /**
   * Metoda wysyłająca aktualny stan połączonego klienta
   *
   * @param message wiadomośc
   * @param action typo akcji
   */
  private void sendInfo(Message message, String action) {
    ClientHandler clientHandlerTo = clientsMap.get(message.getConnectedUser());
    Message acceptMessage = new Message();
    acceptMessage.setAction(action);
    acceptMessage.setConnectedUser(message.getUsername());
    send(clientHandlerTo.getOut(), acceptMessage);
  }

  /**
   * Metoda sprawdza czy w liście wiadomości danego klienta jest wpis o tym że wybrał kategorię
   * filu. Jeśli jest pobiera go z listy. Na podstawie pobrango wpisu i wpisu z parametru message
   * pobiera z bazy danych filmy pasujące do danych kategori, nastepnie wysła je do obydówch
   * klientów.
   *
   * @param message wiadomość otrzymna od klienta
   */
  private void categorySelection(Message message) {
    ClientHandler clientHandler = clientsMap.get(message.getUsername());
    if (clientHandler.getCommonList().stream()
        .map(Message::getAction)
        .collect(Collectors.toList())
        .contains("category")) {
      Iterator<Message> iterator = clientHandler.getCommonList().iterator();
      while (iterator.hasNext()) {
        Message next = iterator.next();
        if (next.getAction().equals("category")
            && !next.getUsername().equals(message.getUsername())) {

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

  /**
   * Metoda wysła liste wybraną liste filmów do dwóch użytkowników.
   *
   * @param message wiadomośc do wysłania, zawierająca nazwy użytkowników obdywóch klientów
   * @param genres lista filmów do wysłania
   */
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

  /**
   * @param from nazwa użytkownika od którego wysyłana jest prośba o połączenie
   * @param to nazwa użytkownika do którego wysyłana jest prośba o połączenie
   */
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
    System.out.println("************** " + addFriendToDataBase(to, from));
  }

  /**
   * Metoda sprawdza czy na wspłólnej liście klientów znajduję sie film o ID wybrany przez jednego z
   * klientów. Jeśli na liście znajduje się taki film i drugi użytkownik wybrał film o tym samym ID,
   * z listy usuwany jest dany wpis, a następnie do obydówch użytkowników wysyłana jest informacja o
   * współnym wyborze tego samego filmu.
   *
   * @param message wiadomośc w której jest zawarata informacja od wybranym filmie
   */
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

  /**
   * Metoda odsyła tą samą wiadośc do użytkownika, który ją wysłał
   *
   * @param message wiadomoś
   */
  private void echo(Message message) {
    ClientHandler clientHandler = clientsMap.get(message.getUsername());
    send(clientHandler.getOut(), message);
  }

  /**
   * Metoda wylogowyjąca danego użytkownika z serwera. Usuwa go z mapy aktywnych użytkowników.
   *
   * @param message
   */
  private void logout(Message message) {
    ClientHandler user = clientsMap.get(message.getUsername());
    clientsMap.remove(message.getUsername());
  }

  /**
   * Metoda obsługuję przypadek wyścia jednego z użytkowników z panelu wyboru kategori. Wysła do do
   * połączonego użytkownika informacje o tym fakcie.
   *
   * @param message
   */
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

  /**
   * Pobiera z bazy danych liste znajomych danego klienta z którym łączył się już.
   *
   * @param message
   */
  private void getFriendsList(Message message) {
    ClientHandler clientHandler = clientsMap.get(message.getUsername());
    List<String> friendsList = DBQueries.getFriendsList(message.getUsername());
    Set<String> strings = clientsMap.keySet();
    List<String> list = new LinkedList<>();
    for (String s : friendsList) {
      if (strings.contains(s)) list.add(s);
    }
    message.setFriendsList(list);
    send(clientHandler.getOut(), message);
  }

  /**
   * Obługuję wyjście jednego z użytkowników z oanelu wyboru filmu. Wysyła tą informacje do klienta
   * połączonego z nim.
   *
   * @param message
   */
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

  /**
   * Metoda sprawdza czy dany użytkownik znajduję się w mapie klientów (czy jest zalogowany)
   *
   * @param username nazwa użytkjownika
   * @return nazwa użytkownika którego chcemy sprawdzic czy aktualnie jest zalogowany
   */
  private boolean checkIfUserIsLoggedIn(String username) {
    return clientsMap.containsKey(username);
  }

  /**
   * Metoda wysła do danego klienta wiadomośc o błędzie
   *
   * @param message
   * @param errorText opis błędu
   */
  private void sendError(Message message, String errorText) {
    Message msg = new Message();
    msg.setAction("error");
    msg.setError(errorText);
    msg.setUsername(message.getUsername());
    msg.setConnectedUser(message.getConnectedUser());
    send(clientsMap.get(message.getUsername()).getOut(), msg);
  }
}
