package thedrake.core.move.impl;

import thedrake.core.GameState;
import thedrake.core.board.BoardPos;
import thedrake.core.move.Move;

public class PlaceFromStack extends Move {

    public PlaceFromStack(BoardPos target) {
        super(target);
    }

    @Override
    public GameState execute(GameState originState) {
        return originState.placeFromStack(target());
    }

}
