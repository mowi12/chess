package de.wieland.Chess.engine.pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.ImmutableList;

import de.wieland.Chess.engine.Alliance;
import de.wieland.Chess.engine.board.Board;
import de.wieland.Chess.engine.board.BoardUtils;
import de.wieland.Chess.engine.board.Move;
import de.wieland.Chess.engine.board.Tile;
import de.wieland.Chess.engine.board.Move.MajorAttackMove;
import de.wieland.Chess.engine.board.Move.MajorMove;

/**
 * Public class Queen.
 * 
 * @author Moritz Wieland
 * @version 1.0
 * @date 10.09.2021
 */
public class Queen extends Piece {
	private static final int[] CANDIDATE_MOVE_VECTOR_COORDINATES = {-9, -8, -7, -1, 1, 7, 8, 9};

	public Queen(final Alliance pieceAlliance,
				 final int piecePosition) {
		super(PieceType.QUEEN, piecePosition, pieceAlliance, true);
	}
	
	public Queen(final Alliance pieceAlliance,
				 final int piecePosition,
				 final boolean isFirstMove) {
		super(PieceType.QUEEN, piecePosition, pieceAlliance, isFirstMove);
	}

	@Override
	public Collection<Move> calculateLegalMoves(final Board board) {
		final  List<Move> legalMoves = new ArrayList<>();
		
		for (final int candidateCoordinateOffset : CANDIDATE_MOVE_VECTOR_COORDINATES) {
			int candidateDestinationCoordinate = this.piecePosition;
			
			while (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
				if(isFirstColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset) || isEighthColumnExclusion(candidateDestinationCoordinate, candidateCoordinateOffset)) {
					break;
				}
				
				candidateDestinationCoordinate += candidateCoordinateOffset;
				
				if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
					final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
					
					if(!candidateDestinationTile.isTileOccupied()) {
						legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
					} else {
						final Piece pieceAtDestination = candidateDestinationTile.getPiece();
						final Alliance pieceAlliance = pieceAtDestination.pieceAlliance;
						
						if(this.pieceAlliance != pieceAlliance) {
							legalMoves.add(new MajorAttackMove(board, this, candidateDestinationCoordinate, pieceAtDestination));
						}
						
						break;
					}
				}
			}
		}
		
		return ImmutableList.copyOf(legalMoves);
	}
	
	@Override
	public int locationBonus() {
		return pieceAlliance.queenBonus(piecePosition);
	}
	
	@Override
	public Queen movePiece(final Move move) {
		return new Queen(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
	}
	
	@Override
	public String toString() {
		return pieceType.toString();
	}
	
	private static boolean isFirstColumnExclusion(final int currentPosition,
												  final int CandidateOffset) {
		return BoardUtils.FIRST_COLUMN[currentPosition] &&
			   ((CandidateOffset == -9) || (CandidateOffset == -1) || (CandidateOffset == 7));
	}
	
	private static boolean isEighthColumnExclusion(final int currentPosition,
												   final int CandidateOffset) {
		return BoardUtils.EIGHTH_COLUMN[currentPosition] &&
			   ((CandidateOffset == -7) || (CandidateOffset == 1) || (CandidateOffset == 9));
	}
}
