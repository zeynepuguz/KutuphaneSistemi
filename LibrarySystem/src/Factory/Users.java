package Factory;

public abstract class Users {
    private int id;
    private String ad;
    private String soyad;
    private String email;
    private String parola;
    private String yetki;

    public Users(int id, String ad, String soyad, String email, String parola, String yetki) {
        this.id = id;
        this.ad = ad;
        this.soyad = soyad;
        this.email = email;
        this.parola = parola;
        this.yetki = yetki;
    }

    public int getId() {
        return id;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getSoyad() {
        return soyad;
    }
    public abstract void login();
}
