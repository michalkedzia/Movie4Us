package com.movie4us;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

public class GenreSelectionActivity extends AppCompatActivity {
  private Toolbar toolbar;
  private Spinner spinnerCategories;
  private Button buttonCategories;
  private Message message;
  private Gson gson;
  boolean listener;
  private Connection connection;
  private Runnable listenerThread;

  //TODO:testy
  private ArrayList<CategoryItem> mCategoryList;
  private CategoryAdapter mAdapter;
  //

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    //TODO:testy
    initList();
    //
    setContentView(R.layout.genre_selection);
    spinnerCategories = findViewById(R.id.spinnerCategories);
    buttonCategories = findViewById(R.id.buttonCategories);

    //TODO:testy
    mAdapter=new CategoryAdapter(this,mCategoryList);
    spinnerCategories.setAdapter(mAdapter);
    final String[] clickedCategoryName = new String[1];
    spinnerCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        CategoryItem clickedItem=(CategoryItem)parent.getItemAtPosition(position);
        clickedCategoryName[0] =clickedItem.getCategoryName();
        Toast.makeText(GenreSelectionActivity.this, clickedCategoryName[0] + " selected",Toast.LENGTH_SHORT).show();
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {

      }
    });
    ///
    gson = new Gson();
    message = new Message();

    toolbar = findViewById(R.id.myToolBar);
    setSupportActionBar(toolbar);
    connection = Connection.getConnection();
    listener = true;

    TextView logout = findViewById(R.id.logout);
    logout.setOnClickListener(
        v -> {
          connection
              .getExecutorService()
              .execute(
                  () -> {
                    message.setUsername(connection.getUsername());
                    message.setAction("logout");
                    connection.send(gson.toJson(message));
                  });
          logout();
        });

    listenerThread =
        () -> {
          System.out.println("listenerThread - genreActivity start !!!!!");
          while (listener) {
            try {
              String s = connection.getIn().readLine();
              message = gson.fromJson(s, Message.class);

              switch (message.getAction()) {
                case "selectedGenres":
                  {
                    listener = false;
                    System.out.println("genres selected");
                    Intent intent = new Intent(getApplicationContext(), CardSwipeActivity.class);
                    startActivity(intent);

                    break;
                  }
                case "logout":
                  {
                    listener = false;
                    System.out.println("logout server");
                    break;
                  }
              }

              System.out.println(s);
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
        };

    connection.getExecutorService().execute(listenerThread);

    buttonCategories.setOnClickListener(
        v ->
            connection
                .getExecutorService()
                .execute(
                    () -> {
                      message.setAction("category");
                      message.setUsername(connection.getUsername());
//                      message.setSelectedCategory(spinnerCategories.getSelectedItem().toString());
                      message.setSelectedCategory(clickedCategoryName[0]);
                      connection.send(gson.toJson(message));
                    }));
  }

  //TODO:testy
  private void initList()
  {
    mCategoryList=new ArrayList<>();
    mCategoryList.add(new CategoryItem("War",R.drawable.two_across_swords));
    mCategoryList.add(new CategoryItem("Music",R.drawable.headphone));
    mCategoryList.add(new CategoryItem("Comedy",R.drawable.comedy));
    mCategoryList.add(new CategoryItem("Documentary",R.drawable.documentary));
    mCategoryList.add(new CategoryItem("History",R.drawable.history));
    mCategoryList.add(new CategoryItem("Western",R.drawable.western));
    mCategoryList.add(new CategoryItem("Adventure",R.drawable.adventure));
    mCategoryList.add(new CategoryItem("Fantasy",R.drawable.fantasy));
    mCategoryList.add(new CategoryItem("Science Fiction",R.drawable.science_fiction));
    mCategoryList.add(new CategoryItem("Animation",R.drawable.animation));
    mCategoryList.add(new CategoryItem("Crime",R.drawable.crime));
    mCategoryList.add(new CategoryItem("Mystery",R.drawable.mystery));
    mCategoryList.add(new CategoryItem("Drama",R.drawable.drama));
    mCategoryList.add(new CategoryItem("TV Movie",R.drawable.tv_movie));
    mCategoryList.add(new CategoryItem("Thriller",R.drawable.thriller));
    mCategoryList.add(new CategoryItem("Horror",R.drawable.horror));
    mCategoryList.add(new CategoryItem("Action",R.drawable.action));
    mCategoryList.add(new CategoryItem("Romance",R.drawable.romance));
    mCategoryList.add(new CategoryItem("Family",R.drawable.family));
  }
  ///

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {

    switch (item.getItemId()) {
      case R.id.logout:
        Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
        break;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.menu, menu);
    return true;
  }

  private void logout() {
    connection.getExecutorService().shutdown();
    connection.getExecutorService().shutdownNow();

    try {
      connection.getOut().close();
      connection.getIn().close();
      connection.getSocket().close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    connection.getExecutorService().shutdownNow();
    System.out.println("logout koniec");
    Intent intent = new Intent(getApplicationContext(), Login.class);
    startActivity(intent);
    finish();
  }
}
