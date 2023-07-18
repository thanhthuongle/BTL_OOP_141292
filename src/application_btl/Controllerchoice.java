package application_btl;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

public class Controllerchoice implements Initializable{
	@FXML
	private ComboBox<String> combobox;
 
	ObservableList<String> list = FXCollections.observableArrayList("100%", "90%", "83.33333%", "80%", "75%", "70%", "66.66667%", "60%", "50%", "40%", "33.33333%", "30%", "25%", "20%", "16.66667%", "14.28571%", "12.5%", "10%", "5%", "-5%", "-10%", "-12.5%", "-14.28571%", "-16.66667%", "-20%", "-25%", "-30%", "-33.33333%", "-40%", "-50%", "-60%", "-66.66667%", "-70%", "-75%", "-80%", "-83.33333%");
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		combobox.setItems(list);
	}
}
