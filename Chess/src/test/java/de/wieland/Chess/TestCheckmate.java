package de.wieland.Chess;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.wieland.Chess.engine.Alliance;
import de.wieland.Chess.engine.board.Board;
import de.wieland.Chess.engine.board.Board.Builder;
import de.wieland.Chess.engine.board.BoardUtils;
import de.wieland.Chess.engine.board.MoveTransition;
import de.wieland.Chess.engine.board.Move.MoveFactory;
import de.wieland.Chess.engine.pieces.Bishop;
import de.wieland.Chess.engine.pieces.King;
import de.wieland.Chess.engine.pieces.Knight;
import de.wieland.Chess.engine.pieces.Pawn;
import de.wieland.Chess.engine.pieces.Queen;
import de.wieland.Chess.engine.pieces.Rook;

/**
 * Public class TestCheckmate - JUnit Test Case.
 * 
 * @author Moritz Wieland
 * @version 1.0
 * @date 10.09.2021
 */
public class TestCheckmate {

    @Test
    public void testFoolsMate() {

        final Board board = Board.createStandardBoard();
        final MoveTransition t1 = board.getCurrentPlayer()
                .makeMove(MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("f2"),
                                BoardUtils.getCoordinateAtPosition("f3")));

        assertTrue(t1.getMoveStatus().isDone());

