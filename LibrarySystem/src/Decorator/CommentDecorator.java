package Decorator;
import Singleton.DatabaseConnection;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CommentDecorator extends BookDecorator {
    private String yorum;
    public CommentDecorator(BookComponent bookComponent, String yorum) {
        super(bookComponent);
        this.yorum = yorum;
    }
    @Override
    public void display() {
        super.display();
        System.out.println("Yorum: " + yorum);
    }

    public void yorumEkle(int kullaniciId, int kitapId, String yorum) {
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO yorumlar (kullanici_id, kitap_id, yorum) VALUES (?, ?, ?)")) {
            stmt.setInt(1, kullaniciId);
            stmt.setInt(2, kitapId);
            stmt.setString(3, yorum);
            stmt.executeUpdate();
            System.out.println("Yorum eklendi: " + yorum);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void yorumlarıGor(int kitapId, String kitapAdi) {
        JFrame frame = new JFrame("Yorumlar - " + kitapAdi);
        frame.setSize(300, 300);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);

        JTextArea yorumlarTextArea = new JTextArea();
        yorumlarTextArea.setEditable(false);

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT y.yorum, k.ad, k.soyad FROM yorumlar y JOIN kullanıcılar k ON y.kullanici_id = k.id WHERE y.kitap_id = ?")) {
            stmt.setInt(1, kitapId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String kullaniciAdi = rs.getString("ad");
                String kullaniciSoyadi = rs.getString("soyad");
                String yorum = rs.getString("yorum");
                yorumlarTextArea.append(kullaniciAdi + " " + kullaniciSoyadi + ": " + yorum + "\n");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        frame.add(new JScrollPane(yorumlarTextArea), BorderLayout.CENTER);
        frame.setVisible(true);
    }
}
