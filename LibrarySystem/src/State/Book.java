package State;

public class Book {
    private String kitapAdi;
    private BookState mevcutDurum;

    public Book(String kitapAdi) {
        this.kitapAdi = kitapAdi;
        this.mevcutDurum = new OnTheShelf();  // başlangıç durumu = rafta
    }

    public void setState(BookState state) {
        this.mevcutDurum = state;
        mevcutDurum.state(this);
    }

    public String getKitapAdi() {
        return kitapAdi;
    }

}
