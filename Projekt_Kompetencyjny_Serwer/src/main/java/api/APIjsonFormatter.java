package api;

import api.data.MovieData;
import api.data.MovieWatchProviderData;
import api.data.PageMovieData;
import api.data.WatchProviderData;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import exceptions.APIException;

import java.util.ArrayList;

/** Klasa parsująca pliki JSON i tworząca odpowiednie obiekty */
public class APIjsonFormatter {

  /**
   * Metoda parsująca plik JSON z danymi o filmach do obiektu PageMovieData
   *
   * @param rootObj obiekt źródłowy JSON
   * @return obiekt PageMovieData ze stroną z informacjami o filmach
   * @throws APIException.WrongJsonObjectException
   */
  public PageMovieData parseToObjectPageMovieData(JsonObject rootObj)
      throws APIException.WrongJsonObjectException {
    if (!rootObj.has("results")) {
      throw new APIException.WrongJsonObjectException("Wrong json object passed");
    }

    ArrayList<MovieData> movieDataArray = new ArrayList<>();

    Gson gson = new Gson();
    PageMovieData pageMovieData = gson.fromJson(rootObj.toString(), PageMovieData.class);

    for (int i = 0; i < rootObj.getAsJsonArray("results").size(); i++) {
      movieDataArray.add(
          gson.fromJson(rootObj.getAsJsonArray("results").get(i).toString(), MovieData.class));
    }
    pageMovieData.movieDataArray = movieDataArray;
    pageMovieData.results_on_page = movieDataArray.size();

    return pageMovieData;
  }

  /**
   * Metoda parsująca plik JSON z serwisami streamingowymi do obiektu MovieWatchProviderData
   *
   * @param rootObj obiekt źródłowy JSON
   * @return obiekt MovieWatchProviderData z informacjami o serwisach streamingowych
   * @throws APIException.WrongJsonObjectException
   */
  public MovieWatchProviderData parseToObjectMovieWatchProviderData(JsonObject rootObj)
      throws APIException.WrongJsonObjectException {
    if (!rootObj.has("results")) {
      throw new APIException.WrongJsonObjectException("Wrong json object passed");
    }

    Gson gson = new Gson();
    MovieWatchProviderData movieWatchProviderData =
        gson.fromJson(rootObj.toString(), MovieWatchProviderData.class);

    ArrayList<WatchProviderData> PL = new ArrayList<>();
    ArrayList<WatchProviderData> US = new ArrayList<>();

    if (rootObj.getAsJsonObject("results").has("PL")) {
      JsonObject PLObject = rootObj.getAsJsonObject("results").getAsJsonObject("PL");
      if (PLObject.has("flatrate")) {
        for (int i = 0; i < PLObject.getAsJsonArray("flatrate").size(); i++) {
          PL.add(
              gson.fromJson(
                  PLObject.getAsJsonArray("flatrate").get(i).toString(), WatchProviderData.class));
        }
      }
      movieWatchProviderData.PL = PL;
    }

    if (rootObj.getAsJsonObject("results").has("US")) {
      JsonObject USObject = rootObj.getAsJsonObject("results").getAsJsonObject("US");
      if (USObject.has("flatrate")) {
        for (int i = 0; i < USObject.getAsJsonArray("flatrate").size(); i++) {
          US.add(
              gson.fromJson(
                  USObject.getAsJsonArray("flatrate").get(i).toString(), WatchProviderData.class));
        }
      }
      movieWatchProviderData.US = US;
    }

    return movieWatchProviderData;
  }
}
