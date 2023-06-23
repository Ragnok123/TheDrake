package thedrake.core.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import thedrake.GameSceneController;
import thedrake.core.GameState;
import thedrake.core.troop.Troop;
import thedrake.core.troop.TroopFace;
import thedrake.core.utils.Controller;
import thedrake.core.utils.PlayingSide;

public class StackView extends HBox implements Controller {

	private PlayingSide playingSide;
	private GameState gameState;
	private boolean stackSelected = false;
	private GameSceneController context;

	private Border selected = new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3)));

	public StackView() {
		setOnMouseClicked(e -> {
			select();
		});
	}
	private void select() {
		if (gameState != null && gameState.sideOnTurn() == playingSide) {
			stackSelected = true;
			selectUpdate();
		}
	}

	public void unselect() {
		stackSelected = false;
		selectUpdate();
	}

	private void selectUpdate() {
		if (stackSelected)
			context.stackViewSelected(playingSide);
		if (stackSelected && !getChildren().isEmpty()) {
			setBorder(selected);
		} else {
			setBorder(null);
		}
	}


	@Override
	public void onGameStateChange(GameState gameState) {
		this.gameState = gameState;
		unselect();
		getChildren().clear();
		int i = 0;
		for(Troop troop : gameState.army(playingSide).stack()) {
			StackTileView view = new StackTileView(troop, playingSide);
			view.setPadding(new Insets(i * 10.0, 0.0, 0.0, 5.0));
			getChildren().add(0, view);
			i++;
		}
		setPadding(new Insets(15));
		setAlignment(Pos.CENTER);
		if (playingSide.name().equals(gameState.sideOnTurn().name())) {
			Text text = new Text("On turn");
			text.setFont(new Font("MinecraftiaRegular", 20));
			text.setFill(Color.GREENYELLOW);
			getChildren().add(text);
		}
	}

	public void stackSelected(PlayingSide playingSide) {
		if (playingSide != this.playingSide && stackSelected) {
			unselect();
		}
	}
	
	public void tileSelected(TileView tileView) {
		unselect();
	}

	public void setContext(GameSceneController context) {
		this.context = context;
	}

	public PlayingSide getPlayingSide() {
		return playingSide;
	}

	public void setPlayingSide(PlayingSide playingSide) {
		this.playingSide = playingSide;
	}

	public GameState getGameState() {
		return gameState;
	}

	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}

	public boolean isStackSelected() {
		return stackSelected;
	}

	public void setStackSelected(boolean stackSelected) {
		this.stackSelected = stackSelected;
	}

	public Border getSelected() {
		return selected;
	}

	public void setSelected(Border selected) {
		this.selected = selected;
	}

	public GameSceneController getContext() {
		return context;
	}

}
