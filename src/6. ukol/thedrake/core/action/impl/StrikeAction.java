package thedrake.core.action.impl;

import java.util.ArrayList;
import java.util.List;

import thedrake.core.GameState;
import thedrake.core.action.TroopAction;
import thedrake.core.api.TilePos;
import thedrake.core.board.BoardPos;
import thedrake.core.move.Move;
import thedrake.core.move.impl.CaptureOnly;
import thedrake.core.utils.Offset2D;
import thedrake.core.utils.PlayingSide;

public class StrikeAction extends TroopAction {

	public StrikeAction(Offset2D offset) {
		super(offset);
	}

	public StrikeAction(int offsetX, int offsetY) {
		super(offsetX, offsetY);
	}

	@Override
	public List<Move> movesFrom(BoardPos origin, PlayingSide side, GameState state) {
		List<Move> moves = new ArrayList<Move>();
		TilePos target = origin.stepByPlayingSide(offset(), side);
		if (state.canCapture(origin, target)) {
			moves.add(new CaptureOnly(origin, (BoardPos) target));
		}
		return moves;

	}

}
