package Observer;

import State.Book;
import State.Borrowed;
import State.OnTheShelf;

import java.util.ArrayList;
import java.util.List;

public class BookManagement {
    private Book book;
    private List<Observer> observers = new ArrayList<>();

    public BookManagement(Book book) {
        this.book = book;
    }

    // Gözlemci ekleme
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    // gözlemcilere bildirim gönder
    private void gozlemciyeBildir(String durum) {
        for (Observer observer : observers) {
            observer.update(book.getKitapAdi(), durum);
        }
    }

    // kitabı ödünç alma bildirimi
    public void oduncAlma() {
        book.setState(new Borrowed());
        gozlemciyeBildir("Ödünç Alındı");
    }

    // kitap iade bildirimi
    public void iadeEtme() {
        book.setState(new OnTheShelf());
        gozlemciyeBildir("Rafta");
    }

}
