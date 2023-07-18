package application_btl;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class questionview {
	private StringProperty question;
	private StringProperty actions;
	
	public questionview(String question) {
        this.question = new SimpleStringProperty(question);
        this.actions = new SimpleStringProperty("Edit");
    }
	public String getQuestion() {
		return question.get();
	}
	public StringProperty questionProperty() {
		return question;
	}
	public String getActions() {
		return actions.get();
	}
	public StringProperty actionsProperty() {
		return actions;
	}
}
