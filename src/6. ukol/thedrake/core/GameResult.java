package thedrake.core;

import java.io.PrintWriter;

import thedrake.core.api.JSONSerializable;

public enum GameResult implements JSONSerializable{
    VICTORY, DRAW, IN_PLAY;
	
	public void toJSON(PrintWriter writer) {
		writer.print("\"" + this + "\"");
	}
}
