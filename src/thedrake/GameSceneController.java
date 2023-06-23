package thedrake;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import thedrake.core.GameResult;
import thedrake.core.GameState;
import thedrake.core.api.BoardTile;
import thedrake.core.board.Board;
import thedrake.core.factory.PositionFactory;
import thedrake.core.move.Move;
import thedrake.core.ui.BoardView;
import thedrake.core.ui.CapturedView;
import thedrake.core.ui.StackView;
import thedrake.core.ui.TileView;
import thedrake.core.utils.Controller;
import thedrake.core.utils.PlayingSide;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class GameSceneController implements Initializable {

	@FXML
	public StackView blueStack;
	@FXML
	public StackView orangeStack;

	@FXML
	public CapturedView blueCaptured;
	@FXML
	public CapturedView orangeCaptured;

	@FXML
	public BoardView board;
	public BorderPane gameScreen;

	private GameState gameState;

	private Set<Controller> stateControllers = new HashSet<Controller>();
	private Set<StackView> stackSelectors = new HashSet<StackView>();

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		blueStack.setPlayingSide(PlayingSide.BLUE);
		orangeStack.setPlayingSide(PlayingSide.ORANGE);

		blueCaptured.setPlayingSide(PlayingSide.BLUE);
		orangeCaptured.setPlayingSide(PlayingSide.ORANGE);

		board.setContext(this);
		blueStack.setContext(this);
		orangeStack.setContext(this);

		stateControllers.add(board);
		stateControllers.add(blueStack);
		stateControllers.add(orangeStack);
		stateControllers.add(blueCaptured);
		stateControllers.add(orangeCaptured);
		stackSelectors.add(orangeStack);
		stackSelectors.add(blueStack);
		gameState = initialBoardState();
		update(gameState);
	}

	private static GameState initialBoardState() {
		Board board = new Board(4);
		PositionFactory positionFactory = board.positionFactory();
		Random random = new Random();
		int num = random.nextInt(4);
		switch(num) {
		case 1:
			board = board.withTiles(new Board.TileAt(positionFactory.pos(1, 1), BoardTile.MOUNTAIN)).withTiles(new Board.TileAt(positionFactory.pos(2, 2), BoardTile.MOUNTAIN));
			break;
		case 2:
			board = board.withTiles(new Board.TileAt(positionFactory.pos(0, 1), BoardTile.MOUNTAIN)).withTiles(new Board.TileAt(positionFactory.pos(3, 2), BoardTile.MOUNTAIN));
			break;
		case 3:
			board = board.withTiles(new Board.TileAt(positionFactory.pos(2, 1), BoardTile.MOUNTAIN)).withTiles(new Board.TileAt(positionFactory.pos(1, 2), BoardTile.MOUNTAIN));
			break;
		default:
			board = board.withTiles(new Board.TileAt(positionFactory.pos(3, 1), BoardTile.MOUNTAIN)).withTiles(new Board.TileAt(positionFactory.pos(0, 2), BoardTile.MOUNTAIN));
			break;
		}
		return new StandardDrakeSetup().startState(board);
	}
	
	private void update(GameState gameState) {
		for(Controller controller : this.stateControllers) {
			controller.onGameStateChange(gameState);
		}
		if(gameState.result() == GameResult.VICTORY) {
			endGame();
		} else if(board.getValidMoves().allMoves().isEmpty()) {
			endGame();
		}
	}

	private void endGame() {
		ButtonType newGame = new ButtonType("New Game", ButtonBar.ButtonData.OK_DONE);
		ButtonType mainMenu = new ButtonType("Main Menu", ButtonBar.ButtonData.CANCEL_CLOSE);
		
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", newGame, mainMenu);
		alert.getDialogPane().setGraphic(null);
		alert.setTitle("Game end");
		alert.setHeaderText("Victory!");
		alert.setContentText("The winner is " + gameState.armyNotOnTurn().side().name() + "!");

		Optional<ButtonType> buttonPressed = alert.showAndWait();
		if (buttonPressed.isEmpty()) {
			loadMenu();
		} else if (buttonPressed.get() == newGame) {
			Stage stage = (Stage) this.board.getScene().getWindow();
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("GameScene.fxml"));
			Scene scene;
			try {
				scene = new Scene(fxmlLoader.load(), 1000, 850);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			stage.setMinWidth(900);
			stage.setMinHeight(800);
			stage.setWidth(1000);
			stage.setHeight(875);
			stage.setX((Screen.getPrimary().getVisualBounds().getWidth() - 1000) / 2);
			stage.setY((Screen.getPrimary().getVisualBounds().getHeight() - 875) / 2);
			stage.setScene(scene);
			stage.show();
		} else {
			loadMenu();
		}
	}

	private void loadMenu() {
		FXMLLoader fxmlLoader = new FXMLLoader(TheDrake.class.getResource("MainScene.fxml"));
		Scene scene = null;
		try {
			scene = new Scene(fxmlLoader.load());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		Stage stage = (Stage) this.board.getScene().getWindow();
		stage.setHeight(400);
		stage.setWidth(640);
		stage.setTitle("The Drake");
		stage.setScene(scene);
		stage.setResizable(false);
		stage.show();
	}

	public void stackViewSelected(PlayingSide playingSide) {
		board.stackSelected(playingSide);
		for (StackView selector : stackSelectors) {
			selector.stackSelected(playingSide);
		}
	}


	public void tileViewSelected(TileView tileView) {
		for (StackView selector : stackSelectors) {
			selector.tileSelected(tileView);
		}
		board.tileViewSelected(tileView);
	}

	public void executeMove(Move move) {
		gameState = move.execute(gameState);
		update(gameState);
	}

	public StackView getBlueStack() {
		return this.blueStack;
	}

	public void setVlueStack(StackView blueStack) {
		this.blueStack = blueStack;
	}

	public StackView getOrangeStack() {
		return this.orangeStack;
	}

	public void setOrangeStack(StackView orangeStack) {
		this.orangeStack = orangeStack;
	}

	public CapturedView getBlueCaptured() {
		return this.blueCaptured;
	}

	public void setBlueCapture(CapturedView blueCaptured) {
		this.blueCaptured = blueCaptured;
	}

	public CapturedView getOrangeCaptured() {
		return this.orangeCaptured;
	}

	public void setCapturedOrange(CapturedView orangeCaptured) {
		this.orangeCaptured = orangeCaptured;
	}

	public BoardView getBoard() {
		return board;
	}

	public void setBoard(BoardView board) {
		this.board = board;
	}

	public BorderPane getGameScreen() {
		return gameScreen;
	}

	public void setGameScreen(BorderPane gameScreen) {
		this.gameScreen = gameScreen;
	}

	public GameState getGameState() {
		return gameState;
	}

	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}

	public Set<Controller> getStateControllers() {
		return stateControllers;
	}

	public void setStateControllers(Set<Controller> stateControllers) {
		this.stateControllers = stateControllers;
	}

	public Set<StackView> getStackSelectors() {
		return stackSelectors;
	}

	public void setStackSelectors(Set<StackView> stackSelectors) {
		this.stackSelectors = stackSelectors;
	}

}
