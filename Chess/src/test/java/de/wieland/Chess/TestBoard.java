package de.wieland.Chess;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.common.collect.Iterables;

import de.wieland.Chess.engine.Alliance;
import de.wieland.Chess.engine.board.Board;
import de.wieland.Chess.engine.board.Board.Builder;
import de.wieland.Chess.engine.board.BoardUtils;
import de.wieland.Chess.engine.board.Move;
import de.wieland.Chess.engine.board.MoveTransition;
import de.wieland.Chess.engine.board.Move.MoveFactory;
import de.wieland.Chess.engine.pieces.Bishop;
import de.wieland.Chess.engine.pieces.King;
import de.wieland.Chess.engine.pieces.Knight;
import de.wieland.Chess.engine.pieces.Pawn;
import de.wieland.Chess.engine.pieces.Piece;
import de.wieland.Chess.engine.pieces.Queen;
import de.wieland.Chess.engine.pieces.Rook;
import de.wieland.Chess.engine.player.ai.BoardEvaluator;
import de.wieland.Chess.engine.player.ai.StandardBoardEvaluator;

/**
 * Public class TestBoard - JUnit Test Case.
 * 
 * @author Moritz Wieland
 * @version 1.0
 * @date 10.09.2021
 */
public class TestBoard {
	@Test
    public void initialBoard() {

        final Board board = Board.createStandardBoard();
        assertEquals(board.getCurrentPlayer().getLegalMoves().size(), 20);
        assertEquals(board.getCurrentPlayer().getOpponent().getLegalMoves().size(), 20);
        assertFalse(board.getCurrentPlayer().isInCheck());
        assertFalse(board.getCurrentPlayer().isInCheckMate());
        assertFalse(board.getCurrentPlayer().isCastled());
        assertTrue(board.getCurrentPlayer().isKingSideCastleCapable());
        assertTrue(board.getCurrentPlayer().isQueenSideCastleCapable());
        assertEquals(board.getCurrentPlayer(), board.getWhitePlayer());
        assertEquals(board.getCurrentPlayer().getOpponent(), board.getBlackPlayer());
        assertFalse(board.getCurrentPlayer().getOpponent().isInCheck());
        assertFalse(board.getCurrentPlayer().getOpponent().isInCheckMate());
        assertFalse(board.getCurrentPlayer().getOpponent().isCastled());
        assertTrue(board.getCurrentPlayer().getOpponent().isKingSideCastleCapable());
        assertTrue(board.getCurrentPlayer().getOpponent().isQueenSideCastleCapable());
        assertTrue(board.getWhitePlayer().toString().equals("White"));
        assertTrue(board.getBlackPlayer().toString().equals("Black"));

        final Iterable<Piece> allPieces = board.getAllPieces();
        final Iterable<Move> allMoves = Iterables.concat(board.getWhitePlayer().getLegalMoves(), board.getBlackPlayer().getLegalMoves());
        for(final Move move : allMoves) {
            assertFalse(move.isAttack());
            assertFalse(move.isCastlingMove());
//            assertEquals(MoveUtils.exchangeScore(move), 1);
        }

        assertEquals(Iterables.size(allMoves), 40);
        assertEquals(Iterables.size(allPieces), 32);
        assertFalse(BoardUtils.isEndGame(board));
        assertFalse(BoardUtils.isThreatenedBoardImmediate(board));
        assertEquals(StandardBoardEvaluator.get().evaluate(board, 0), 0);
        assertEquals(board.getPiece(35), null);
    }

    @Test
    public void testPlainKingMove() {

        final Builder builder = new Builder();
        // Black Layout
        builder.setPiece(new King(Alliance.BLACK, 4, false, false));
        builder.setPiece(new Pawn(Alliance.BLACK, 12));
        // White Layout
        builder.setPiece(new Pawn(Alliance.WHITE, 52));
        builder.setPiece(new King(Alliance.WHITE, 60, false, false));
        builder.setMoveMaker(Alliance.WHITE);
        // Set the current player
        final Board board = builder.build();
        System.out.println(board);

        assertEquals(board.getWhitePlayer().getLegalMoves().size(), 6);
        assertEquals(board.getBlackPlayer().getLegalMoves().size(), 6);
        assertFalse(board.getCurrentPlayer().isInCheck());
        assertFalse(board.getCurrentPlayer().isInCheckMate());
        assertFalse(board.getCurrentPlayer().getOpponent().isInCheck());
        assertFalse(board.getCurrentPlayer().getOpponent().isInCheckMate());
        assertEquals(board.getCurrentPlayer(), board.getWhitePlayer());
        assertEquals(board.getCurrentPlayer().getOpponent(), board.getBlackPlayer());
        BoardEvaluator evaluator = StandardBoardEvaluator.get();
        System.out.println(evaluator.evaluate(board, 0));
        assertEquals(StandardBoardEvaluator.get().evaluate(board, 0), 0);

        final Move move = MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("e1"),
                BoardUtils.getCoordinateAtPosition("f1"));

