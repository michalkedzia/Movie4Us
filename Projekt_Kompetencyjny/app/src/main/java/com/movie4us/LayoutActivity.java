package com.movie4us;

import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import com.yuyakaido.android.cardstackview.*;
import data.MovieData;
import data.PageMovieData;
import swipemenu.ItemModel;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class LayoutActivity extends AppCompatActivity {
    private static final String TAG = "LayoutActivity";
    private CardStackLayoutManager manager;
    private CardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        CardStackView cardStackView = findViewById(R.id.card_stack_view);
        manager = new CardStackLayoutManager(this, new CardStackListener() {
            @Override
            public void onCardDragging(Direction direction, float ratio) {
                //Log.d(TAG, "onCardDragging: " + direction.name() + "ratio: " + ratio);
            }

            @Override
            public void onCardSwiped(Direction direction) {
                Log.d(TAG, "onCardSwiped: " + manager.getTopPosition() + " driection = " + direction);
                if(direction == Direction.Right){
                    Toast.makeText(LayoutActivity.this, "Accept film", Toast.LENGTH_SHORT).show();
                }
                if(direction == Direction.Left){
                    Toast.makeText(LayoutActivity.this, "Reject film", Toast.LENGTH_SHORT).show();
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
            public void onCardAppeared(View view, int position) {
                Log.d(TAG, "view: " + view.getContext() + " position: " + position);
                List<ItemModel> list = adapter.getItems();
                ItemModel model = list.get(position);
                String title = model.getTitle();
                Log.d(TAG, "title: " + title);
            }

            @Override
            public void onCardDisappeared(View view, int position) {
            }
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
        adapter = new CardAdapter(addList());
        cardStackView.setLayoutManager(manager);
        cardStackView.setAdapter(adapter);
        cardStackView.setItemAnimator(new DefaultItemAnimator());
    }

    private List<ItemModel> addList() {
        List<ItemModel> items = new ArrayList<>();
        /*
        PageMovieData data = new PageMovieData();

        List<MovieData> movielist = data.getMovieDataArray();
        for(MovieData movie : movielist){
            items.add(new ItemModel(movie.getPoster_path(), movie.title, movie.overview, movie.release_date));
        }
*/
        items.add(new ItemModel("https://image.tmdb.org/t/p/original/vzvKcPQ4o7TjWeGIn0aGC9FeVNu.jpg", "Title 1", "Description 1: blah blah blah sdjfowesacuqwe qweqwec", "Film rating 2 stars"));
        items.add(new ItemModel("https://image.tmdb.org/t/p/w600_and_h900_bestv2/vIZl6POj8CrKOIdfZJEFBoBfC3F.jpg", "Title 2", "Description 2: blah blah blah sdjfowesacuqwe qweqwec", "Film rating 4 stars"));
        items.add(new ItemModel("https://image.tmdb.org/t/p/w600_and_h900_bestv2/opwCl56Zi8mextLETtM3d0ryVFU.jpg", "Title 3", "Description 3: blah blah blah sdjfowesacuqwe qweqwec", "Film rating 5 stars"));

        return items;
    }
}