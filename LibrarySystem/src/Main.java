import Singleton.DatabaseConnection;

import javax.swing.*;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            DatabaseConnection dbInstance = DatabaseConnection.getInstance();
            try (Connection connection = dbInstance.getConnection()) {
                if (connection != null) {
                    // LoginForm başlatma
                    new LoginForm();
                } else {
                    System.out.println("Veritabanı bağlantısı başarısız.");
                }
            } catch (SQLException e) {
                System.out.println("Veritabanı bağlantısında hata: " + e.getMessage());
            }
        });
    }
}
