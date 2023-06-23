package thedrake.core.move.impl;

import thedrake.core.GameState;
import thedrake.core.board.BoardPos;
import thedrake.core.move.BoardMove;

public class StepAndCapture extends BoardMove {

    public StepAndCapture(BoardPos origin, BoardPos target) {
        super(origin, target);
    }

    @Override
    public GameState execute(GameState originState) {
        return originState.stepAndCapture(origin(), target());
    }

}
