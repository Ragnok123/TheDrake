package thedrake.core.utils;

import thedrake.core.GameState;

public interface Controller {
	
	void onGameStateChange(GameState state);
	
}