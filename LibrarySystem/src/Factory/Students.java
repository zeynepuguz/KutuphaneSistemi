package Factory;

import Decorator.*;
import Observer.BookManagement;
import Observer.DatabaseUpdate;
import Observer.NotificationSystem;
import Singleton.DatabaseConnection;
import State.Book;
import Strategy.SearchByAuthor;
import Strategy.SearchByBookName;
import Strategy.SearchByCategory;
import Strategy.SearchContext;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
public class Students extends Users {

    private String oduncAlinanKitap = null;
    private BookManagement bookManagement;
    private Book book;
    private final SearchContext searchContext;

    public Students(int id, String ad, String soyad, String email, String parola) {
        super(id, ad, soyad, email, parola, "Öğrenci");
        this.book = new Book(oduncAlinanKitap);
        this.bookManagement = new BookManagement(this.book);
        this.searchContext = new SearchContext();
    }

    @Override
    public void login() {
        System.out.println("Öğrenci olarak giriş yapıldı: " + getAd() + " " + getSoyad());
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(
                     "SELECT k.ad FROM odunc o " +
                             "JOIN kitaplar k ON o.kitap_id = k.id " +
                             "WHERE o.kullanici_id = ? AND o.durum = 'Devam Ediyor'")) {
            stmt.setInt(1, getId());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                oduncAlinanKitap = rs.getString("ad");
            } else {
                oduncAlinanKitap = null;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void kitapListele() {
        JFrame frame = new JFrame("Kitap Listesi");
        frame.setSize(600, 500);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);

        JPanel kitapPanel = new JPanel();
        kitapPanel.setLayout(new GridLayout(8, 1, 10, 10));

        JLabel oduncLabel = new JLabel();
        if (oduncAlinanKitap != null) {
            oduncLabel.setText("Ödünç aldığınız kitap: " + oduncAlinanKitap);
        } else {
            oduncLabel.setText("Henüz ödünç alınmış bir kitap bulunmamaktadır.");
        }
        frame.add(oduncLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton kitapAraButonu = new JButton("Kitap Ara");
        kitapAraButonu.addActionListener(e -> kitapAra(frame));
        buttonPanel.add(kitapAraButonu);

        frame.add(buttonPanel, BorderLayout.SOUTH);

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT id, ad, yazar, durum FROM Kitaplar");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int bookId = rs.getInt("id");
                String kitapAdi = rs.getString("ad");
                String yazar = rs.getString("yazar");
                String durum = rs.getString("durum");

                JLabel kitapLabel = new JLabel(kitapAdi + " - " + yazar);
                JButton oduncButonu = new JButton("Ödünç Al");
                JButton iadeButonu = new JButton("İade Et");
                JButton yorumlaPuanla = new JButton("Kitabı puanla ve görüşünü bırak");
                JButton yorumPuanGoster = new JButton("Kitap Puanlamaları ve Yorumları");

                oduncButonu.setEnabled(oduncAlinanKitap == null && "Rafta".equalsIgnoreCase(durum));
                iadeButonu.setEnabled(oduncAlinanKitap != null && oduncAlinanKitap.equals(kitapAdi));

                oduncButonu.addActionListener(e -> {
                    Connection conn3 = DatabaseConnection.getInstance().getConnection();
                    try {
                        this.book = new Book(kitapAdi);
                        this.bookManagement = new BookManagement(this.book);
                        bookManagement.addObserver(new NotificationSystem());
                        bookManagement.addObserver(new DatabaseUpdate());
                        bookManagement.oduncAlma();

                        try (PreparedStatement updateKitapStmt = conn3.prepareStatement("UPDATE kitaplar SET durum = 'Ödünç Alındı' WHERE id = ?")) {
                            updateKitapStmt.setInt(1, bookId);
                            updateKitapStmt.executeUpdate();
                        }

                        try (PreparedStatement insertOduncStmt = conn3.prepareStatement(
                                "INSERT INTO odunc (kullanici_id, kitap_id, odunc_tarihi, durum) VALUES (?, ?, NOW(), 'Devam Ediyor')")) {
                            insertOduncStmt.setInt(1, getId());
                            insertOduncStmt.setInt(2, bookId);
                            insertOduncStmt.executeUpdate();
                        }

                        oduncAlinanKitap = kitapAdi;
                        JOptionPane.showMessageDialog(frame, kitapAdi + " ödünç alındı.", "Bilgi", JOptionPane.INFORMATION_MESSAGE);
                        frame.dispose();
                        kitapListele();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                });

                iadeButonu.addActionListener(e -> {
                    Connection conn2 = DatabaseConnection.getInstance().getConnection();
                    try {
                        this.book = new Book(kitapAdi);
                        this.bookManagement = new BookManagement(this.book);
                        bookManagement.addObserver(new NotificationSystem());
                        bookManagement.addObserver(new DatabaseUpdate());
                        bookManagement.iadeEtme();

                        try (PreparedStatement updateKitapStmt = conn2.prepareStatement("UPDATE kitaplar SET durum = 'Rafta' WHERE id = ?")) {
                            updateKitapStmt.setInt(1, bookId);
                            updateKitapStmt.executeUpdate();
                        }

                        try (PreparedStatement updateOduncStmt = conn2.prepareStatement(
                                "UPDATE odunc SET durum = 'İade Edildi', iade_tarihi = NOW() WHERE kitap_id = ? AND kullanici_id = ? AND durum = 'Devam Ediyor'")) {
                            updateOduncStmt.setInt(1, bookId);
                            updateOduncStmt.setInt(2, getId());
                            updateOduncStmt.executeUpdate();
                        }

                        oduncAlinanKitap = null;
                        JOptionPane.showMessageDialog(frame, kitapAdi + " iade edildi.", "Bilgi", JOptionPane.INFORMATION_MESSAGE);
                        frame.dispose();
                        kitapListele();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                });

                yorumlaPuanla.addActionListener(e -> {
                    yorumlaPuanlaPaneli(bookId, kitapAdi);
                });

                yorumPuanGoster.addActionListener(e -> {
                    yorumvePuanlamalariGor(bookId, kitapAdi);
                });

                JPanel kitapPaneli = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

                kitapPaneli.add(kitapLabel);
                kitapPaneli.add(oduncButonu);
                kitapPaneli.add(iadeButonu);
                kitapPaneli.add(yorumlaPuanla);
                kitapPaneli.add(yorumPuanGoster);

                kitapPanel.add(kitapPaneli);

            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        JScrollPane scrollPane = new JScrollPane(kitapPanel);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    // kitap arama
    public void kitapAra(JFrame parentFrame) {
        String[] aramaTurleri = {"Kitap Adı", "Yazar", "Kategori"};
        String secim = (String) JOptionPane.showInputDialog(null,
                "Arama Türü Seçiniz:",
                "Arama",
                JOptionPane.QUESTION_MESSAGE,
                null,
                aramaTurleri,
                aramaTurleri[0]);

        String query = JOptionPane.showInputDialog("Aramak istediğiniz kelimeyi giriniz:");

        if (secim == null || query == null) return;

        switch (secim) {
            case "Kitap Adı" -> searchContext.setSearchStrategy(new SearchByBookName());
            case "Yazar" -> searchContext.setSearchStrategy(new SearchByAuthor());
            case "Kategori" -> searchContext.setSearchStrategy(new SearchByCategory());
        }

        List<String> results = searchContext.search(query);
        if (results.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Sonuç bulunamadı.");
        } else {
            JDialog resultDialog = new JDialog(parentFrame, "Arama Sonuçları", true);
            resultDialog.setSize(300, 300);
            resultDialog.setLayout(new BorderLayout());
            resultDialog.setLocationRelativeTo(parentFrame);

            JPanel resultPanel = new JPanel(new GridLayout(0, 2, 10, 10));

            for (String kitapAdi : results) {
                JLabel kitapLabel = new JLabel(kitapAdi);
                JButton oduncButton = new JButton("Ödünç Al");

                oduncButton.addActionListener(e -> {
                    Connection conn = DatabaseConnection.getInstance().getConnection();
                    try {
                        this.book = new Book(kitapAdi);
                        this.bookManagement = new BookManagement(this.book);
                        bookManagement.addObserver(new NotificationSystem());
                        bookManagement.addObserver(new DatabaseUpdate());
                        bookManagement.oduncAlma();

                        try (PreparedStatement stmt = conn.prepareStatement(
                                "UPDATE kitaplar SET durum = 'Ödünç Alındı' WHERE ad = ?")) {
                            stmt.setString(1, kitapAdi);
                            stmt.executeUpdate();
                        }

                        oduncAlinanKitap = kitapAdi;
                        JOptionPane.showMessageDialog(resultDialog, kitapAdi + " ödünç alındı.", "Bilgi", JOptionPane.INFORMATION_MESSAGE);
                        resultDialog.dispose();
                        parentFrame.dispose();
                        kitapListele();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                });

                resultPanel.add(kitapLabel);
                resultPanel.add(oduncButton);
            }

            JScrollPane scrollPane = new JScrollPane(resultPanel);
            resultDialog.add(scrollPane, BorderLayout.CENTER);
            resultDialog.setVisible(true);
        }
    }

    public void yorumlaPuanlaPaneli(int kitapId, String kitapAdi) {
        JFrame frame = new JFrame("Yorum ve Puanla - " + kitapAdi);
        frame.setSize(300, 400);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);

        // yorum alanı
        JTextArea yorumTextArea = new JTextArea(5, 30);
        yorumTextArea.setLineWrap(true);
        yorumTextArea.setWrapStyleWord(true);
        JScrollPane yorumScrollPane = new JScrollPane(yorumTextArea);

        // puan verme alanı
        JLabel puanLabel = new JLabel("Puan Ver (1-5):");
        SpinnerNumberModel model = new SpinnerNumberModel(1, 1, 5, 1);
        JSpinner puanSpinner = new JSpinner(model);

        JButton gonderButonu = new JButton("Gönder");

        gonderButonu.addActionListener(e -> {
            String yorum = yorumTextArea.getText().trim();
            int puan = (int) puanSpinner.getValue();

            if (yorum.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Lütfen bir yorum yazın!", "Hata", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // yorum ve puan ekleme
            try {
                BookComponent kitap = new ConcreteBook(kitapAdi);

                CommentDecorator yorumluKitap = new CommentDecorator(kitap, yorum);
                yorumluKitap.yorumEkle(getId(), kitapId, yorum);

                RatingDecorator puanliKitap = new RatingDecorator(kitap, puan);
                puanliKitap.puanVer(getId(), kitapId, puan);

                JOptionPane.showMessageDialog(frame, "Yorum ve Puan başarıyla eklendi!", "Bilgi", JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Bir hata oluştu: " + ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(4, 1, 10, 10));
        inputPanel.add(new JLabel("Yorumunuzu yazın:"));
        inputPanel.add(yorumScrollPane);
        inputPanel.add(puanLabel);
        inputPanel.add(puanSpinner);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(gonderButonu);

        frame.add(inputPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    public void yorumvePuanlamalariGor(int kitapId, String kitapAdi) {
        BookComponent kitap = new ConcreteBook(kitapAdi);

        // yorumları görüntüleme
        CommentDecorator yorumDecorator = new CommentDecorator(kitap, "Görünüm");
        yorumDecorator.yorumlarıGor(kitapId, kitapAdi);

        // puanları görüntüleme
        RatingDecorator puanDecorator = new RatingDecorator(kitap, 0);
        puanDecorator.puanlamalariGor(kitapId, kitapAdi);
    }
}
