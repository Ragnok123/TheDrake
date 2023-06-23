package thedrake.core.utils;

import java.io.PrintWriter;

import thedrake.core.api.JSONSerializable;

public enum PlayingSide implements JSONSerializable{
	
	ORANGE, BLUE;

	@Override
	public void toJSON(PrintWriter writer) {
		// TODO Auto-generated method stub
		writer.print("\"" + this.toString() + "\"");
	}
	
}