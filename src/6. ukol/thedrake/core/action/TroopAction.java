package thedrake.core.action;

import java.util.List;

import thedrake.core.GameState;
import thedrake.core.board.BoardPos;
import thedrake.core.move.Move;
import thedrake.core.utils.Offset2D;
import thedrake.core.utils.PlayingSide;

public abstract class TroopAction {
    private final Offset2D offset;

    protected TroopAction(int offsetX, int offsetY) {
        this(new Offset2D(offsetX, offsetY));
    }

    public TroopAction(Offset2D offset) {
        this.offset = offset;
    }

    public Offset2D offset() {
        return offset;
    }

    public abstract List<Move> movesFrom(BoardPos origin, PlayingSide side, GameState state);
}
