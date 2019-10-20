package kk.chessbot.moves;

import com.carrotsearch.hppc.IntHashSet;
import kk.chessbot.Board;
import kk.chessbot.Piece;
import kk.chessbot.wrappers.Move;
import kk.chessbot.wrappers.Position;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.stream.StreamSupport.stream;
import static kk.chessbot.moves.BoardUtils.board;
import static kk.chessbot.moves.BoardUtils.boardFromString;
import static kk.chessbot.wrappers.Move.move;

class MovesTest {

    private static Set<Move> generateAllMoves(Board board) {
        MoveCollector moveCollector = new MoveCollector();
        board.forEachPiece(moveCollector::generateMoves);
        IntHashSet moves = moveCollector.get();
        return stream(moves.spliterator(), false).map(v -> new Move(v.value)).collect(Collectors.toSet());

    }

    private static void testMoves(Board board, Move... expectedMoves) {
        testMoves(board, move -> true, expectedMoves);
    }

    private static void testMoves(Board board, Predicate<Move> filter, Move... expectedMoves) {
        Set<Move> moves = generateAllMoves(board);
        moves.removeIf(filter.negate());
        Assertions.assertEquals(new HashSet<>(Arrays.asList(expectedMoves)), moves);
    }


    @Test
    void test_pawn_moves_from_first_line() {
        testMoves(board()
                        .with(Piece.Pawn, true, Position.position("a2"))
                        .with(Piece.Pawn, true, Position.position("h2"))
                        .with(Piece.Pawn, false, Position.position("a7"))
                        .with(Piece.Pawn, false, Position.position("h7"))
                        .build(),
                move("a2a3"),
                move("a2a4"),
                move("h2h3"),
                move("h2h4"),
                move("a7a6"),
                move("a7a5"),
                move("h7h6"),
                move("h7h5")
        );
    }

    @Test
    void test_pawn_moves() {
        testMoves(boardFromString("" +
                        /*8*/"        " +
                        /*7*/"        " +
                        /*6*/"    ♟♙  " +
                        /*5*/"  ♟  ♙  " +
                        /*4*/"  ♙♟♟   " +
                        /*3*/"   ♟    " +
                        /*2*/"    ♙   " +
                        /*1*/"        "),
//                            ABCDEFGH
                move("e2e3"),
                move("e2xd3"),
                move("f5xe6"),
                move("f6f7"),
                move("e6e5"),
                move("e6xf5"),
                move("e4e3"),
                move("d3d2"),
                move("d3xe2")
        );
    }

    @Test
    void test_knight_moves() {
        testMoves(boardFromString("" +
                        /*8*/"♘       " +
                        /*7*/"  ♟     " +
                        /*6*/"        " +
                        /*5*/"        " +
                        /*4*/"    ♞   " +
                        /*3*/"  ♘     " +
                        /*2*/"  ♟     " +
                        /*1*/"♞       "),
//                            ABCDEFGH
                move -> move.getPiece() == Piece.Knight,
                move("Na1b3"),
                move("Nc3a2"),

                move("Nc3b1"),
                move("Nc3a4"),
                move("Nc3a2"),
                move("Nc3b5"),
                move("Nc3xe4"),
                move("Nc3d5"),
                move("Nc3e2"),
                move("Nc3d1"),

                move("Ne4d2"),
                move("Ne4d6"),
                move("Ne4xc3"),
                move("Ne4c5"),
                move("Ne4f6"),
                move("Ne4f2"),
                move("Ne4g5"),
                move("Ne4g3"),

                move("Na8xc7"),
                move("Na8b6")
        );
    }

    /*
                "♜♞♝♛♚♝♞♜" +
                "♟♟♟♟♟♟♟♟" +
                "             " +
                "             " +
                "             " +
                "             " +
                "♙♙♙♙♙♙♙♙" +
                "♖♘♗♕♔♗♘♖");
     */
}