        final MoveTransition t2 = t1.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t1.getToBoard(), BoardUtils.getCoordinateAtPosition("e7"),
                                BoardUtils.getCoordinateAtPosition("e5")));

        assertTrue(t2.getMoveStatus().isDone());

        final MoveTransition t3 = t2.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t2.getToBoard(), BoardUtils.getCoordinateAtPosition("g2"),
                                BoardUtils.getCoordinateAtPosition("g4")));

        assertTrue(t3.getMoveStatus().isDone());

        final MoveTransition t4 = t3.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t3.getToBoard(), BoardUtils.getCoordinateAtPosition("d8"),
                                BoardUtils.getCoordinateAtPosition("h4")));

        assertTrue(t4.getMoveStatus().isDone());

        assertTrue(t4.getToBoard().getCurrentPlayer().isInCheckMate());

    }

    @Test
    public void testScholarsMate() {

        final Board board = Board.createStandardBoard();
        final MoveTransition t1 = board.getCurrentPlayer()
                .makeMove(MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("e2"),
                                BoardUtils.getCoordinateAtPosition("e4")));

        assertTrue(t1.getMoveStatus().isDone());

        final MoveTransition t2 = t1.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t1.getToBoard(), BoardUtils.getCoordinateAtPosition("a7"),
                        BoardUtils.getCoordinateAtPosition("a6")));

        assertTrue(t2.getMoveStatus().isDone());

        final MoveTransition t3 = t2.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t2.getToBoard(), BoardUtils.getCoordinateAtPosition("d1"),
                        BoardUtils.getCoordinateAtPosition("f3")));

        assertTrue(t3.getMoveStatus().isDone());

        final MoveTransition t4 = t3.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t3.getToBoard(), BoardUtils.getCoordinateAtPosition("a6"),
                        BoardUtils.getCoordinateAtPosition("a5")));

        assertTrue(t4.getMoveStatus().isDone());

        final MoveTransition t5 = t4.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t4.getToBoard(), BoardUtils.getCoordinateAtPosition("f1"),
                        BoardUtils.getCoordinateAtPosition("c4")));

        assertTrue(t5.getMoveStatus().isDone());

        final MoveTransition t6 = t5.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t5.getToBoard(), BoardUtils.getCoordinateAtPosition("a5"),
                        BoardUtils.getCoordinateAtPosition("a4")));

        assertTrue(t6.getMoveStatus().isDone());

        final MoveTransition t7 = t6.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t6.getToBoard(), BoardUtils.getCoordinateAtPosition("f3"),
                        BoardUtils.getCoordinateAtPosition("f7")));

        assertTrue(t7.getMoveStatus().isDone());
        assertTrue(t7.getToBoard().getCurrentPlayer().isInCheckMate());

    }

    @Test
    public void testLegalsMate() {

        final Board board = Board.createStandardBoard();
        final MoveTransition t1 = board.getCurrentPlayer()
                .makeMove(MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("e2"),
                                BoardUtils.getCoordinateAtPosition("e4")));

        assertTrue(t1.getMoveStatus().isDone());

        final MoveTransition t2 = t1.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t1.getToBoard(), BoardUtils.getCoordinateAtPosition("e7"),
                        BoardUtils.getCoordinateAtPosition("e5")));

        assertTrue(t2.getMoveStatus().isDone());

        final MoveTransition t3 = t2.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t2.getToBoard(), BoardUtils.getCoordinateAtPosition("f1"),
                        BoardUtils.getCoordinateAtPosition("c4")));

        assertTrue(t3.getMoveStatus().isDone());

        final MoveTransition t4 = t3.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t3.getToBoard(), BoardUtils.getCoordinateAtPosition("d7"),
                        BoardUtils.getCoordinateAtPosition("d6")));

        assertTrue(t4.getMoveStatus().isDone());

        final MoveTransition t5 = t4.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t4.getToBoard(), BoardUtils.getCoordinateAtPosition("g1"),
                        BoardUtils.getCoordinateAtPosition("f3")));

        assertTrue(t5.getMoveStatus().isDone());

        final MoveTransition t6 = t5.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t5.getToBoard(), BoardUtils.getCoordinateAtPosition("c8"),
                        BoardUtils.getCoordinateAtPosition("g4")));

        assertTrue(t6.getMoveStatus().isDone());

        final MoveTransition t7 = t6.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t6.getToBoard(), BoardUtils.getCoordinateAtPosition("b1"),
                        BoardUtils.getCoordinateAtPosition("c3")));

        assertTrue(t7.getMoveStatus().isDone());

        final MoveTransition t8 = t7.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t7.getToBoard(), BoardUtils.getCoordinateAtPosition("g7"),
                        BoardUtils.getCoordinateAtPosition("g6")));

        assertTrue(t8.getMoveStatus().isDone());

        final MoveTransition t9 = t8.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t8.getToBoard(), BoardUtils.getCoordinateAtPosition("f3"),
                        BoardUtils.getCoordinateAtPosition("e5")));

        assertTrue(t9.getMoveStatus().isDone());

        final MoveTransition t10 = t9.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t9.getToBoard(), BoardUtils.getCoordinateAtPosition("g4"),
                        BoardUtils.getCoordinateAtPosition("d1")));

        assertTrue(t10.getMoveStatus().isDone());

        final MoveTransition t11 = t10.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t10.getToBoard(), BoardUtils.getCoordinateAtPosition("c4"),
                        BoardUtils.getCoordinateAtPosition("f7")));

        assertTrue(t11.getMoveStatus().isDone());

        final MoveTransition t12 = t11.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t11.getToBoard(), BoardUtils.getCoordinateAtPosition("e8"),
                        BoardUtils.getCoordinateAtPosition("e7")));

        assertTrue(t12.getMoveStatus().isDone());

        final MoveTransition t13 = t12.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t12.getToBoard(), BoardUtils.getCoordinateAtPosition("c3"),
                        BoardUtils.getCoordinateAtPosition("d5")));

        assertTrue(t13.getMoveStatus().isDone());
        assertTrue(t13.getToBoard().getCurrentPlayer().isInCheckMate());

    }

    @Test
    public void testSevenMoveMate() {

        final Board board = Board.createStandardBoard();
        final MoveTransition t1 = board.getCurrentPlayer()
                .makeMove(MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("e2"),
                                BoardUtils.getCoordinateAtPosition("e4")));

        assertTrue(t1.getMoveStatus().isDone());

        final MoveTransition t2 = t1.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t1.getToBoard(), BoardUtils.getCoordinateAtPosition("e7"),
                        BoardUtils.getCoordinateAtPosition("e5")));

        assertTrue(t2.getMoveStatus().isDone());

        final MoveTransition t3 = t2.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t2.getToBoard(), BoardUtils.getCoordinateAtPosition("d2"),
                        BoardUtils.getCoordinateAtPosition("d4")));

        assertTrue(t3.getMoveStatus().isDone());

        final MoveTransition t4 = t3.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t3.getToBoard(), BoardUtils.getCoordinateAtPosition("d7"),
                        BoardUtils.getCoordinateAtPosition("d6")));

        assertTrue(t4.getMoveStatus().isDone());

        final MoveTransition t5 = t4.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t4.getToBoard(), BoardUtils.getCoordinateAtPosition("d4"),
                        BoardUtils.getCoordinateAtPosition("e5")));

        assertTrue(t5.getMoveStatus().isDone());

        final MoveTransition t6 = t5.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t5.getToBoard(), BoardUtils.getCoordinateAtPosition("d8"),
                        BoardUtils.getCoordinateAtPosition("e7")));

        assertTrue(t6.getMoveStatus().isDone());

        final MoveTransition t7 = t6.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t6.getToBoard(), BoardUtils.getCoordinateAtPosition("e5"),
                        BoardUtils.getCoordinateAtPosition("d6")));

        assertTrue(t7.getMoveStatus().isDone());

        final MoveTransition t8 = t7.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t7.getToBoard(), BoardUtils.getCoordinateAtPosition("e7"),
                        BoardUtils.getCoordinateAtPosition("e4")));

        assertTrue(t8.getMoveStatus().isDone());

        final MoveTransition t9 = t8.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t8.getToBoard(), BoardUtils.getCoordinateAtPosition("f1"),
                        BoardUtils.getCoordinateAtPosition("e2")));

        assertTrue(t9.getMoveStatus().isDone());

        final MoveTransition t10 = t9.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t9.getToBoard(), BoardUtils.getCoordinateAtPosition("e4"),
                        BoardUtils.getCoordinateAtPosition("g2")));

        assertTrue(t10.getMoveStatus().isDone());

        final MoveTransition t11 = t10.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t10.getToBoard(), BoardUtils.getCoordinateAtPosition("d6"),
                        BoardUtils.getCoordinateAtPosition("c7")));

        assertTrue(t11.getMoveStatus().isDone());

        final MoveTransition t12 = t11.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t11.getToBoard(), BoardUtils.getCoordinateAtPosition("g2"),
                        BoardUtils.getCoordinateAtPosition("h1")));

        assertTrue(t12.getMoveStatus().isDone());

        final MoveTransition t13 = t12.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t12.getToBoard(), BoardUtils.getCoordinateAtPosition("d1"),
                        BoardUtils.getCoordinateAtPosition("d8")));

        assertTrue(t13.getMoveStatus().isDone());
        assertTrue(t13.getToBoard().getCurrentPlayer().isInCheckMate());

    }

    @Test
    public void testGrecoGame() {

        final Board board = Board.createStandardBoard();
        final MoveTransition t1 = board.getCurrentPlayer()
                .makeMove(MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("d2"),
                                BoardUtils.getCoordinateAtPosition("d4")));

        assertTrue(t1.getMoveStatus().isDone());

        final MoveTransition t2 = t1.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t1.getToBoard(), BoardUtils.getCoordinateAtPosition("g8"),
                        BoardUtils.getCoordinateAtPosition("f6")));

        assertTrue(t2.getMoveStatus().isDone());

        final MoveTransition t3 = t2.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t2.getToBoard(), BoardUtils.getCoordinateAtPosition("b1"),
                        BoardUtils.getCoordinateAtPosition("d2")));

        assertTrue(t3.getMoveStatus().isDone());

        final MoveTransition t4 = t3.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t3.getToBoard(), BoardUtils.getCoordinateAtPosition("e7"),
                        BoardUtils.getCoordinateAtPosition("e5")));

        assertTrue(t4.getMoveStatus().isDone());

        final MoveTransition t5 = t4.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t4.getToBoard(), BoardUtils.getCoordinateAtPosition("d4"),
                        BoardUtils.getCoordinateAtPosition("e5")));

        assertTrue(t5.getMoveStatus().isDone());

        final MoveTransition t6 = t5.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t5.getToBoard(), BoardUtils.getCoordinateAtPosition("f6"),
                        BoardUtils.getCoordinateAtPosition("g4")));


        assertTrue(t6.getMoveStatus().isDone());

        final MoveTransition t7 = t6.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t6.getToBoard(), BoardUtils.getCoordinateAtPosition("h2"),
                        BoardUtils.getCoordinateAtPosition("h3")));

        assertTrue(t7.getMoveStatus().isDone());

        final MoveTransition t8 = t7.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t7.getToBoard(), BoardUtils.getCoordinateAtPosition("g4"),
                        BoardUtils.getCoordinateAtPosition("e3")));

        assertTrue(t8.getMoveStatus().isDone());

        final MoveTransition t9 = t8.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t8.getToBoard(), BoardUtils.getCoordinateAtPosition("f2"),
                        BoardUtils.getCoordinateAtPosition("e3")));

        assertTrue(t9.getMoveStatus().isDone());

        final MoveTransition t10 = t9.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t9.getToBoard(), BoardUtils.getCoordinateAtPosition("d8"),
                        BoardUtils.getCoordinateAtPosition("h4")));

        assertTrue(t10.getMoveStatus().isDone());

        final MoveTransition t11 = t10.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t10.getToBoard(), BoardUtils.getCoordinateAtPosition("g2"),
                        BoardUtils.getCoordinateAtPosition("g3")));

        assertTrue(t11.getMoveStatus().isDone());

        final MoveTransition t12 = t11.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t11.getToBoard(), BoardUtils.getCoordinateAtPosition("h4"),
                        BoardUtils.getCoordinateAtPosition("g3")));

        assertTrue(t12.getMoveStatus().isDone());
        assertTrue(t12.getToBoard().getCurrentPlayer().isInCheckMate());

    }

    @Test
    public void testOlympicGame() {

        final Board board = Board.createStandardBoard();
        final MoveTransition t1 = board.getCurrentPlayer()
                .makeMove(MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("e2"),
                        BoardUtils.getCoordinateAtPosition("e4")));

        assertTrue(t1.getMoveStatus().isDone());

        final MoveTransition t2 = t1.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t1.getToBoard(), BoardUtils.getCoordinateAtPosition("c7"),
                        BoardUtils.getCoordinateAtPosition("c6")));

        assertTrue(t2.getMoveStatus().isDone());

        final MoveTransition t3 = t2.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t2.getToBoard(), BoardUtils.getCoordinateAtPosition("g1"),
                        BoardUtils.getCoordinateAtPosition("f3")));

        assertTrue(t3.getMoveStatus().isDone());

        final MoveTransition t4 = t3.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t3.getToBoard(), BoardUtils.getCoordinateAtPosition("d7"),
                        BoardUtils.getCoordinateAtPosition("d5")));

        assertTrue(t4.getMoveStatus().isDone());

        final MoveTransition t5 = t4.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t4.getToBoard(), BoardUtils.getCoordinateAtPosition("b1"),
                        BoardUtils.getCoordinateAtPosition("c3")));

        assertTrue(t5.getMoveStatus().isDone());

        final MoveTransition t6 = t5.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t5.getToBoard(), BoardUtils.getCoordinateAtPosition("d5"),
                        BoardUtils.getCoordinateAtPosition("e4")));

        assertTrue(t6.getMoveStatus().isDone());

        final MoveTransition t7 = t6.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t6.getToBoard(), BoardUtils.getCoordinateAtPosition("c3"),
                        BoardUtils.getCoordinateAtPosition("e4")));

        assertTrue(t7.getMoveStatus().isDone());

        final MoveTransition t8 = t7.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t7.getToBoard(), BoardUtils.getCoordinateAtPosition("b8"),
                        BoardUtils.getCoordinateAtPosition("d7")));

        assertTrue(t8.getMoveStatus().isDone());

        final MoveTransition t9 = t8.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t8.getToBoard(), BoardUtils.getCoordinateAtPosition("d1"),
                        BoardUtils.getCoordinateAtPosition("e2")));

        assertTrue(t9.getMoveStatus().isDone());

        final MoveTransition t10 = t9.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t9.getToBoard(), BoardUtils.getCoordinateAtPosition("g8"),
                        BoardUtils.getCoordinateAtPosition("f6")));

        assertTrue(t10.getMoveStatus().isDone());

        final MoveTransition t11 = t10.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t10.getToBoard(), BoardUtils.getCoordinateAtPosition("e4"),
                        BoardUtils.getCoordinateAtPosition("d6")));

        assertTrue(t11.getMoveStatus().isDone());
        assertTrue(t11.getToBoard().getCurrentPlayer().isInCheckMate());

    }

    @Test
    public void testAnotherGame() {

        final Board board = Board.createStandardBoard();
        final MoveTransition t1 = board.getCurrentPlayer()
                .makeMove(MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("e2"),
                                BoardUtils.getCoordinateAtPosition("e4")));

        assertTrue(t1.getMoveStatus().isDone());

        final MoveTransition t2 = t1.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t1.getToBoard(), BoardUtils.getCoordinateAtPosition("e7"),
                        BoardUtils.getCoordinateAtPosition("e5")));

        assertTrue(t2.getMoveStatus().isDone());

        final MoveTransition t3 = t2.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t2.getToBoard(), BoardUtils.getCoordinateAtPosition("g1"),
                        BoardUtils.getCoordinateAtPosition("f3")));

        assertTrue(t3.getMoveStatus().isDone());

        final MoveTransition t4 = t3.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t3.getToBoard(), BoardUtils.getCoordinateAtPosition("b8"),
                        BoardUtils.getCoordinateAtPosition("c6")));

        assertTrue(t4.getMoveStatus().isDone());

        final MoveTransition t5 = t4.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t4.getToBoard(), BoardUtils.getCoordinateAtPosition("f1"),
                        BoardUtils.getCoordinateAtPosition("c4")));

        assertTrue(t5.getMoveStatus().isDone());

        final MoveTransition t6 = t5.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t5.getToBoard(), BoardUtils.getCoordinateAtPosition("c6"),
                        BoardUtils.getCoordinateAtPosition("d4")));

        assertTrue(t6.getMoveStatus().isDone());

        final MoveTransition t7 = t6.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t6.getToBoard(), BoardUtils.getCoordinateAtPosition("f3"),
                        BoardUtils.getCoordinateAtPosition("e5")));

        assertTrue(t7.getMoveStatus().isDone());

        final MoveTransition t8 = t7.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t7.getToBoard(), BoardUtils.getCoordinateAtPosition("d8"),
                        BoardUtils.getCoordinateAtPosition("g5")));

        assertTrue(t8.getMoveStatus().isDone());

        final MoveTransition t9 = t8.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t8.getToBoard(), BoardUtils.getCoordinateAtPosition("e5"),
                        BoardUtils.getCoordinateAtPosition("f7")));

        assertTrue(t9.getMoveStatus().isDone());

        final MoveTransition t10 = t9.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t9.getToBoard(), BoardUtils.getCoordinateAtPosition("g5"),
                        BoardUtils.getCoordinateAtPosition("g2")));

        assertTrue(t10.getMoveStatus().isDone());

        final MoveTransition t11 = t10.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t10.getToBoard(), BoardUtils.getCoordinateAtPosition("h1"),
                        BoardUtils.getCoordinateAtPosition("f1")));

        assertTrue(t11.getMoveStatus().isDone());

        final MoveTransition t12 = t11.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t11.getToBoard(), BoardUtils.getCoordinateAtPosition("g2"),
                        BoardUtils.getCoordinateAtPosition("e4")));

        assertTrue(t12.getMoveStatus().isDone());

        final MoveTransition t13 = t12.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t12.getToBoard(), BoardUtils.getCoordinateAtPosition("c4"),
                        BoardUtils.getCoordinateAtPosition("e2")));

        assertTrue(t13.getMoveStatus().isDone());

        final MoveTransition t14 = t13.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t13.getToBoard(), BoardUtils.getCoordinateAtPosition("d4"),
                        BoardUtils.getCoordinateAtPosition("f3")));

        assertTrue(t14.getMoveStatus().isDone());
        assertTrue(t14.getToBoard().getCurrentPlayer().isInCheckMate());

    }

    @Test
    public void testSmotheredMate() {

        final Board board = Board.createStandardBoard();
        final MoveTransition t1 = board.getCurrentPlayer()
                .makeMove(MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("e2"),
                                BoardUtils.getCoordinateAtPosition("e4")));

        assertTrue(t1.getMoveStatus().isDone());

        final MoveTransition t2 = t1.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t1.getToBoard(), BoardUtils.getCoordinateAtPosition("e7"),
                        BoardUtils.getCoordinateAtPosition("e5")));

        assertTrue(t2.getMoveStatus().isDone());

        final MoveTransition t3 = t2.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t2.getToBoard(), BoardUtils.getCoordinateAtPosition("g1"),
                        BoardUtils.getCoordinateAtPosition("e2")));

        assertTrue(t3.getMoveStatus().isDone());

        final MoveTransition t4 = t3.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t3.getToBoard(), BoardUtils.getCoordinateAtPosition("b8"),
                        BoardUtils.getCoordinateAtPosition("c6")));

        assertTrue(t4.getMoveStatus().isDone());

        final MoveTransition t5 = t4.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t4.getToBoard(), BoardUtils.getCoordinateAtPosition("b1"),
                        BoardUtils.getCoordinateAtPosition("c3")));

        assertTrue(t4.getMoveStatus().isDone());

        final MoveTransition t6 = t5.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t5.getToBoard(), BoardUtils.getCoordinateAtPosition("c6"),
                        BoardUtils.getCoordinateAtPosition("d4")));

        assertTrue(t6.getMoveStatus().isDone());

        final MoveTransition t7 = t6.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t6.getToBoard(), BoardUtils.getCoordinateAtPosition("g2"),
                        BoardUtils.getCoordinateAtPosition("g3")));

        assertTrue(t7.getMoveStatus().isDone());

        final MoveTransition t8 = t7.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t7.getToBoard(), BoardUtils.getCoordinateAtPosition("d4"),
                        BoardUtils.getCoordinateAtPosition("f3")));

        assertTrue(t8.getMoveStatus().isDone());
        assertTrue(t8.getToBoard().getCurrentPlayer().isInCheckMate());

    }

    @Test
    public void testHippopotamusMate() {

        final Board board = Board.createStandardBoard();
        final MoveTransition t1 = board.getCurrentPlayer()
                .makeMove(MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("e2"),
                                BoardUtils.getCoordinateAtPosition("e4")));

        assertTrue(t1.getMoveStatus().isDone());

        final MoveTransition t2 = t1.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t1.getToBoard(), BoardUtils.getCoordinateAtPosition("e7"),
                        BoardUtils.getCoordinateAtPosition("e5")));

        assertTrue(t2.getMoveStatus().isDone());

        final MoveTransition t3 = t2.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t2.getToBoard(), BoardUtils.getCoordinateAtPosition("g1"),
                        BoardUtils.getCoordinateAtPosition("e2")));

        assertTrue(t3.getMoveStatus().isDone());

        final MoveTransition t4 = t3.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t3.getToBoard(), BoardUtils.getCoordinateAtPosition("d8"),
                        BoardUtils.getCoordinateAtPosition("h4")));

        assertTrue(t4.getMoveStatus().isDone());

        final MoveTransition t5 = t4.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t4.getToBoard(), BoardUtils.getCoordinateAtPosition("b1"),
                        BoardUtils.getCoordinateAtPosition("c3")));

        assertTrue(t5.getMoveStatus().isDone());

        final MoveTransition t6 = t5.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t5.getToBoard(), BoardUtils.getCoordinateAtPosition("b8"),
                        BoardUtils.getCoordinateAtPosition("c6")));

        assertTrue(t6.getMoveStatus().isDone());

        final MoveTransition t7 = t6.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t6.getToBoard(), BoardUtils.getCoordinateAtPosition("g2"),
                        BoardUtils.getCoordinateAtPosition("g3")));

        assertTrue(t7.getMoveStatus().isDone());

        final MoveTransition t8 = t7.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t7.getToBoard(), BoardUtils.getCoordinateAtPosition("h4"),
                        BoardUtils.getCoordinateAtPosition("g5")));


        assertTrue(t8.getMoveStatus().isDone());

        final MoveTransition t9 = t8.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t8.getToBoard(), BoardUtils.getCoordinateAtPosition("d2"),
                        BoardUtils.getCoordinateAtPosition("d4")));

        assertTrue(t9.getMoveStatus().isDone());

        final MoveTransition t10 = t9.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t9.getToBoard(), BoardUtils.getCoordinateAtPosition("c6"),
                        BoardUtils.getCoordinateAtPosition("d4")));

        assertTrue(t10.getMoveStatus().isDone());

        final MoveTransition t11 = t10.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t10.getToBoard(), BoardUtils.getCoordinateAtPosition("c1"),
                        BoardUtils.getCoordinateAtPosition("g5")));

        assertTrue(t11.getMoveStatus().isDone());

        final MoveTransition t12 = t11.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t11.getToBoard(), BoardUtils.getCoordinateAtPosition("d4"),
                        BoardUtils.getCoordinateAtPosition("f3")));

        assertTrue(t12.getMoveStatus().isDone());
        assertTrue(t12.getToBoard().getCurrentPlayer().isInCheckMate());

    }

    @Test
    public void testBlackburneShillingMate() {

        final Board board = Board.createStandardBoard();

        final MoveTransition t1 = board.getCurrentPlayer()
                .makeMove(MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("e2"),
                                BoardUtils.getCoordinateAtPosition("e4")));

        assertTrue(t1.getMoveStatus().isDone());

        final MoveTransition t2 = t1.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t1.getToBoard(), BoardUtils.getCoordinateAtPosition("e7"),
                        BoardUtils.getCoordinateAtPosition("e5")));

        assertTrue(t2.getMoveStatus().isDone());

        final MoveTransition t3 = t2.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t2.getToBoard(), BoardUtils.getCoordinateAtPosition("g1"),
                        BoardUtils.getCoordinateAtPosition("f3")));

        assertTrue(t3.getMoveStatus().isDone());

        final MoveTransition t4 = t3.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t3.getToBoard(), BoardUtils.getCoordinateAtPosition("b8"),
                        BoardUtils.getCoordinateAtPosition("c6")));

        assertTrue(t4.getMoveStatus().isDone());

        final MoveTransition t5 = t4.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t4.getToBoard(), BoardUtils.getCoordinateAtPosition("f1"),
                        BoardUtils.getCoordinateAtPosition("c4")));

        assertTrue(t5.getMoveStatus().isDone());

        final MoveTransition t6 = t5.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t5.getToBoard(), BoardUtils.getCoordinateAtPosition("c6"),
                        BoardUtils.getCoordinateAtPosition("d4")));

        assertTrue(t6.getMoveStatus().isDone());

        final MoveTransition t7 = t6.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t6.getToBoard(), BoardUtils.getCoordinateAtPosition("f3"),
                        BoardUtils.getCoordinateAtPosition("e5")));

        assertTrue(t7.getMoveStatus().isDone());

        final MoveTransition t8 = t7.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t7.getToBoard(), BoardUtils.getCoordinateAtPosition("d8"),
                        BoardUtils.getCoordinateAtPosition("g5")));

        assertTrue(t8.getMoveStatus().isDone());

        final MoveTransition t9 = t8.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t8.getToBoard(), BoardUtils.getCoordinateAtPosition("e5"),
                        BoardUtils.getCoordinateAtPosition("f7")));

        assertTrue(t9.getMoveStatus().isDone());

        final MoveTransition t10 = t9.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t9.getToBoard(), BoardUtils.getCoordinateAtPosition("g5"),
                        BoardUtils.getCoordinateAtPosition("g2")));

        assertTrue(t10.getMoveStatus().isDone());

        final MoveTransition t11 = t10.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t10.getToBoard(), BoardUtils.getCoordinateAtPosition("h1"),
                        BoardUtils.getCoordinateAtPosition("f1")));

        assertTrue(t11.getMoveStatus().isDone());

        final MoveTransition t12 = t11.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t11.getToBoard(), BoardUtils.getCoordinateAtPosition("g2"),
                        BoardUtils.getCoordinateAtPosition("e4")));

        assertTrue(t12.getMoveStatus().isDone());

        final MoveTransition t13 = t12.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t12.getToBoard(), BoardUtils.getCoordinateAtPosition("c4"),
                        BoardUtils.getCoordinateAtPosition("e2")));

        assertTrue(t13.getMoveStatus().isDone());

        final MoveTransition t14 = t13.getToBoard()
                .getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t13.getToBoard(), BoardUtils.getCoordinateAtPosition("d4"),
                        BoardUtils.getCoordinateAtPosition("f3")));

        assertTrue(t14.getMoveStatus().isDone());
        assertTrue(t14.getToBoard().getCurrentPlayer().isInCheckMate());

    }

    @Test
    public void testAnastasiaMate() {

        final Builder builder = new Builder();

        // Black Layout
        builder.setPiece(new Rook(Alliance.BLACK, 0));
        builder.setPiece(new Rook(Alliance.BLACK, 5));
        builder.setPiece(new Pawn(Alliance.BLACK, 8));
        builder.setPiece(new Pawn(Alliance.BLACK, 9));
        builder.setPiece(new Pawn(Alliance.BLACK, 10));
        builder.setPiece(new Pawn(Alliance.BLACK, 13));
        builder.setPiece(new Pawn(Alliance.BLACK, 14));
        builder.setPiece(new King(Alliance.BLACK, 15, false, false));
        // White Layout
        builder.setPiece(new Knight(Alliance.WHITE, 12));
        builder.setPiece(new Rook(Alliance.WHITE, 27));
        builder.setPiece(new Pawn(Alliance.WHITE, 41));
        builder.setPiece(new Pawn(Alliance.WHITE, 48));
        builder.setPiece(new Pawn(Alliance.WHITE, 53));
        builder.setPiece(new Pawn(Alliance.WHITE, 54));
        builder.setPiece(new Pawn(Alliance.WHITE, 55));
        builder.setPiece(new King(Alliance.WHITE, 62, false, false));
        // Set the current player
        builder.setMoveMaker(Alliance.WHITE);

        final Board board = builder.build();
        final MoveTransition t1 = board.getCurrentPlayer()
                .makeMove(MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("d5"),
                                BoardUtils.getCoordinateAtPosition("h5")));

        assertTrue(t1.getMoveStatus().isDone());
        assertTrue(t1.getToBoard().getCurrentPlayer().isInCheckMate());
    }

    @Test
    public void testTwoBishopMate() {

        final Builder builder = new Builder();

        builder.setPiece(new King(Alliance.BLACK, 7, false, false));
        builder.setPiece(new Pawn(Alliance.BLACK, 8));
        builder.setPiece(new Pawn(Alliance.BLACK, 10));
        builder.setPiece(new Pawn(Alliance.BLACK, 15));
        builder.setPiece(new Pawn(Alliance.BLACK, 17));
        // White Layout
        builder.setPiece(new Bishop(Alliance.WHITE, 40));
        builder.setPiece(new Bishop(Alliance.WHITE, 48));
        builder.setPiece(new King(Alliance.WHITE, 53, false, false));
        // Set the current player
        builder.setMoveMaker(Alliance.WHITE);

        final Board board = builder.build();
        final MoveTransition t1 = board.getCurrentPlayer()
                .makeMove(MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("a3"),
                                BoardUtils.getCoordinateAtPosition("b2")));

        assertTrue(t1.getMoveStatus().isDone());
        assertTrue(t1.getToBoard().getCurrentPlayer().isInCheckMate());
    }

    @Test
    public void testQueenRookMate() {

        final Builder builder = new Builder();

        // Black Layout
        builder.setPiece(new King(Alliance.BLACK, 5, false, false));
        // White Layout
        builder.setPiece(new Rook(Alliance.WHITE, 9));
        builder.setPiece(new Queen(Alliance.WHITE, 16));
        builder.setPiece(new King(Alliance.WHITE, 59, false, false));
        // Set the current player
        builder.setMoveMaker(Alliance.WHITE);

        final Board board = builder.build();
        final MoveTransition t1 = board.getCurrentPlayer()
                .makeMove(MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("a6"),
                                BoardUtils.getCoordinateAtPosition("a8")));

        assertTrue(t1.getMoveStatus().isDone());
        assertTrue(t1.getToBoard().getCurrentPlayer().isInCheckMate());

    }

    @Test
    public void testQueenKnightMate() {

        final Builder builder = new Builder();

        // Black Layout
        builder.setPiece(new King(Alliance.BLACK, 4, false, false));
        // White Layout
        builder.setPiece(new Queen(Alliance.WHITE, 15));
        builder.setPiece(new Knight(Alliance.WHITE, 29));
        builder.setPiece(new Pawn(Alliance.WHITE, 55));
        builder.setPiece(new King(Alliance.WHITE, 60, false, false));
        // Set the current player
        builder.setMoveMaker(Alliance.WHITE);

        final Board board = builder.build();
        final MoveTransition t1 = board.getCurrentPlayer()
                .makeMove(MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("h7"),
                                BoardUtils.getCoordinateAtPosition("e7")));

        assertTrue(t1.getMoveStatus().isDone());
        assertTrue(t1.getToBoard().getCurrentPlayer().isInCheckMate());

    }

    @Test
    public void testBackRankMate() {

        final Builder builder = new Builder();
        // Black Layout
        builder.setPiece(new King(Alliance.BLACK, 4, false, false));
        builder.setPiece(new Rook(Alliance.BLACK, 18));
        // White Layout
        builder.setPiece(new Pawn(Alliance.WHITE, 53));
        builder.setPiece(new Pawn(Alliance.WHITE, 54));
        builder.setPiece(new Pawn(Alliance.WHITE, 55));
        builder.setPiece(new King(Alliance.WHITE, 62, false, false));
        // Set the current player
        builder.setMoveMaker(Alliance.BLACK);

        final Board board = builder.build();

        final MoveTransition t1 = board.getCurrentPlayer()
                .makeMove(MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("c6"),
                                BoardUtils.getCoordinateAtPosition("c1")));

        assertTrue(t1.getMoveStatus().isDone());
        assertTrue(t1.getToBoard().getCurrentPlayer().isInCheckMate());

    }

