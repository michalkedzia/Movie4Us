package com.movie4us;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import com.google.gson.Gson;
import com.yuyakaido.android.cardstackview.*;
import data.MovieData;
import data.PageMovieData;
import model.Message;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Klasa CardSwipeActivity obsługuje aktywność wyboru filmów, który polega na przesuwaniu kart z ich
 * widokiem. Umożliwia komunikację z serwerem, oraz reakcje na wstrzymanie/zakończenie/uśpienie aplikacji przez użytkownika.
 *
 */
public class CardSwipeActivity extends AppCompatActivity {
  private static final String TAG = "CardSwipeActivity";
  /**
   * Menadżer obsługi przewijania kart i sposobie zaprojektowania widoku pod względem graficznym
   * (wbudowana bilbioteka CardStackLayoutManager).
   */
  private CardStackLayoutManager manager;
  /** Pole klasy CardAdaptera, z którego zostaną zaimplementowane */
  private CardAdapter adapter;
  /** Wiadomość otrzymywana od serwera */
  Message message;
  /** Obiekt Gson do wysyłania informacji do serwera. */
  Gson gson = new Gson();

  private boolean litener = true;
  /** Pole klasy connection z parametrami połączenia z serwerem. */
  Connection connection;
  /** Zmienna obsługująca wątki przy komunikacji z serwerem. */
  Runnable litenerThread;

  boolean cancelMatch = true;

  /**
   * Metoda onCreate tworzy nową scenę interfejsu graficznego użytkownika. Ustawiany jest widok tła,
   * na którym załadowany zostaje kolejny widok mogący przechowywać stos kilku takich samych
   * obiektów kart (w tym przypadku CardStackView).
   *
   * <p>Metoda poprzez wbudowane metody CardStacklayoutManager'a nasłuchuje czy na stworzonym stosie
   * załadownaych kart następuje jakaś akcja. Sprawdza czy karta została przewinięta do końca, a co
   * za tym idzie usunięta z góry stosu kart z filmami. Jeśli nastąpiła taka akcja, sprawdzany jest
   * kierunek przesunięcia karty. Domyślnie przesunięta karta w prawą stronę ma oznaczać akceptację
   * danego widoku (karty wyświetlającej szczegółowe informacje o filmie), natomiast przesunieta
   * karta w lewą stronę ma oznaczać odrzucenie takiej karty. W przypadku akceptacji wysyłana jest
   * wiadomość do serwera o wybranym filmie wraz z wyciągnięta informacją o id filmu, oraz krótki
   * komunikat u użytkownika o zaakceptowaniu danego filmu. W przypadku odrzucenia wyświetlany jest
   * tylko komunikat o odrzuceniu filmu.
   *
   * <p>Metoda obsługuje także nasłuchiwanie przychodzących wiadomości od serwera. Na tym etapie
   * aktywności aplikacji odbiera wiadomość z określoną akcją do możliwej obsługi. Może to być
   * wybranie kategorii, na której podstawie ładowane są na przygotowany wcześniej CardStackView
   * adapter z wieloma filmami, infromacja o wybraniu filmów, po której sprawdzane są wartości
   * filmów czy nie zostały wybrane już dwa takie same filmy, bądź informacja o tym, że drugi
   * użytkownik wstrzymał się wyborem filmów.
   *
   * @param savedInstanceState Służy do przekazania informacji o stanie aktywności aplikacji
   *     Android'a. W przypadku zmiany orientacji, zamknięcia aplikacji itp., które prowadzą do
   *     ponownego wywołania metody onCreate, pakiet ten może zostać użyty do załadowania informacji
   *     o poprzednim stanie aplikacji.
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_menu);
    message = new Message();
    connection = Connection.getConnection();
    litener = true;
    CardStackView cardStackView = findViewById(R.id.card_stack_view);
    manager =
        new CardStackLayoutManager(
            this,
            new CardStackListener() {
              @Override
              public void onCardDragging(Direction direction, float ratio) {
                // Log.d(TAG, "onCardDragging: " + direction.name() + "ratio: " + ratio);
              }

              @Override
              public void onCardSwiped(Direction direction) {
                Log.d(
                    TAG,
                    "onCardSwiped: "
                        + adapter.getItems().get(manager.getTopPosition() - 1).id
                        + " driection = "
                        + direction);
                if (direction == Direction.Right) {
                  Toast.makeText(CardSwipeActivity.this, "Accept film", Toast.LENGTH_SHORT).show();
                  message.setAction("selectedMovie");
                  message.setUsername(connection.getUsername());
                  message.setMovieId(adapter.getItems().get(manager.getTopPosition() - 1).id);
                  connection
                      .getExecutorService()
                      .execute(() -> connection.send(gson.toJson(message)));
                }
                if (direction == Direction.Left) {
                  Toast.makeText(CardSwipeActivity.this, "Reject film", Toast.LENGTH_SHORT).show();
                }
              }

              @Override
              public void onCardRewound() {
                Log.d(TAG, "onCardRewound: " + manager.getTopPosition());
              }

              @Override
              public void onCardCanceled() {
                Log.d(TAG, "onCardRewound: " + manager.getTopPosition());
              }

              @Override
              public void onCardAppeared(View view, int position) {}

              @Override
              public void onCardDisappeared(View view, int position) {}
            });

    manager.setStackFrom(StackFrom.None);
    manager.setVisibleCount(3);
    manager.setTranslationInterval(8.0f);
    manager.setScaleInterval(0.95f);
    manager.setSwipeThreshold(0.3f);
    manager.setMaxDegree(20.0f);
    manager.setDirections(Direction.FREEDOM);
    manager.setCanScrollHorizontal(true);
    manager.setCanScrollVertical(false);
    manager.setSwipeableMethod(SwipeableMethod.Manual);
    manager.setOverlayInterpolator(new LinearInterpolator());

    litenerThread =
        () -> {
          Gson gson = new Gson();
          String s = null;
          while (litener) {

            try {
              s = connection.getIn().readLine();
            } catch (IOException e) {
              e.printStackTrace();
            }
            message = gson.fromJson(s, Message.class);
            switch (message.getAction()) {
              case "category":
                {
                  runOnUiThread(
                      () -> {
                        adapter = new CardAdapter(addMovieList(message.getMovies()));
                        cardStackView.setLayoutManager(manager);
                        cardStackView.setAdapter(adapter);
                        cardStackView.setItemAnimator(new DefaultItemAnimator());
                      });
                  break;
                }
              case "match":
                {
                  cancelMatch = false;
                  Intent matach = new Intent(getApplicationContext(), Match.class);
                  matach.putExtra(
                      "movie", getSelectedMovie(adapter.getItems(), message.getMovieId()));
                  startActivity(matach);

                  break;
                }
              case "matchStop":
                {
                  litener = false;
                  if (!message.getUsername().equals(connection.getUsername())) {
                    finish();
                  }
                  break;
                }
            }
          }
        };

    connection.getExecutorService().execute(litenerThread);
  }

  /**
   * Metoda addMovieList wyciąga z klasy PageMovieData jedynie listę już szczegółowych informacji o
   * poszczególnych filmach.
   *
   * @param pageMovieData Klasa przechowywująca informację m.in. o ilości pobranych przez API
   *     filmów.
   * @return Zwraca listę pobranych filmów z bazy danych.
   */
  private ArrayList<MovieData> addMovieList(PageMovieData pageMovieData) {
    return pageMovieData.movieDataArray;
  }

