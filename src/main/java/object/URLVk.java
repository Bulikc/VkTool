package object;

public class URLVk {
    private String uri;

    public URLVk(String uri) {
        this.uri = uri;
    }

    public boolean isGroup(){
       return false;
    }

    public boolean isMarker(){
        return false;
    }

    public Market getMarket(){
        try{
        Market market=new Market();
        int start=0,end=0;
        String editUri=uri;
        start=uri.indexOf("market")+7;
        end=uri.indexOf("?");
        market.setIdMarket(Integer.parseInt(uri.substring(start,end)));
        start=uri.indexOf("album_")+6;
        market.setIdAlbum(Integer.parseInt(uri.substring(start)));
        return market;} catch (Exception e){
            return null;
        }
    }




}
