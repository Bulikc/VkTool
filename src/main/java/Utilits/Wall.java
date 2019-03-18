package Utilits;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.objects.groups.GroupFull;
import com.vk.api.sdk.objects.market.MarketItemFull;
import com.vk.api.sdk.objects.market.responses.GetExtendedResponse;
import com.vk.api.sdk.objects.photos.Photo;
import com.vk.api.sdk.objects.wall.WallPostFull;
import com.vk.api.sdk.objects.wall.responses.GetResponse;
import com.vk.api.sdk.queries.wall.WallGetFilter;
import object.ProductItem;
import object.Products;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

public class Wall {

    private UserActor actor;



    public static ArrayList<WallPostFull> getAll(VkApiClient vk, UserActor actor, String groupName, int count){

        ArrayList<WallPostFull> listGroup=new ArrayList<>();
        try {
            List<GroupFull> getResponse = vk.groups().getById(actor).groupId(groupName).execute();
            int idGroup=getResponse.get(0).getId();
            System.out.println(idGroup);
           // GetResponse getResponseWall = vk.wall().get(actor).ownerId(-1*idGroup).count(count).filter(WallGetFilter.ALL).execute();
            int size=0;
            int itterations=0;
            int offcet=0;
            if(count<100){
                size=count;
            itterations=0;
            } else{
                    itterations=count/100;
                    size=count%100;}

            System.out.println(size);
            for(int i=0;i<itterations;i++,offcet+=100){
                Thread.sleep(100);
                GetResponse getResponseWall = vk.wall().get(actor).ownerId(-1*idGroup).offset(offcet).count(100).filter(WallGetFilter.ALL).execute();
                listGroup.addAll(getResponseWall.getItems());

            }

                GetResponse getResponseWall = vk.wall().get(actor).ownerId(-1*idGroup).offset(offcet).count(size).filter(WallGetFilter.ALL).execute();

                listGroup.addAll(getResponseWall.getItems());

            return listGroup;


        } catch (ApiException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Products<ProductItem> getProducts(ArrayList<WallPostFull> list){
        Products<ProductItem> products=new Products<ProductItem>();
        int name=0;
        for(WallPostFull mr: list)
        {name++;
            List<Photo> photoList=new ArrayList<>();
            if(mr.getAttachments()!=null)
            for(int i=0;i<mr.getAttachments().size();i++){
                photoList.add(mr.getAttachments().get(i).getPhoto());

            }
             products.add(new ProductItem(photoList,mr.getText(),"",name+""));
        }
        return products;
    }

}
