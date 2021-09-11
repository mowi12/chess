package de.wieland.Chess.engine.player.ai;

import de.wieland.Chess.engine.board.Board;
import de.wieland.Chess.engine.board.Move;

/**
 * Public interface MoveStrategy.
 * 
 * @author Moritz Wieland
 * @version 1.0
 * @date 10.09.2021
 */
public interface MoveStrategy {
	Move execute(final Board board);
	long getNumBoardsEvaluated();
}
