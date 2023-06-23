package thedrake.core.board;

import java.io.PrintWriter;

import thedrake.core.api.BoardTile;
import thedrake.core.api.JSONSerializable;
import thedrake.core.api.TilePos;
import thedrake.core.factory.PositionFactory;

public class Board implements JSONSerializable {

	private final int dimension;
	private final BoardTile[][] tiles;
	private final PositionFactory factory;

	public Board(int dimension) {
		this.dimension = dimension;
		this.tiles = new BoardTile[dimension][dimension];
		for (int j = 0; j < dimension; ++j) {
			for (int i = 0; i < dimension; ++i) {
				tiles[i][j] = BoardTile.EMPTY;
			}
		}
		this.factory = new PositionFactory(this.dimension);
	}

	public Board(int dimension, BoardTile[][] tiles) {
		this.dimension = dimension;
		this.tiles = tiles;
		this.factory = new PositionFactory(this.dimension);
	}

	public int dimension() {
		return this.dimension;
	}

	public BoardTile at(TilePos pos) {
		return tiles[pos.i()][pos.j()];
	}

	public Board withTiles(TileAt... ats) {
		BoardTile[][] clone = new BoardTile[this.tiles.length][];
		for (int i = 0; i < tiles.length; ++i) {
			clone[i] = tiles[i].clone();
		}

		int n = ats.length;
		for (int i = 0; i < n; ++i) {
			TileAt tileAt = ats[i];
			clone[tileAt.pos.i()][tileAt.pos.j()] = tileAt.tile;
		}
		return new Board(this.dimension, clone);
	}

	public PositionFactory positionFactory() {
		return this.factory;
	}

	public static class TileAt {
		public final BoardPos pos;
		public final BoardTile tile;

		public TileAt(BoardPos pos, BoardTile tile) {
			this.pos = pos;
			this.tile = tile;
		}
	}

	public void toJSON(PrintWriter writer) {
		writer.print("{\"dimension\":" + dimension);
		writer.print(",\"tiles\":[");
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[i].length; j++) {
				tiles[j][i].toJSON(writer);
				if (!(i == tiles.length - 1 && j == tiles[i].length - 1)) writer.print(",");
			}
		}

		writer.print("]}");

	}
}
