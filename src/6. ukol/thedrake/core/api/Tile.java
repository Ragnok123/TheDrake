package thedrake.core.api;

import java.util.List;

import thedrake.core.GameState;
import thedrake.core.board.BoardPos;
import thedrake.core.move.Move;

public interface Tile {

    // Vrací True, pokud je tato dlaždice volná a lze na ni vstoupit.
    public boolean canStepOn();

    // Vrací True, pokud tato dlaždice obsahuje jednotku
    public boolean hasTroop();

    public List<Move> movesFrom(BoardPos pos, GameState state);
}
