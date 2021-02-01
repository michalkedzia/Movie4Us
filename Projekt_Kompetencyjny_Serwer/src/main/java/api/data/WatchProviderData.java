package api.data;

/**
 * Klasa przechowująca dane o serwisie streamingowym
 *
 * <p>Dane pobierane są z zewnętrznej bazy danych TMDB za pomocą API.
 */
public class WatchProviderData {

  /** Identyfikator serwisu streamingowego */
  public int provider_id;
  /** Nazwa serwisu streamingowego */
  public String provider_name;
  /** Ścieżka do logo serwisu streamingowego */
  private String logo_path;

  public String getLogo_path() {
    return "https://image.tmdb.org/t/p/original" + logo_path;
  }

  @Override
  public String toString() {
    return "WatchProviderData{"
        + "\nprovider_id="
        + provider_id
        + ", \nprovider_name="
        + provider_name
        + '}';
  }
}
