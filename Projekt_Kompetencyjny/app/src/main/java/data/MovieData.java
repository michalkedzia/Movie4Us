package data;

public class MovieData {
  public double popularity;
  public int vote_count;
  public String poster_path;
  public int id;
  public String title;
  public float vote_average;
  public String overview;
  public String release_date;

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
