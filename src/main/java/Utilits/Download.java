package Utilits;

import interfaces.ObjectInterface;
import javafx.scene.control.ProgressBar;
import object.ProductItem;
import object.Products;
import org.asynchttpclient.uri.Uri;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class Download<T extends ObjectInterface> implements Runnable {


    private void download(String uri,String path){
        BufferedInputStream bfin=null;
        FileOutputStream bfout=null;
        try {
            File file=new File(path);
            URLConnection ur=new URL(uri).openConnection();
            bfin=new BufferedInputStream(ur.getInputStream());
            bfout=new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int c;
            while((c=bfin.read(buffer,0,1024))!=0)
                bfout.write(buffer,0,c);

        } catch (IndexOutOfBoundsException e){} catch (Exception e) {
            System.out.println("error "+e.getMessage());

        } finally {
            try {
                bfin.close();
                bfout.close();


            } catch (IOException e) {
                System.out.println("error "+e.getMessage());
            }

        }

    }


    private T item;
    private String path;
    ProgressBar progressBar;

    public void download(T object,String patch,int num){
        File file=null;
        String name=object.getName().replace("//"," ");
        name=name.replace("?"," ");
        name=name.replace(":"," ");
        name=name.replace("/"," ");
        name=name.replace("*"," ");
        name=name.replace("\""," ");
        name=name.replace("<"," ");
        name=name.replace(">"," ");

        try{
            name+=" ["+num+"]";
         file=new File(patch+"\\"+name);
         file.mkdir();

        }
        catch (Exception e){
            System.out.println("error "+e.getMessage());
        }

        ArrayList<String> photos=object.getPhoto();
        for(int i=0;i<photos.size();i++){
        download(photos.get(i),file.getAbsolutePath()+"\\"+i+".jpg");}

        File fileText=new File(patch+"\\"+name+"\\"+"text.txt");
       // System.out.println(object.getDescription());
        try {
            fileText.createNewFile();
           // FileOutputStream fileOutputStream=new FileOutputStream(fileText);
            FileWriter fwText=new FileWriter(fileText);
            fwText.write(object.getDescription()+"/n цена "+object.getPrice());
            fwText.close();
            UiInterface.update();
            System.out.println("поток "+name + " сдох "+incProgress);
            progressBar.setProgress(incProgress+progressBar.getProgress());
        } catch (IOException e) {
            System.out.println("error "+e.getMessage());
        }

    }
    float incProgress;
    public Download(T object,String path,int num,ProgressBar pr,float incrProgress) {
        this.item=object;
        this.path=path;
        this.num=num;
        this.progressBar=pr;
        this.incProgress=incrProgress;
    }

    int num;

    @Override
    public void run() {

        download(item,path,num);


    }
}
