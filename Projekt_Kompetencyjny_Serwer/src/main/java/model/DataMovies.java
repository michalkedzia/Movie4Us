package model;

import api.APICaller;
import api.APIUrlBuilder;
import api.APIjsonFormatter;
import api.data.PageMovieData;
import api.utils.FilterType;
import com.google.gson.JsonObject;
import exceptions.APIException;

import static api.utils.CallType.DISCOVER;
import static api.utils.CallType.GENRES;

public class DataMovies {

//    APIUrlBuilder apiUrlBuilder;
//    String stringUrl;
//    APICaller apiCaller;
//
//
//
//    void asd(){
//                  APIUrlBuilder apiUrlBuilder = new APIUrlBuilder();
//      String stringUrl = apiUrlBuilder.createBasicUrl(DISCOVER);
//      try {
//        apiUrlBuilder.addDiscoverFilter(FilterType.LANGUAGE, "english");
//        apiUrlBuilder.addDiscoverFilter(FilterType.SORT, "popularity.desc");
//        apiUrlBuilder.addDiscoverFilter(FilterType.PAGE, "1");
//        stringUrl = apiUrlBuilder.addDiscoverFilter(FilterType.GENRE, "Drama");
//        System.out.println(stringUrl);
//      } catch (APIException.WrongCallTypeException | APIException.InvalidFilterValueException e) {
//        e.printStackTrace();
//      }
//
//      APICaller apiCaller = null;
//      try {
//        apiCaller = new APICaller(stringUrl, DISCOVER);
//      } catch (APIException.WrongCallTypeException e) {
//        e.printStackTrace();
//      }
//
//      assert apiCaller != null;
//      JsonObject obj = apiCaller.sendAPIrequest();
//
//      APIjsonFormatter formatter = new APIjsonFormatter();
//      try {
//        PageMovieData page = formatter.parseToObject(obj);
//              System.out.println(page.toString());
//              System.out.println(page.movieDataArray.get(15).toString());
//      } catch (APIException.WrongJsonObjectException e) {
//        e.printStackTrace();
//      }
//    }




//          APIUrlBuilder apiUrlBuilder = new APIUrlBuilder();
//      String stringUrl = apiUrlBuilder.createBasicUrl(DISCOVER);
//      try {
//        apiUrlBuilder.addDiscoverFilter(FilterType.LANGUAGE, "english");
//        apiUrlBuilder.addDiscoverFilter(FilterType.SORT, "popularity.desc");
//        apiUrlBuilder.addDiscoverFilter(FilterType.PAGE, "1");
//        stringUrl = apiUrlBuilder.addDiscoverFilter(FilterType.GENRE, "Drama");
//        System.out.println(stringUrl);
//      } catch (APIException.WrongCallTypeException | APIException.InvalidFilterValueException e) {
//        e.printStackTrace();
//      }
//
//      APICaller apiCaller = null;
//      try {
//        apiCaller = new APICaller(stringUrl, DISCOVER);
//      } catch (APIException.WrongCallTypeException e) {
//        e.printStackTrace();
//      }
//
//      assert apiCaller != null;
//      JsonObject obj = apiCaller.sendAPIrequest();
//
//      APIjsonFormatter formatter = new APIjsonFormatter();
//      try {
//        PageMovieData page = formatter.parseToObject(obj);
//              System.out.println(page.toString());
//              System.out.println(page.movieDataArray.get(15).toString());
//      } catch (APIException.WrongJsonObjectException e) {
//        e.printStackTrace();
//      }

}
