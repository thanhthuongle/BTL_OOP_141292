package application_btl;

import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.YearMonth;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Controller5 {
    @FXML
    private ComboBox<Integer> openDayComboBox;
    @FXML
    private ComboBox<Month> openMonthComboBox;
    @FXML
    private ComboBox<Integer> openYearComboBox;
    @FXML
    private ComboBox<Integer> closeDayComboBox;
    @FXML
    private ComboBox<Month> closeMonthComboBox;
    @FXML
    private ComboBox<Integer> closeYearComboBox;
    @FXML
    private ComboBox<Integer> openHourComboBox;
    @FXML
    private ComboBox<Integer> openMinuteComboBox;
    @FXML
    private ComboBox<Integer> closeHourComboBox; 
    @FXML
    private ComboBox<Integer> closeMinuteComboBox; 
    @FXML
    private TextField textFieldTimeLimit;
    @FXML
    private TextField textFieldName;
    @FXML
    private ComboBox<String> optionTimeLimit;
    @FXML
    private CheckBox checkCloseTime;
    @FXML
    private CheckBox checkOpenTime;
    @FXML
    private CheckBox checkLimitTime;
    @FXML
    private Tooltip noticeError;
    @FXML
    private TextField tooltipView;
    @FXML
    
    public boolean checkNameQuizExists(){
    	File myFile = new File("src/data/ExamList.txt");
    	try (Scanner myReader = new Scanner(myFile)) {
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String firstPart = data.split(",")[0];
                if (firstPart.equals(textFieldName.getText().trim())) {
                	myReader.close();
                	return true;
                }	
            }
            myReader.close();
        } catch (FileNotFoundException event) {
            event.printStackTrace();
        }
		return false;
    }
    private void initialize() {
        // Add options to ComboBoxes
    	openDayComboBox.setItems(FXCollections.observableArrayList(IntStream.rangeClosed(1, 31).boxed().collect(Collectors.toList())));
    	openMonthComboBox.setItems(FXCollections.observableArrayList(Month.values()));
    	openYearComboBox.setItems(FXCollections.observableArrayList(IntStream.rangeClosed(1900, 2100).boxed().collect(Collectors.toList())));

        // Set initial values based on current date
        LocalDate currentDate = LocalDate.now();
        openDayComboBox.setValue(currentDate.getDayOfMonth());
        openMonthComboBox.setValue(currentDate.getMonth());
        openYearComboBox.setValue(currentDate.getYear());

        // Add listener to ComboBoxes
        openMonthComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> updateDayComboBox());
        openYearComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> updateDayComboBox());
        
        // Add options to ComboBoxes
    	closeDayComboBox.setItems(FXCollections.observableArrayList(IntStream.rangeClosed(1, 31).boxed().collect(Collectors.toList())));
    	closeMonthComboBox.setItems(FXCollections.observableArrayList(Month.values()));
    	closeYearComboBox.setItems(FXCollections.observableArrayList(IntStream.rangeClosed(1900, 2100).boxed().collect(Collectors.toList())));

        // Set initial values based on current date
        //LocalDate currentDate = LocalDate.now();
        closeDayComboBox.setValue(currentDate.getDayOfMonth());
        closeMonthComboBox.setValue(currentDate.getMonth());
        closeYearComboBox.setValue(currentDate.getYear());

        // Add listener to ComboBoxes
        closeMonthComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> updateDayComboBox1());
        closeYearComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> updateDayComboBox1());
        
        // Initialize openHourComboBox
        openHourComboBox.setItems(FXCollections.observableArrayList(IntStream.rangeClosed(0, 23).boxed().collect(Collectors.toList())));
        openHourComboBox.setValue(LocalTime.now().getHour());

        // Initialize openMinuteComboBox
        openMinuteComboBox.setItems(FXCollections.observableArrayList(IntStream.rangeClosed(0, 59).boxed().collect(Collectors.toList())));
        openMinuteComboBox.setValue(LocalTime.now().getMinute());
        
        // Initialize closeHourComboBox
        closeHourComboBox.setItems(FXCollections.observableArrayList(IntStream.rangeClosed(0, 23).boxed().collect(Collectors.toList())));
        closeHourComboBox.setValue(LocalTime.now().getHour());

        // Initialize closeMinuteComboBox
        closeMinuteComboBox.setItems(FXCollections.observableArrayList(IntStream.rangeClosed(0, 59).boxed().collect(Collectors.toList())));
        closeMinuteComboBox.setValue(LocalTime.now().getMinute());
        
        // chỉ điền số vào ô thời gian
        textFieldTimeLimit.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
            	textFieldTimeLimit.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
     // không điền số "," vào ô name
        textFieldName.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.contains(",")) {
            	textFieldName.setText(oldValue);
            }
        });
    }

    private void updateDayComboBox() {
        Month selectedMonth = openMonthComboBox.getValue();
        int selectedYear = openYearComboBox.getValue();
        int daysInMonth = getDaysInMonth(selectedMonth, selectedYear);

        // Get the currently selected day
        Integer currentDay = openDayComboBox.getValue();

        // Clear current options in day ComboBox
        openDayComboBox.getItems().clear();

        // Add options for day based on valid days
        openDayComboBox.setItems(FXCollections.observableArrayList(IntStream.rangeClosed(1, daysInMonth).boxed().collect(Collectors.toList())));

        // If the previously selected day is still valid, reselect it
        if (currentDay != null && currentDay <= daysInMonth) {
        	openDayComboBox.setValue(currentDay);
        }
    }
    
    private void updateDayComboBox1() {
        Month selectedMonth = closeMonthComboBox.getValue();
        int selectedYear = closeYearComboBox.getValue();
        int daysInMonth = getDaysInMonth(selectedMonth, selectedYear);

        // Get the currently selected day
        Integer currentDay = closeDayComboBox.getValue();

        // Clear current options in day ComboBox
        closeDayComboBox.getItems().clear();

        // Add options for day based on valid days
        closeDayComboBox.setItems(FXCollections.observableArrayList(IntStream.rangeClosed(1, daysInMonth).boxed().collect(Collectors.toList())));

        // If the previously selected day is still valid, reselect it
        if (currentDay != null && currentDay <= daysInMonth) {
        	closeDayComboBox.setValue(currentDay);
        }
    }
    
    public void checkOpeneEnable(MouseEvent e){
		if(openDayComboBox.isDisable() == false)
			openDayComboBox.setDisable(true);
		else
			openDayComboBox.setDisable(false);
		if(openMonthComboBox.isDisable() == false)
			openMonthComboBox.setDisable(true);
		else
			openMonthComboBox.setDisable(false);
		if(openYearComboBox.isDisable() == false)
			openYearComboBox.setDisable(true);
		else
			openYearComboBox.setDisable(false);
		if(openMinuteComboBox.isDisable() == false)
			openMinuteComboBox.setDisable(true);
		else
			openMinuteComboBox.setDisable(false);
		if(openHourComboBox.isDisable() == false)
			openHourComboBox.setDisable(true);
		else
			openHourComboBox.setDisable(false);
	}
    
    public void checkCloseEnable(MouseEvent e){
		if(closeDayComboBox.isDisable() == false)
			closeDayComboBox.setDisable(true);
		else
			closeDayComboBox.setDisable(false);
		if(closeMonthComboBox.isDisable() == false)
			closeMonthComboBox.setDisable(true);
		else
			closeMonthComboBox.setDisable(false);
		if(closeYearComboBox.isDisable() == false)
			closeYearComboBox.setDisable(true);
		else
			closeYearComboBox.setDisable(false);
		if(closeHourComboBox.isDisable() == false)
			closeHourComboBox.setDisable(true);
		else
			closeHourComboBox.setDisable(false);
		if(closeMinuteComboBox.isDisable() == false)
			closeMinuteComboBox.setDisable(true);
		else
			closeMinuteComboBox.setDisable(false);
	}
    
    public void checkTimeLimitEnable(MouseEvent e){
		if(textFieldTimeLimit.isDisable() == false)
			textFieldTimeLimit.setDisable(true);
		else
			textFieldTimeLimit.setDisable(false);
		if(optionTimeLimit.isDisable() == false)
			optionTimeLimit.setDisable(true);
		else
			optionTimeLimit.setDisable(false);
	}
    
    private int getDaysInMonth(Month month, int year) {
        YearMonth yearMonthObject = YearMonth.of(year, month.getValue());
        return yearMonthObject.lengthOfMonth();
    }
    
    public void cancelSceneAddNewQuiz(ActionEvent e) throws IOException {
        Stage stage = (Stage)((Node) e.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Sample1.fxml"));
        Parent AddNewQuizParent = loader.load();
        Scene scene = new Scene(AddNewQuizParent);
        scene.getStylesheets().add(getClass().getResource("application1.css").toExternalForm());
        stage.setScene(scene);
    }
    
    public void createSceneAddNewQuiz(ActionEvent e) throws IOException {
    	
    	if(textFieldName.getText().isBlank()) {
    		noticeError.setText("Tên không thể để trống");
    		noticeError.show(tooltipView, tooltipView.getLayoutX(), tooltipView.getLayoutY());
        	PauseTransition pause = new PauseTransition(javafx.util.Duration.seconds(5));
        	pause.setOnFinished(event -> noticeError.hide());
        	pause.play();
    	}else if(checkNameQuizExists()) {
    		noticeError.setText("Tên đã tồn tại, hãy thay đổi tên");
    		noticeError.show(tooltipView, tooltipView.getLayoutX(), tooltipView.getLayoutY());
        	PauseTransition pause = new PauseTransition(javafx.util.Duration.seconds(5));
        	pause.setOnFinished(event -> noticeError.hide());
        	pause.play();
    	} else {
    		//xử lý dữ liệu
        	String name = textFieldName.getText().trim();
        	LocalDateTime openTime = null;
        	if (checkOpenTime.isSelected()) {
    	        openTime = LocalDateTime.of(openYearComboBox.getValue(), openMonthComboBox.getValue(), openDayComboBox.getValue(), openHourComboBox.getValue(), openMinuteComboBox.getValue());
    	    }
        	LocalDateTime closeTime = null;
        	if (checkCloseTime.isSelected()) {
    	        closeTime = LocalDateTime.of(closeYearComboBox.getValue(), closeMonthComboBox.getValue(), closeDayComboBox.getValue(), closeHourComboBox.getValue(), closeMinuteComboBox.getValue());
    	    }
    	    Duration limitTime = null;
    	    if (checkLimitTime.isSelected()) {
    	        limitTime = Duration.ofMinutes(Long.parseLong(textFieldTimeLimit.getText()));
    	    }
    	    if (closeTime != null && openTime != null && openTime.isAfter(closeTime)) {
    	    	noticeError.setText("Thời gian mở không thể sau thời gian đóng");
        		noticeError.show(tooltipView, tooltipView.getLayoutX()-20, tooltipView.getLayoutY());
            	PauseTransition pause = new PauseTransition(javafx.util.Duration.seconds(5));
            	pause.setOnFinished(event -> noticeError.hide());
            	pause.play();
    	    }else {
    	    	Exam exam = new Exam(name, openTime, closeTime, limitTime);
        	    String examString = exam.toString();
        	    try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/data/ExamList.txt", true))) {
            		File file = new File("src/data/ExamList.txt");
            	    if (file.length() != 0) {
            	        writer.newLine();
            	    }
            	    writer.write(examString);
            	} catch (IOException e1) {
            	    e1.printStackTrace();
            	}
            	
                Stage stage = (Stage)((Node) e.getSource()).getScene().getWindow();
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("Sample1.fxml"));
                Parent AddNewQuizParent = loader.load();
                Scene scene = new Scene(AddNewQuizParent);
                scene.getStylesheets().add(getClass().getResource("application1.css").toExternalForm());
                stage.setScene(scene);
    	    }
    	}
    }
}








