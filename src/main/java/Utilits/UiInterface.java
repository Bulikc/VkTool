package Utilits;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;

public class UiInterface {
    private static ProgressBar thisProgress;
    private static Label thisTextCount;
    private static Label thisTextFinishItems;
    private static TextField thisTextPach;
    private static float part;
    private static int thisCount;
    private static int finishedCount=0;
    private static String thisPach;



    public static void initialize(ProgressBar progress,Label textCount,Label textFinishItems,TextField textPach,String pach,int count){
        thisProgress=progress;
        thisTextCount=textCount;
        thisTextFinishItems=textFinishItems;
        thisTextPach=textPach;
        thisCount=count;
        part=1.0f/thisCount;
        thisTextCount.setText("Сохранит " + thisCount + " товаров");
        thisPach=pach;
        thisTextPach.setText(thisPach);
    }


    public synchronized  static void update(){
        Platform.runLater(new Runnable() {
            public void run() {

                if(thisProgress!=null) {
                    thisProgress.setProgress(thisProgress.getProgress() + part);

                    finishedCount++;
                    thisTextFinishItems.setText("Прогресс " + finishedCount + " из " + thisCount);

                }
            }
        });

    }






}
