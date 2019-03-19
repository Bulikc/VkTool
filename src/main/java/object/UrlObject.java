package object;

public class UrlObject {
    private int idMarket;
    private int idAlbum;

    public UrlObject(int idMarket, int idAlbum) {
        this.idMarket = idMarket;
        this.idAlbum = idAlbum;
    }

    public UrlObject() {
    }

    public int getIdMarket() {
        return idMarket;
    }

    public void setIdMarket(int idMarket) {
        this.idMarket = idMarket;
    }

    public int getIdAlbum() {
        return idAlbum;
    }

    public void setIdAlbum(int idAlbum) {
        this.idAlbum = idAlbum;
    }
}
