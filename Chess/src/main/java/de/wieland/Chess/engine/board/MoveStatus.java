package de.wieland.Chess.engine.board;

/**
 * Public enum MoveStatus.
 * 
 * @author Moritz Wieland
 * @version 1.0
 * @date 10.09.2021
 */
public enum MoveStatus {
	DONE {
		@Override
		public boolean isDone() {
			return true;
		}
	},
	ILLEGAL_MOVE {
		@Override
		public boolean isDone() {
			return false;
		}
	},
	LEAVES_PLAYER_IN_CHECK {
		@Override
		public boolean isDone() {
			return false;
		}
	};
	
	public abstract boolean isDone();
}
