package thedrake.core.troop;

import java.io.PrintWriter;

import thedrake.core.api.JSONSerializable;

public enum TroopFace implements JSONSerializable{
	
	AVERS, REVERS;

	@Override
	public void toJSON(PrintWriter writer) {
		writer.print("\"" + this.toString() + "\"");
	}
	
}