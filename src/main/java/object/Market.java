package object;

public class Market {
    private int idMarket;
    private int idAlbum;

    public Market(int idMarket, int idAlbum) {
        this.idMarket = idMarket;
        this.idAlbum = idAlbum;
    }

    public Market() {
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
