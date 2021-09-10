package de.wieland.Chess.engine.player.ai;

import de.wieland.Chess.engine.board.Board;

/**
 * public interface BoardEvaluator
 * 
 * @author Moritz Wieland
 * @version 1.0
 * @date 10.09.2021
 */
public interface BoardEvaluator {
	int evaluate(final Board board, final int depth);
}
