package de.wieland.Chess.engine.pieces;

/**
 * Public enum PieceType.
 * 
 * @author Moritz Wieland
 * @version 1.0
 * @date 10.09.2021
 */
public enum PieceType {
	PAWN("P", 100) {
		@Override
		public boolean isKing() {
			return false;
		}

		@Override
		public boolean isRook() {
			return false;
		}

		@Override
		public boolean isBishop() {
			return false;
		}
	},
	KNIGHT("N", 300) {
		@Override
		public boolean isKing() {
			return false;
		}
		
		@Override
		public boolean isRook() {
			return false;
		}
		
		@Override
		public boolean isBishop() {
			return false;
		}
	},
	BISHOP("B", 300) {
		@Override
		public boolean isKing() {
			return false;
		}
		
		@Override
		public boolean isRook() {
			return false;
		}
		
		@Override
		public boolean isBishop() {
			return true;
		}
	},
	ROOK("R", 500) {
		@Override
		public boolean isKing() {
			return false;
		}
		
		@Override
		public boolean isRook() {
			return true;
		}
		
		@Override
		public boolean isBishop() {
			return false;
		}
	},
	QUEEN("Q", 900) {
		@Override
		public boolean isKing() {
			return false;
		}
		
		@Override
		public boolean isRook() {
			return false;
		}
		
		@Override
		public boolean isBishop() {
			return false;
		}
	},
	KING("K", 10000) {
		@Override
		public boolean isKing() {
			return true;
		}
		
		@Override
		public boolean isRook() {
			return false;
		}
		
		@Override
		public boolean isBishop() {
			return false;
		}
	};
	
	private final String pieceName;
	private final int pieceValue;
	
	PieceType(final String pieceName, final int pieceValue) {
		this.pieceName = pieceName;
		this.pieceValue = pieceValue;
	}
	
	@Override
	public String toString() {
		return pieceName;
	}
	
	public abstract boolean isKing();
	public abstract boolean isRook();
	public abstract boolean isBishop();
	
	public int getPieceValue() { return this.pieceValue; }
}