//    @Test
//    public void testMateInTwoTest1() {
//        final Board board = FenUtilities.createGameFromFEN("6k1/1b4pp/1B1Q4/4p1P1/p3q3/2P3r1/P1P2PP1/R5K1 w - - 1 0");
//        final MoveStrategy alphaBeta = new StockAlphaBeta(6);
//        final Move bestMove = alphaBeta.execute(board);
//        assertEquals(bestMove, Move.MoveFactory
//                .createMove(board, BoardUtils.getCoordinateAtPosition("d6"), BoardUtils.getCoordinateAtPosition("e6")));
//    }
//
//    @Test
//    public void testMateInTwoTest2() {
//        final Board board = FenUtilities.createGameFromFEN("3r3r/1Q5p/p3q2k/3NBp1B/3p3n/5P2/PP4PP/4R2K w - - 1 0");
//        final MoveStrategy alphaBeta = new StockAlphaBeta(6);
//        final Move bestMove = alphaBeta.execute(board);
//        assertEquals(
//                bestMove,
//                Move.MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("b7"),
//                        BoardUtils.getCoordinateAtPosition("g7")));
//    }
//
//    @Test
//    public void testMateInTwoTest3() {
//        final Board board = FenUtilities.createGameFromFEN("rn3rk1/1R3ppp/2p5/8/PQ2P3/1P5P/2P1qPP1/3R2K1 w - - 1 0");
//        final MoveStrategy alphaBeta = new StockAlphaBeta(1);
//        final Move bestMove = alphaBeta.execute(board);
//        assertEquals(bestMove, Move.MoveFactory
//                .createMove(board, BoardUtils.getCoordinateAtPosition("b4"), BoardUtils.getCoordinateAtPosition("f8")));
//    }
//
//    @Test
//    public void testMateInFourTest1() {
//        final Board board = FenUtilities.createGameFromFEN("7k/4r2B/1pb5/2P5/4p2Q/2q5/2P2R2/1K6 w - - 1 0");
//        final MoveStrategy alphaBeta = new StockAlphaBeta(6);
//        final Move bestMove = alphaBeta.execute(board);
//        assertEquals(bestMove, Move.MoveFactory
//                .createMove(board, BoardUtils.getCoordinateAtPosition("f2"), BoardUtils.getCoordinateAtPosition("f8")));
//    }
//
//    @Test
//    public void testMagnusBlackToMoveAndWinTest1() {
//        final Board board = FenUtilities.createGameFromFEN("2rr2k1/pb3pp1/4q2p/2pn4/2Q1P3/P4P2/1P3BPP/2KR2NR b - - 0 1");
//        final MoveStrategy alphaBeta = new StockAlphaBeta(6);
//        final Move bestMove = alphaBeta.execute(board);
//        assertEquals(bestMove, Move.MoveFactory
//                .createMove(board, BoardUtils.getCoordinateAtPosition("d5"), BoardUtils.getCoordinateAtPosition("e3")));
//    }

}

