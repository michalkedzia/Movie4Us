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

public class CardSwipeActivity extends AppCompatActivity {
  private static final String TAG = "CardSwipeActivity";
  private CardStackLayoutManager manager;
  private CardAdapter adapter;
  Message message;
  Gson gson = new Gson();
  private boolean litener = true;
  Connection connection;
  Runnable litenerThread;
  boolean cancelMatch = true;

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

  private ArrayList<MovieData> addMovieList(PageMovieData pageMovieData) {
    return pageMovieData.movieDataArray;
  }

  @Override
  protected void onStop() {
    super.onStop();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
  }

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

  @Override
  protected void onRestart() {
    super.onRestart();
    cancelMatch = true;
    litener = true;
    connection.getExecutorService().execute(litenerThread);
  }

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
