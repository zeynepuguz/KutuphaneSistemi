package Observer;
import Singleton.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class DatabaseUpdate implements Observer {
    @Override
    public void update(String kitapAdi, String kitapDurumu) {
        if (kitapAdi == null || kitapAdi.isEmpty()) {
            throw new IllegalArgumentException("Kitap adı boş olamaz!"); // Hata fırlat
        }

        Connection conn = DatabaseConnection.getInstance().getConnection();
        try {
            String query = "UPDATE kitaplar SET durum = ? WHERE ad = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, kitapDurumu);
            stmt.setString(2, kitapAdi);
            stmt.executeUpdate();
            System.out.println("Veritabanı güncellendi: " + kitapAdi + " durumu: " + kitapDurumu);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

