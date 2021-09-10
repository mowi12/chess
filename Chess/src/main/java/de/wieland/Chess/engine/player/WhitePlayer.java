package de.wieland.Chess.engine.player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.ImmutableList;

import de.wieland.Chess.engine.Alliance;
import de.wieland.Chess.engine.board.Board;
import de.wieland.Chess.engine.board.Move;
import de.wieland.Chess.engine.board.Tile;
import de.wieland.Chess.engine.board.Move.*;
import de.wieland.Chess.engine.pieces.Piece;
import de.wieland.Chess.engine.pieces.Rook;

/**
 * public class WhitePlayer
 * 
 * @author Moritz Wieland
 * @version 1.0
 * @date 10.09.2021
 */
public class WhitePlayer extends Player {

	public WhitePlayer(final Board board,
					   final Collection<Move> whiteStandardLegalMoves,
					   final Collection<Move> blackStandardLegalMoves) {
		super(board, whiteStandardLegalMoves, blackStandardLegalMoves);
	}
	
	@Override
	public Collection<Move> calculateKingCastles(final Collection<Move> playerLegals,
												 final Collection<Move> opponentLegals) {
		final List<Move> kingCastles = new ArrayList<>();
		
		if(playerKing.isFirstMove() && !isInCheck()) {
			//whites king side castle
			if(!board.getTile(61).isTileOccupied() && !board.getTile(62).isTileOccupied()) {
				final Tile rookTile = board.getTile(63);
				
				if(rookTile.isTileOccupied() && rookTile.getPiece().getPieceType().isRook() && rookTile.getPiece().isFirstMove()) {
					if(Player.calculateAttacksOnTile(61, opponentLegals).isEmpty() && Player.calculateAttacksOnTile(62, opponentLegals).isEmpty()) {
						kingCastles.add(new KingSideCastleMove(board, playerKing, 62, (Rook)rookTile.getPiece(), rookTile.getTileCoordinate(), 61));
					}
				}
			}
			
			//whites queen side castle
			if(!board.getTile(59).isTileOccupied() && !board.getTile(58).isTileOccupied() && !board.getTile(57).isTileOccupied()) {
				final Tile rookTile = board.getTile(56);
				
				if(rookTile.isTileOccupied() && rookTile.getPiece().getPieceType().isRook() && rookTile.getPiece().isFirstMove()) {
					if(Player.calculateAttacksOnTile(59, opponentLegals).isEmpty() && Player.calculateAttacksOnTile(58, opponentLegals).isEmpty() && Player.calculateAttacksOnTile(57, opponentLegals).isEmpty()) {
						kingCastles.add(new QueenSideCastleMove(board, playerKing, 58, (Rook)rookTile.getPiece(), rookTile.getTileCoordinate(), 59));
					}
				}
			}
		}
		
		kingCastles.forEach(e -> e.toString());
		
		return ImmutableList.copyOf(kingCastles);
	}
	
	@Override
	public String toString() {
		return Alliance.WHITE.toString();
	}

	@Override
	public Collection<Piece> getActivePieces() { return board.getWhitePieces(); }
	@Override
	public Alliance getAlliance() { return Alliance.WHITE; }
	@Override
	public Player getOpponent() { return this.board.getBlackPlayer(); }
}
