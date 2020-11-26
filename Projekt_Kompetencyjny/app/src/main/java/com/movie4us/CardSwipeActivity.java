package com.movie4us;

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

import java.io.IOException;
import java.util.ArrayList;

public class CardSwipeActivity extends AppCompatActivity {
    private static final String TAG = "CardSwipeActivity";
    private CardStackLayoutManager manager;
    private CardAdapter adapter;
    Message message ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        message = new Message();

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
                                        TAG, "onCardSwiped: " + adapter.getItems().get(manager.getTopPosition()).id + " driection = " + direction);
                                if (direction == Direction.Right) {
                                    Toast.makeText(CardSwipeActivity.this, "Accept film", Toast.LENGTH_SHORT).show();

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




        Connection connection = Connection.getConnection();
    connection
        .getExecutorService()
        .execute(
            new Runnable() {
              @Override
              public void run() {
                //                                Message message = new Message();
                Gson gson = new Gson();
                String s = null;
                while (true) {

                  try {
                    s = connection.getIn().readLine();
                  } catch (IOException e) {
                    e.printStackTrace();
                  }
                  message = gson.fromJson(s, Message.class);

                  switch (message.getAction()) {
                    case "category":
                      {
                        ArrayList<MovieData> movieDataArray = message.getMovies().movieDataArray;
                        for (MovieData movieData : movieDataArray) {
                          System.out.println("id: "   + movieData.id);
                        }

                        runOnUiThread(
                            () -> {
                              adapter = new CardAdapter(addMovieList(message.getMovies()));
                              cardStackView.setLayoutManager(manager);
                              cardStackView.setAdapter(adapter);
                              cardStackView.setItemAnimator(new DefaultItemAnimator());
                            });

                        break;
                      }
                  }
                }
              }
            });

//        adapter = new CardAdapter(addMovieList(pageMovieData));
//        cardStackView.setLayoutManager(manager);
//        cardStackView.setAdapter(adapter);
//        cardStackView.setItemAnimator(new DefaultItemAnimator());



    }

    private ArrayList<MovieData> addMovieList(PageMovieData pageMovieData) {
        return pageMovieData.movieDataArray;
    }
}
