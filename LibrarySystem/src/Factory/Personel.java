package Factory;
import Singleton.DatabaseConnection;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Personel extends Users {
    public Personel(int id, String ad, String soyad, String email, String parola) {
        super(id, ad, soyad, email, parola, "Personel");
    }
    @Override
    public void login() {
        System.out.println("Personel olarak giriş yapıldı: " + getAd() + " " + getSoyad());
    }

    public void kitapEkle() {
        JFrame frame = new JFrame("Kitap Ekle");
        frame.setSize(400, 300);
        frame.setLayout(new GridLayout(5, 2));
        frame.setLocationRelativeTo(null);

        JLabel kitapAdiLabel = new JLabel("Kitap Adı:");
        JTextField kitapAdiField = new JTextField();
        JLabel yazarLabel = new JLabel("Yazar:");
        JTextField yazarField = new JTextField();
        JLabel kategoriLabel = new JLabel("Kategori:");
        JTextField kategoriField = new JTextField();
        JLabel durumLabel = new JLabel("Durum:");
        JTextField durumField = new JTextField();

        JButton ekleButton = new JButton("Ekle");
        ekleButton.addActionListener(e -> {
            String kitapAdi = kitapAdiField.getText();
            String yazar = yazarField.getText();
            String kategori = kategoriField.getText();
            String durum = durumField.getText();

            if (kitapAdi.isEmpty() || yazar.isEmpty() || kategori.isEmpty() || durum.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Tüm alanlar doldurulmalıdır!", "Uyarı", JOptionPane.WARNING_MESSAGE);
            } else {
                String query = "INSERT INTO Kitaplar (ad, yazar, kategori, durum) VALUES (?, ?, ?, ?)";
                try (Connection connection = DatabaseConnection.getInstance().getConnection();
                     PreparedStatement stmt = connection.prepareStatement(query)) {
                    stmt.setString(1, kitapAdi);
                    stmt.setString(2, yazar);
                    stmt.setString(3, kategori);
                    stmt.setString(4, durum);
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(frame, "Kitap başarıyla eklendi!", "Bilgi", JOptionPane.INFORMATION_MESSAGE);
                    kitapAdiField.setText("");
                    yazarField.setText("");
                    kategoriField.setText("");
                    durumField.setText("");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Kitap eklenirken bir hata oluştu!", "Hata", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        frame.add(kitapAdiLabel);
        frame.add(kitapAdiField);
        frame.add(yazarLabel);
        frame.add(yazarField);
        frame.add(kategoriLabel);
        frame.add(kategoriField);
        frame.add(durumLabel);
        frame.add(durumField);
        frame.add(new JLabel());
        frame.add(ekleButton);
        frame.setVisible(true);
    }

    public void kitapSil() {
        JFrame frame = new JFrame("Kitap Sil");
        frame.setSize(300, 200);
        frame.setLayout(new GridLayout(3, 2));
        frame.setLocationRelativeTo(null);

        JLabel kitapAdiLabel = new JLabel("Kitap Adı:");
        JTextField kitapAdiField = new JTextField();
        JLabel yazarLabel = new JLabel("Yazar:");
        JTextField yazarField = new JTextField();

        JButton silButton = new JButton("Sil");
        silButton.addActionListener(e -> {
            String kitapAdi = kitapAdiField.getText();
            String yazar = yazarField.getText();

            if (kitapAdi.isEmpty() || yazar.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Kitap Adı ve Yazar bilgisi doldurulmalıdır!", "Uyarı", JOptionPane.WARNING_MESSAGE);
            } else {
                String query = "DELETE FROM Kitaplar WHERE ad = ? AND yazar = ?";
                try (Connection connection = DatabaseConnection.getInstance().getConnection();
                     PreparedStatement stmt = connection.prepareStatement(query)) {
                    stmt.setString(1, kitapAdi);
                    stmt.setString(2, yazar);
                    int rows = stmt.executeUpdate();
                    if (rows > 0) {
                        JOptionPane.showMessageDialog(frame, "Kitap başarıyla silindi!", "Bilgi", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(frame, "Kitap bulunamadı!", "Uyarı", JOptionPane.WARNING_MESSAGE);
                    }
                    kitapAdiField.setText("");
                    yazarField.setText("");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Kitap silinirken bir hata oluştu!", "Hata", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        frame.add(kitapAdiLabel);
        frame.add(kitapAdiField);
        frame.add(yazarLabel);
        frame.add(yazarField);
        frame.add(new JLabel());
        frame.add(silButton);
        frame.setVisible(true);
    }

    public void kitapGuncelle() {
        JFrame frame = new JFrame("Kitap Güncelle");
        frame.setSize(400, 300);
        frame.setLayout(new GridLayout(5, 2));
        frame.setLocationRelativeTo(null);

        JLabel kitapAdiLabel = new JLabel("Kitap Adı:");
        JTextField kitapAdiField = new JTextField();
        JLabel yeniYazarLabel = new JLabel("Yeni Yazar:");
        JTextField yeniYazarField = new JTextField();
        JLabel yeniKategoriLabel = new JLabel("Yeni Kategori:");
        JTextField yeniKategoriField = new JTextField();
        JLabel yeniDurumLabel = new JLabel("Yeni Durum:");
        JTextField yeniDurumField = new JTextField();

        JButton guncelleButton = new JButton("Güncelle");
        guncelleButton.addActionListener(e -> {
            String kitapAdi = kitapAdiField.getText();
            String yeniYazar = yeniYazarField.getText();
            String yeniKategori = yeniKategoriField.getText();
            String yeniDurum = yeniDurumField.getText();

            if (kitapAdi.isEmpty() || yeniYazar.isEmpty() || yeniKategori.isEmpty() || yeniDurum.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Tüm alanlar doldurulmalıdır!", "Uyarı", JOptionPane.WARNING_MESSAGE);
            } else {
                String query = "UPDATE Kitaplar SET yazar = ? WHERE ad = ?";
                try (Connection connection = DatabaseConnection.getInstance().getConnection();
                     PreparedStatement stmt = connection.prepareStatement(query)) {
                    stmt.setString(1, yeniYazar);
                    stmt.setString(2, kitapAdi);
                    int rows = stmt.executeUpdate();
                    if (rows > 0) {
                        JOptionPane.showMessageDialog(frame, "Kitap başarıyla güncellendi!", "Bilgi", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(frame, "Kitap bulunamadı!", "Uyarı", JOptionPane.WARNING_MESSAGE);
                    }
                    kitapAdiField.setText("");
                    yeniYazarField.setText("");
                    yeniKategoriField.setText("");
                    yeniDurumField.setText("");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Kitap güncellenirken bir hata oluştu!", "Hata", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        frame.add(kitapAdiLabel);
        frame.add(kitapAdiField);
        frame.add(yeniYazarLabel);
        frame.add(yeniYazarField);
        frame.add(yeniKategoriLabel);
        frame.add(yeniKategoriField);
        frame.add(yeniDurumLabel);
        frame.add(yeniDurumField);
        frame.add(new JLabel());
        frame.add(guncelleButton);
        frame.setVisible(true);
    }
}
