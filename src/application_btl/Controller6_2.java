package application_btl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Controller6_2 {
	@FXML
    private Label linkQuiz;

    @FXML
    private Label titleQuiz;
    
    @FXML
    private VBox vbox;
    
    @FXML
    private AnchorPane popup;
    
    @FXML
    private Label label1;

    @FXML
    private Label label2;
    
    public void initialize() throws IOException {
    	int countQuestion = 0;
    	linkQuiz.setText(Controller1.labelExam);
    	titleQuiz.setText(Controller1.labelExam);
    	Path path = Paths.get("src/data/PrepareForQuiz.txt");
    	if(Files.size(path) != 0) {
    		try (Stream<String> lines = Files.lines(path)) {
            	String[] questions = lines.map(String::trim).collect(Collectors.joining("\n")).split("\n\n");
            	countQuestion = questions.length;
            	for (int i = 0; i < questions.length; i++) {
            		FXMLLoader loader = new FXMLLoader(getClass().getResource("QuestionQuiz.fxml"));
                    AnchorPane newPane = loader.load();
                 // Lấy Label cũ từ AnchorPane
                    Label STT = (Label) newPane.getChildren().get(0); // Thay đổi chỉ số nếu cần
                    Label Content = (Label) newPane.getChildren().get(1); // Thay đổi chỉ số nếu cần
                    ImageView zoomQuestion = (ImageView) newPane.getChildren().get(3); // Thay đổi chỉ số nếu cần
                    ImageView deleteQuestion = (ImageView) newPane.getChildren().get(4); // Thay đổi chỉ số nếu cần
                    
                    STT.setText(Integer.toString(i+1));
                    Content.setText(questions[i].replace("\n", "").trim());
                    deleteQuestion.setOnMouseClicked(event -> {
                    	Node component = (Node) event.getSource();
                    	AnchorPane selectedAnchorPane = (AnchorPane) component.getParent();
                    	int index = vbox.getChildren().indexOf(selectedAnchorPane);
                    	if(index > 0) {
                    		try (Stream<String> lines1 = Files.lines(path)){
                    			String[] questions1 = lines1.map(String::trim).collect(Collectors.joining("\n")).split("\n\n");
                    		    String content = new String(Files.readAllBytes(path));
                    		    String replace = "\n" + questions1[index] + "\n";
                    		    String modifiedContent = content.replace(replace, ""); // replace đoạn muốn xóa
                    		    Files.write(path, modifiedContent.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
                    		} catch (IOException e1) {
                    		    e1.printStackTrace();
                    		}
                    	} else {
                    		try (Stream<String> lines1 = Files.lines(path)){
                    			String[] questions1 = lines1.map(String::trim).collect(Collectors.joining("\n")).split("\n\n");
                    		    String content = new String(Files.readAllBytes(path));
                    		    String replace = questions1[index] + "\n";
                    		    String modifiedContent = content.replace(replace, ""); // replace đoạn muốn xóa
                    		    if (!modifiedContent.isEmpty() && modifiedContent.startsWith("\n")) {
                    		    	modifiedContent = modifiedContent.replaceFirst("\n", "");
                    		    }
                    		    Files.write(path, modifiedContent.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
                    		} catch (IOException e1) {
                    		    e1.printStackTrace();
                    		}
                    	}
                    	vbox.getChildren().clear();
                    	try {
							initialize();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                    });
                    
                    zoomQuestion.setOnMouseClicked(event -> {
                    	Node component = (Node) event.getSource();
                    	AnchorPane selectedAnchorPane = (AnchorPane) component.getParent();
                    	int index = vbox.getChildren().indexOf(selectedAnchorPane);
                    	
                    	Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    	alert.setTitle("Chi tiết câu hỏi");
                    	alert.setHeaderText(null);
                    	alert.setContentText(questions[index]);
                    	alert.showAndWait();
                    });
                    vbox.getChildren().add(newPane);
                }
            }catch (IOException e1) {
        	    e1.printStackTrace();
        	}
    	}
    	
    	label1.setText("Question: " + Integer.toString(countQuestion) + " | This quiz is open");
    	label2.setText("Total of mark: " + Integer.toString(countQuestion) + ".00");
    }
    
    public void changeSceneAddQuestionBank(MouseEvent e) throws IOException {
        Stage stage = (Stage)((Node) e.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Sample6_3.fxml"));
        Parent AddQuestionBank = loader.load();
        Scene scene = new Scene(AddQuestionBank);
        scene.getStylesheets().add(getClass().getResource("application6_3.css").toExternalForm());
        stage.setScene(scene);
    }
    
    public void popup(MouseEvent e){
		if(popup.isVisible() == false)
			popup.setVisible(true);
		else
			popup.setVisible(false);
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
