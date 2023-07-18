package application_btl;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class QuestionQuizView {
	private CheckBox checkbox;
	private StringProperty question;
	private ImageView zoom;
	
	public QuestionQuizView(String question) {
        this.question = new SimpleStringProperty(question);
        this.checkbox = new CheckBox();
        Image image = new Image("/images/zoom_8273077.png", 15, 15, true, true);
        this.zoom = new ImageView(image);
    }
	
	public String getQuestion() {
		return question.get();
	}
	public StringProperty questionProperty() {
		return question;
	}
	public CheckBox getCheckBox() {
        return checkbox;
    }

    public ImageView getZoom() {
        return zoom;
    }
}
