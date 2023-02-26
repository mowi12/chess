package de.wieland.Chess.engine.pieces;

import java.util.Collection;

import de.wieland.Chess.engine.Alliance;
import de.wieland.Chess.engine.board.Board;
import de.wieland.Chess.engine.board.Move;

/**
 * Public abstract class Piece.
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

	/**
	 * Abstract methods.
	 */
	public abstract int locationBonus();
	public abstract Collection<Move> calculateLegalMoves(final Board board);
	public abstract Piece movePiece(Move move);
	
	/**
	 * Getter and Setter methods.
	 */
	public boolean isFirstMove() { return isFirstMove; }
	public PieceType getPieceType() { return pieceType; }
	public int getPiecePosition() { return piecePosition; }
	public Alliance getPieceAlliance() { return pieceAlliance; }
	public int getPieceValue() { return pieceType.getPieceValue(); }
}
