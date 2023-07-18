package application_btl;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class Controller6_1 {
	@FXML
    private Label linkQuiz;

    @FXML
    private Label titleQuiz;
    
    @FXML
    private Label timeLimit;
	
    public void initialize() throws IOException {
    	linkQuiz.setText(Controller1.labelExam);
    	titleQuiz.setText(Controller1.labelExam);
    	timeLimit.setText("Time limit: " + Exam.getLimitTimeByName(Controller1.labelExam) + " huors");
    }
    
    public void changeSceneEditingQuiz(MouseEvent e) throws IOException {
        Stage stage = (Stage)((Node) e.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Sample6_2.fxml"));
        Parent EditingQuiz = loader.load();
        Scene scene = new Scene(EditingQuiz);
        scene.getStylesheets().add(getClass().getResource("application6_2.css").toExternalForm());
        stage.setScene(scene);
    }
    
    public void changeSceneToTHICUOIKY(MouseEvent e) throws IOException {
        Stage stage = (Stage)((Node) e.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Sample1.fxml"));
        Parent AddNewQuizParent = loader.load();
        Scene scene = new Scene(AddNewQuizParent);
        scene.getStylesheets().add(getClass().getResource("application1.css").toExternalForm());
        stage.setScene(scene);
    }
    
    
}
