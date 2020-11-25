package com.movie4us;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MovieListActivity extends AppCompatActivity {
    @Override
    protected void onDestroy() {
    System.out.println("Destroy list");
        super.onDestroy();
    }

    @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.movie_list);

    Button button = findViewById(R.id.changeAct);
      System.out.println("on create list");

    button.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent1);
            System.out.println("------------ MovieListActivity");
          }
        });
  }
}


