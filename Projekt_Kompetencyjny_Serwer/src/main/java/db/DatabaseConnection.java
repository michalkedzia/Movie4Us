package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/** Klasa połaćzenia bazy danych MySQL. */
public class DatabaseConnection {

  /** Instancja bazyd danych. */
  private static DatabaseConnection instance;
  /** Sesjca bazy dabych */
  private Connection connection;
  /** Adres bazy dancyh */
  private String url = "jdbc:mysql://localhost:3306/movie4us";
  /** Nazwa użytkownika bazy */
  private String username = "root";
  /** Hasło do bazy danych */
  private String password = "";

  /**
   * Intializacja połączenia z bazą
   *
   * @throws SQLException
   */
  private DatabaseConnection() throws SQLException {
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
      this.connection = DriverManager.getConnection(url, username, password);
    } catch (ClassNotFoundException ex) {
      System.out.println("Database Connection Creation Failed : " + ex.getMessage());
    }
  }

  /** @return connection do bazy danych */
  public Connection getConnection() {
    return connection;
  }

  /** Zamykanie połączenia z bazą danych */
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
