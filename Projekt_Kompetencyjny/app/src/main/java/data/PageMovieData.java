package data;

import java.util.ArrayList;

public class PageMovieData {
  public int page;
  public int total_results;
  public int total_pages;
  public ArrayList<MovieData> movieDataArray;
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

  public ArrayList<MovieData> getMovieDataArray() {
    return movieDataArray;
  }
}
