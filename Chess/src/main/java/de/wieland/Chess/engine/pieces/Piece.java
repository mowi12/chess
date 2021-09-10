package de.wieland.Chess.engine.pieces;

import java.util.Collection;

import de.wieland.Chess.engine.Alliance;
import de.wieland.Chess.engine.board.Board;
import de.wieland.Chess.engine.board.Move;

/**
 * public abstract class Piece
 * 
 * @author Moritz Wieland
 * @version 1.0
 * @date 10.09.2021
 */
public abstract class Piece {
	protected final PieceType pieceType;
	protected final int piecePosition;
	protected final Alliance pieceAlliance;
	protected final boolean isFirstMove;
	private final int cachedHashCode;
	
	Piece(final PieceType pieceType,
		  final int piecePosition,
		  final Alliance pieceAlliance,
		  final boolean isFirstMove) {
		this.pieceType = pieceType;
		this.piecePosition = piecePosition;
		this.pieceAlliance = pieceAlliance;
		this.isFirstMove = isFirstMove;
		cachedHashCode = computeHashCode();
	}
	
	@Override
	public boolean equals(final Object other) {
		if(this == other) {
			return true;
		}
		
		if(!(other instanceof Piece)) {
			return false;
		}
		
		final Piece otherPiece = (Piece) other;
		
		return (piecePosition == otherPiece.getPiecePosition()) && (pieceType == otherPiece.getPieceType()) &&
			   (pieceAlliance == otherPiece.getPieceAlliance());
	}
	
	@Override
	public int hashCode() {
		return cachedHashCode;
	}
	
	private int computeHashCode() {
		int result = pieceType.hashCode();
		result = 31 * result + pieceAlliance.hashCode();
		result = 31 * result + piecePosition;
		result = 31 * result + (isFirstMove ? 1 : 0);
		return result;
	}

	public abstract int locationBonus();
	public abstract Collection<Move> calculateLegalMoves(final Board board);
	public abstract Piece movePiece(Move move);

	public boolean isFirstMove() { return isFirstMove; }
	public PieceType getPieceType() { return pieceType; }
	public int getPiecePosition() { return piecePosition; }
	public Alliance getPieceAlliance() { return pieceAlliance; }
	public int getPieceValue() { return pieceType.getPieceValue(); }
	
	
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
}
