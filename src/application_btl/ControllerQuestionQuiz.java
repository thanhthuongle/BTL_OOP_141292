package application_btl;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

public class ControllerQuestionQuiz {
	@FXML
    private ImageView zoom;
	
	@FXML
    private ImageView delete;

    public ImageView getZoom() {
		return zoom;
	}

	public ImageView getDelete() {
		return delete;
	}
}
