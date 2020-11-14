package api;

import api.utils.CallType;
import api.utils.FilterType;
import exceptions.APIException;

import java.time.Year;
import java.util.HashMap;
import java.util.Map;

public class APIUrlBuilder {
  private static final String API_KEY = "f315a3238066ebb41551a49a0984e185";
  private static final String API_URL = "https://api.themoviedb.org/3/";
  private HashMap<Integer, String> GENRES_MAP;

  private String retUrl = "";
  private CallType callType = null;

  public String createBasicUrl(
      CallType callType) { // Creates basic API url with api key and search category
    switch (callType) {
      case DISCOVER:
        {
          try {
            GenresHashMap genresHashMap = new GenresHashMap();
            GENRES_MAP = genresHashMap.getGenresMap();
          } catch (Exception e) {
            e.printStackTrace();
          }

          this.callType = callType;
          retUrl = API_URL + "discover/movie?api_key=" + API_KEY + "&page=1";
          break;
        }
      case GENRES:
        {
          this.callType = callType;
          retUrl = API_URL + "genre/movie/list?api_key=" + API_KEY;
          break;
        }
    }
    return retUrl;
  }

  public String addDiscoverFilter(FilterType filter, String value)
      throws APIException.InvalidFilterValueException, APIException.WrongCallTypeException {
    if (callType == null) {
      throw new APIException.WrongCallTypeException("No calltype asigned");
    }
    if (GENRES_MAP == null) {
      throw new APIException.WrongCallTypeException("Wrong calltype asigned");
    }
    validateFilter(filter, value);

    switch (filter) {
      case LANGUAGE:
        {
          if (value.compareToIgnoreCase("english") == 0) {
            retUrl = retUrl + "&language=en";
          } else if (value.compareToIgnoreCase("polish") == 0) {
            retUrl = retUrl + "&language=pl";
          } else if (value.compareToIgnoreCase("german") == 0) {
            retUrl = retUrl + "&language=de";
          }
          break;
        }
      case YEAR:
        retUrl = retUrl + "&year=" + Integer.parseInt(value);
        break;
      case GENRE:
        {
          if (!GENRES_MAP.containsValue(value)) {
            retUrl = retUrl + "&with_genres=" + Integer.parseInt(value);
          } else {
            for (Map.Entry<Integer, String> entry : GENRES_MAP.entrySet()) {
              if (value.compareToIgnoreCase(entry.getValue()) == 0) {
                retUrl = retUrl + "&with_genres=" + entry.getKey();
                break;
              }
            }
          }
          break;
        }
      case SORT:
        retUrl = retUrl + "&sort_by=" + value.toLowerCase();
        break;
      case PAGE:
        retUrl = retUrl.replaceFirst("page=\\d", "page=" + value);
        break;
    }
    return retUrl;
  }

  // Method that checks if provided value is valid for provided filtertype
  private void validateFilter(FilterType filter, String value)
      throws APIException.InvalidFilterValueException {
    switch (filter) {
      case LANGUAGE:
        {
          if (value.compareToIgnoreCase("english") != 0
              && value.compareToIgnoreCase("polish") != 0
              && value.compareToIgnoreCase("german") != 0) {
            throw new APIException.InvalidFilterValueException(
                "Invalid filter value provided: Wrong language" + value);
          }
          break;
        }
      case YEAR:
        {
          int intValue = Integer.parseInt(value);
          if (intValue < 1900 || intValue > Year.now().getValue()) {
            throw new APIException.InvalidFilterValueException(
                "Invalid filter value provided: Wrong year value" + value);
          }
          break;
        }
      case GENRE:
        {
          if (!GENRES_MAP.containsValue(value)) {
            try {
              if (GENRES_MAP.containsKey(Integer.parseInt(value))) {
                return;
              }
            } catch (NumberFormatException e) {
              throw new APIException.InvalidFilterValueException(
                  "Invalid filter value provided: Isn't an integer" + value);
            }
          }
          break;
        }
      case SORT:
        {
          if (value.compareToIgnoreCase("popularity.asc") != 0
              && value.compareToIgnoreCase("popularity.desc") != 0
              && value.compareToIgnoreCase("release_date.asc") != 0
              && value.compareToIgnoreCase("release_date.desc") != 0
              && value.compareToIgnoreCase("revenue.asc") != 0
              && value.compareToIgnoreCase("revenue.desc") != 0
              && value.compareToIgnoreCase("primary_release_date.asc") != 0
              && value.compareToIgnoreCase("primary_release_date.desc") != 0
              && value.compareToIgnoreCase("original_title.asc") != 0
              && value.compareToIgnoreCase("original_title.desc") != 0
              && value.compareToIgnoreCase("vote_average.asc") != 0
              && value.compareToIgnoreCase("vote_average.desc") != 0
              && value.compareToIgnoreCase("vote_count.asc") != 0
              && value.compareToIgnoreCase("vote_count.desc") != 0) {
            throw new APIException.InvalidFilterValueException(
                "Invalid filter value provided: Wrong sorting parameter" + value);
          }
          break;
        }
      case PAGE:
        {
          try {
            int intValue = Integer.parseInt(value);
            if (intValue < 1 || intValue > 500) {
              throw new APIException.InvalidFilterValueException(
                  "Invalid filter value provided: Wrong page number" + value);
            }
          } catch (NumberFormatException e) {
            throw new APIException.InvalidFilterValueException(
                "Invalid filter value provided: Value isn't an integer" + value);
          }
          break;
        }
    }
  }

  public String getRetUrl() {
    return retUrl;
  }
}
