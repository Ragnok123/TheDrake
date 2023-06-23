package thedrake.core.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import thedrake.core.GameState;
import thedrake.core.troop.Troop;
import thedrake.core.utils.Controller;
import thedrake.core.utils.PlayingSide;

public class CapturedView extends HBox implements Controller {

	private PlayingSide playingSide;
	
	public PlayingSide getPlayingSide() {
		return playingSide;
	}

	public void setPlayingSide(PlayingSide playingSide) {
		this.playingSide = playingSide;
	}

	@Override
	public void onGameStateChange(GameState gameState) {
		getChildren().clear();
		int i = 0;
		for (Troop troop : gameState.army(playingSide).captured()) {
			StackTileView view = new StackTileView(troop, playingSide.opposite());
			view.setPadding(new Insets(i * 10.0, 0.0, 0.0, 0.0));
			getChildren().add(0, view);
			i++;
		}
		setPadding(new Insets(15));
		setAlignment(Pos.CENTER);
	}

}
