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
/**
 * Klasa odpytująca serwis TMDB za pomocą API.
 *
 * <p>Klasa wysyła zapytanie do serwisu TMDB i zwraca obiekt JSON.
 */
public class APICaller {
  /** Url z zapytaniem */
  private String callUrl;

  /** Typ zapytania */
  private CallType callType;

  /**
   * Konstruktor sprawdza czy callType jest prawidłowy i tworzy obiekt APICaller
   *
   * @param callUrl url z zapytaniem
   * @param callType rodzaj zapytania
   * @throws APIException.WrongCallTypeException
   */
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

  /**
   * Metoda wysyłająca zapytanie do serwisu TMDB.
   *
   * @return zwraca obiekt JSON
   */
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

  public String getCallUrl() {
    return callUrl;
  }

  public CallType getCallType() {
    return callType;
  }
}
