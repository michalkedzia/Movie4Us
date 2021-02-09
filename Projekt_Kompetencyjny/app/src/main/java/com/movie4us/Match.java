package com.movie4us;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import data.MovieData;

/**
 * Klasa Match obsługuje nowe okno aplikacji. Wywoływana z CardSwipeActivity (skąd przekazywane są
 * odpowiednie dane, tj. informacje o wybranym filmie) tworzy okno/widok aplikacji wybranych filmów
 * i uruchamia je.
 */
public class Match extends AppCompatActivity {
  /** Obiekt przechowywujacy informacje o obrazie wyświetlanym w widoku XML'a aplikacji. */
  private ImageView imageView;

  /** Pola tekstowe ustawiane w widoku XML'a aplikacji. */
  private TextView title, linkStatus;

  /**
   * Metoda ustawia nową scenę aplikacji pokazującą informację o wybranym filmie. Zostaje ustawiony
   * nowy widok, wyświetlający wybrany film oraz dodatkowe informacje o dostępności filmu (w
   * zależności od ilości providerów) z klasy MovideData przekazywanej z odpowiednimi parametrami z
   * poprzedniego widoku aplikacji.
   *
   * @param savedInstanceState Służy do przekazania informacji o stanie aktywności aplikacji
   *     Android'a. W przypadku zmiany orientacji, zamknięcia aplikacji itp., które prowadzą do
   *     ponownego wywołania metody onCreate, pakiet ten może zostać użyty do załadowania informacji
   *     o poprzednim stanie aplikacji.
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_match);
    Intent intent = getIntent();
    Gson gson = new Gson();

    MovieData movieData = gson.fromJson(intent.getStringExtra("movie"), MovieData.class);
    System.out.println(
        "id: "
            + movieData.id
            + ", tytul: "
            + movieData.title
            + ", "
            + movieData.watchProviderData.getPLProvider());

    imageView = findViewById(R.id.filmImage);
    title = findViewById(R.id.Title);
    linkStatus = findViewById(R.id.LinkStatus);

    LinearLayout layout = findViewById(R.id.imageContainer);
    LayoutInflater inflater = LayoutInflater.from(this);

    loadImageFromUrl(movieData.getPoster_path(), imageView);
    title.setText("'" + movieData.title + "'");

    int numberOfProviders = movieData.watchProviderData.getPLProvider().size();
    System.out.println("numbers of providers: " + numberOfProviders);

    if (numberOfProviders == 0) {
      linkStatus.setText("No availability");
    } else {
      for (int i = 0; i < numberOfProviders; i++) {
        View view = inflater.inflate(R.layout.provider_item, layout, false);

        TextView text = view.findViewById(R.id.providerName);
        text.setText(movieData.watchProviderData.getPLProvider().get(i));
        ImageView image = view.findViewById(R.id.providerImage);
        loadImageFromUrl(movieData.watchProviderData.getPLProviderLogo().get(i), image);

        layout.addView(view);
      }
    }
  }

  /**
   * Metoda ładuje z podanej ścieżki url (wyciąganej z bazy filmów) obraz przedstawiający dany film,
   * bądź logo providera do obiektu ImageView za pomocą biblioteki do pobierania i buforowania
   * obrazów dla Android'a 'Picasso'.
   *
   * @param url Sciezka url skąd pobierany jest obraz.
   * @param image Widok do którego ma być załadowany obraz.
   * @see Picasso
   */
  private void loadImageFromUrl(String url, ImageView image) {
    Picasso.get().load(url).into(image);
  }
}
