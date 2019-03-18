package interfaces;

import java.util.ArrayList;

public interface VkObjectInterface<T> {
    public ArrayList<T> getList();
    public void add(T object);
    public void remove(T object);
    public T getItem(int i);

    public ArrayList<String> getPhotos();
    public ArrayList<String> getDescriptions();
    public ArrayList<String> getPrices();
}
