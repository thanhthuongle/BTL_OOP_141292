package application_btl;

import java.awt.Dimension;
import java.awt.Toolkit;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try{
        	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        	double screenWidth = screenSize.getWidth();
            double screenHeight = screenSize.getHeight() - 40;
            Parent root = FXMLLoader.load(getClass().getResource("Sample1.fxml"));
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("application1.css").toExternalForm());
            primaryStage.setWidth(screenWidth);
            primaryStage.setHeight(screenHeight);
            primaryStage.setScene(scene);
            primaryStage.show();
        }catch (Exception e){
           e.printStackTrace();
        }
    }
    
}
