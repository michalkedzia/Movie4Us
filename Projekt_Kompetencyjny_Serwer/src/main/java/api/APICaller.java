package api;

import api.data.MovieWatchProviderData;
import api.utils.CallType;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import exceptions.APIException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import static api.utils.CallType.*;

public class APICaller {
  private String callUrl;
  private CallType callType;

  public APICaller(String callUrl, CallType callType) throws APIException.WrongCallTypeException {
    if (callType == DISCOVER) {
      if (!callUrl.contains("discover")) {
        throw new APIException.WrongCallTypeException("Wrong callUrl to provided CallType");
      }
    } else if (callType == GENRES) {
      if (!callUrl.contains("list")) {
        throw new APIException.WrongCallTypeException("Wrong callUrl to provided CallType");
      }
    } else if (callType == PROVIDERS) {
      if (!callUrl.contains("watch/providers")) {
        throw new APIException.WrongCallTypeException("Wrong callUrl to provided CallType");
      }
    }

    this.callUrl = callUrl;
    this.callType = callType;
  }

  public JsonObject sendAPIrequest() {
    JsonObject rootObj = null;
    try {
      URL url = new URL(callUrl);
      URLConnection request = url.openConnection();
      request.connect();

      JsonParser jp = new JsonParser(); // from gson
      rootObj =
          jp.parse(new InputStreamReader((InputStream) request.getContent())).getAsJsonObject();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return rootObj;
  }

  // ----------------------Example use of the url builder, API caller and JSON formatter
  // ----------------------------

  public static void main(String[] args) {
    APIUrlBuilder apiUrlBuilder = new APIUrlBuilder();
    //            String stringUrl = apiUrlBuilder.createBasicUrl(DISCOVER);
    //            try {
    //              apiUrlBuilder.addDiscoverFilter(FilterType.LANGUAGE, "english");
    //              apiUrlBuilder.addDiscoverFilter(FilterType.SORT, "popularity.desc");
    //              apiUrlBuilder.addDiscoverFilter(FilterType.PAGE, "1");
    //              stringUrl = apiUrlBuilder.addDiscoverFilter(FilterType.GENRE, "Drama");
    //              System.out.println(stringUrl);
    //              stringUrl = apiUrlBuilder.addDiscoverFilter(FilterType.GENRE, "Action");
    //              System.out.println(stringUrl);
    //    //          stringUrl = apiUrlBuilder.addDiscoverFilter(FilterType.YEAR, "2019");
    //              System.out.println(stringUrl);
    //            } catch (APIException.WrongCallTypeException |
    //     APIException.InvalidFilterValueException e)
    //       {
    //              e.printStackTrace();
    //            }

    String stringUrl = apiUrlBuilder.createBasicUrl(PROVIDERS);
    try {
      stringUrl = apiUrlBuilder.addProviderMovieId(475557);
    } catch (APIException.WrongMovieIdException e) {
      e.printStackTrace();
    }
    // System.out.println(stringUrl);

    APICaller apiCaller = null;
    try {
      apiCaller = new APICaller(stringUrl, PROVIDERS);
    } catch (APIException.WrongCallTypeException e) {
      e.printStackTrace();
    }

    assert apiCaller != null;
    JsonObject obj = apiCaller.sendAPIrequest();

    APIjsonFormatter formatter = new APIjsonFormatter();
    try {
      MovieWatchProviderData movie = formatter.parseToObjectMovieWatchProviderData(obj);
      System.out.println(movie.toString());
    } catch (APIException.WrongJsonObjectException e) {
      e.printStackTrace();
    }
  }

  public String getCallUrl() {
    return callUrl;
  }

  public CallType getCallType() {
    return callType;
  }
}
