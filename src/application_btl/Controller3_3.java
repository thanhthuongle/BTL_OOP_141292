package application_btl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Controller3_3 {
	@FXML
	private ComboBox<String> combobox1;
	
	@FXML
	private ComboBox<String> combobox2;
	
	@FXML
    private VBox vbox;
	
	@FXML
    private Label labelCategory;

    @FXML
    private TextField questionName;

    @FXML
    private TextArea questionText;
    
    @FXML
    private TextField defaultMark;
    
    @FXML
    private TextField viewTooltip;
	
	@FXML
    private Tooltip noticeError;
    
    ObservableList<String> list = FXCollections.observableArrayList("100%", "90%", "83.33333%", "80%", "75%", "70%", "66.66667%", "60%", "50%", "40%", "33.33333%", "30%", "25%", "20%", "16.66667%", "14.28571%", "12.5%", "10%", "5%", "-5%", "-10%", "-12.5%", "-14.28571%", "-16.66667%", "-20%", "-25%", "-30%", "-33.33333%", "-40%", "-50%", "-60%", "-66.66667%", "-70%", "-75%", "-80%", "-83.33333%");
    String questionWillChange = Controller2.questionNeedChange;
    public void initialize() throws IOException {
    	String answerOf3_3 = Controller2.answerto3_3;
    	labelCategory.setText("  " + Controller2.categoryTo3_3);
    	questionName.setText(Controller2.questionNameto3_3);
    	questionText.setText(Controller2.questionTextto3_3);
    	String[] lines = Controller2.choiceto3_3.split("\n");
    	int numberOfLines = lines.length;
    	while (numberOfLines > 2) {
    		ActionEvent event = new ActionEvent();
    		handleButtonAction(event);
    		numberOfLines -= 3;
    		count += 3;
    	}
    	int checkmark;
    	String[] part1 = Controller2.answerto3_3.split("\\:");
    	if(part1[0].trim() == Controller2.answerto3_3.trim()) {
    		defaultMark.setText("1");
    		String[] part2 = Controller2.answerto3_3.split("\\:", 2);
    		char ch = part2[1].trim().charAt(0);
    		checkmark = (int) ch - 65;
    		AnchorPane anchorPane = (AnchorPane) vbox.getChildren().get(checkmark);
    	    ComboBox<String> grade = (ComboBox) anchorPane.getChildren().get(2); // ComboBox là thành phần thứ 3 trong AnchorPane
    	    grade.setValue("100%");
    	}else {
    		String[] part2 = Controller2.answerto3_3.split("\\:");
    		int numberOfPart2 = part2.length;
    		defaultMark.setText(part2[numberOfPart2-1]);
    		answerOf3_3 = answerOf3_3.replaceAll("[^,\\.0-9]", "");
    		String[] part3 = answerOf3_3.split(",");
    		for (int i = 0; i < vbox.getChildren().size() && i < lines.length; i++) {
        	    AnchorPane anchorPane = (AnchorPane) vbox.getChildren().get(i);
        	    ComboBox<String> grade = (ComboBox) anchorPane.getChildren().get(2); // TextField là thành phần thứ 4 trong AnchorPane
        	    grade.setValue(part3[i]);
        	}
    	}
    	for (int i = 0; i < vbox.getChildren().size() && i < lines.length; i++) {
    	    AnchorPane anchorPane = (AnchorPane) vbox.getChildren().get(i);
    	    TextField choiceTextField = (TextField) anchorPane.getChildren().get(3); // TextField là thành phần thứ 4 trong AnchorPane
    	    String[] part = lines[i].split("\\.");
    	    choiceTextField.setText(part[1].trim());
    	}
    	
    	defaultMark.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                defaultMark.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    	
    	combobox1.setItems(list);
        combobox2.setItems(list);
    	
    }
    
	public void Save(ActionEvent e) throws IOException {
		int countChoice = 0;
    	// Lấy dữ liệu từ giao diện
    	if(questionName.getText().isBlank()) {
    		noticeError.setText("Question name không thể để trống");
    		noticeError.show(viewTooltip, viewTooltip.getLayoutX(), 250);
        	PauseTransition pause = new PauseTransition(Duration.seconds(5));
        	pause.setOnFinished(event -> noticeError.hide());
        	pause.play();
    	}else if(questionText.getText().isBlank()){
    		noticeError.setText("Question text không thể để trống");
    		noticeError.show(viewTooltip, viewTooltip.getLayoutX(), 250);
        	PauseTransition pause = new PauseTransition(Duration.seconds(5));
        	pause.setOnFinished(event -> noticeError.hide());
        	pause.play();
    	}else if(defaultMark.getText().isBlank()) {
    		noticeError.setText("Default mark không thể để trống");
    		noticeError.show(viewTooltip, viewTooltip.getLayoutX(), 250);
        	PauseTransition pause = new PauseTransition(Duration.seconds(5));
        	pause.setOnFinished(event -> noticeError.hide());
        	pause.play();
    	}else {
    		String questionTextPart1 = questionName.getText().replace("\n", "").trim(); // Lấy từ textfield phần 1 của câu hỏi
            String questionTextPart2 = questionText.getText().replace("\n", "").trim(); // Lấy từ textarea phần 2 của câu hỏi
            String questionText = questionTextPart1 + " " + questionTextPart2; // Ghép hai phần lại thành câu hỏi đầy đủ

        	Map<String, String> choices = new HashMap<>();
        	Map<String, Double> correctAnswers = new HashMap<>();

        	// Duyệt qua tất cả các AnchorPane trong VBox
        	int fakechoice = 0;
        	for (int i = 0; i < vbox.getChildren().size(); i++) {
        	    AnchorPane anchorPane = (AnchorPane) vbox.getChildren().get(i);
        	    TextField choiceTextField = (TextField) anchorPane.getChildren().get(3); // TextField là thành phần thứ 4 trong AnchorPane
        	    if(choiceTextField.getText().isBlank()) {
        	    	fakechoice += 1;
        	    	continue;         	    	
        	    }      	    
        	    ComboBox<String> answerComboBox = (ComboBox<String>) anchorPane.getChildren().get(2); // ComboBox là thành phần thứ ba trong AnchorPane

        	    String choiceKey = Character.toString((char) ('A' + i - fakechoice)); // Tạo key cho lựa chọn (A, B, C, ...)
        	    choices.put(choiceKey, choiceTextField.getText()); // Lấy từ textfield lựa chọn

        	 // Lấy từ combobox điểm số
        	    String comboBoxValue = answerComboBox.getValue();
        	    double score = 0;
        	    if (comboBoxValue != null) {
        	        score = Double.parseDouble(comboBoxValue.replace("%", "")) / 100;
        	        score *= Double.parseDouble(defaultMark.getText());
        	    }
        	    correctAnswers.put(choiceKey, score); 
        	    countChoice += 1;
        	}
        	
        	if(countChoice < 2) {
        		noticeError.setText("Số lựa chọn tối thiểu là 2");
        		noticeError.show(viewTooltip, viewTooltip.getLayoutX(), 250);
            	PauseTransition pause = new PauseTransition(Duration.seconds(5));
            	pause.setOnFinished(event -> noticeError.hide());
            	pause.play();
        	} else {
        		// thêm mức điểm của câu hỏi
        		correctAnswers.put("Mark", Double.parseDouble(defaultMark.getText()));
        		// Tạo AikenQuestion
            	AikenQuestion question = new AikenQuestion(questionText, choices, correctAnswers);

            	// Chuyển đổi AikenQuestion thành định dạng Aiken
            	String aikenText = question.toAikenFormat();

            	// Ghi đè Aiken mới vào file
            	Path path = Paths.get("src/data/"+labelCategory.getText().trim()+".txt");
            	// Đọc file và chia thành các câu hỏi
            	try (Stream<String> lines = Files.lines(path)) {
                	String[] questions = lines.map(String::trim).collect(Collectors.joining("\n")).split("\n\n");
                	for (int i = 0; i < questions.length; i++) {
                        if (questions[i].equals(questionWillChange)) {
                            questions[i] = aikenText;
                            // Ghi lại danh sách câu hỏi đã cập nhật vào file
                            Files.write(path, String.join("\n\n", questions).getBytes());
                            break;
                        }
                    }
                }catch (IOException e1) {
            	    e1.printStackTrace();
            	}
            	
                Stage stage = (Stage)((Node) e.getSource()).getScene().getWindow();
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("Sample2.fxml"));
                Parent CreatQuestionParent = loader.load();
                Scene scene = new Scene(CreatQuestionParent);
                scene.getStylesheets().add(getClass().getResource("application2.css").toExternalForm());
                stage.setScene(scene);
        	}
    	}
    }
    
    public void SaveAndContinueEdit(ActionEvent e) throws IOException {
    	int countChoice = 0;
    	// Lấy dữ liệu từ giao diện
    	if(questionName.getText().isBlank()) {
    		noticeError.setText("Question name không thể để trống");
    		noticeError.show(viewTooltip, viewTooltip.getLayoutX(), 250);
        	PauseTransition pause = new PauseTransition(Duration.seconds(5));
        	pause.setOnFinished(event -> noticeError.hide());
        	pause.play();
    	}else if(questionText.getText().isBlank()){
    		noticeError.setText("Question text không thể để trống");
    		noticeError.show(viewTooltip, viewTooltip.getLayoutX(), 250);
        	PauseTransition pause = new PauseTransition(Duration.seconds(5));
        	pause.setOnFinished(event -> noticeError.hide());
        	pause.play();
    	}else if(defaultMark.getText().isBlank()) {
    		noticeError.setText("Default mark không thể để trống");
    		noticeError.show(viewTooltip, viewTooltip.getLayoutX(), 250);
        	PauseTransition pause = new PauseTransition(Duration.seconds(5));
        	pause.setOnFinished(event -> noticeError.hide());
        	pause.play();
    	}else {
    		String questionTextPart1 = questionName.getText().replace("\n", "").trim(); // Lấy từ textfield phần 1 của câu hỏi
            String questionTextPart2 = questionText.getText().replace("\n", "").trim(); // Lấy từ textarea phần 2 của câu hỏi
            String questionText = questionTextPart1 + " " + questionTextPart2; // Ghép hai phần lại thành câu hỏi đầy đủ

        	Map<String, String> choices = new HashMap<>();
        	Map<String, Double> correctAnswers = new HashMap<>();

        	// Duyệt qua tất cả các AnchorPane trong VBox
        	int fakechoice = 0;
        	for (int i = 0; i < vbox.getChildren().size(); i++) {
        	    AnchorPane anchorPane = (AnchorPane) vbox.getChildren().get(i);
        	    TextField choiceTextField = (TextField) anchorPane.getChildren().get(3); // TextField là thành phần thứ 4 trong AnchorPane
        	    if(choiceTextField.getText().isBlank()) {
        	    	fakechoice += 1;
        	    	continue;         	    	
        	    }      	    
        	    ComboBox<String> answerComboBox = (ComboBox<String>) anchorPane.getChildren().get(2); // ComboBox là thành phần thứ ba trong AnchorPane

        	    String choiceKey = Character.toString((char) ('A' + i - fakechoice)); // Tạo key cho lựa chọn (A, B, C, ...)
        	    choices.put(choiceKey, choiceTextField.getText()); // Lấy từ textfield lựa chọn

        	 // Lấy từ combobox điểm số
        	    String comboBoxValue = answerComboBox.getValue();
        	    double score = 0;
        	    if (comboBoxValue != null) {
        	        score = Double.parseDouble(comboBoxValue.replace("%", "")) / 100;
        	        score *= Double.parseDouble(defaultMark.getText());
        	    }
        	    correctAnswers.put(choiceKey, score); 
        	    countChoice += 1;
        	}
        	
        	if(countChoice < 2) {
        		noticeError.setText("Số lựa chọn tối thiểu là 2");
        		noticeError.show(viewTooltip, viewTooltip.getLayoutX(), 250);
            	PauseTransition pause = new PauseTransition(Duration.seconds(5));
            	pause.setOnFinished(event -> noticeError.hide());
            	pause.play();
        	} else {
        		// thêm mức điểm của câu hỏi
        		correctAnswers.put("Mark", Double.parseDouble(defaultMark.getText()));
        		// Tạo AikenQuestion
            	AikenQuestion question = new AikenQuestion(questionText, choices, correctAnswers);

            	// Chuyển đổi AikenQuestion thành định dạng Aiken
            	String aikenText = question.toAikenFormat();

            	// Ghi đè Aiken mới vào file
            	Path path = Paths.get("src/data/"+labelCategory.getText().trim()+".txt");
            	// Đọc file và chia thành các câu hỏi
            	try (Stream<String> lines = Files.lines(path)) {
                	String[] questions = lines.map(String::trim).collect(Collectors.joining("\n")).split("\n\n");
                	for (int i = 0; i < questions.length; i++) {
                        if (questions[i].equals(questionWillChange)) {
                            questions[i] = aikenText;
                            // Ghi lại danh sách câu hỏi đã cập nhật vào file
                            Files.write(path, String.join("\n\n", questions).getBytes());
                            break;
                        }
                    }
                }catch (IOException e1) {
            	    e1.printStackTrace();
            	}
        	}
    	}
    }
    
    public void Cancel(ActionEvent e) throws IOException {
        Stage stage = (Stage)((Node) e.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Sample2.fxml"));
        Parent CreatQuestionParent = loader.load();
        Scene scene = new Scene(CreatQuestionParent);
        scene.getStylesheets().add(getClass().getResource("application2.css").toExternalForm());
        stage.setScene(scene);
    }
    
    private int count = 2;
    public void handleButtonAction(ActionEvent event) throws IOException {
        for (int i = 1; i <= 3; i++) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Choice.fxml"));
            AnchorPane newPane = loader.load();
         // Lấy Label cũ từ AnchorPane
            Label oldLabel = (Label) newPane.getChildren().get(0); // Thay đổi chỉ số nếu cần
            
            // Thay đổi nội dung của Label cũ
            count += 1;
            oldLabel.setText("Choice" + count);
            vbox.getChildren().add(newPane);
        }
    }
}
