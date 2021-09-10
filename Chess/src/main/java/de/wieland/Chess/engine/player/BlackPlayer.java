package de.wieland.Chess.engine.player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.ImmutableList;

import de.wieland.Chess.engine.Alliance;
import de.wieland.Chess.engine.board.Board;
import de.wieland.Chess.engine.board.Move;
import de.wieland.Chess.engine.board.Move.*;
import de.wieland.Chess.engine.board.Tile;
import de.wieland.Chess.engine.pieces.Piece;
import de.wieland.Chess.engine.pieces.Rook;

/**
 * public class BlackPlayer
 * 
 * @author Moritz Wieland
 * @version 1.0
 */
public class BlackPlayer extends Player {

	public BlackPlayer(final Board board,
					   final Collection<Move> whiteStandardLegalMoves,
					   final Collection<Move> blackStandardLegalMoves) {
		super(board, blackStandardLegalMoves, whiteStandardLegalMoves);
	}
	
	@Override
	public Collection<Move> calculateKingCastles(final Collection<Move> playerLegals,
												 final Collection<Move> opponentLegals) {
		final List<Move> kingCastles = new ArrayList<>();
		
		if(playerKing.isFirstMove() && !isInCheck()) {
			//blacks king side castle
			if(!board.getTile(5).isTileOccupied() && !board.getTile(6).isTileOccupied()) {
				final Tile rookTile = board.getTile(7);
				
				if(rookTile.isTileOccupied() && rookTile.getPiece().getPieceType().isRook() && rookTile.getPiece().isFirstMove()) {
					if(Player.calculateAttacksOnTile(5, opponentLegals).isEmpty() && Player.calculateAttacksOnTile(6, opponentLegals).isEmpty()) {
						kingCastles.add(new KingSideCastleMove(board, playerKing, 6, (Rook)rookTile.getPiece(), rookTile.getTileCoordinate(), 5));
					}
				}
			}
			
			//blacks queen side castle
			if(!board.getTile(3).isTileOccupied() && !board.getTile(2).isTileOccupied() && !board.getTile(1).isTileOccupied()) {
				final Tile rookTile = board.getTile(0);
				
				if(rookTile.isTileOccupied() && rookTile.getPiece().getPieceType().isRook() && rookTile.getPiece().isFirstMove()) {
					if(Player.calculateAttacksOnTile(3, opponentLegals).isEmpty() && Player.calculateAttacksOnTile(2, opponentLegals).isEmpty() && Player.calculateAttacksOnTile(1, opponentLegals).isEmpty()) {
						kingCastles.add(new QueenSideCastleMove(board, playerKing, 2, (Rook)rookTile.getPiece(), rookTile.getTileCoordinate(), 3));
					}
				}
			}
		}
		
		return ImmutableList.copyOf(kingCastles);
	}
	
	@Override
	public String toString() {
		return Alliance.BLACK.toString();
	}

	@Override
	public Collection<Piece> getActivePieces() { return board.getBlackPieces(); }
	@Override
	public Alliance getAlliance() { return Alliance.BLACK; }
	@Override
	public Player getOpponent() { return this.board.getWhitePlayer(); }
}
