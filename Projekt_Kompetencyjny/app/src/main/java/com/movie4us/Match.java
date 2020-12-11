package com.movie4us;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.squareup.picasso.Picasso;

public class Match extends AppCompatActivity {

    ImageView item_image2;
    TextView item_title, description, film_rating;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);
        Intent i=getIntent();

        String url=i.getStringExtra("value");
        String title=i.getStringExtra("value2");
        String overview=i.getStringExtra("value3");
        String vote_average=i.getStringExtra("value4");

        item_image2 = findViewById(R.id.item_image);
        item_title = findViewById(R.id.item_title);
        description = findViewById(R.id.item_description);
        film_rating = findViewById(R.id.item_film_rating);

        loadImageFromUrl(url);
        item_title.setText(title);
        description.setText(overview);
        film_rating.setText(vote_average);
    }

    private void loadImageFromUrl(String url)
    {
        Picasso.get().load(url).into(item_image2);
    }
}