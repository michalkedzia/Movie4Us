package com.movie4us;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import data.MovieData;

public class Match extends AppCompatActivity {

  private ImageView imageView, imageProvider;
  private TextView title, linkStatus, links;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_match);
    Intent intent = getIntent();
    Gson gson = new Gson();

    MovieData movieData = gson.fromJson(intent.getStringExtra("movie"), MovieData.class);
    System.out.println("id: " + movieData.id + ", tytul: " +  movieData.title + ", " + movieData.watchProviderData.getPLProvider());


    imageView = findViewById(R.id.filmImage);
    title = findViewById(R.id.Title);
    linkStatus = findViewById(R.id.LinkStatus);
    LinearLayout layout = findViewById(R.id.imageContainer);
    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

    loadImageFromUrl(movieData.getPoster_path(), imageView);
    title.setText("'" + movieData.title + "'");

    int numberOfProviders = movieData.watchProviderData.getPLProvider().size();
    System.out.println("numbers of providers: " + numberOfProviders);

    if(numberOfProviders == 0) {
      linkStatus.setText("Brak w PL");
    } else {
      for(int i=0; i<numberOfProviders; i++){
        layoutParams.setMargins(20, 20, 20, 20);
        layoutParams.height = 150;
        layoutParams.width = 150;
        layoutParams.gravity = Gravity.CENTER;
        ImageView providerImage = new ImageView(this);

        loadImageFromUrl(movieData.watchProviderData.getPLProvider().get(i), providerImage);
        providerImage.setLayoutParams(layoutParams);

        layout.addView(providerImage);
      }
    }
  }

  private void loadImageFromUrl(String url, ImageView image) {
    Picasso.get().load(url).into(image);
  }


}