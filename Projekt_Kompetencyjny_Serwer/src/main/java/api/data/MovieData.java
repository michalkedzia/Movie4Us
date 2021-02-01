package api.data;
/**
 * Klasa przechowująca dane o filmie.
 *
 * <p>Dane pobierane są z zewnętrznej bazy danych TMDB za pomocą API.
 */
public class MovieData {

  /** popularity parametr przechowujący popularność filmu */
  public double popularity;
  /** vote_count parametr przechowujący ilość ocen użytkowników */
  public int vote_count;
  /** parametr przechowujący link do plakatu */
  public String poster_path;
  /** parametr przechowujący unikalny identyfikator filmu */
  public int id;
  /** parametr przechowujący tytuł filmu */
  public String title;
  /** parametr przechowujący średnią ocenę filmu */
  public float vote_average;
  /** parametr przechowujący opis filmu */
  public String overview;
  /** parametr przechowujący datę premiery filmu */
  public String release_date;
  /** obiekt przechowujący dane o serwisach streamingowych które mają w swojej ofercie dany film */
  public MovieWatchProviderData watchProviderData;

  @Override
  public String toString() {
    return "MovieData{"
        + "\npopularity="
        + popularity
        + ", \nvote_count="
        + vote_count
        + ", \nid="
        + id
        + ", \ntitle="
        + title
        + ", \nvote_average="
        + vote_average
        + ", \noverview="
        + overview
        + ", \nrelease_date="
        + release_date
        + '}';
  }

  public String getPoster_path() {
    return "https://image.tmdb.org/t/p/original" + poster_path;
  }
}
