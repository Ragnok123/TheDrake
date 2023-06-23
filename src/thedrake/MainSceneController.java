package thedrake;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.Screen;

public class MainSceneController implements Initializable {
	@Override
	public void initialize(URL url, ResourceBundle bundle) {
	}
	
	public void onGame(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("GameScene.fxml"));
        Scene scene = new Scene(fxmlLoader.load(),850,1000);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setWidth(1000);
        stage.setHeight(850);
        stage.setMinWidth(900);
        stage.setMinHeight(850);
        stage.setX((Screen.getPrimary().getVisualBounds().getWidth() - 1000) / 2);
        stage.setY((Screen.getPrimary().getVisualBounds().getHeight() - 850) / 2);
        stage.setTitle("The Drake");
        stage.setScene(scene);
        stage.show();

	}

	public void onExit() {
		Platform.exit();
	}
}
