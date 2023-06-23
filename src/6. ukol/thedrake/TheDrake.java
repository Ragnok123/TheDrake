package thedrake;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class TheDrake extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage arg0) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("MainScene.fxml"));
		Scene scene = new Scene(root);
		arg0.getIcons().add(new Image(getClass().getResourceAsStream("logo.jpg")));
		arg0.setScene(scene);
		arg0.setTitle("TheDrake");
		arg0.setResizable(false);
		arg0.show();
	}

}
