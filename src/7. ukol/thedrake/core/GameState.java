package thedrake.core;

import java.io.PrintWriter;
import java.util.Optional;

import thedrake.core.api.JSONSerializable;
import thedrake.core.api.Tile;
import thedrake.core.api.TilePos;
import thedrake.core.board.Board;
import thedrake.core.board.BoardPos;
import thedrake.core.troop.Troop;
import thedrake.core.troop.TroopTile;
import thedrake.core.utils.PlayingSide;

public class GameState implements JSONSerializable {
	private final Board board;
	private final PlayingSide sideOnTurn;
	private final Army blueArmy;
	private final Army orangeArmy;
	private final GameResult result;

	public GameState(Board board, Army blueArmy, Army orangeArmy) {
		this(board, blueArmy, orangeArmy, PlayingSide.BLUE, GameResult.IN_PLAY);
	}

	public GameState(Board board, Army blueArmy, Army orangeArmy, PlayingSide sideOnTurn, GameResult result) {
		this.board = board;
		this.sideOnTurn = sideOnTurn;
		this.blueArmy = blueArmy;
		this.orangeArmy = orangeArmy;
		this.result = result;
	}

	public Board board() {
		return board;
	}

	public PlayingSide sideOnTurn() {
		return sideOnTurn;
	}

	public GameResult result() {
		return result;
	}

	public Army army(PlayingSide side) {
		if (side == PlayingSide.BLUE) {
			return blueArmy;
		}

		return orangeArmy;
	}

	public Army armyOnTurn() {
		return army(sideOnTurn);
	}

	public Army armyNotOnTurn() {
		if (sideOnTurn == PlayingSide.BLUE)
			return orangeArmy;

		return blueArmy;
	}

	public Tile tileAt(TilePos pos) {
		if (this.blueArmy.boardTroops().at(pos).isPresent()) {
			return this.blueArmy.boardTroops().at(pos).get();
		} else if (this.orangeArmy.boardTroops().at(pos).isPresent()) {
			return this.orangeArmy.boardTroops().at(pos).get();
		}
		return this.board.at(pos);
	}

	private boolean canStepFrom(TilePos origin) {
		Army army = this.armyOnTurn();
		if (this.result != GameResult.IN_PLAY)
			return false;
		if (army.boardTroops().isPlacingGuards())
			return false;
		Optional<TroopTile> tile = army.boardTroops().at(origin);
		return tile.isPresent() && tile.get().side() == this.sideOnTurn;
	}

	private boolean canStepTo(TilePos target) {
		if (this.result != GameResult.IN_PLAY)
			return false;
		if (target == TilePos.OFF_BOARD)
			return false;
		return this.tileAt(new BoardPos(this.board.dimension(), target.i(), target.j())).canStepOn();
	}

	private boolean canCaptureOn(TilePos target) {
		if (this.result != GameResult.IN_PLAY) {
			return false;
		}
		return this.armyNotOnTurn().boardTroops().at(target).isPresent();
	}

	public boolean canStep(TilePos origin, TilePos target) {
		return canStepFrom(origin) && canStepTo(target);
	}

	public boolean canCapture(TilePos origin, TilePos target) {
		return canStepFrom(origin) && canCaptureOn(target);
	}

	public boolean canPlaceFromStack(TilePos target) {
		if (this.result != GameResult.IN_PLAY || target == TilePos.OFF_BOARD || this.armyOnTurn().stack().isEmpty()
				|| !this.tileAt((BoardPos) target).canStepOn()) {
			return false;
		}
		Army army = this.armyOnTurn();
		if (!army.boardTroops().isLeaderPlaced()) {
			if (this.sideOnTurn == PlayingSide.BLUE) {
				return target.j() == 0;
			}
			return target.j() == this.board.dimension() - 1;
		}
		if (army.boardTroops().isPlacingGuards()) {
			return army.boardTroops().leaderPosition().isNextTo(target);
		}
		for (BoardPos pos : army.boardTroops().troopPositions()) {
			if (!pos.isNextTo(target))
				continue;
			return true;
		}
		return false;

	}

	public GameState stepOnly(BoardPos origin, BoardPos target) {
		if (canStep(origin, target))
			return createNewGameState(armyNotOnTurn(), armyOnTurn().troopStep(origin, target), GameResult.IN_PLAY);

		throw new IllegalArgumentException();
	}

	public GameState stepAndCapture(BoardPos origin, BoardPos target) {
		if (canCapture(origin, target)) {
			Troop captured = armyNotOnTurn().boardTroops().at(target).get().troop();
			GameResult newResult = GameResult.IN_PLAY;

			if (armyNotOnTurn().boardTroops().leaderPosition().equals(target))
				newResult = GameResult.VICTORY;

			return createNewGameState(armyNotOnTurn().removeTroop(target),
					armyOnTurn().troopStep(origin, target).capture(captured), newResult);
		}

		throw new IllegalArgumentException();
	}

	public GameState captureOnly(BoardPos origin, BoardPos target) {
		if (canCapture(origin, target)) {
			Troop captured = armyNotOnTurn().boardTroops().at(target).get().troop();
			GameResult newResult = GameResult.IN_PLAY;

			if (armyNotOnTurn().boardTroops().leaderPosition().equals(target))
				newResult = GameResult.VICTORY;

			return createNewGameState(armyNotOnTurn().removeTroop(target),
					armyOnTurn().troopFlip(origin).capture(captured), newResult);
		}

		throw new IllegalArgumentException();
	}

	public GameState placeFromStack(BoardPos target) {
		if (canPlaceFromStack(target)) {
			return createNewGameState(armyNotOnTurn(), armyOnTurn().placeFromStack(target), GameResult.IN_PLAY);
		}

		throw new IllegalArgumentException();
	}

	public GameState resign() {
		return createNewGameState(armyNotOnTurn(), armyOnTurn(), GameResult.VICTORY);
	}

	public GameState draw() {
		return createNewGameState(armyOnTurn(), armyNotOnTurn(), GameResult.DRAW);
	}

	private GameState createNewGameState(Army armyOnTurn, Army armyNotOnTurn, GameResult result) {
		if (armyOnTurn.side() == PlayingSide.BLUE) {
			return new GameState(board, armyOnTurn, armyNotOnTurn, PlayingSide.BLUE, result);
		}

		return new GameState(board, armyNotOnTurn, armyOnTurn, PlayingSide.ORANGE, result);
	}

	@Override
	public void toJSON(PrintWriter writer) {
		writer.print("{\"result\":");
		result.toJSON(writer);
		writer.print(",\"board\":");
		board.toJSON(writer);
		writer.print(",\"blueArmy\":");
		blueArmy.toJSON(writer);
		writer.print(",\"orangeArmy\":");
		orangeArmy.toJSON(writer);
		writer.print("}");

	}
}