  @Override
  protected void onStop() {
    super.onStop();
  }

  /** Metoda obsługuje zniszczenie aktualnego activity. */
  @Override
  protected void onDestroy() {
    super.onDestroy();
  }

  /**
   * Metoda reaguje na zatrzymanie/wstrzymanie obsługi aplikacji, kiedy użytkownik opuszcza tą
   * aktywność. Nie musi być ona od razu niszczona, gdyż użytkownik może korzystać z trybu wielu
   * okien. Metoda pozwala na wstrzymanie operacji, poprzez wysłanie takiej informacji do serwera,
   * że użytkownik wstrzymał obsłguę okna aplikacji
   */
  @Override
  protected void onPause() {
    super.onPause();
    if (cancelMatch) {
      connection
          .getExecutorService()
          .execute(
              () -> {
                message.setUsername(connection.getUsername());
                message.setAction("matchStop");
                connection.send(gson.toJson(message));
              });
    }
  }

  /** */
  @Override
  protected void onRestart() {
    super.onRestart();
    cancelMatch = true;
    litener = true;
    connection.getExecutorService().execute(litenerThread);
  }

  /**
   * Metoda getSelectedMovie sprawdza wybór filmu na podstawie informacji otrzymanej od serwera.
   * Jeśli wiadomość od serwera zawiera informacje o wyborze danego filmu przez drugiego połączonego
   * użytkownika w postaci id wybranego filmu metoda na podstawie stosu kart przechowywanych w
   * adapterze sprawdza czy film o takim samymy id nie został zaakceptowany.
   *
   * @param items Lista przechowywująca obiekty MovieData z informacjami o filmach.
   * @param id Parametr id danego filmu.
   * @return W postaci obiektu Gson zwracany jest poszczególny film.
   */
  private String getSelectedMovie(ArrayList<MovieData> items, int id) {
    Iterator<MovieData> iterator = adapter.getItems().iterator();
    MovieData movieData = null;
    while (iterator.hasNext()) {
      movieData = iterator.next();
      if (movieData.id == message.getMovieId()) {
        break;
      }
    }
    return gson.toJson(movieData);
  }
}
