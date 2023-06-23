package thedrake;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.Initializable;

public class MainSceneController implements Initializable {
	@Override
	public void initialize(URL url, ResourceBundle bundle) {
	}

	public void onExit() {
		Platform.exit();
	}
}
