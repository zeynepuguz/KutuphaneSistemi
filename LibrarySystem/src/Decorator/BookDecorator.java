package Decorator;

public abstract class BookDecorator implements BookComponent {
    protected BookComponent bookComponent;

    public BookDecorator(BookComponent bookComponent) {
        this.bookComponent = bookComponent;
    }

    @Override
    public void display() {
        bookComponent.display();
    }
}
