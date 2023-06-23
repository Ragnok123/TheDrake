package thedrake.core.troop;

import java.io.PrintWriter;
import java.util.List;

import thedrake.core.action.TroopAction;
import thedrake.core.api.JSONSerializable;
import thedrake.core.utils.Offset2D;

public class Troop implements JSONSerializable {

	private String troopName;
	private Offset2D aversPivot;
	private Offset2D reversPivot;
	private final List<TroopAction> aversActions;
	private final List<TroopAction> reversActions;

	public Troop(String name, Offset2D aversPivot, Offset2D reversPivot, List<TroopAction> avers,
			List<TroopAction> revers) {
		this.troopName = name;
		this.aversPivot = aversPivot;
		this.reversPivot = reversPivot;
		this.aversActions = avers;
		this.reversActions = revers;
	}

	public Troop(String name, Offset2D pivot, List<TroopAction> avers, List<TroopAction> revers) {
		this(name, pivot, pivot, avers, revers);
	}

	public Troop(String name, List<TroopAction> avers, List<TroopAction> revers) {
		this(name, new Offset2D(1, 1), avers, revers);
	}

	public String name() {
		return this.troopName;
	}

	public Offset2D pivot(TroopFace face) {
		return face == TroopFace.AVERS ? this.aversPivot : this.reversPivot;
	}

	public List<TroopAction> actions(TroopFace face) {
		return face == TroopFace.AVERS ? this.aversActions : this.reversActions;
	}

	@Override
	public void toJSON(PrintWriter writer) {
		writer.print("\"" + troopName + "\"");

	}

}