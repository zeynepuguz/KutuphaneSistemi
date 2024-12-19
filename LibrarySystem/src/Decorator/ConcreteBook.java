package Decorator;

public class ConcreteBook implements BookComponent {

    private String kitapAdi;

    public ConcreteBook(String kitapAdi) {
        this.kitapAdi = kitapAdi;
    }
    @Override
    public void display() {
        System.out.println("Kitap: " + kitapAdi);
    }


}
