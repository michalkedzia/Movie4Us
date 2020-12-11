package api.data;

public class WatchProviderData {
  public int provider_id;
  public String provider_name;
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
