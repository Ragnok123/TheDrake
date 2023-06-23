package thedrake.core.ui;

import javafx.scene.image.Image;
import thedrake.core.troop.TroopFace;
import thedrake.core.utils.PlayingSide;

import java.io.InputStream;

public class TroopView{
	private final Image troopFrontBlue;
	private final Image troopBackBlue;
	private final Image troopFrontOrange;
	private final Image troopBackOrange;

	public TroopView(String troopName) {
		troopFrontBlue = new Image(assetFromJAR("front" + troopName + "B"));
		troopBackBlue = new Image(assetFromJAR("back" + troopName + "B"));
		troopFrontOrange = new Image(assetFromJAR("front" + troopName + "O"));
		troopBackOrange = new Image(assetFromJAR("back" + troopName + "O"));
	}

	private InputStream assetFromJAR(String fileName) {
		return getClass().getResourceAsStream("/assets/" + fileName + ".png");
	}

	public Image get(PlayingSide side, TroopFace face) {
		Image image = null;
		switch(side) {
		case BLUE:
			if(face == TroopFace.AVERS) {
				image = troopFrontBlue;
			} else {
				image = troopBackBlue;
			}
			break;
		case ORANGE:
			if(face == TroopFace.AVERS) {
				image = troopFrontOrange;
			} else {
				image = troopBackOrange;
			}
			break;
		}
		return image;
	}
}
