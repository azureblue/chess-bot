package kk.chessbot.player;

import kk.chessbot.Board;
import kk.chessbot.Fen;
import kk.chessbot.Side;
import org.junit.jupiter.api.Test;

import static kk.chessbot.Fen.fen;
import static kk.chessbot.Side.Black;
import static kk.chessbot.Side.White;
import static kk.chessbot.moves.BoardUtils.fromString;
import static kk.chessbot.moves.TestUtils.set;
import static kk.chessbot.moves.TestUtils.testMoves;
import static org.junit.jupiter.api.Assertions.*;

class NotSoRandomPlayerTest {

    private static String makeMove(Board board, Side side) {
        return new NotSoRandomPlayer(board, side).makeMove().toLongNotation();
    }

    @Test
    void avoid_scholars_mate() {
        assertTrue(set(
                "Ke8e7",
                "g7g6",
                "Qd8e7",
                "Qd8f6",
                "Qd8g5",
                "Qd8h4",
                "Ng8h6",
                "d7d5"
        ).contains(makeMove(fromString("" +
                        "♜ ♝♛♚♝♞♜" +
                        "♟♟♟♟ ♟♟♟" +
                        "  ♞     " +
                        "    ♟  ♕" +
                        "  ♗ ♙   " +
                        "        " +
                        "♙♙♙♙ ♙♙♙" +
                        "♖♘♗ ♔ ♘♖"),
                Black)));

        assertTrue(set(
                "Ke1e2",
                "g2g3",
                "Qd1e2",
                "Qd1f3",
                "Qd1g4",
                "Qd1h5",
                "Ng1h3",
                "d2d4"
        ).contains(makeMove(fromString("" +
                        "♜♞♝ ♚ ♞♜" +
                        "♟♟♟♟ ♟♟♟" +
                        "        " +
                        "  ♝ ♟   " +
                        "    ♙  ♛" +
                        "♙ ♘     " +
                        " ♙♙♙ ♙♙♙" +
                        "♖ ♗♕♔♗♘♖"),
                Side.White)));
    }

    @Test
    void test_save_rook_trivial_evaluator() {
        Fen fen = fen("r1bqkbnr/n2p3p/1p4p1/p1pPB3/2B5/2P3N1/PP4PP/RN1Q1RK1 b kq - 0 13");
        String move = makeMove(fen.createBoard(), fen.activeSide());
        System.out.println(move);
        assertNotEquals("Bf8g7", move);
    }

    @Test
    void test_dont_loose_queen() {
        Fen fen = fen("8/p1k5/4q2p/2p2r1P/2P1p1p1/P3P3/2P5/R2QK1NR w - - 4 27");
        String move = makeMove(fen.createBoard(), fen.activeSide());
        System.out.println(move);
        assertNotEquals("Qd1xg4", move);
    }

    @Test
    void testTrivialValuatedMoves() {

        assertEquals("c2xb3", makeMove(fromString("" +
                /*8*/"  ♚     " +
                /*7*/"        " +
                /*6*/"        " +
                /*5*/"        " +
                /*4*/"        " +
                /*3*/"♜♜      " +
                /*2*/"  ♙     " +
                /*1*/" ♔      "), White));

        assertEquals("c4xb3", makeMove(fromString("" +
                /*8*/"  ♔     " +
                /*7*/"        " +
                /*6*/"        " +
                /*5*/"        " +
                /*4*/"  ♟     " +
                /*3*/"♖♖      " +
                /*2*/"        " +
                /*1*/" ♚      "), Black));


        testMoves(set(
                "Nf4h5",
                "Nf4e6",
                "Nf4g6",
                "Na1c2"),
                makeMove(fromString("" +
                        /*8*/" ♜♝♛♚♝ ♜" +
                        /*7*/"♟♟♟♟♟♟♟♟" +
                        /*6*/"        " +
                        /*5*/"        " +
                        /*4*/"  ♙♙♔♞ ♙" +
                        /*3*/"♗ ♘  ♕  " +
                        /*2*/"♙    ♙♙ " +
                        /*1*/"♞    ♗ ♖"), Black));

    }

    @Test
    void escapeFromMate() {

        assertEquals("c2xb3", makeMove(fromString("" +
                        /*8*/"        " +
                        /*7*/"        " +
                        /*6*/"        " +
                        /*5*/"        " +
                        /*4*/"        " +
                        /*3*/"♜♜      " +
                        /*2*/"  ♙     " +
                        /*1*/" ♔ ♚    "),
                Side.White));

        assertEquals("Kb1c2", makeMove(fromString("" +
                        /*8*/"        " +
                        /*7*/"        " +
                        /*6*/"        " +
                        /*5*/"        " +
                        /*4*/"        " +
                        /*3*/"♜♜♙     " +
                        /*2*/"        " +
                        /*1*/" ♔ ♜    "),
                Side.White));

        assertEquals("Rg3b3", makeMove(fromString("" +
                        /*8*/"        " +
                        /*7*/"        " +
                        /*6*/"        " +
                        /*5*/"        " +
                        /*4*/"♜♜♜     " +
                        /*3*/"      ♖ " +
                        /*2*/" ♔  ♖   " +
                        /*1*/"        "),
                Side.White));

        //black
        assertEquals("Rg3b3", makeMove(fromString("" +
                        /*8*/"        " +
                        /*7*/"        " +
                        /*6*/"        " +
                        /*5*/"        " +
                        /*4*/"♖♖♖     " +
                        /*3*/"      ♜ " +
                        /*2*/" ♚  ♜   " +
                        /*1*/"        "),
                Side.Black));

        assertEquals("c4xb3", makeMove(fromString("" +
                        /*8*/"        " +
                        /*7*/"        " +
                        /*6*/"        " +
                        /*5*/"        " +
                        /*4*/"  ♟     " +
                        /*3*/"♖♖      " +
                        /*2*/"        " +
                        /*1*/" ♚ ♔    "),
                Side.Black));

        assertEquals("Kb1c2", makeMove(fromString("" +
                        /*8*/"        " +
                        /*7*/"        " +
                        /*6*/"        " +
                        /*5*/"        " +
                        /*4*/"        " +
                        /*3*/"♖♖♟     " +
                        /*2*/"        " +
                        /*1*/" ♚ ♖    "),
                Side.Black));
    }

}
