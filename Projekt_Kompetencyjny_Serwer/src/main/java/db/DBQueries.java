package db;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DBQueries {
  public static List<String> getFriendsList(String userName) {
    List<String> list = new ArrayList<>();
    try {
      DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
      Connection connection = databaseConnection.getConnection();
      PreparedStatement preparedStatementUserID =
          connection.prepareStatement("SELECT * FROM users WHERE username = ?");
      PreparedStatement preparedStatementUsername =
          connection.prepareStatement("SELECT username FROM friends WHERE friendID = ?");
      preparedStatementUserID.setString(1, userName);
      ResultSet resultSet = preparedStatementUserID.executeQuery();
      // TODO wyjatek jesli nie znajdziemy rekordu
      if (resultSet.next()) {
        preparedStatementUsername.setInt(1, resultSet.getInt("userid"));
        resultSet = preparedStatementUsername.executeQuery();
        while (resultSet.next()) {
          list.add(resultSet.getString("username"));
        }
      }
    } catch (SQLException exception) {
      exception.printStackTrace();
    }
    return list;
  }

  public static List<String> getAllUsers() {
    List<String> list = new LinkedList<>();
    try {
      DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
      Connection connection = databaseConnection.getConnection();
      Statement statement = connection.createStatement();
      ResultSet resultSet = statement.executeQuery("SELECT * FROM users");
      while (resultSet.next()) {
        list.add(resultSet.getString("username"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return list;
  }

  public static void insertFriend(String userName, String friendName) {

    try {
      DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
      Connection connection = databaseConnection.getConnection();
      PreparedStatement preparedStatementID =
          connection.prepareStatement("SELECT * FROM users WHERE username = ?");
      preparedStatementID.setString(1, friendName);
      ResultSet resultSet = preparedStatementID.executeQuery();
      int ID = 0;
      if (resultSet.next()) {
        ID = resultSet.getInt("userid");
      }

      PreparedStatement preparedStatement =
          connection.prepareStatement(
              "INSERT INTO friends ( `username`, `friendID`) VALUES (?,?) ");
      preparedStatement.setString(1, userName);
      preparedStatement.setInt(2, ID);
      preparedStatement.execute();

    } catch (SQLException exception) {
      exception.printStackTrace();
    }
  }
}
