package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

  private static DatabaseConnection instance;
  private Connection connection;
  private String url = "jdbc:mysql://localhost:3307/movie4us";
  private String username = "root";
  private String password = "";

  private DatabaseConnection() throws SQLException {
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
      this.connection = DriverManager.getConnection(url, username, password);
    } catch (ClassNotFoundException ex) {
      System.out.println("Database Connection Creation Failed : " + ex.getMessage());
    }
  }

  public Connection getConnection() {
    return connection;
  }

  public void close() {
    try {
      this.connection.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public static DatabaseConnection getInstance() throws SQLException {
    if (instance == null) {
      instance = new DatabaseConnection();
    } else if (instance.getConnection().isClosed()) {
      instance = new DatabaseConnection();
    }

    return instance;
  }
}
