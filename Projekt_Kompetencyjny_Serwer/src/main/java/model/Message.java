package model;

import api.data.PageMovieData;

import java.util.List;

/**
 * Message to klasa-model, za pomocą któtej klient wymiania dane z serwerem.
 *
 * @author MK
 */
public class Message {
  /** Nazwa użytkownika. */
  private String username;
  /** Nazwa użytkownika z którym chcemy się połączyć lub nazwa użytkownika aktualnie połączonego. */
  private String connectedUser;
  /** Typ wiadomości na podstwie której klient lub serwer odczytuje potrzebne informacje. */
  private String action;
  /** Staus klienta który jest z nami połączony. */
  private String status;
  /** Nazwa kategori wybrana przez klienta. */
  private String selectedCategory;
  /** Lista filmów wybrana przez obydwóch klientów. */
  private PageMovieData movies;
  /** ID filmu zaakceptowane przez klienta. */
  private int movieId;
  /** Lista użytkowników z którymu łączylismy się wcześniej. */
  private List<String> friendsList;
  /** Opis, typ błędu. */
  private String error;

  /** @return zwraca napotakny błąd po stronie serwera */
  public String getError() {
    return error;
  }

  /** @param error typ błędu który ma zostać przekazany do serwera */
  public void setError(String error) {
    this.error = error;
  }

  /** @return zwraca liste użytkowników z którymi łączyliśmy sie wcześniej */
  public List<String> getFriendsList() {
    return friendsList;
  }

  /** @param friendsList lista użytkowników */
  public void setFriendsList(List<String> friendsList) {
    this.friendsList = friendsList;
  }

  /** @return zwraca nazwę użytkownika który wysłał daną wiadomość */
  public String getUsername() {
    return username;
  }

  /** @return zwraca ID filmu wybranego przez użytkownika wysyłącego Message */
  public int getMovieId() {
    return movieId;
  }

  /** @param movieId */
  public void setMovieId(int movieId) {
    this.movieId = movieId;
  }

  @Override
  public String toString() {
    return "Message{"
        + "username='"
        + username
        + '\''
        + ", connectedUser='"
        + connectedUser
        + '\''
        + ", action='"
        + action
        + '\''
        + ", selectedCategory='"
        + selectedCategory
        + '\''
        + ", movies="
        + movies
        + '}';
  }

  /** @return zwraca nazwe wybranej przez użytkownika nazwe kategori */
  public String getSelectedCategory() {
    return selectedCategory;
  }

  /** @return zwraca obiekt zawierający liste wybranych filmów przez dwóch uzytkowników */
  public PageMovieData getMovies() {
    return movies;
  }

  /** @param movies lista wybranych filmów przez obydwóch uzytkowników */
  public void setMovies(PageMovieData movies) {
    this.movies = movies;
  }

  /** @param selectedCategory wybrana kategoria filmów prze zuzytkownika */
  public void setSelectedCategory(String selectedCategory) {
    this.selectedCategory = selectedCategory;
  }

  /** @param username nazwa użytkownika */
  public void setUsername(String username) {
    this.username = username;
  }

  /** @return zwraca nazwe uzytkownika połączonego z danym klientem */
  public String getConnectedUser() {
    return connectedUser;
  }

  /** @param connectedUser nazwa użytkownika połączonego z danym klientem */
  public void setConnectedUser(String connectedUser) {
    this.connectedUser = connectedUser;
  }

  /**
   * @return typ danej wiadomości na podstawie którego możliwe jest odczytanie okreslonych danych
   */
  public String getAction() {
    return action;
  }

  /** @param action typ wiadmości */
  public void setAction(String action) {
    this.action = action;
  }

  /** @return satus użytkownika połączonego z danym klientem */
  public String getStatus() {
    return status;
  }

  /** @param status satus użytkownika połączonego z danym klientem */
  public void setStatus(String status) {
    this.status = status;
  }
}
