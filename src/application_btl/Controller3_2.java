package application_btl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;


public class Controller3_2 implements Initializable{
	@FXML
    private TreeView<String> treeView;
	
	@FXML
	private Label labelcategory;
	
	@FXML
	private VBox vbox;
	
	@FXML
	private ComboBox<String> combobox1;
	
	@FXML
	private ComboBox<String> combobox2;
	
	@FXML
    private TextField questionTextField;
	
	@FXML
    private TextArea questionTextArea;
	
	@FXML
	private TextField defaultMark;
	
	@FXML
    private TextField viewTooltip;
	
	@FXML
    private Tooltip noticeError;

	private boolean checkTemp = false;
	ObservableList<String> list = FXCollections.observableArrayList("100%", "90%", "83.33333%", "80%", "75%", "70%", "66.66667%", "60%", "50%", "40%", "33.33333%", "30%", "25%", "20%", "16.66667%", "14.28571%", "12.5%", "10%", "5%", "-5%", "-10%", "-12.5%", "-14.28571%", "-16.66667%", "-20%", "-25%", "-30%", "-33.33333%", "-40%", "-50%", "-60%", "-66.66667%", "-70%", "-75%", "-80%", "-83.33333%");
	@Override
    public void initialize(URL location, ResourceBundle resources) {
        IndentationTreeItem rootItem = new IndentationTreeItem("Course: IT", 0);
        treeView.setRoot(rootItem);

        try (BufferedReader reader = new BufferedReader(new FileReader("src/data/Categories_question.txt"))) {
            String line;
            IndentationTreeItem currentItem = rootItem;

            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    int indentationLevel = getIndentationLevel(line);
                    String itemName = getItemName(line);

                    IndentationTreeItem newItem = new IndentationTreeItem(itemName, indentationLevel);

                    if (indentationLevel > currentItem.getIndentation()) {
                        currentItem.getChildren().add(newItem);
                    } else {
                        IndentationTreeItem parentItem = findParentItem(currentItem, indentationLevel);
                        parentItem.getChildren().add(newItem);
                    }

                    currentItem = newItem;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        treeView.setCellFactory(param -> new TreeCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(item);
                    if (item.equals("Course: IT")) {
                        setFont(Font.font("System", FontWeight.BOLD, 12));
                    } else {
                    	setFont(Font.font("System", FontWeight.NORMAL, 12));
                    }
                }
            }
        });
        
        expandAllItems(treeView.getRoot());
        
        treeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
    	    if (newValue != null && newValue.isLeaf()) {
    	        String selectedLeaf = newValue.getValue();
    	        labelcategory.setText("   "+ selectedLeaf);
    	        treeView.setVisible(false);
    	    }
    	});
        
        combobox1.setItems(list);
        combobox2.setItems(list);
        
        defaultMark.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                defaultMark.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    private void expandAllItems(TreeItem<String> item) {
        if (item != null) {
            item.setExpanded(true);
            for (TreeItem<String> child : item.getChildren()) {
                expandAllItems(child);
            }
        }
    }
    
    private int getIndentationLevel(String line) {
        int level = 0;
        int index = 0;
        while (line.charAt(index) == ' ') {
            level++;
            index++;
        }
        return level / 2;
    }

    private String getItemName(String line) {
        return line.trim();
    }

    private IndentationTreeItem findParentItem(IndentationTreeItem currentItem, int indentationLevel) {
        IndentationTreeItem parentItem = currentItem;
        while (parentItem.getIndentation() >= indentationLevel && parentItem.getParent() != null) {
            parentItem = (IndentationTreeItem) parentItem.getParent();
        }
        return parentItem;
    }
   
    private int count = 3;
    public void handleButtonAction(ActionEvent event) throws IOException {
        for (int i = 1; i <= 3; i++) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Choice.fxml"));
            AnchorPane newPane = loader.load();
         // Lấy Label cũ từ AnchorPane
            Label oldLabel = (Label) newPane.getChildren().get(0); // Thay đổi chỉ số nếu cần
            
            // Thay đổi nội dung của Label cũ
            oldLabel.setText("Choice" + count);
            count += 1;
            vbox.getChildren().add(newPane);
        }
    }
    public void treeView_popup(MouseEvent e){
		if(treeView.isVisible() == false)
			treeView.setVisible(true);
		else
			treeView.setVisible(false);
	}
    
    public void Save(ActionEvent e) throws IOException {
    	int countChoice = 0;
    	
    	// Lấy dữ liệu từ giao diện
    	if(questionTextField.getText().isBlank()) {
    		noticeError.setText("Question name không thể để trống");
    		noticeError.show(viewTooltip, viewTooltip.getLayoutX(), 250);
        	PauseTransition pause = new PauseTransition(Duration.seconds(5));
        	pause.setOnFinished(event -> noticeError.hide());
        	pause.play();
    	}else if(questionTextArea.getText().isBlank()){
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
    		String questionTextPart1 = questionTextField.getText().replace("\n", "").trim(); // Lấy từ textfield phần 1 của câu hỏi
            String questionTextPart2 = questionTextArea.getText().replace("\n", "").trim(); // Lấy từ textarea phần 2 của câu hỏi
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

            	// Ghi chuỗi Aiken vào file
            	try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/data/"+labelcategory.getText().trim()+".txt", true))) {
            		File file = new File("src/data/"+labelcategory.getText().trim()+".txt");
            	    if (file.length() != 0) {
            	        writer.newLine();
            	    }
            	    writer.write(aikenText);
            	} catch (IOException e1) {
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
    	if(questionTextField.getText().isBlank()) {
    		noticeError.setText("Question name không thể để trống");
    		noticeError.show(viewTooltip, viewTooltip.getLayoutX(), 250);
        	PauseTransition pause = new PauseTransition(Duration.seconds(5));
        	pause.setOnFinished(event -> noticeError.hide());
        	pause.play();
    	}else if(questionTextArea.getText().isBlank()){
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
    		String questionTextPart1 = questionTextField.getText().replace("\n", "").trim(); // Lấy từ textfield phần 1 của câu hỏi
            String questionTextPart2 = questionTextArea.getText().replace("\n", "").trim(); // Lấy từ textarea phần 2 của câu hỏi
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

            	// Ghi chuỗi Aiken vào file
            	try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/data/tempAddQuestion.txt"))) {
            	    writer.write(aikenText);
            	} catch (IOException e1) {
            	    e1.printStackTrace();
            	}
            	checkTemp = true;
        	}
    	}
    }
    
    public void Cancel(ActionEvent e) throws IOException {
    	if(checkTemp) {
    		try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/data/"+labelcategory.getText().trim()+".txt", true))) {
        		File file = new File("src/data/"+labelcategory.getText().trim()+".txt");
        		Path path = Paths.get("src/data/tempAddQuestion.txt");
        		String str = Files.readString(path, StandardCharsets.UTF_8);
        	    if (file.length() != 0) {
        	        writer.newLine();
        	    }
        	    writer.write(str);
        	} catch (IOException e1) {
        	    e1.printStackTrace();
        	}
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
