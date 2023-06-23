package thedrake.core.action.impl;

import java.util.ArrayList;
import java.util.List;

import thedrake.core.GameState;
import thedrake.core.action.TroopAction;
import thedrake.core.api.TilePos;
import thedrake.core.board.BoardPos;
import thedrake.core.move.Move;
import thedrake.core.move.impl.StepAndCapture;
import thedrake.core.move.impl.StepOnly;
import thedrake.core.utils.Offset2D;
import thedrake.core.utils.PlayingSide;

public class SlideAction extends TroopAction {

	public SlideAction(Offset2D offset) {
		super(offset);
	}

	public SlideAction(int offsetX, int offsetY) {
		super(offsetX, offsetY);
	}

	@Override
	public List<Move> movesFrom(BoardPos origin, PlayingSide side, GameState state) {
		List<Move> moves = new ArrayList<Move>();
		TilePos target = origin.stepByPlayingSide(offset(), side);
		while (target != BoardPos.OFF_BOARD) {
			if (state.canStep(origin, target)) {
				moves.add(new StepOnly(origin, (BoardPos) target));
			} else if (state.canCapture(origin, target)) {
				moves.add(new StepAndCapture(origin, (BoardPos) target));
				break;
			} else if (!state.canCapture(origin, target))
				break;
			target = target.stepByPlayingSide(offset(), side);
		}

		return moves;
	}

}
