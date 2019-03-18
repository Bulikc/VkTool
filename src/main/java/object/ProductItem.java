package object;

import interfaces.ObjectInterface;
import com.vk.api.sdk.objects.photos.Photo;

import java.util.ArrayList;
import java.util.List;

public class ProductItem implements ObjectInterface {
    private String[] photo;
    private String description, price, name;
    private ArrayList<Photo> photolist;

    public ProductItem(List<Photo> photoList, String description, String price, String name) {
        photo = new String[6];
        this.photolist= (ArrayList<Photo>) photoList;
        this.description = description;
        this.price = price;
        this.name = name;
    }


    private String bestPhoto(Photo photo){
        if(photo!=null) {
            if (photo.getPhoto2560() != null)
                return photo.getPhoto2560();
            else if (photo.getPhoto1280() != null)
                return photo.getPhoto1280();
            else if (photo.getPhoto807() != null)
                return photo.getPhoto807();
            else if (photo.getPhoto604() != null)
                return photo.getPhoto604();
            else if (photo.getPhoto130() != null)
                return photo.getPhoto130();
        }
        return null;
    }

    public ArrayList<String> getPhoto() {

        ArrayList<String> bestPhotoLisr=new ArrayList<>();
        for (int i = 0; i < photolist.size(); i++) {
           if(bestPhoto(photolist.get(i))!=null)
               bestPhotoLisr.add(bestPhoto(photolist.get(i)));
        }
        return bestPhotoLisr;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }


    public String getName() {
        return name;
    }}

