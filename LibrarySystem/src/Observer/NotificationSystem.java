package Observer;

public class NotificationSystem implements Observer {
@Override
public void update(String kitapAdi, String kitapDurumu) {
    System.out.println("Bildirim: " + kitapAdi + " kitabının yeni durumu: " + kitapDurumu);
}
}
