package thedrake.core.ui;

import thedrake.core.move.Move;

public interface TileViewContext {

    void tileViewSelected(TileView tileView);

    void executeMove(Move move);

}
