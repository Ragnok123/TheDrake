package thedrake.core.ui;

import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import thedrake.GameSceneController;
import thedrake.core.GameState;
import thedrake.core.board.BoardPos;
import thedrake.core.factory.PositionFactory;
import thedrake.core.move.Move;
import thedrake.core.utils.Controller;
import thedrake.core.utils.PlayingSide;

public class BoardView extends GridPane implements TileViewContext, Controller {

	private GameState gameState;
	private ValidMoves validMoves;
	private TileView selected;
	private boolean isCreated = false;
	private GameSceneController controller;
	private PlayingSide selectedStackSide;

	public void setContext(GameSceneController controller) {
		this.controller = controller;
	}

	public GameSceneController getContext() {
		return this.controller;
	}

	public void init(GameState gameState) {
		this.gameState = gameState;
		this.validMoves = new ValidMoves(gameState);
		isCreated = true;

		PositionFactory positionFactory = gameState.board().positionFactory();
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				BoardPos boardPos = positionFactory.pos(x, 3 - y);
				add(new TileView(boardPos, gameState.tileAt(boardPos), this.controller), x, y);
			}
		}

		setHgap(5);
		setVgap(5);
		setPadding(new Insets(15));
		setAlignment(Pos.CENTER);
	}

	public ValidMoves getValidMoves() {
		return this.validMoves;
	}

	public void stackSelected(PlayingSide side) {
		this.selectedStackSide = side;
		if (selected != null) {
			selected.unselect();
		}
		selected = null;
		clearMoves();
		if (side == gameState.sideOnTurn())
			showMoves(validMoves.movesFromStack());
	}

	@Override
	public void tileViewSelected(TileView tileView) {
		if (selected != null && selected != tileView) {
			selected.unselect();
		}

		selected = tileView;

		clearMoves();
		showMoves(validMoves.boardMoves(tileView.position()));
	}

	@Override
	public void executeMove(Move move) {
		selected.unselect();
		selected = null;
		clearMoves();
		gameState = move.execute(gameState);
		validMoves = new ValidMoves(gameState);
		updateTiles();
	}

	private void updateTiles() {
		for(Node node : getChildren()) {
			TileView tileView = (TileView) node;
			tileView.setTile(gameState.tileAt(tileView.position()));
			tileView.update();
		}
	}

	private void clearMoves() {
		for (Node node : getChildren()) {
			TileView tileView = (TileView) node;
			tileView.clearMove();
		}
	}

	private void showMoves(List<Move> moveList) {
		for (Move move : moveList)
			tileViewAt(move.target()).setMove(move);
	}

	private TileView tileViewAt(BoardPos target) {
		int index = (3 - target.j()) * 4 + target.i();
		return (TileView) getChildren().get(index);
	}

	@Override
	public void onGameStateChange(GameState state) {
		this.gameState = state;
		this.validMoves = new ValidMoves(state);
		if (!isCreated)
			init(state);
		if (selected != null)
			selected.unselect();
		selected = null;
		clearMoves();
		updateTiles();
	}
}
