package de.wieland.Chess;

import static org.junit.Assert.*;

import org.junit.Test;

import de.wieland.Chess.engine.board.Board;
import de.wieland.Chess.engine.board.BoardUtils;
import de.wieland.Chess.engine.board.Move;
import de.wieland.Chess.engine.board.MoveTransition;
import de.wieland.Chess.engine.player.ai.MiniMax;
import de.wieland.Chess.engine.player.ai.MoveStrategy;

public class TestAI {
	@Test
	public void testFoolsMate() {
		final Board board = Board.createStandardBoard();
		final MoveTransition t1 = board.getCurrentPlayer()
				.makeMove(Move.MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("f2"),
				BoardUtils.getCoordinateAtPosition("f3")));
		
		assertTrue(t1.getMoveStatus().isDone());
		
		final MoveTransition t2 = t1.getToBoard()
				.getCurrentPlayer()
				.makeMove(Move.MoveFactory.createMove(t1.getToBoard(), BoardUtils.getCoordinateAtPosition("e7"),
				BoardUtils.getCoordinateAtPosition("e5")));
		
		assertTrue(t2.getMoveStatus().isDone());
		
		final MoveTransition t3 = t2.getToBoard()
				.getCurrentPlayer()
				.makeMove(Move.MoveFactory.createMove(t2.getToBoard(), BoardUtils.getCoordinateAtPosition("g2"),
				BoardUtils.getCoordinateAtPosition("g4")));
		
		assertTrue(t3.getMoveStatus().isDone());
		
		final MoveStrategy strategy = new MiniMax(4);
		
		final Move aiMove = strategy.execute(t3.getToBoard());
		
		final Move bestMove = Move.MoveFactory.createMove(t3.getToBoard(), BoardUtils.getCoordinateAtPosition("d8"), BoardUtils.getCoordinateAtPosition("h4"));
		
		assertEquals(aiMove, bestMove);
	}
}
