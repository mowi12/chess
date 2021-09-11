package de.wieland.Chess.engine.board;

import de.wieland.Chess.engine.board.Board.Builder;
import de.wieland.Chess.engine.pieces.Pawn;
import de.wieland.Chess.engine.pieces.Piece;
import de.wieland.Chess.engine.pieces.Rook;

/**
 * Public abstract class Move.
 * 
 * @author Moritz Wieland
 * @version 1.0
 * @date 10.09.2021
 */
public abstract class Move {
	protected final Board board;
	protected final Piece movedPiece;
	protected final int destinationCoordinate;
	protected final boolean isFirstMove;
	
	private Move(final Board board,
				 final Piece movedPiece,
				 final int destinationCoordinate) {
		this.board = board;
		this.movedPiece = movedPiece;
		this.destinationCoordinate = destinationCoordinate;
		this.isFirstMove = movedPiece.isFirstMove();
	}
	
	public Move(final Board board,
				final int destinationCoordinate) {
		this.board = board;
		this.destinationCoordinate = destinationCoordinate;
		this.movedPiece = null;
		this.isFirstMove = false;
	}
	
	@Override
	public boolean equals(final Object other) {
		if(this == other) {
			return true;
		}
		
		if(!(other instanceof Move)) {
			return false;
		}
		
		final Move otherMove = (Move) other;
		
		return (getCurrentCoordinate() == otherMove.getCurrentCoordinate()) &&
			   (getDestinationCoordinate() == otherMove.getDestinationCoordinate()) &&
			   (getMovedPiece().equals(otherMove.getMovedPiece()));
	}
	
	@Override
	public int hashCode() {
		int result = 1;
		result = 31 * result + destinationCoordinate;
		result = 31 * result + movedPiece.hashCode();
		result = 31 * result + movedPiece.getPiecePosition();
		result = 31 * result + (isFirstMove ? 1 : 0);
		return result;
	}
	
	public Board execute() {
		final Builder builder = new Builder();
		
		for (final Piece piece : board.getCurrentPlayer().getActivePieces()) {
			if(!movedPiece.equals(piece)) {
				builder.setPiece(piece);
			}
		}
		
		for (final Piece piece : board.getCurrentPlayer().getOpponent().getActivePieces()) {
			builder.setPiece(piece);
		}
		
		builder.setPiece(movedPiece.movePiece(this));
		builder.setMoveMaker(board.getCurrentPlayer().getOpponent().getAlliance());
//		builder.setMoveTransition(this); TODO
		
		return builder.build();
	}
	
	public Board undo() {
		final Builder builder = new Builder();
		
		for (final Piece piece : board.getAllPieces()) {
			builder.setPiece(piece);
		}
		
		builder.setMoveMaker(board.getCurrentPlayer().getAlliance());
		
		return builder.build();
	}
	
