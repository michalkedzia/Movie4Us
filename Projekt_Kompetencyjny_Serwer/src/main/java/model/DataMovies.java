package model;

import api.APICaller;
import api.APIUrlBuilder;
import api.APIjsonFormatter;
import api.data.PageMovieData;
import api.utils.FilterType;
import com.google.gson.JsonObject;
import exceptions.APIException;

import java.util.List;

import static api.utils.CallType.DISCOVER;

public class DataMovies {

  private String currentPage = "1";
  private List<String> genres;

  public DataMovies(List<String> genres) {
    this.genres = genres;
  }

  public String getCurrentPage() {
    return currentPage;
  }

  public void setCurrentPage(String currentPage) {
    this.currentPage = currentPage;
  }

  public PageMovieData getMovies() {
    APIUrlBuilder apiUrlBuilder = new APIUrlBuilder();
    String stringUrl = apiUrlBuilder.createBasicUrl(DISCOVER);
    try {
      apiUrlBuilder.addDiscoverFilter(FilterType.LANGUAGE, "english");
      apiUrlBuilder.addDiscoverFilter(FilterType.SORT, "popularity.desc");
      apiUrlBuilder.addDiscoverFilter(FilterType.PAGE, currentPage);
      for (String genre : genres) {
        stringUrl = apiUrlBuilder.addDiscoverFilter(FilterType.GENRE, genre);
      }

    } catch (APIException.WrongCallTypeException | APIException.InvalidFilterValueException e) {
      e.printStackTrace();
    }

    APICaller apiCaller = null;
    try {
      apiCaller = new APICaller(stringUrl, DISCOVER);
    } catch (APIException.WrongCallTypeException e) {
      e.printStackTrace();
    }

    assert apiCaller != null;
    JsonObject obj = apiCaller.sendAPIrequest();

    APIjsonFormatter formatter = new APIjsonFormatter();
    PageMovieData page = null;
    try {
      page = formatter.parseToObject(obj);
    } catch (APIException.WrongJsonObjectException e) {
      e.printStackTrace();
    }
    return page;
  }
}
