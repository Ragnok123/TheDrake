package thedrake.core.ui;

import javafx.scene.layout.Pane;
import thedrake.core.troop.Troop;
import thedrake.core.troop.TroopFace;
import thedrake.core.utils.PlayingSide;

class StackTileView extends Pane {
    public StackTileView(Troop troop, PlayingSide playingSide) {
        setPrefSize(100,100);
        TileBackgrounds troopBkg = new TileBackgrounds();
        setBackground(troopBkg.getTroop(troop,playingSide, TroopFace.AVERS));
    }
}