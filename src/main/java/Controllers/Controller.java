package Controllers;

import Utilits.Download;
import Utilits.Market;
import Utilits.UiInterface;
import Utilits.Wall;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.UserAuthResponse;
import com.vk.api.sdk.objects.wall.responses.GetResponse;
import com.vk.api.sdk.queries.wall.WallGetFilter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import object.ProductItem;
import object.Products;
import object.URLVk;
import sample.Main;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.*;

public class Controller {
    @FXML
    private TextField login;

    @FXML
    private TextField uriMarket;

    @FXML
    private TextField txtPachMarket;

    @FXML
    private TextField txtPachGroup;

    @FXML
    private  TextField uriGroup;
    @FXML
    private Label txtCount;

    @FXML
    private Label txtProgress;

    @FXML
    private WebView web;

    @FXML
    private Button btbStartMarket;

    private static String PERMISSIONS_FRIENDS = "friends";
    private static String PERMISSIONS_NOTES = "notes";
    private static String PERMISSIONS_STATUS = "status";
    private static String PERMISSIONS_WALL = "wall";
    private static String PERMISSIONS_GROUPS = "groups";
    private static String PERMISSIONS_MESSAGES = "messages";
    private static String PERMISSIONS_STATS = "stats";
    private static String PERMISSIONS_OFFLINE = "offline";
    int APP_ID = 6888506;
    final FileChooser fileChooser = new FileChooser();
    String CLIENT_SECRET = "esyLl7ysqHJW776z7WyO";
    String REDIRECT_URI = "https://oauth.vk.com/blank.html";
    String code = "";

    private   WebEngine  webEngine;
    VkApiClient vk=null;
    UserActor actor=null;
    private static final String PERMISSIONS = "status,wall,offline,market";
    @FXML
    private ProgressBar progress;

    private String urlFile;
    public void initialize(){
       webEngine = web.getEngine();
        String url = "https://oauth.vk.com/authorize?client_id=" + APP_ID + "&display=mobile&redirect_uri=" + REDIRECT_URI + "&scope="+PERMISSIONS+"&response_type=code&v=5.92&state=123456";
        webEngine.load(url);
        webEngine.getLoadWorker().stateProperty().addListener(
                new ChangeListener<State>() {
                    @Override public void changed(ObservableValue ov, State oldState, State newState) {
                        //System.out.println("called"+newState.name());

                        if(newState.name().equals("SUCCEEDED")){
                            String code=testCorrectCode();
                           if( code!=null)
                               Autorization(code);
                        }

                    }
                });





        webEngine.load(url);

        System.out.println(webEngine.getLocation());
    }


    public void Autorization(String code){

        try {
            TransportClient transportClient = HttpTransportClient.getInstance();
            vk = new VkApiClient(transportClient);
            UserAuthResponse authResponse = null;


            authResponse = vk.oauth()
                    .userAuthorizationCodeFlow(APP_ID, CLIENT_SECRET, REDIRECT_URI, code)
                    .execute();

            actor = new UserActor(authResponse.getUserId(), authResponse.getAccessToken());




        } catch (ApiException e) {
            vk=null;
            actor=null;
            System.out.println("error "+e.getMessage());
        } catch (ClientException e) {
            vk=null;
            actor=null;
            System.out.println("error "+e.getMessage());
        }

    }





    public String testCorrectCode(){
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("try code");
        String code=webEngine.getLocation();
       // System.out.println(code);
        int start=code.indexOf("code=");
        if(start>-1){
        start+=5;
        code=code.substring(start);
        int end=code.indexOf("&");
        code=code.substring(0,end);
        System.out.println(code);
        return code;}
        return null;
       // return true;
    }

