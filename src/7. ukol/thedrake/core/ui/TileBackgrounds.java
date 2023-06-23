package thedrake.core.ui;

import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.paint.Color;
import thedrake.core.api.BoardTile;
import thedrake.core.api.Tile;
import thedrake.core.troop.Troop;
import thedrake.core.troop.TroopFace;
import thedrake.core.troop.TroopTile;
import thedrake.core.utils.PlayingSide;

public class TileBackgrounds {

	public static final Background EMPTY_BG = new Background(
			new BackgroundFill(new Color(0.9, 0.9, 0.9, 1), null, null));
	private final Background mountainBg;

	public TileBackgrounds() {
		Image img = new Image(getClass().getResourceAsStream("/assets/mountain.png"));
		this.mountainBg = new Background(new BackgroundImage(img, null, null, null, null));
	}

	public Background get(Tile tile) {
		if (tile.hasTroop()) {
			TroopTile armyTile = ((TroopTile) tile);
			return getTroop(armyTile.troop(), armyTile.side(), armyTile.face());
		}

		if (tile == BoardTile.MOUNTAIN) {
			return mountainBg;
		}

		return EMPTY_BG;
	}

	public Background getTroop(Troop info, PlayingSide side, TroopFace face) {
		TroopView images = new TroopView(info.name());
		BackgroundImage bgImage = new BackgroundImage(images.get(side, face), null, null, null, null);

		return new Background(bgImage);
	}
}
