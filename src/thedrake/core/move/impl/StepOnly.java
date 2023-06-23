package thedrake.core.move.impl;

import thedrake.core.GameState;
import thedrake.core.board.BoardPos;
import thedrake.core.move.BoardMove;

public class StepOnly extends BoardMove {

    public StepOnly(BoardPos origin, BoardPos target) {
        super(origin, target);
    }

    @Override
    public GameState execute(GameState originState) {
        return originState.stepOnly(origin(), target());
    }

}
