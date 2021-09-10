package de.wieland.Chess.engine.player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import de.wieland.Chess.engine.Alliance;
import de.wieland.Chess.engine.board.Board;
import de.wieland.Chess.engine.board.Move;
import de.wieland.Chess.engine.board.MoveStatus;
import de.wieland.Chess.engine.board.MoveTransition;
import de.wieland.Chess.engine.pieces.King;
import de.wieland.Chess.engine.pieces.Piece;

/**
 * public abstract class Player
 * 
 * @author Moritz Wieland
 * @version 1.0
 * @date 10.09.2021
 */
public abstract class Player {
	protected final Board board;
	protected final King playerKing;
	protected final Collection<Move> legalMoves;
	private boolean isInCheck;
	
	Player(final Board board,
		   final Collection<Move> legalMoves,
		   final Collection<Move> opponentLegalMoves) {
		this.board = board;
		this.playerKing = establishKing();
		this.legalMoves = ImmutableList.copyOf(Iterables.concat(legalMoves, calculateKingCastles(legalMoves, opponentLegalMoves)));
		isInCheck = !calculateAttacksOnTile(this.playerKing.getPiecePosition(), opponentLegalMoves).isEmpty();
	}

	private King establishKing() {
		for (final Piece piece : getActivePieces()) {
			if(piece.getPieceType().isKing()) {
				return (King) piece;
			}
		}
		
		throw new RuntimeException("Should not reach here! Not a valid board!");
	}
	
	protected static Collection<Move> calculateAttacksOnTile(final int piecePosition, Collection<Move> opponentMoves) {
		final List<Move> attackMoves = new ArrayList<>();
		
		for (final Move move : opponentMoves) {
			if(piecePosition == move.getDestinationCoordinate()) {
				attackMoves.add(move);
			}
		}
		
		return ImmutableList.copyOf(attackMoves);
	}
	
	public MoveTransition makeMove(final Move move) {
		if(!isMoveLegal(move)) {
			return new MoveTransition(board, board, move, MoveStatus.ILLEGAL_MOVE);
		}
		
		final Board transitionBoard = move.execute();
		
		final Collection<Move> kingAttacks = Player.calculateAttacksOnTile(transitionBoard.getCurrentPlayer().getOpponent().getPlayerKing().getPiecePosition(),
																		   transitionBoard.getCurrentPlayer().getLegalMoves());
		
		if (!kingAttacks.isEmpty()) {
			return new MoveTransition(board, board, move, MoveStatus.LEAVES_PLAYER_IN_CHECK);
		}
		
		return new MoveTransition(board, transitionBoard, move, MoveStatus.DONE);
	}
	
	public MoveTransition unMakeMove(final Move move) {
		return new MoveTransition(this.board, move.undo(), move, MoveStatus.DONE);
	}
	
	protected boolean hasEscapeMoves() {
		for (final Move move : legalMoves) {
			final MoveTransition transition = makeMove(move);
			
			if(transition.getMoveStatus().isDone()) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean isMoveLegal(final Move move) { return legalMoves.contains(move); }
	public boolean isInCheck() { return isInCheck; }
	public boolean isInCheckMate() { return isInCheck && !hasEscapeMoves(); }
	public boolean isInStaleMate() { return !isInCheck && !hasEscapeMoves(); }
	public boolean isKingSideCastleCapable() { return this.playerKing.isKingSideCastleCapable(); }
    public boolean isQueenSideCastleCapable() { return this.playerKing.isQueenSideCastleCapable(); }
	public boolean isCastled() { return playerKing.isCastled(); }
	
	public King getPlayerKing() { return playerKing; }
	public Collection<Move> getLegalMoves() { return legalMoves; }
	
	public abstract Collection<Move> calculateKingCastles(final Collection<Move> playerLegals, final Collection<Move> opponentLegals);
	public abstract Collection<Piece> getActivePieces();
	public abstract Alliance getAlliance();
	public abstract Player getOpponent();
}