	public boolean isAttack() { return false; }
	public boolean isCastlingMove() { return false; }
	//TODO
	public boolean isCheckMove() {
		final int kingPosition = board.getCurrentPlayer().getPlayerKing().getPiecePosition();
		
		for (final Move move : movedPiece.calculateLegalMoves(board)) {
			if(move.getDestinationCoordinate() == kingPosition) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Getter and Setter methods.
	 */
	public Board getBoard() { return board; }
	public Piece getMovedPiece() { return movedPiece; }
	public Piece getAttackedPiece() { return null; }
	public int getCurrentCoordinate() { return movedPiece.getPiecePosition(); }
	public int getDestinationCoordinate() { return destinationCoordinate; }
	
	
	/**
	 * Public static final class MajorMove.
	 * 
	 * @author Moritz Wieland
	 * @version 1.0
	 * @date 10.09.2021
	 */
	public static final class MajorMove extends Move {

		public MajorMove(final Board board,
						 final Piece piece,
						 final int destinationCoordinate) {
			super(board, piece, destinationCoordinate);
		}
		
		@Override
		public boolean equals(final Object other) {
			return (this == other) || (other instanceof MajorMove && super.equals(other));
		}
		
		@Override
		public String toString() {
			return movedPiece.getPieceType().toString() +
				   BoardUtils.getPositionAtCoordinate(destinationCoordinate);
		}
	}
	
	
	/**
	 * Public static class AttackMove.
	 * 
	 * @author Moritz Wieland
	 * @version 1.0
	 * @date 10.09.2021
	 */
	public static class AttackMove extends Move {
		private final Piece attackedPiece;
		
		public AttackMove(final Board board,
						  final Piece piece,
						  final int destinationCoordinate,
						  final Piece attackedPiece) {
			super(board, piece, destinationCoordinate);
			this.attackedPiece = attackedPiece;
		}
		
		@Override
		public boolean equals(final Object other) {
			if(this == other) {
				return true;
			}
			
			if(!(other instanceof AttackMove)) {
				return false;
			}
			
			final AttackMove otherAttackMove = (AttackMove) other;
			
			return (super.equals(otherAttackMove)) && (getAttackedPiece().equals(otherAttackMove.getAttackedPiece()));
		}
		
		@Override
		public int hashCode() {
			return attackedPiece.hashCode() + super.hashCode();
		}
		
		@Override
		public boolean isAttack() { return true; }
		
		@Override
		public Piece getAttackedPiece() { return attackedPiece; }
	}
	
	
	/**
	 * Public static class MajorAttackMove.
	 * 
	 * @author Moritz Wieland
	 * @version 1.0
	 * @date 10.09.2021
	 */
	public static class MajorAttackMove extends AttackMove {

		public MajorAttackMove(final Board board,
							   final Piece piece,
							   final int destinationCoordinate,
							   final Piece attackedPiece) {
			super(board, piece, destinationCoordinate, attackedPiece);
		}
		
		@Override
		public boolean equals(final Object other) {
			return (this == other) || (other instanceof MajorAttackMove && super.equals(other));
		}
		
		@Override
		public String toString() {
			return movedPiece.getPieceType() +
				   BoardUtils.getPositionAtCoordinate(destinationCoordinate);
		}
	}
	
	
	/**
	 * Public static final class PawnMove.
	 * 
	 * @author Moritz Wieland
	 * @version 1.0
	 * @date 10.09.2021
	 */
	public static final class PawnMove extends Move {

		public PawnMove(final Board board,
						final Piece piece,
						final int destinationCoordinate) {
			super(board, piece, destinationCoordinate);
		}
		
		@Override
		public boolean equals(final Object other) {
			return (this == other) || (other instanceof PawnMove && super.equals(other));
		}
		
		@Override
		public String toString() {
			return BoardUtils.getPositionAtCoordinate(destinationCoordinate);
		}
	}
	
	
	/**
	 * Public static class PawnAttackMove.
	 * 
	 * @author Moritz Wieland
	 * @version 1.0
	 * @date 10.09.2021
	 */
	public static class PawnAttackMove extends AttackMove {

		public PawnAttackMove(final Board board,
							  final Piece piece,
							  final int destinationCoordinate,
							  final Piece attackedPiece) {
			super(board, piece, destinationCoordinate, attackedPiece);
		}
		
		@Override
		public boolean equals(final Object other) {
			return (this == other) || (other instanceof PawnAttackMove && super.equals(other));
		}
		
		@Override
		public String toString() {
			return BoardUtils.getPositionAtCoordinate(movedPiece.getPiecePosition()).substring(0, 1) +
				   "x" +
				   BoardUtils.getPositionAtCoordinate(destinationCoordinate);
		}
	}
	
	
	/**
	 * Public static final class PawnEnPassantAttackMove.
	 * 
	 * @author Moritz Wieland
	 * @version 1.0
	 * @date 10.09.2021
	 */
	public static final class PawnEnPassantAttackMove extends PawnAttackMove {

		public PawnEnPassantAttackMove(final Board board,
									   final Piece piece,
									   final int destinationCoordinate,
									   final Piece attackedPiece) {
			super(board, piece, destinationCoordinate, attackedPiece);
		}
		
		@Override
		public Board execute() {
			final Builder builder = new Builder();
			
			for (final Piece piece : board.getCurrentPlayer().getActivePieces()) {
				if(!movedPiece.equals(piece)) {
					builder.setPiece(piece);
				}
			}
			
			for (final Piece piece : board.getCurrentPlayer().getOpponent().getActivePieces()) {
				if(!piece.equals(getAttackedPiece())) {
					builder.setPiece(piece);
				}
			}
			
			builder.setPiece(movedPiece.movePiece(this));
			builder.setMoveMaker(board.getCurrentPlayer().getOpponent().getAlliance());
//			builder.setMoveTransition(this); TODO
			
			return builder.build();
		}
		
		@Override
		public Board undo() {
			final Builder builder = new Builder();
			
			for (final Piece piece : board.getAllPieces()) {
				builder.setPiece(piece);
			}
			
			builder.setEnPassantPawn((Pawn) getAttackedPiece());
			builder.setMoveMaker(board.getCurrentPlayer().getAlliance());
			
			return builder.build();
		}
		
		@Override
		public boolean equals(final Object other) {
			return (this == other) || (other instanceof PawnEnPassantAttackMove && super.equals(other));
		}
	}
	
	
	/**
	 * Public static class PawnPromotion.
	 * 
	 * @author Moritz Wieland
	 * @version 1.0
	 * @date 10.09.2021
	 */
	public static class PawnPromotion extends Move {
		final Move decoratedMove;
		final Pawn promotedPawn;
		final Piece promotionPiece;

		public PawnPromotion(final Move decoratedMove,
							 final Piece promotionPiece) {
			super(decoratedMove.getBoard(), decoratedMove.getMovedPiece(), decoratedMove.getDestinationCoordinate());
			this.decoratedMove = decoratedMove;
			this.promotedPawn = (Pawn) decoratedMove.getMovedPiece();
			this.promotionPiece = promotionPiece;
		}
		
		@Override
		public Board execute() {
			final Board pawnMoveBoard = decoratedMove.execute();
			final Builder builder = new Builder();
			
			for (final Piece piece : pawnMoveBoard.getCurrentPlayer().getActivePieces()) {
				if(!promotedPawn.equals(piece)) {
					builder.setPiece(piece);
				}
			}
			
			for (final Piece piece : pawnMoveBoard.getCurrentPlayer().getOpponent().getActivePieces()) {
				builder.setPiece(piece);
			}
			
			builder.setPiece(promotionPiece.movePiece(this));
			builder.setMoveMaker(pawnMoveBoard.getCurrentPlayer().getAlliance());
//			builder.setMoveTransition(this); TODO
			
			return builder.build();
		}
		
		@Override
		public String toString() {
			return BoardUtils.getPositionAtCoordinate(movedPiece.getPiecePosition()) + "-" +
				   BoardUtils.getPositionAtCoordinate(destinationCoordinate) +
				   "=" +
				   promotionPiece.getPieceType();
		}
		
		@Override
		public boolean equals(final Object other) {
			return (this == other) || (other instanceof PawnPromotion && super.equals(other));
		}
		
		@Override
		public int hashCode() {
			return decoratedMove.hashCode() + (31 * promotedPawn.hashCode());
		}
		
		@Override
		public boolean isAttack() {
			return decoratedMove.isAttack();
		}
		
		@Override
		public Piece getAttackedPiece() {
			return decoratedMove.getAttackedPiece();
		}
	}
	
	
	/**
	 * Public static final class PawnJump.
	 * 
	 * @author Moritz Wieland
	 * @version 1.0
	 * @date 10.09.2021
	 */
	public static final class PawnJump extends Move {

		public PawnJump(final Board board,
						final Piece piece,
						final int destinationCoordinate) {
			super(board, piece, destinationCoordinate);
		}
		
		@Override
		public Board execute() {
			final Builder builder = new Builder();
			
			for (final Piece piece : board.getCurrentPlayer().getActivePieces()) {
				if(!movedPiece.equals(piece)) {
					builder.setPiece(piece);
				}
			}
			
			for (final Piece piece : board.getCurrentPlayer().getOpponent().getActivePieces()) {
				builder.setPiece(piece);
			}
			
			final Pawn movedPawn = (Pawn) movedPiece.movePiece(this);
			
			builder.setPiece(movedPawn);
			builder.setEnPassantPawn(movedPawn);;
			builder.setMoveMaker(board.getCurrentPlayer().getOpponent().getAlliance());
//			builder.setMoveTransition(this); TODO
			
			return builder.build();
		}
		
		@Override
        public boolean equals(final Object other) {
            return this == other || other instanceof PawnJump && super.equals(other);
        }
		
		@Override
		public String toString() {
			return BoardUtils.getPositionAtCoordinate(destinationCoordinate);
		}
	}
	
	
	/**
	 * Static abstract class CastleMove.
	 * 
	 * @author Moritz Wieland
	 * @version 1.0
	 * @date 10.09.2021
	 */
	static abstract class CastleMove extends Move {
		protected final Rook castleRook;
		protected final int castleRookStart;
		protected final int castleRookDestination;
		

		public CastleMove(final Board board,
						  final Piece piece,
						  final int destinationCoordinate,
						  final Rook castleRook,
						  final int castleRookStart,
						  final int castleRookDestination) {
			super(board, piece, destinationCoordinate);
			this.castleRook = castleRook;
			this.castleRookStart = castleRookStart;
			this.castleRookDestination = castleRookDestination;
		}
		
		@Override
		public Board execute() {
			final Builder builder = new Builder();
			
			for (final Piece piece : board.getCurrentPlayer().getActivePieces()) {
				if(!movedPiece.equals(piece) && !castleRook.equals(piece)) {
					builder.setPiece(piece);
				}
			}
			
			for (final Piece piece : board.getCurrentPlayer().getOpponent().getActivePieces()) {
				builder.setPiece(piece);
			}
			
			builder.setPiece(movedPiece.movePiece(this));
			builder.setPiece(new Rook(castleRook.getPieceAlliance(), castleRookDestination, false));
			builder.setMoveMaker(board.getCurrentPlayer().getOpponent().getAlliance());
//			builder.setMoveTransition(this); TODO
			
			return builder.build();
		}
		
		@Override
		public boolean equals(final Object other) {
			if(this == other) {
				return true;
			}
			
			if(!(other instanceof CastleMove)) {
				return false;
			}
			
			final CastleMove otherCastleMove = (CastleMove) other;
			
			return super.equals(otherCastleMove) && castleRook.equals(otherCastleMove.getCastleRook());
		}
		
		@Override
		public int hashCode() {
			int result = super.hashCode();
			result = 31 * result + castleRook.hashCode();
			result = 31 * result + castleRookDestination;
			return result;
		}
		
		@Override
		public boolean isCastlingMove() { return true; }
		
		public Rook getCastleRook() { return castleRook; }
	}
	
	
	/**
	 * Public static final class KingSideCastleMove.
	 * 
	 * @author Moritz Wieland
	 * @version 1.0
	 * @date 10.09.2021
	 */
	public static final class KingSideCastleMove extends CastleMove {

		public KingSideCastleMove(final Board board,
								  final Piece piece,
								  final int destinationCoordinate,
								  final Rook castleRook,
								  final int castleRookStart,
								  final int castleRookDestination) {
			super(board, piece, destinationCoordinate, castleRook, castleRookStart, castleRookDestination);
		}
		
		@Override
        public boolean equals(final Object other) {
            if (this == other) {
                return true;
            }
            
            if (!(other instanceof KingSideCastleMove)) {
                return false;
            }
            
            final KingSideCastleMove otherKingSideCastleMove = (KingSideCastleMove) other;
            
            return super.equals(otherKingSideCastleMove) && castleRook.equals(otherKingSideCastleMove.getCastleRook());
        }
		
		@Override
		public String toString() {
			return "O-O";
		}
	}
	
	
	/**
	 * Public static final class QueenSideCastleMove.
	 * 
	 * @author Moritz Wieland
	 * @version 1.0
	 * @date 10.09.2021
	 */
	public static final class QueenSideCastleMove extends CastleMove {

		public QueenSideCastleMove(final Board board,
								   final Piece piece,
								   final int destinationCoordinate,
								   final Rook castleRook,
								   final int castleRookStart,
								   final int castleRookDestination) {
			super(board, piece, destinationCoordinate, castleRook, castleRookStart, castleRookDestination);
		}
		
		@Override
        public boolean equals(final Object other) {
            if (this == other) {
                return true;
            }
            
            if (!(other instanceof QueenSideCastleMove)) {
                return false;
            }
            
            final QueenSideCastleMove otherQueenSideCastleMove = (QueenSideCastleMove) other;
            
            return super.equals(otherQueenSideCastleMove) && castleRook.equals(otherQueenSideCastleMove.getCastleRook());
        }
		
		@Override
		public String toString() {
			return "O-O-O";
		}
	}
	
	
	/**
	 * Public static final class NullMove.
	 * 
	 * @author Moritz Wieland
	 * @version 1.0
	 * @date 10.09.2021
	 */
	public static final class NullMove extends Move {

		public NullMove() {
			super(null, 65);
		}
		
		@Override
        public String toString() {
            return "Null Move";
        }
		
		@Override
		public Board execute() {
			throw new RuntimeException("cannot execute the null move!");
		}
		
		@Override
		public int getCurrentCoordinate() {
			return 65;
		}
		
		@Override
        public int getDestinationCoordinate() {
            return 65;
        }
	}
	
	
	/**
	 * Public static class MoveFactory.
	 * 
	 * @author Moritz Wieland
	 * @version 1.0
	 * @date 10.09.2021
	 */
	public static class MoveFactory {
		private static final Move NULL_MOVE = new NullMove();
		
		private MoveFactory() {
			throw new RuntimeException("You cannot instantiate me!");
		}
		
		public static Move createMove(final Board board,
									  final int currentCoordinate,
									  final int destinationCoordinate) {
			for (final Move move : board.getAllLegalMoves()) {
				if(move.getCurrentCoordinate() == currentCoordinate &&
				   move.getDestinationCoordinate() == destinationCoordinate) {
					return move;
				}
			}
			
			return NULL_MOVE;
		}
		
		/**
		 * Getter method.
		 */
		public static Move getNullMove() { return NULL_MOVE; }
	}
}
