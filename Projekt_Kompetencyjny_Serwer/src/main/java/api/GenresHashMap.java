package api;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

public class GenresHashMap {
  private static final String GENRES_URL =
      "https://api.themoviedb.org/3/genre/movie/list?api_key=f315a3238066ebb41551a49a0984e185&language=en-US";
  private HashMap<Integer, String> GENRES_MAP;

  public HashMap<Integer, String> getGenresMap() throws IOException {
    if (GENRES_MAP != null) {
      return GENRES_MAP;
    }

    URL url = new URL(GENRES_URL);
    URLConnection request = url.openConnection();
    request.connect();

    JsonParser jp = new JsonParser(); // from gson
    JsonElement root =
        jp.parse(
            new InputStreamReader(
                (InputStream) request.getContent())); // Convert the input stream to a json element
    JsonArray array = root.getAsJsonObject().getAsJsonArray("genres");

    // Creates Hashmap containing genres names and their id's as keys
    GENRES_MAP = new HashMap<>();
    for (int i = 0; i < array.size(); i++) {
      GENRES_MAP.put(
          array.get(i).getAsJsonObject().get("id").getAsInt(),
          array.get(i).getAsJsonObject().get("name").getAsString());
    }

    return GENRES_MAP;
  }
}
