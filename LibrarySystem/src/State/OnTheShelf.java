package State;

public class OnTheShelf implements BookState {
    @Override
    public void state(Book book) {
        System.out.println(book.getKitapAdi()+" rafta ");
    }
}
