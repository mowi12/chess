package de.wieland.Chess.engine.player.ai;

import com.google.common.annotations.VisibleForTesting;

import de.wieland.Chess.engine.board.Board;
import de.wieland.Chess.engine.board.Move;
import de.wieland.Chess.engine.pieces.Piece;
import de.wieland.Chess.engine.player.Player;

/**
 * Public final class StandardBoardEvaluator.
 * 
 * @author Moritz Wieland
 * @version 1.0
 * @date 10.09.2021
 */
public final class StandardBoardEvaluator implements BoardEvaluator {
	private static final int CHECKMATE_BONUS = 10000;
	private static final int CHECK_BONUS = 45;
	private static final int CASTLE_BONUS = 25;
	private static final int MOBILITY_MULTIPLIER = 5;
	private static final int ATTACK_MULTIPLIER = 1;
	private static final int TWO_BISHOPS_BONUS = 25;
	private static final int DEPTH_BONUS = 100;
	private static final StandardBoardEvaluator INSTANCE = new StandardBoardEvaluator();
	
	StandardBoardEvaluator() {}
	
	public static StandardBoardEvaluator get() {
		return INSTANCE;
	}

	@Override
	public int evaluate(final Board board,
						final int depth) {
		return scorePlayer(board, board.getWhitePlayer(), depth) -
			   scorePlayer(board, board.getBlackPlayer(), depth);
	}
	
	@VisibleForTesting
	private int scorePlayer(final Board board,
							final Player player,
							final int depth) {
		return pieceValue(player) +
			   mobility(player) +
			   attacks(player) +
			   kingThreats(player, depth) + 
			   castled(player);
	}
	
	private static int pieceValue(final Player player) {
		int pieceValueScore = 0;
		
		int numBishops = 0;
		for (final Piece piece : player.getActivePieces()) {
			pieceValueScore += piece.getPieceValue() + piece.locationBonus();
			
			if(piece.getPieceType().isBishop()) {
				numBishops++;
			}
		}
		
		return pieceValueScore + (numBishops == 2 ? TWO_BISHOPS_BONUS : 0);
	}
	
	private static int mobility(final Player player) {
		return mobilityRatio(player) * MOBILITY_MULTIPLIER;
	}
	
	private static int mobilityRatio(final Player player) {
		return (int) ((player.getLegalMoves().size() * 10.0f) / player.getOpponent().getLegalMoves().size());
	}
	
	private static int attacks(final Player player) {
        int attackScore = 0;
        
        for(final Move move : player.getLegalMoves()) {
            if(move.isAttack()) {
                final Piece movedPiece = move.getMovedPiece();
                final Piece attackedPiece = move.getAttackedPiece();
                
                if(movedPiece.getPieceValue() <= attackedPiece.getPieceValue()) {
                    attackScore++;
                }
            }
        }
        
        return attackScore * ATTACK_MULTIPLIER;
	}
	
	private static int kingThreats(final Player player,
								   final int depth) {
		return player.getOpponent().isInCheckMate() ? CHECKMATE_BONUS * depthBonus(depth) : check(player);
	}
	
	private static int check(final Player player) {
		return player.getOpponent().isInCheck() ? CHECK_BONUS : 0;
	}

	private static int depthBonus(final int depth) {
		return depth == 0 ? 1 : DEPTH_BONUS * depth;
	}
	
	private static int castled(final Player player) {
		return player.isCastled() ? CASTLE_BONUS : 0;
	}
	
	public String evaluationDetails(final Board board,
									final int depth) {
		return ("White Mobility : " + mobility(board.getWhitePlayer()) + "\n") +
                "White kingThreats : " + kingThreats(board.getWhitePlayer(), depth) + "\n" +
                "White attacks : " + attacks(board.getWhitePlayer()) + "\n" +
                "White castle : " + castled(board.getWhitePlayer()) + "\n" +
                "White pieceEval : " + pieceValue(board.getWhitePlayer()) + "\n" +
                "---------------------\n" +
                "Black Mobility : " + mobility(board.getBlackPlayer()) + "\n" +
                "Black kingThreats : " + kingThreats(board.getBlackPlayer(), depth) + "\n" +
                "Black attacks : " + attacks(board.getBlackPlayer()) + "\n" +
                "Black castle : " + castled(board.getBlackPlayer()) + "\n" +
                "Black pieceEval : " + pieceValue(board.getBlackPlayer()) + "\n" +
                "Final Score = " + evaluate(board, depth);
	}
}
