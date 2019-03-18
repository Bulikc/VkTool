package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.SQLOutput;

public class Main extends Application {

    private static Stage st;

    @Override
    public void start(Stage primaryStage) throws Exception{
        System.out.println(getClass().getClassLoader().getResource("FXML/sample.fxml"));
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("FXML/sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 330, 425));

        st=primaryStage;
        primaryStage.show();
        System.out.println(method());

    }
    static String method (){
        try {
            throw new Exception();
        } catch (Exception e) {
            return "catch";
        } finally {
            return "finally";
        }
    }

    static public Stage getPrimaryStage() {
        return st;
    }
    public static void main(String[] args) {
        launch(args);
    }
}
