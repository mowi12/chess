package de.wieland.Chess.engine.board;

import de.wieland.Chess.engine.pieces.Piece;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

/**
 * Public abstract class Tile.
 * 
 * @author Moritz Wieland
 * @version 1.0
 * @date 10.09.2021
 */
public abstract class Tile {
	protected final int tileCoordinate;
	private static final Map<Integer, EmptyTile> EMPTY_TILES_CACHE = createAllPossibleEmptyTiles();
	
	Tile(final int tileCoordinate) {
		this.tileCoordinate = tileCoordinate;
	}
	
	private static Map<Integer, EmptyTile> createAllPossibleEmptyTiles() {
		final Map<Integer, EmptyTile> emptyTileMap = new HashMap<>();
		
		for(int i = 0; i < BoardUtils.NUM_TILES; i++) {
			emptyTileMap.put(i, new EmptyTile(i));
		}
		
		return ImmutableMap.copyOf(emptyTileMap);
	}
	
	public static Tile createTile(final int tileCoordinate, final Piece piece) {
		return piece != null ? new OccupiedTile(tileCoordinate, piece) : EMPTY_TILES_CACHE.get(tileCoordinate);
	}
	
	/**
	 * Abstract methods.
	 */
	public abstract boolean isTileOccupied();
	public abstract Piece getPiece();
	
	/**
	 * Getter method.
	 */
	public int getTileCoordinate() { return tileCoordinate; }
	
	
	/**
	 * Public static final class EmptyTile.
	 * 
	 * @author Moritz Wieland
	 * @version 1.0
	 * @date 10.09.2021
	 */
	public static final class EmptyTile extends Tile {
		
		private EmptyTile(final int tileCoordinate) {
			super(tileCoordinate);
		}

		@Override
		public boolean isTileOccupied() {
			return false;
		}

		@Override
		public Piece getPiece() {
			return null;
		}
		
		@Override
		public String toString() {
			return "-";
		}
	}
	
	
	/**
	 * Public static final class OccupiedTile.
	 * 
	 * @author Moritz Wieland
	 * @version 1.0
	 * @date 10.09.2021
	 */
	public static final class OccupiedTile extends Tile {
		private final Piece pieceOnTile;

		private OccupiedTile(final int tileCoordinate, final Piece pieceOnTile) {
			super(tileCoordinate);
			this.pieceOnTile = pieceOnTile;
		}

		@Override
		public boolean isTileOccupied() {
			return true;
		}

		@Override
		public Piece getPiece() {
			return pieceOnTile;
		}

		@Override
		public String toString() {
			return pieceOnTile.getPieceAlliance().isBlack() ? pieceOnTile.toString().toLowerCase() : pieceOnTile.toString();
		}
	}
}