    private  boolean chekStart=false;
    private   URLVk vkUri=null;
    public void actionButtons(ActionEvent actionEvent) throws ClientException, ApiException {
        Node node=(Node)actionEvent.getSource();
        Object object=actionEvent.getSource();
        String uri;


        if(!(object instanceof Button))
            return;
        Button button=(Button)actionEvent.getSource();
        System.out.println(button.getId());

        switch (button.getId()){
            case "btnSaveAsGroup":
                uri=uriGroup.getText();
               // List<GroupFull> getResponse = vk.groups().getById(actor).groupId("canada_in").execute();
                //int idGroup=getResponse.get(0).getId();

                /*GetResponse getResponse = vk.wall().get(actor).ownerId(-115151363).count(3).filter(WallGetFilter.ALL).execute();
                System.out.println(getResponse.getItems().get(2).getAttachments().get(0).getPhoto().getPhoto2560());
                System.out.println(getResponse.getItems().get(2).getAttachments().get(0).getPhoto().getPhoto1280());
                System.out.println(getResponse.getItems().get(2).getAttachments().get(0).getPhoto().getPhoto807());
                System.out.println(getResponse.getItems().get(2).getAttachments().get(0).getPhoto());
               // System.out.println(getResponse.get);
*/
                Products<ProductItem> pp=Wall.getProducts(Wall.getAll(vk, actor, "canada_in", 1000));
                ArrayList<String> list=pp.getPhotos();
                for (String s:list ) {
                    System.out.println(s);
                }
                ExecutorService pool = Executors.newFixedThreadPool(10);

                int count=pp.getList().size();
                UiInterface.initialize(progress,txtCount,txtProgress,txtPachMarket,urlFile,count);
                float procent=1.0f/count;
                System.out.println(txtCount.getText());
                for(int i=0;i<count;i++) {

                    pool.execute(new Thread(new Download<>(pp.getItem(i),"F:\\2",i,progress,procent),i+""));
                    System.out.println("поток создан "+i);
                }


                if(uri.length()>5){
                    /*Stage st= Main.getPrimaryStage();
                    DirectoryChooser dr=new DirectoryChooser();
                    File file=dr.showDialog(st);
                    urlFile="";
                    if (file != null) {
                        urlFile=file.getAbsolutePath();
                        */vkUri=new URLVk(uri);
                        chekStart=true;

                    }//}
                break;


            case "btbStartMarket":
                System.out.println("tygdsgrtr"+chekStart);
                if(chekStart){

                 pool = Executors.newFixedThreadPool(10);

                Products<ProductItem> p=Market.getProducts(Market.getAllMarket(vk,actor,vkUri.getMarket().getIdMarket(),vkUri.getMarket().getIdAlbum()));
                 count=p.getList().size();
                UiInterface.initialize(progress,txtCount,txtProgress,txtPachMarket,urlFile,count);
                 procent=1.0f/count;
                System.out.println(txtCount.getText());
                for(int i=0;i<count;i++) {

                    pool.execute(new Thread(new Download<>(p.getItem(i),urlFile,i,progress,procent),i+""));
                    System.out.println("поток создан "+i);
                } }
               break;

            case "btnSaveAsMarket":
            uri=uriMarket.getText();
            if(uri.length()>5){
                Stage st= Main.getPrimaryStage();
                DirectoryChooser dr=new DirectoryChooser();
                File file=dr.showDialog(st);
                 urlFile="";
                if (file != null) {
                    urlFile=file.getAbsolutePath();
                    vkUri=new URLVk(uri);
                    chekStart=true;

                }}
                break;
        }
    }

    public static String getPermissions(){
        String s = "";
        s = s + PERMISSIONS_FRIENDS + ",";
        s = s + PERMISSIONS_NOTES + ",";
        s = s + PERMISSIONS_STATUS + ",";
        s = s + PERMISSIONS_WALL + ",";
        s = s + PERMISSIONS_GROUPS + ",";
        s = s + PERMISSIONS_MESSAGES + ",";
        s = s + PERMISSIONS_STATS + ",";
        s = s + PERMISSIONS_OFFLINE;
        System.out.println("Request authorization: " + s);
        return s;
    }
}