        final MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);

        assertEquals(moveTransition.getTransitionMove(), move);
        assertEquals(moveTransition.getFromBoard(), board);
        assertEquals(moveTransition.getToBoard().getCurrentPlayer(), moveTransition.getToBoard().getBlackPlayer());

        assertTrue(moveTransition.getMoveStatus().isDone());
        assertEquals(moveTransition.getToBoard().getWhitePlayer().getPlayerKing().getPiecePosition(), 61);
        System.out.println(moveTransition.getToBoard());

    }

    @Test
    public void testBoardConsistency() {
        final Board board = Board.createStandardBoard();
        assertEquals(board.getCurrentPlayer(), board.getWhitePlayer());

        final MoveTransition t1 = board.getCurrentPlayer().makeMove(MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("e2"), BoardUtils.getCoordinateAtPosition("e4")));
        final MoveTransition t2 = t1.getToBoard().getCurrentPlayer().makeMove(MoveFactory.createMove(t1.getToBoard(), BoardUtils.getCoordinateAtPosition("e7"), BoardUtils.getCoordinateAtPosition("e5")));
        final MoveTransition t3 = t2.getToBoard().getCurrentPlayer().makeMove(MoveFactory.createMove(t2.getToBoard(), BoardUtils.getCoordinateAtPosition("g1"), BoardUtils.getCoordinateAtPosition("f3")));
        final MoveTransition t4 = t3.getToBoard().getCurrentPlayer().makeMove(MoveFactory.createMove(t3.getToBoard(), BoardUtils.getCoordinateAtPosition("d7"), BoardUtils.getCoordinateAtPosition("d5")));
        final MoveTransition t5 = t4.getToBoard().getCurrentPlayer().makeMove(MoveFactory.createMove(t4.getToBoard(), BoardUtils.getCoordinateAtPosition("e4"), BoardUtils.getCoordinateAtPosition("d5")));
        final MoveTransition t6 = t5.getToBoard().getCurrentPlayer().makeMove(MoveFactory.createMove(t5.getToBoard(), BoardUtils.getCoordinateAtPosition("d8"), BoardUtils.getCoordinateAtPosition("d5")));
        final MoveTransition t7 = t6.getToBoard().getCurrentPlayer().makeMove(MoveFactory.createMove(t6.getToBoard(), BoardUtils.getCoordinateAtPosition("f3"), BoardUtils.getCoordinateAtPosition("g5")));
        final MoveTransition t8 = t7.getToBoard().getCurrentPlayer().makeMove(MoveFactory.createMove(t7.getToBoard(), BoardUtils.getCoordinateAtPosition("f7"), BoardUtils.getCoordinateAtPosition("f6")));
        final MoveTransition t9 = t8.getToBoard().getCurrentPlayer().makeMove(MoveFactory.createMove(t8.getToBoard(), BoardUtils.getCoordinateAtPosition("d1"), BoardUtils.getCoordinateAtPosition("h5")));
        final MoveTransition t10 = t9.getToBoard().getCurrentPlayer().makeMove(MoveFactory.createMove(t9.getToBoard(), BoardUtils.getCoordinateAtPosition("g7"), BoardUtils.getCoordinateAtPosition("g6")));
        final MoveTransition t11 = t10.getToBoard().getCurrentPlayer().makeMove(MoveFactory.createMove(t10.getToBoard(), BoardUtils.getCoordinateAtPosition("h5"), BoardUtils.getCoordinateAtPosition("h4")));
        final MoveTransition t12 = t11.getToBoard().getCurrentPlayer().makeMove(MoveFactory.createMove(t11.getToBoard(), BoardUtils.getCoordinateAtPosition("f6"), BoardUtils.getCoordinateAtPosition("g5")));
        final MoveTransition t13 = t12.getToBoard().getCurrentPlayer().makeMove(MoveFactory.createMove(t12.getToBoard(), BoardUtils.getCoordinateAtPosition("h4"), BoardUtils.getCoordinateAtPosition("g5")));
        final MoveTransition t14 = t13.getToBoard().getCurrentPlayer().makeMove(MoveFactory.createMove(t13.getToBoard(), BoardUtils.getCoordinateAtPosition("d5"), BoardUtils.getCoordinateAtPosition("e4")));

        assertTrue(t14.getToBoard().getWhitePlayer().getActivePieces().size() == calculatedActivesFor(t14.getToBoard(), Alliance.WHITE));
        assertTrue(t14.getToBoard().getBlackPlayer().getActivePieces().size() == calculatedActivesFor(t14.getToBoard(), Alliance.BLACK));

    }

    @Test(expected=RuntimeException.class)
    public void testInvalidBoard() {

        final Builder builder = new Builder();
        // Black Layout
        builder.setPiece(new Rook(Alliance.BLACK, 0));
        builder.setPiece(new Knight(Alliance.BLACK, 1));
        builder.setPiece(new Bishop(Alliance.BLACK, 2));
        builder.setPiece(new Queen(Alliance.BLACK, 3));
        builder.setPiece(new Bishop(Alliance.BLACK, 5));
        builder.setPiece(new Knight(Alliance.BLACK, 6));
        builder.setPiece(new Rook(Alliance.BLACK, 7));
        builder.setPiece(new Pawn(Alliance.BLACK, 8));
        builder.setPiece(new Pawn(Alliance.BLACK, 9));
        builder.setPiece(new Pawn(Alliance.BLACK, 10));
        builder.setPiece(new Pawn(Alliance.BLACK, 11));
        builder.setPiece(new Pawn(Alliance.BLACK, 12));
        builder.setPiece(new Pawn(Alliance.BLACK, 13));
        builder.setPiece(new Pawn(Alliance.BLACK, 14));
        builder.setPiece(new Pawn(Alliance.BLACK, 15));
        // White Layout
        builder.setPiece(new Pawn(Alliance.WHITE, 48));
        builder.setPiece(new Pawn(Alliance.WHITE, 49));
        builder.setPiece(new Pawn(Alliance.WHITE, 50));
        builder.setPiece(new Pawn(Alliance.WHITE, 51));
        builder.setPiece(new Pawn(Alliance.WHITE, 52));
        builder.setPiece(new Pawn(Alliance.WHITE, 53));
        builder.setPiece(new Pawn(Alliance.WHITE, 54));
        builder.setPiece(new Pawn(Alliance.WHITE, 55));
        builder.setPiece(new Rook(Alliance.WHITE, 56));
        builder.setPiece(new Knight(Alliance.WHITE, 57));
        builder.setPiece(new Bishop(Alliance.WHITE, 58));
        builder.setPiece(new Queen(Alliance.WHITE, 59));
        builder.setPiece(new Bishop(Alliance.WHITE, 61));
        builder.setPiece(new Knight(Alliance.WHITE, 62));
        builder.setPiece(new Rook(Alliance.WHITE, 63));
        //white to move
        builder.setMoveMaker(Alliance.WHITE);
        //build the board
        builder.build();
    }

    @Test
    public void testAlgebreicNotation() {
        assertEquals(BoardUtils.getPositionAtCoordinate(0), "a8");
        assertEquals(BoardUtils.getPositionAtCoordinate(1), "b8");
        assertEquals(BoardUtils.getPositionAtCoordinate(2), "c8");
        assertEquals(BoardUtils.getPositionAtCoordinate(3), "d8");
        assertEquals(BoardUtils.getPositionAtCoordinate(4), "e8");
        assertEquals(BoardUtils.getPositionAtCoordinate(5), "f8");
        assertEquals(BoardUtils.getPositionAtCoordinate(6), "g8");
        assertEquals(BoardUtils.getPositionAtCoordinate(7), "h8");
    }

    @Test
    public void mem() {
        final Runtime runtime = Runtime.getRuntime();
        long start, end;
        runtime.gc();
        start = runtime.freeMemory();
        @SuppressWarnings("unused")
		Board board = Board.createStandardBoard();
        end =  runtime.freeMemory();
        System.out.println("That took " + (start-end) + " bytes.");
    }
    
    private static int calculatedActivesFor(final Board board, final Alliance alliance) {
        int count = 0;
        for (final Piece p : board.getAllPieces()) {
            if (p.getPieceAlliance().equals(alliance)) {
                count++;
            }
        }
        
        return count;
    }
}
