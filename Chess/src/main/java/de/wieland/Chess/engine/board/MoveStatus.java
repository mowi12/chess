package de.wieland.Chess.engine.board;

/**
 * public enum MoveStatus
 * 
 * @author Moritz Wieland
 * @version 1.0
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
