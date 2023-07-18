package application_btl;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ExamViewController {
	@FXML
    private Label examNameLabel;
	
	public void setExam(Exam exam) {
        examNameLabel.setText(exam.getName());
//        if (!exam.isOpen()) {
//            examNameLabel.setDisable(true);
//            examNameLabel.setStyle("-fx-opacity: 0.5;");
//        } else {
//            examNameLabel.setDisable(false);
//            examNameLabel.setStyle("-fx-opacity: 1;");
//        }
    }
}
