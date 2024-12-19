package Singleton;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class DatabaseConnection {

    private static DatabaseConnection instance;
    private Connection connection;

    private static final String URL = "jdbc:postgresql://localhost:5432/LibrarySystem";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "195601";

    private DatabaseConnection() {
        try {
            Class.forName("org.postgresql.Driver");
            // bağlantı başlatılır
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Bağlantı başarılı bir şekilde sağlandı.");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Veritabanı bağlantısı kurulamadı.", e);
        }
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            synchronized (DatabaseConnection.class) {
                if (instance == null) {
                    instance = new DatabaseConnection();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                System.out.println("Bağlantı kapalı, yeniden bağlanılıyor...");
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Veritabanı bağlantısı yenilenemedi.", e);
        }
        return connection;
    }



}
