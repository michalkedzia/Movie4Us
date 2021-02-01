package com.movie4us;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import data.MovieData;

import java.util.ArrayList;

/**
 * Klasa CardAdapter odpowiada za stworzenie kart do wyświetlania informacji o filmie (w postaci
 * interfejsu graficznego dla użytkownika - plików xml'a), które będzie można wyświetlić w postaci
 * stosu.
 */
public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {
  /** Lista filmów */
  private ArrayList<MovieData> items;

  /**
   * Konstruktor CardAdapter'a
   *
   * @param items lista filmów
   */
  public CardAdapter(ArrayList<MovieData> items) {
    this.items = items;
  }

  public ArrayList<MovieData> getItems() {
    return items;
  }

  public void setItems(ArrayList<MovieData> items) {
    this.items = items;
  }

  /**
   * Metoda ViewHolder tworzy/przygotowywuje nowy widok w postaci stosu kart, w zależności od ilości
   * filmów w liście pobranej z bazy danych.
   *
   * @param parent
   * @param viewType
   * @return Zwraca widok do wyświetlenia przez activity dla użytkownika o filmach na załadowanych
   *     karatch całego adapetra.
   */
  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());
    View view = inflater.inflate(R.layout.item_card, parent, false);
    return new ViewHolder(view);
  }

  // TODO: Testy teściki
  String returnPathToImage(int position) {
    return items.get(position).getPoster_path();
  }

  String returnTitle(int position) {
    return items.get(position).title;
  }

  String returnDescription(int position) {
    return items.get(position).overview;
  }

  String returnFilmRating(int position) {
    String Rating = Float.toString(items.get(position).vote_average);
    return Rating;
  }

  /**
   * Metoda onBindViewHolder jest wywoływana przez RecyclerView w celu wyświetlenia danych na
   * określonej pozycji.
   *
   * @param holder Uchwyt dla klasy ViewHolder dla której będzie ustawiany widok pojedyńczej karty
   *     filmu.
   * @param position Pozycja w liście elementów - w tym przypadku w liście filmów do ustawienia na
   *     kartach adaptera.
   */
  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    holder.setData(items.get(position));
  }

  @Override
  public int getItemCount() {
    return items.size();
  }

  /**
   * Klasa ViewHolder posiada pola odpowiadające obiektom zaprojektowanym dla interfejsu graficznego
   * w postaci xml'a.
   *
   * <p>Konstruktor ViewHoldera przypisuje pod pole obrazu, oraz pola tekstowe odpowiednio: tytył
   * filmu, krótki opis i ogólną ocenę, odpowiadające im widoki na scenie xml'a. Aby każda
   * informacja była wyświetlona w odpowiednim miejscu.
   *
   * <p>Metoda setData jest wykorzystywana do ustawienia widoku karty, tak aby wyświetlić porządany
   * obraz/plakat filmu, jego tytuł, opis i ocenę.
   */
  class ViewHolder extends RecyclerView.ViewHolder {
    ImageView image;
    TextView title, description, film_rating;

    ViewHolder(@NonNull View itemView) {
      super(itemView);
      image = itemView.findViewById(R.id.item_image);
      title = itemView.findViewById(R.id.item_title);
      description = itemView.findViewById(R.id.item_description);
      film_rating = itemView.findViewById(R.id.item_film_rating);
    }

    void setData(MovieData movieCardModel) {
      Picasso.get().load(movieCardModel.getPoster_path()).into(image);
      title.setText(movieCardModel.title);
      description.setText(movieCardModel.overview);
      film_rating.setText(Float.toString(movieCardModel.vote_average));
    }
  }
}
