package sample;

import javafx.application.Platform;
import sample.Controllers.MainController;
import sample.Data.Controller;
import sample.Data.Window;

public class TimeThread extends Thread{
    public int noActiveDelay = 0;
    private MainController mainController =null;
    private Controller controller = null;
    @Override
    public void run() {

        do {
            if (!Thread.interrupted()) {
                if(noActiveDelay>Settings.TimeOut*60){
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            if (!Thread.interrupted()) {
                                if(controller!=null) {
                                    controller.getStage().close();
                                }
                                mainController.logOut();
                            }
                        }
                    });
                    return;
                }
            }
            else return;
            try {
                Thread.sleep(1000);
                noActiveDelay++;
                System.out.println("DElay: "+noActiveDelay);
            } catch (InterruptedException e) {
                return;
            }
        }
        while (true);
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }
    public void setNoActiveDelay(int noActiveDelay) {
        this.noActiveDelay = noActiveDelay;
    }

}
