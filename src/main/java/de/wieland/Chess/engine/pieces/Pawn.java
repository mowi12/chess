package de.wieland.Chess.engine.pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.ImmutableList;

import de.wieland.Chess.engine.Alliance;
import de.wieland.Chess.engine.board.Board;
import de.wieland.Chess.engine.board.BoardUtils;
import de.wieland.Chess.engine.board.Move;
import de.wieland.Chess.engine.board.Move.PawnMove;
import de.wieland.Chess.engine.board.Move.PawnJump;
import de.wieland.Chess.engine.board.Move.PawnAttackMove;
import de.wieland.Chess.engine.board.Move.PawnPromotion;
import de.wieland.Chess.engine.board.Move.PawnEnPassantAttackMove;

/**
 * Public class Pawn.
 * 
 * @author Moritz Wieland
 * @version 1.0
 * @date 10.09.2021
 */
public class Pawn extends Piece {
	private static final int[] CANDIDATE_MOVE_COORDINATES = {7, 8, 9, 16};

	public Pawn(final Alliance pieceAlliance,
				final int piecePosition) {
		super(PieceType.PAWN, piecePosition, pieceAlliance, true);
	}
	
	public Pawn(final Alliance pieceAlliance,
				final int piecePosition,
				final boolean isFirstMove) {
		super(PieceType.PAWN, piecePosition, pieceAlliance, isFirstMove);
	}

	@Override
	public Collection<Move> calculateLegalMoves(final Board board) {
		final List<Move> legalMoves = new ArrayList<>();
		
		for (final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATES) {
			final int candidateDestinationCoordinate = this.piecePosition + (this.pieceAlliance.getDirection() * currentCandidateOffset);
			
			if(!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
				continue;
			}
			
			if(currentCandidateOffset == 8 && !board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
				if(pieceAlliance.isPawnPromotionSquare(candidateDestinationCoordinate)) {
					legalMoves.add(new PawnPromotion(
							new PawnMove(board, this, candidateDestinationCoordinate), new Queen(pieceAlliance, candidateDestinationCoordinate, false)));
					legalMoves.add(new PawnPromotion(
							new PawnMove(board, this, candidateDestinationCoordinate), new Rook(pieceAlliance, candidateDestinationCoordinate, false)));
					legalMoves.add(new PawnPromotion(
							new PawnMove(board, this, candidateDestinationCoordinate), new Bishop(pieceAlliance, candidateDestinationCoordinate, false)));
					legalMoves.add(new PawnPromotion(
							new PawnMove(board, this, candidateDestinationCoordinate), new Knight(pieceAlliance, candidateDestinationCoordinate, false)));
				} else {
					legalMoves.add(new PawnMove(board, this, candidateDestinationCoordinate));
				}
			} else if(currentCandidateOffset == 16 && this.isFirstMove() &&
					 ((BoardUtils.SEVENTH_RANK[this.piecePosition] && this.pieceAlliance.isBlack()) ||
					 (BoardUtils.SECOND_RANK[this.piecePosition] && this.pieceAlliance.isWhite()))) {
				final int behindCandidateDestinationCoordinate = this.piecePosition + (this.pieceAlliance.getDirection() * 8);
				
				if(!board.getTile(behindCandidateDestinationCoordinate).isTileOccupied() &&
				   !board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
					legalMoves.add(new PawnJump(board, this, candidateDestinationCoordinate));
				}
			} else if((currentCandidateOffset == 7) &&
					  !((BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack()) ||
						(BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite()))) {
				if(board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
					final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
					
					if(this.pieceAlliance != pieceOnCandidate.pieceAlliance) {
						if(pieceAlliance.isPawnPromotionSquare(candidateDestinationCoordinate)) {
							legalMoves.add(new PawnPromotion(
									new PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate), new Queen(pieceAlliance, candidateDestinationCoordinate, false)));
							legalMoves.add(new PawnPromotion(
									new PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate), new Rook(pieceAlliance, candidateDestinationCoordinate, false)));
							legalMoves.add(new PawnPromotion(
									new PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate), new Bishop(pieceAlliance, candidateDestinationCoordinate, false)));
							legalMoves.add(new PawnPromotion(
									new PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate), new Knight(pieceAlliance, candidateDestinationCoordinate, false)));
						} else {
							legalMoves.add(new PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
						}						
					}
				} else if(board.getEnPassantPawn() != null) {
					if(board.getEnPassantPawn().getPiecePosition() == (piecePosition + (this.pieceAlliance.getOppositeDirection()))) {
						final Piece pieceOnCandidate = board.getEnPassantPawn();
						
						if(this.pieceAlliance != pieceOnCandidate.getPieceAlliance()) {
							legalMoves.add(new PawnEnPassantAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
						}
					}
				}
			} else if((currentCandidateOffset == 9) &&
					  !((BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite()) ||
						(BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack()))) {
				if(board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
					final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
					
					if(this.pieceAlliance != pieceOnCandidate.pieceAlliance) {
						if(pieceAlliance.isPawnPromotionSquare(candidateDestinationCoordinate)) {
							legalMoves.add(new PawnPromotion(
									new PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate), new Queen(pieceAlliance, candidateDestinationCoordinate, false)));
							legalMoves.add(new PawnPromotion(
									new PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate), new Rook(pieceAlliance, candidateDestinationCoordinate, false)));
							legalMoves.add(new PawnPromotion(
									new PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate), new Bishop(pieceAlliance, candidateDestinationCoordinate, false)));
							legalMoves.add(new PawnPromotion(
									new PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate), new Knight(pieceAlliance, candidateDestinationCoordinate, false)));
						} else {
							legalMoves.add(new PawnAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
						}					}
				} else if(board.getEnPassantPawn() != null) {
					if(board.getEnPassantPawn().getPiecePosition() == (piecePosition - (this.pieceAlliance.getOppositeDirection()))) {
						final Piece pieceOnCandidate = board.getEnPassantPawn();
						
						if(this.pieceAlliance != pieceOnCandidate.getPieceAlliance()) {
							legalMoves.add(new PawnEnPassantAttackMove(board, this, candidateDestinationCoordinate, pieceOnCandidate));
						}
					}
				}
			}
		}
		
		return ImmutableList.copyOf(legalMoves);
	}
	
	@Override
	public int locationBonus() {
		return pieceAlliance.pawnBonus(piecePosition);
	}
	
	@Override
	public Pawn movePiece(final Move move) {
		return new Pawn(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
	}
	
	@Override
	public String toString() {
		return pieceType.toString();
	}
}
