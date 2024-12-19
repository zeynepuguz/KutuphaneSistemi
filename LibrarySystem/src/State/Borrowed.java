package State;

public class Borrowed implements BookState {
    @Override
    public void state(Book book) {
        System.out.println(book.getKitapAdi() + " ödünç alındı. ");
    }
}
