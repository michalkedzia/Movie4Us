package api.data;

import java.util.ArrayList;

/**
 * Klasa przechowująca dane o stronie z filmami (20 filmów).
 *
 * <p>Dane pobierane są z zewnętrznej bazy danych TMDB za pomocą API.
 */
public class PageMovieData {

  /** Numer strony */
  public int page;
  /** Liczba wszystkich filmów */
  public int total_results;
  /** Liczba wszystkich stron */
  public int total_pages;
  /** Lista przechowująca obiekty MovieData */
  public ArrayList<MovieData> movieDataArray;
  /** liczba filmów na danej stronie */
  public int results_on_page;

  @Override
  public String toString() {
    return "PageMovieData{"
        + "\npage="
        + page
        + ", \ntotal_results="
        + total_results
        + ", \ntotal_pages="
        + total_pages
        + ", \nresults_on_page="
        + results_on_page
        + '}';
  }
}
