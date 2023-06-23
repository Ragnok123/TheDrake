package thedrake.core.troop;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import thedrake.core.GameState;
import thedrake.core.action.TroopAction;
import thedrake.core.api.JSONSerializable;
import thedrake.core.api.Tile;
import thedrake.core.board.BoardPos;
import thedrake.core.move.Move;
import thedrake.core.utils.PlayingSide;

public class TroopTile implements Tile, JSONSerializable {

	private final Troop troop;
	private final PlayingSide side;
	private final TroopFace face;

	public TroopTile(Troop troop, PlayingSide side, TroopFace face) {
		this.troop = troop;
		this.side = side;
		this.face = face;
	}

	public PlayingSide side() {
		return this.side;
	}

	public TroopFace face() {
		return this.face;
	}

	public Troop troop() {
		return this.troop;
	}

	public boolean canStepOn() {
		return false;
	}

	public boolean hasTroop() {
		return true;
	}

	public TroopTile flipped() {
		TroopTile tile = null;
		switch (this.face) {
		case AVERS:
			tile = new TroopTile(this.troop, this.side, TroopFace.REVERS);
			break;
		case REVERS:
			tile = new TroopTile(this.troop, this.side, TroopFace.AVERS);
		}
		return tile;
	}

	@Override
	public List<Move> movesFrom(BoardPos pos, GameState state) {
		List<TroopAction> actions = troop.actions(face());
		List<Move> moves = new ArrayList<Move>();
		for (TroopAction action : actions) {
			moves.addAll(action.movesFrom(pos, state.sideOnTurn(), state));
		}
		return moves;
	}

	@Override
	public void toJSON(PrintWriter writer) {
		writer.print("{\"troop\":\"" + troop.name());
		writer.print("\",\"side\":");
		side.toJSON(writer);
		writer.print(",\"face\":");
		face.toJSON(writer);
		writer.print("}");

	}

}
