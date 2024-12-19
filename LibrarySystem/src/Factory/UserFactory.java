package Factory;

import Singleton.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserFactory {

    public static Users createUser(String email, String parola) {
        Connection connection = DatabaseConnection.getInstance().getConnection();
        try {
            String query = "SELECT * FROM kullanıcılar WHERE email = ? AND parola = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, email);
            stmt.setString(2, parola);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String ad = rs.getString("ad");
                String soyad = rs.getString("soyad");
                String yetki = rs.getString("yetki");
                String emailDB = rs.getString("email");
                String parolaDB = rs.getString("parola");

                if (yetki.equalsIgnoreCase("Personel")) {
                    return new Personel(id, ad, soyad, emailDB, parolaDB);
                } else if (yetki.equalsIgnoreCase("Öğrenci")) {
                    Students students = new Students(id, ad, soyad, emailDB, parolaDB);
                    return students;
                } else {
                    System.out.println("Bilinmeyen kullanıcı türü: " + yetki);
                    return null;
                }
            } else {
                System.out.println("Kullanıcı bulunamadı.");
                return null;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
