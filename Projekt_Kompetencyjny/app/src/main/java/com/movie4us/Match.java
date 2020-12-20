package com.movie4us;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import data.MovieData;

public class Match extends AppCompatActivity {

  private ImageView imageView;
  private TextView itemTitle, description, filmRating;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_match);
    Intent intent = getIntent();
    Gson gson = new Gson();

    MovieData movieData = gson.fromJson(intent.getStringExtra("movie"), MovieData.class);

    imageView = findViewById(R.id.item_image);
    itemTitle = findViewById(R.id.item_title);
    description = findViewById(R.id.item_description);
    filmRating = findViewById(R.id.item_film_rating);

    loadImageFromUrl(movieData.getPoster_path());
    itemTitle.setText(movieData.title);
    description.setText(movieData.overview);
    filmRating.setText(String.valueOf(movieData.vote_average));
  }

  private void loadImageFromUrl(String url) {
    Picasso.get().load(url).into(imageView);
  }
}
