package object;
import interfaces.*;

import java.util.ArrayList;


public class Products<T extends  ObjectInterface> implements VkObjectInterface<T> {

    private ArrayList<T> list;

    public Products() {
        this.list = new ArrayList<T>();
    }

    @Override
    public ArrayList<T> getList() {
        return list;
    }

    @Override
    public void add(T object) {
    list.add(object);
    }

    @Override
    public void remove(T object) {
    list.remove(object);
    }

    @Override
    public T getItem(int i) {
        return list.get(i);
    }

    @Override
    public ArrayList<String> getPhotos() {
        ArrayList<String> photo=new ArrayList<>();
        for(int i=0;i<list.size();i++){
            ArrayList<String> listPhoto=list.get(i).getPhoto();
                photo.addAll(listPhoto);
        }
        return photo;
    }

    @Override
    public ArrayList<String> getDescriptions() {
        ArrayList<String> descriptions=new ArrayList<>();
        for(int i=0;i<list.size();i++)
            descriptions.add(list.get(i).getDescription());
        return descriptions;
    }

    @Override
    public ArrayList<String> getPrices() {
        ArrayList<String> prices=new ArrayList<>();
        for(int i=0;i<list.size();i++)
            prices.add(list.get(i).getPrice());
        return prices;
    }
}
