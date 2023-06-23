package thedrake.core.board;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import thedrake.core.api.JSONSerializable;
import thedrake.core.api.TilePos;
import thedrake.core.troop.Troop;
import thedrake.core.troop.TroopFace;
import thedrake.core.troop.TroopTile;
import thedrake.core.utils.PlayingSide;

public class BoardTroops implements JSONSerializable {
	private final PlayingSide playingSide;
	private final Map<BoardPos, TroopTile> troopMap;
	private final TilePos leaderPosition;
	private final int guards;

	public BoardTroops(PlayingSide playingSide) {
		this(playingSide, new HashMap<BoardPos, TroopTile>(), TilePos.OFF_BOARD, 0);
	}

	public BoardTroops(PlayingSide playingSide, Map<BoardPos, TroopTile> troopMap, TilePos leaderPosition, int guards) {
		this.playingSide = playingSide;
		this.troopMap = troopMap;
		this.leaderPosition = leaderPosition;
		this.guards = guards;
	}

	public Optional<TroopTile> at(TilePos pos) {
		if (this.troopMap.containsKey(pos)) {
			return Optional.of(this.troopMap.get(pos));
		}
		return Optional.empty();
	}

	public PlayingSide playingSide() {
		return this.playingSide;
	}

	public TilePos leaderPosition() {
		return this.leaderPosition;
	}

	public int guards() {
		return this.guards;
	}

	public boolean isLeaderPlaced() {
		return this.leaderPosition != TilePos.OFF_BOARD;
	}

	public boolean isPlacingGuards() {
		if (this.leaderPosition != TilePos.OFF_BOARD) {
			if (this.guards < 2) {
				return true;
			}
			return false;
		}
		return false;
	}

	public Set<BoardPos> troopPositions() {
		return this.troopMap.keySet();
	}

	public BoardTroops placeTroop(Troop troop, BoardPos target) {
		BoardTroops board = null;
		if (this.at(target).isPresent()) {
			throw new IllegalArgumentException("Set is already placed.");
		} else {
			HashMap<BoardPos, TroopTile> newTroops = new HashMap<BoardPos, TroopTile>();
			for (Map.Entry<BoardPos, TroopTile> entry : this.troopMap.entrySet()) {
				newTroops.put(entry.getKey(), entry.getValue());
			}
			newTroops.put(target, new TroopTile(troop, this.playingSide, TroopFace.AVERS));

			int newGuards = this.guards;
			if (this.isPlacingGuards()) {
				++newGuards;
			}

			TilePos newLeaderPos = this.leaderPosition;
			if (this.leaderPosition == TilePos.OFF_BOARD) {
				newLeaderPos = target;
			}
			board = new BoardTroops(this.playingSide, newTroops, newLeaderPos, newGuards);
		}
		return board;
	}

	public BoardTroops troopStep(BoardPos origin, BoardPos target) {
		if (!isLeaderPlaced()) {
			throw new IllegalStateException("Leader is not set.");
		} else if (this.guards < 2) {
			throw new IllegalStateException("Guards are not set yet.");
		}
		if (!this.at(origin).isPresent()) {
			throw new IllegalArgumentException("Origin source " + target.toString() + " is not set.");
		} else if (this.at(target).isPresent()) {
			throw new IllegalArgumentException("Target source " + target.toString() + " is already set.");
		}
		HashMap<BoardPos, TroopTile> newTroops = new HashMap<BoardPos, TroopTile>();
		for (Map.Entry<BoardPos, TroopTile> entry : this.troopMap.entrySet()) {
			newTroops.put(entry.getKey(), entry.getValue());
		}
		TroopTile tile = newTroops.remove(origin);
		newTroops.put(target, tile.flipped());
		TilePos newLeaderPos = this.leaderPosition;
		if (this.leaderPosition.equals(origin)) {
			newLeaderPos = target;
		}
		return new BoardTroops(this.playingSide, newTroops, newLeaderPos, this.guards);
	}

	public BoardTroops troopFlip(BoardPos origin) {
		if (!isLeaderPlaced()) {
			throw new IllegalStateException("Cannot move troops before the leader is placed.");
		}

		if (isPlacingGuards()) {
			throw new IllegalStateException("Cannot move troops before guards are placed.");
		}

		if (!at(origin).isPresent())
			throw new IllegalArgumentException();

		Map<BoardPos, TroopTile> newTroops = new HashMap<>(troopMap);
		TroopTile tile = newTroops.remove(origin);
		newTroops.put(origin, tile.flipped());

		return new BoardTroops(playingSide(), newTroops, leaderPosition, guards);
	}

	public BoardTroops removeTroop(BoardPos target) {
		if (!isLeaderPlaced()) {
			throw new IllegalStateException("Leader is not set.");
		} else if (this.guards < 2) {
			throw new IllegalStateException("Guards are not set yet.");
		}
		if (!this.at(target).isPresent()) {
			throw new IllegalArgumentException("Target source " + target.toString() + " is not set yet.");
		}

		Map<BoardPos, TroopTile> newMap = new HashMap<>();
		for (Map.Entry<BoardPos, TroopTile> entry : this.troopMap.entrySet()) {
			newMap.put(entry.getKey(), entry.getValue());
		}
		newMap.remove(target);

		TilePos newPos = this.leaderPosition;
		if (this.leaderPosition.equals(target)) {
			newPos = TilePos.OFF_BOARD;
		}
		return new BoardTroops(this.playingSide, newMap, newPos, this.guards);

	}

	@Override
	public void toJSON(PrintWriter writer) {
		writer.print("{\"side\":");
		playingSide.toJSON(writer);
		writer.print(",\"leaderPosition\":");
		leaderPosition.toJSON(writer);
		writer.print(",\"guards\":" + guards);
		writer.print(",\"troopMap\":");

		writer.print("{");
		ArrayList<Map.Entry<BoardPos, TroopTile>> troops = new ArrayList<Map.Entry<BoardPos, TroopTile>>(troopMap.entrySet());
		troops.sort(Comparator.comparing(a -> a.getKey().toString()));
		Iterator<Map.Entry<BoardPos, TroopTile>> iter = troops.iterator();
		while (iter.hasNext()) {
			Map.Entry<BoardPos, TroopTile> cur = iter.next();
			cur.getKey().toJSON(writer);
			writer.print(":");
			cur.getValue().toJSON(writer);
			if (iter.hasNext())
				writer.print(",");

		}
		writer.print("}}");

	}
}
