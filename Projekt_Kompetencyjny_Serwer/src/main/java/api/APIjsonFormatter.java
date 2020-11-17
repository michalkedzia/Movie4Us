package api;

import api.data.MovieData;
import api.data.PageMovieData;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import exceptions.APIException;

import java.util.ArrayList;

public class APIjsonFormatter {
  PageMovieData pageMovieData;

 public PageMovieData parseToObject(JsonObject rootObj) throws APIException.WrongJsonObjectException {
    if (!rootObj.has("results")) {
      throw new APIException.WrongJsonObjectException("Wrong json object passed");
    }

    ArrayList<MovieData> movieDataArray = new ArrayList<>();

    Gson gson = new Gson();
    pageMovieData = gson.fromJson(rootObj.toString(), PageMovieData.class);

    for (int i = 0; i < rootObj.getAsJsonArray("results").size(); i++) {
      movieDataArray.add(
          gson.fromJson(rootObj.getAsJsonArray("results").get(i).toString(), MovieData.class));
    }
    pageMovieData.movieDataArray = movieDataArray;
    pageMovieData.results_on_page = movieDataArray.size();

    return pageMovieData;
  }
}
