package sample.Controllers;

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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
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
import java.util.concurrent.*;

public class Controller {
    @FXML
    private TextField login;

    @FXML
    private RadioButton rdSingleMarketSave;

    @FXML
    private RadioButton rdAllMarketSave;

    @FXML
    private Button btnSaveAsGroup;

    @FXML
    private TextField uriMarket;

    @FXML
    private TextField txtPachGroup;
    @FXML
    private TextField uriGroup;
    @FXML
    private TextField txtPachMarket;




    @FXML
    private Label txtCountMarket;

    @FXML
    private Label txtProgressMarket;

    @FXML
    private WebView web;

    @FXML
    private TabPane tabPane;

    @FXML
    private Button btbStartMarket;

    @FXML
    private Button btnStopDownloadMarket;

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





        //webEngine.load(url);

        System.out.println(webEngine.getLocation());
    }


    public void Autorization(String code){

        try {
            tabPane.getSelectionModel().selectNext();
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

        String code=webEngine.getLocation();
        int start=code.indexOf("code=");
        if(start>-1){
        start+=5;
        code=code.substring(start);
        int end=code.indexOf("&");
        code=code.substring(0,end);
        return code;}
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean chekStart=false;
    private ExecutorService pool;

    public void actionButtons2(ActionEvent actionEvent) throws ClientException, ApiException {
        Node node=(Node)actionEvent.getSource();
        Object object=actionEvent.getSource();
        String uri;
        URLVk vkUri=null;
        System.out.println("eefe");
        if(!(object instanceof Button))
            return;
       // Button button=(Button)actionEvent.getSource();
        Button button=null;
        // System.out.println(button.getId());
       // ExecutorService pool = Executors.newFixedThreadPool(10);

        switch (button.getId()){
            case "btnSaveAsGroup":

                uri=uriGroup.getText();
               // System.out.println(uri);
                if(uri.length()>5){
                    Stage st= Main.getPrimaryStage();
                    DirectoryChooser dr=new DirectoryChooser();
                    File file=dr.showDialog(st);
                    urlFile="";
                    if (file != null) {
                        urlFile=file.getAbsolutePath();
                        chekStart=true;
                        txtPachGroup.setText(urlFile);

                    }}


                break;


            case "btbStartMarket":
                SaveStartDownload(chekStart,vk,actor,new URLVk(uriMarket.getText()),new Market());
                break;
            case "btnStopDownloadMarket":
                pool.shutdownNow();
                txtCountMarket.setText("Сохранит   товаров");
                txtProgressMarket.setText("Прогресс ");
                break;

            case "btnSaveAsMarket":
            uri=uriMarket.getText();
                System.out.println(uri);
            if(uri.length()>5){
                Stage st= Main.getPrimaryStage();
                DirectoryChooser dr=new DirectoryChooser();
                File file=dr.showDialog(st);
                 urlFile="";
                if (file != null) {
                    urlFile=file.getAbsolutePath();
                    chekStart=true;
                    txtPachMarket.setText(urlFile);

                }}
                break;
        }
    }

    private void SaveStartDownload(boolean chekStart,VkApiClient vk,UserActor actor,URLVk vkUri, Object type) {
        //
        int count;
        float procent;
        if(chekStart){
         pool = Executors.newFixedThreadPool(10);
        boolean chekTypeDowloand=rdSingleMarketSave.isSelected();
            Products<ProductItem> p=null;
        if(type.getClass()==new Market().getClass()) {
            System.out.println(vkUri.getUri()+" "+vkUri.getEntry().getIdAlbum()+" ");
           p = Market.getProducts(Market.getAllMarket(vk, actor, vkUri.getEntry()));
        }
        else {
           p=Wall.getProducts(Wall.getAll(vk, actor, vkUri.getEntry()));

        }
        count=p.getList().size();

        UiInterface.initialize(progress,txtCountMarket,txtProgressMarket,count);
        procent=1.0f/count;
        System.out.println(txtCountMarket.getText());
        for(int i=0;i<count;i++) {
            pool.execute(new Thread(new Download<>(p.getItem(i),urlFile,i,chekTypeDowloand),i+""));
            System.out.println("поток создан "+i);
        }}
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
