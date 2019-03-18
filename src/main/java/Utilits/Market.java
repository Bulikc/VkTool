package Utilits;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.market.MarketItemFull;
import com.vk.api.sdk.objects.market.responses.GetExtendedResponse;
import object.ProductItem;
import object.Products;

import java.util.ArrayList;

public class Market {

    private UserActor actor;



    public static ArrayList<MarketItemFull> getAllMarket(VkApiClient vk,UserActor actor, int idmarket, int idAlbom){

        ArrayList<MarketItemFull> listMarket=new ArrayList<>();
        try {
            GetExtendedResponse getResponse = vk.market()
                    .getExtended(actor,-1*idmarket).albumId(idAlbom).execute();

            int count=getResponse.getCount();
            int offcet=0;
            for(int i=0;i<count/100;i++,offcet+=100){

                 getResponse = vk.market()
                        .getExtended(actor,-1*idmarket).albumId(idAlbom).count(100).offset(offcet).execute();
                listMarket.addAll(getResponse.getItems());

            }
            getResponse = vk.market()
                    .getExtended(actor,-1*idmarket).albumId(idAlbom).count(count%100).offset(offcet).execute();
            listMarket.addAll(getResponse.getItems());

            return listMarket;


        } catch (ApiException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Products<ProductItem> getProducts(ArrayList<MarketItemFull> list){
        Products<ProductItem> products=new Products<ProductItem>();
        for(MarketItemFull mr: list)
        {
            products.add(new ProductItem(mr.getPhotos(),mr.getDescription(),mr.getPrice().getText(),mr.getTitle()));
        }
    return products;
    }


}
