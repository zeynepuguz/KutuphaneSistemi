package Strategy;

import Singleton.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

//Yazara göre arama
public class SearchByAuthor implements SearchStrategy {

    @Override
    public List<String> search(String query) {
        List<String> results = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT ad FROM kitaplar WHERE yazar ILIKE ?")) {
            stmt.setString(1, "%" + query + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                results.add(rs.getString("ad"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }
}
