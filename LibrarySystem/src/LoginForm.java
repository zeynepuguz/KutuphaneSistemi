import Factory.*;
import Singleton.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LoginForm extends JFrame {

    private JTextField epostaField;
    private JPasswordField parolaField;
    private JButton girisButonu;
    private JButton kayitButonu;
    private JButton kitapSilButonu;
    private JButton kitapEkleButonu;
    private JButton kitapGuncelleButonu;

    public LoginForm() {
        setTitle("Kütüphane Giriş");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2, 10, 10));

        // Kullanıcı giriş alanı
        JLabel emailLabel = new JLabel("Email:");
        epostaField = new JTextField();
        JLabel parolaLabel = new JLabel("Parola:");
        parolaField = new JPasswordField();
        girisButonu = new JButton("Giriş Yap");
        kayitButonu = new JButton("Kayıt Ol");

        girisButonu.addActionListener(e -> girisYap());
        kayitButonu.addActionListener(e -> kayitOl());

        // Panel elemanlarını ekleme
        panel.add(emailLabel);
        panel.add(epostaField);
        panel.add(parolaLabel);
        panel.add(parolaField);
        panel.add(new JLabel());
        panel.add(girisButonu);
        panel.add(new JLabel());
        panel.add(kayitButonu);

        add(panel);
        setVisible(true);
    }

    private void girisYap() {
        String email = epostaField.getText().trim();
        String parola = new String(parolaField.getPassword()).trim();

        if (email.isEmpty() || parola.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lütfen tüm alanları doldurun!", "Uyarı", JOptionPane.WARNING_MESSAGE);
            return;
        }

        UserFactory userFactory = new UserFactory();
        Users kullanici = userFactory.createUser(email, parola);

        if (kullanici != null) {
            JOptionPane.showMessageDialog(this, "Giriş başarılı!", "Bilgi", JOptionPane.INFORMATION_MESSAGE);
            kullanici.login();

            if (kullanici instanceof Students) {
                ogrenciArayuzu((Students) kullanici);
            } else if (kullanici instanceof Personel) {
                personelArayuzu((Personel) kullanici);
            }

            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Hatalı email veya parola! Kayıt olmak ister misiniz?", "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void kayitOl() {
        String email = epostaField.getText().trim();
        String parola = new String(parolaField.getPassword()).trim();

        if (email.isEmpty() || parola.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Lütfen tüm alanları doldurun!", "Uyarı", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String ad = JOptionPane.showInputDialog("Adınızı giriniz:");
        String soyad = JOptionPane.showInputDialog("Soyadınızı giriniz:");

        if (ad == null || soyad == null || ad.trim().isEmpty() || soyad.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ad ve soyad alanları zorunludur!", "Uyarı", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Connection conn = DatabaseConnection.getInstance().getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO kullanıcılar (email, parola, ad, soyad, yetki) VALUES (?, ?, ?, ?, 'Öğrenci')"
            );
            stmt.setString(1, email);
            stmt.setString(2, parola);
            stmt.setString(3, ad);
            stmt.setString(4, soyad);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Kayıt başarıyla oluşturuldu!", "Bilgi", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Kayıt sırasında bir hata oluştu!", "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ogrenciArayuzu(Students ogrenci) {
        JFrame ogrenciFrame = new JFrame("Öğrenci Paneli");
        ogrenciFrame.setSize(400, 300);
        ogrenciFrame.setLayout(new GridLayout(3, 1, 10, 10));
        ogrenciFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ogrenciFrame.setLocationRelativeTo(null);

        JButton kitapListeleButonu = new JButton("Kitapları Listele");
        JButton cikisButonu = new JButton("Çıkış");

        kitapListeleButonu.addActionListener(e -> ogrenci.kitapListele());
        cikisButonu.addActionListener(e -> ogrenciFrame.dispose());

        ogrenciFrame.add(kitapListeleButonu);
        ogrenciFrame.add(cikisButonu);

        ogrenciFrame.setVisible(true);
    }

    private void personelArayuzu(Personel personel) {
        JFrame personelFrame = new JFrame("Personel Paneli");
        personelFrame.setSize(400, 400);
        personelFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        personelFrame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1, 10, 10));

        kitapEkleButonu = new JButton("Kitap Ekle");
        kitapEkleButonu.addActionListener(e -> personel.kitapEkle());

        kitapGuncelleButonu = new JButton("Kitap Güncelle");
        kitapGuncelleButonu.addActionListener(e -> personel.kitapGuncelle());

        kitapSilButonu = new JButton("Kitap Sil");
        kitapSilButonu.addActionListener(e -> personel.kitapSil());

        panel.add(kitapEkleButonu);
        panel.add(kitapGuncelleButonu);
        panel.add(kitapSilButonu);

        personelFrame.add(panel);
        personelFrame.setVisible(true);
    }
}
