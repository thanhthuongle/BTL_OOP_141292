package application_btl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Controller1 {
	@FXML
	private AnchorPane popup;
	@FXML
	private VBox vbox;
	
	public static String labelExam;

	public void initialize() throws IOException {
		vbox.setSpacing(15); // Cài đặt khoảng cách giữa các thành phần là 10 pixel
        List<Exam> exams = Files.lines(Paths.get("src/data/ExamList.txt"))
            .map(Exam::fromString) // chuyển đổi mỗi dòng thành một đối tượng Exam
            .collect(Collectors.toList());

        for (Exam exam : exams) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ExamView.fxml"));
            Label label = loader.load();
            ExamViewController controller = loader.getController();
            controller.setExam(exam);
//            if (!exam.isOpen()) {
//                label.setDisable(true);
//                label.setStyle("-fx-opacity: 0.5;");
//            } else {
//                label.setDisable(false);
//                label.setStyle("-fx-opacity: 1;");
//            }
            vbox.getChildren().add(label);
        }
        
        for (Node child : vbox.getChildren()) {
        	Path filePath = Paths.get("src/data/prepareForQuiz.txt");
            child.setOnMouseClicked(event -> {
                Label label = (Label) child;
                labelExam = label.getText();
                // chuyển scene
                Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("Sample6_1.fxml"));
                Parent QuizParent;
				try {
					Files.newBufferedWriter(filePath, StandardOpenOption.TRUNCATE_EXISTING);
					QuizParent = loader.load();
					Scene scene = new Scene(QuizParent);
	                scene.getStylesheets().add(getClass().getResource("application6_1.css").toExternalForm());
	                stage.setScene(scene);
				} catch (IOException e) {
					e.printStackTrace();
				}
            });
        }
    }
	
	public void popup(MouseEvent e){
		if(popup.isVisible() == false)
			popup.setVisible(true);
		else
			popup.setVisible(false);
	}
	
	public void changeSceneQuestionbank(MouseEvent e) throws IOException {
        Stage stage = (Stage)((Node) e.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Sample2.fxml"));
        Parent QuestionbankParent = loader.load();
        Scene scene = new Scene(QuestionbankParent);
        scene.getStylesheets().add(getClass().getResource("application2.css").toExternalForm());
        stage.setScene(scene);
    }
	
	public void changeSceneAddNewQuiz(MouseEvent e) throws IOException {
        Stage stage = (Stage)((Node) e.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Sample5.fxml"));
        Parent AddNewQuizParent = loader.load();
        Scene scene = new Scene(AddNewQuizParent);
        scene.getStylesheets().add(getClass().getResource("application5.css").toExternalForm());
        stage.setScene(scene);
    }
}
