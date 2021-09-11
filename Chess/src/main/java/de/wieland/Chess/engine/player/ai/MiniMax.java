package de.wieland.Chess.engine.player.ai;

import java.util.Observable;
import java.util.concurrent.atomic.AtomicLong;

import de.wieland.Chess.engine.board.Board;
import de.wieland.Chess.engine.board.BoardUtils;
import de.wieland.Chess.engine.board.Move;
import de.wieland.Chess.engine.board.Move.MoveFactory;
import de.wieland.Chess.engine.board.MoveTransition;

/**
 * public class MiniMax
 * 
 * @author Moritz Wieland
 * @version 1.0
 * @date 10.09.2021
 */
@SuppressWarnings("deprecation")
public class MiniMax extends Observable implements MoveStrategy {
	private final BoardEvaluator boardEvaluator;
	private final int searchDepth;
	
	private long boardsEvaluated;
	private long executionTime;
	
	private FreqTableRow[] freqTable;
	private int freqTableIndex;
	
	public MiniMax(final int searchDepth) {
		boardEvaluator = StandardBoardEvaluator.get();
		this.searchDepth = searchDepth;
		boardsEvaluated = 0;
	}
	
	@Override
	public Move execute(final Board board) {
		final long startTime = System.currentTimeMillis();
		
		Move bestMove = MoveFactory.getNullMove();
		
		int highestSeenValue = Integer.MIN_VALUE;
		int lowestSeenValue = Integer.MAX_VALUE;
		int currentValue;
				
		System.out.println(board.getCurrentPlayer() + " THINKING width depth: " + searchDepth);
		
		freqTable = new FreqTableRow[board.getCurrentPlayer().getLegalMoves().size()];
		freqTableIndex = 0;
		
		int moveCounter = 1;
		int numMoves = board.getCurrentPlayer().getLegalMoves().size();

		for (final Move move : board.getCurrentPlayer().getLegalMoves()) {
			final MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
			final String output;
			
			if(moveTransition.getMoveStatus().isDone()) {
				final FreqTableRow row = new FreqTableRow(move);
				freqTable[freqTableIndex] = row;
				
				currentValue = board.getCurrentPlayer().getAlliance().isWhite() ?
							   min(moveTransition.getToBoard(), searchDepth - 1) :
							   max(moveTransition.getToBoard(), searchDepth - 1);
				
				output = "\t" + toString() + " analyzing move (" + moveCounter + "/" + numMoves + ") " + move +
                        " scores " + currentValue + " " + this.freqTable[this.freqTableIndex];
				System.out.println(output);
				
				freqTableIndex++;
				
				if(board.getCurrentPlayer().getAlliance().isWhite() &&
				   currentValue >= highestSeenValue) {
					highestSeenValue = currentValue;
					bestMove = move;
				} else if(board.getCurrentPlayer().getAlliance().isBlack() &&
						  currentValue <= lowestSeenValue) {
					lowestSeenValue = currentValue;
					bestMove = move;
				}
			} else {
				output = "\t" + toString() + " can't execute move (" + moveCounter + "/" + numMoves+ ") " + move;
				System.out.println(output);
			}
				
			moveCounter++;
			setChanged();
			notifyObservers(output);
		}
			
		executionTime = System.currentTimeMillis() - startTime;
		final String result = board.getCurrentPlayer() + " SELECTS " + bestMove +
							  " [#boards = " + this.boardsEvaluated +
							  ", time taken = " + this.executionTime + "ms" +
							  ", rate = " + (1000 * ((double) this.boardsEvaluated/this.executionTime)) + "]";
		
		System.out.printf("%s SELECTS %s [#boards = %d, time taken = %d ms, rate = %.1f]\n", board.getCurrentPlayer(),
                bestMove, this.boardsEvaluated, this.executionTime, (1000 * ((double)this.boardsEvaluated/this.executionTime)));
		this.setChanged();
		this.notifyObservers(result);
		
		long total = 0;
		
		for (final FreqTableRow row : freqTable) {
			if(row != null) {
				total += row.getCount();
			}
		}
		
		if(boardsEvaluated != total) {
			System.out.println("something is wrong with the # of boards evaluated!");
		}
		
		return bestMove;
	}
	
	@Override
	public long getNumBoardsEvaluated() {
		return boardsEvaluated;
	}
	
	@Override
	public String toString() {
		return "MiniMax";
	}
	
	public int min(final Board board,
				   final int depth) {
		if(depth == 0) {
			boardsEvaluated++;
			freqTable[freqTableIndex].increment();
			return boardEvaluator.evaluate(board, depth);
		}
		
		if(isEndGameScenario(board) ) {
			return boardEvaluator.evaluate(board, depth);
		}
		
		int lowestSeenNumber = Integer.MAX_VALUE;
		
		for (final Move move : board.getCurrentPlayer().getLegalMoves()) {
			final MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
			
			if(moveTransition.getMoveStatus().isDone()) {
				final int currentValue = max(moveTransition.getToBoard(), depth - 1);
				
				if(currentValue <= lowestSeenNumber) {
					lowestSeenNumber = currentValue;
				}
			}
		}
		
		return lowestSeenNumber;
	}
	
	public int max(final Board board, final int depth) {
		if(depth == 0) {
			boardsEvaluated++;
			freqTable[freqTableIndex].increment();
			return boardEvaluator.evaluate(board, depth);
		}
		
		if(isEndGameScenario(board)) {
			return boardEvaluator.evaluate(board, depth);
		}
		
		int highestSeenNumber = Integer.MIN_VALUE;
		
		for (final Move move : board.getCurrentPlayer().getLegalMoves()) {
			final MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
			
			if(moveTransition.getMoveStatus().isDone()) {
				final int currentValue = min(moveTransition.getToBoard(), depth - 1);
				
				if(currentValue >= highestSeenNumber) {
					highestSeenNumber = currentValue;
				}
			}
		}
		
		return highestSeenNumber;
	}
	
	private static boolean isEndGameScenario(final Board board) {
		return board.getCurrentPlayer().isInCheckMate() ||
			   board.getCurrentPlayer().isInStaleMate();
	}
	
	
	private static class FreqTableRow {
		private final Move move;
		private final AtomicLong count;
		
		public FreqTableRow(final Move move) {
			count = new AtomicLong();
			this.move = move;
		}
		
		long getCount() { return count.get(); }
		void increment() { count.incrementAndGet(); }
		
		@Override
		public String toString() {
			return BoardUtils.getPositionAtCoordinate(move.getCurrentCoordinate()) +
				   BoardUtils.getPositionAtCoordinate(move.getDestinationCoordinate()) + " : "  + count;
		}
	}
}
