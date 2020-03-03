package kk.chessbot.moves;

import kk.chessbot.Piece;
import kk.chessbot.wrappers.Move;
import kk.chessbot.wrappers.Position;
import org.junit.jupiter.api.Test;

import static kk.chessbot.moves.BoardUtils.board;
import static kk.chessbot.moves.BoardUtils.fromString;
import static kk.chessbot.moves.TestUtils.moves;
import static kk.chessbot.moves.TestUtils.testMoves;

class MovesTest {


    @Test
    void test_pawn_moves_from_first_line() {
        testMoves(moves(
                Move.from("a2a3"),
                Move.from("a2a4"),
                Move.from("h2h3"),
                Move.from("h2h4"),
                Move.from("a7a6"),
                Move.from("a7a5"),
                Move.from("h7h6"),
                Move.from("h7h5")
                ), board()
                        .with(Piece.Pawn, true, Position.position("a2"))
                        .with(Piece.Pawn, true, Position.position("h2"))
                        .with(Piece.Pawn, false, Position.position("a7"))
                        .with(Piece.Pawn, false, Position.position("h7"))
                        .build()
        );
    }

    @Test
    void test_pawn_promotion() {
        testMoves(moves(
                Move.from("a7a8r"),
                Move.from("a7a8q"),
                Move.from("a7a8n"),
                Move.from("a7a8b"),

                Move.from("b2b1r"),
                Move.from("b2b1q"),
                Move.from("b2b1n"),
                Move.from("b2b1b")
                ), board()
                        .with(Piece.Pawn, true, Position.position("a7"))
                        .with(Piece.Pawn, false, Position.position("b2"))
                        .build()
        );

        testMoves(moves(
                Move.from("a7xb8r"),
                Move.from("a7xb8q"),
                Move.from("a7xb8n"),
                Move.from("a7xb8b")),
                board()
                        .with(Piece.Pawn, true, Position.position("a7"))
                        .with(Piece.Knight, false, Position.position("a8"))
                        .with(Piece.Knight, false, Position.position("b8"))
                        .build(), move -> move.getPiece() == Piece.Pawn

        );

        testMoves(moves(
                Move.from("d2xc1R"),
                Move.from("d2xc1Q"),
                Move.from("d2xc1N"),
                Move.from("d2xc1B")),
                board()
                        .with(Piece.Pawn, false, Position.position("d2"))
                        .with(Piece.Knight, true, Position.position("d1"))
                        .with(Piece.Knight, true, Position.position("c1"))
                        .build(), move -> move.getPiece() == Piece.Pawn

        );

    }

    @Test
    void test_pawn_moves() {
        testMoves(moves(
                Move.from("e2e3"),
                Move.from("e2xd3"),
                Move.from("f5xe6"),
                Move.from("f6f7"),
                Move.from("e6e5"),
                Move.from("e6xf5"),
                Move.from("e4e3"),
                Move.from("d3d2"),
                Move.from("d3xe2")
                ), fromString("" +
                        /*8*/"        " +
                        /*7*/"        " +
                        /*6*/"    ♟♙  " +
                        /*5*/"  ♟  ♙  " +
                        /*4*/"  ♙♟♟   " +
                        /*3*/"   ♟    " +
                        /*2*/"    ♙   " +
                        /*1*/"        ")
//                            ABCDEFGH
        );
    }


    @Test
    void test_queen_moves() {
        testMoves(moves(
                Move.from("Qa8b8"),
                Move.from("Qa8b7"),
                Move.from("Qa8a7")
                ), fromString("" +
                        /*8*/"♕ ♘     " +
                        /*7*/"        " +
                        /*6*/"♘ ♖     " +
                        /*5*/"        " +
                        /*4*/"        " +
                        /*3*/"        " +
                        /*2*/"        " +
                        /*1*/"        "), Position.position("a8")
//                            ABCDEFGH
        );
    }
//
//    @Test
//    void test_castlings_white() {
//        testMoves(moves(
//                Move.from("Ke1d1"),
//                Move.from("Ke1f1"),
//                Move.from("Ke1e2"),
//                Move.from("Ke1f2"),
//                Move.from("Ke1d2"),
//                Move.from("Ke1c1"),
//                Move.from("Ke1g1")
//                ), fromString("" +
//                        /*8*/"♜   ♚  ♜" +
//                        /*7*/"        " +
//                        /*6*/"        " +
//                        /*5*/"        " +
//                        /*4*/"        " +
//                        /*3*/"        " +
//                        /*2*/"        " +
//                        /*1*/"♖   ♔  ♖"),
//                new CastlingStatus(0b1111), Position.position("e1")
////                            ABCDEFGH
//        );
//    }
//
//    @Test
//    void test_castlings_black() {
//        testMoves(moves(
//                Move.from("Ke8d8"),
//                Move.from("Ke8f8"),
//                Move.from("Ke8e7"),
//                Move.from("Ke8f7"),
//                Move.from("Ke8d7"),
//                Move.from("Ke8c8"),
//                Move.from("Ke8g8")
//                ), fromString("" +
//                        /*8*/"♜   ♚  ♜" +
//                        /*7*/"        " +
//                        /*6*/"        " +
//                        /*5*/"        " +
//                        /*4*/"        " +
//                        /*3*/"        " +
//                        /*2*/"        " +
//                        /*1*/"♖   ♔  ♖"),
//                new CastlingStatus(0b1111), Position.position("e8")
////                            ABCDEFGH
//        );
//    }

    @Test
    void test_rook_moves() {
        testMoves(moves(
                Move.from("Rc5c6"),
                Move.from("Rc5c7"),
                Move.from("Rc5c8"),
                Move.from("Rc5c4"),
                Move.from("Rc5c3"),
                Move.from("Rc5c2"),
                Move.from("Rc5c1"),
                Move.from("Rc5b5"),
                Move.from("Rc5a5"),
                Move.from("Rc5d5"),
                Move.from("Rc5e5"),
                Move.from("Rc5f5"),
                Move.from("Rc5g5"),
                Move.from("Rc5h5")

                ), fromString("" +
                        /*8*/"        " +
                        /*7*/"        " +
                        /*6*/"        " +
                        /*5*/"  ♖     " +
                        /*4*/"        " +
                        /*3*/"        " +
                        /*2*/"        " +
                        /*1*/"        ")
//                            ABCDEFGH
        );
    }


    @Test
    void test_knight_moves() {
        testMoves(moves(
                Move.from("Na1b3"),
                Move.from("Nc3a2"),

                Move.from("Nc3b1"),
                Move.from("Nc3a4"),
                Move.from("Nc3a2"),
                Move.from("Nc3b5"),
                Move.from("Nc3xe4"),
                Move.from("Nc3d5"),
                Move.from("Nc3e2"),
                Move.from("Nc3d1"),

                Move.from("Ne4d2"),
                Move.from("Ne4d6"),
                Move.from("Ne4xc3"),
                Move.from("Ne4c5"),
                Move.from("Ne4f6"),
                Move.from("Ne4f2"),
                Move.from("Ne4g5"),
                Move.from("Ne4g3"),

                Move.from("Na8xc7"),
                Move.from("Na8b6")
                ),
                fromString("" +
                        /*8*/"♘       " +
                        /*7*/"  ♟     " +
                        /*6*/"        " +
                        /*5*/"        " +
                        /*4*/"    ♞   " +
                        /*3*/"  ♘     " +
                        /*2*/"  ♟     " +
                        /*1*/"♞       "),
//                            ABCDEFGH
                move -> move.getPiece() == Piece.Knight
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