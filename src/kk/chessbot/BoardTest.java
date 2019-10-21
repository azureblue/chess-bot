package kk.chessbot;

import kk.chessbot.wrappers.Move;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static kk.chessbot.moves.BoardUtils.board;
import static kk.chessbot.moves.BoardUtils.fromString;
import static kk.chessbot.wrappers.Move.move;
import static kk.chessbot.wrappers.Position.position;

class BoardTest {

    @Test
    void test_castling_queenside_w() {
        Board board = new Board();

        board.clearAll();

        Move move = move("Ke1c1");
        int apply = board.apply(move);

        Assertions.assertEquals(board()
                .with(Piece.King, true, position("c1"))
                .with(Piece.Rook, true, position("d1"))
                .build(), board);

        board.revertMove(move, apply);

        Assertions.assertEquals(board()
                .with(Piece.King, true, position("e1"))
                .with(Piece.Rook, true, position("a1"))
                .build(), board);
    }

    @Test
    void test_castling_kingside_w() {
        Board board = new Board();

        board.clearAll();

        Move move = move("Ke1g1");
        int apply = board.apply(move);

        Assertions.assertEquals(board()
                .with(Piece.King, true, position("g1"))
                .with(Piece.Rook, true, position("f1"))
                .build(), board);

        board.revertMove(move, apply);

        Assertions.assertEquals(board()
                .with(Piece.King, true, position("e1"))
                .with(Piece.Rook, true, position("h1"))
                .build(), board);
    }

    @Test
    void test_castling_queenside_b() {
        Board board = new Board();

        board.clearAll();

        Move move = move("Ke8c8");
        int apply = board.apply(move);

        Assertions.assertEquals(board()
                .with(Piece.King, false, position("c8"))
                .with(Piece.Rook, false, position("d8"))
                .build(), board);

        board.revertMove(move, apply);

        Assertions.assertEquals(board()
                .with(Piece.King, false, position("e8"))
                .with(Piece.Rook, false, position("a8"))
                .build(), board);
    }

    @Test
    void test_castling_kingside_b() {
        Board board = new Board();

        board.clearAll();

        Move move = move("Ke8g8");
        int apply = board.apply(move);

        Assertions.assertEquals(board()
                .with(Piece.King, false, position("g8"))
                .with(Piece.Rook, false, position("f8"))
                .build(), board);

        board.revertMove(move, apply);

        Assertions.assertEquals(board()
                .with(Piece.King, false, position("e8"))
                .with(Piece.Rook, false, position("h8"))
                .build(), board);
    }

    @Test
    void test_promotion() {
        Board board = fromString("" +
                /*8*/"  ♝♞    " +
                /*7*/"  ♙♙    " +
                /*6*/"        " +
                /*5*/"        " +
                /*4*/"        " +
                /*3*/"        " +
                /*2*/"        " +
                /*1*/"        ");


        Move c7xd8q = move("c7xd8q");
        int apply = board.apply(c7xd8q);

        Assertions.assertEquals(fromString("" +
                /*8*/"  ♝♕    " +
                /*7*/"   ♙    " +
                /*6*/"        " +
                /*5*/"        " +
                /*4*/"        " +
                /*3*/"        " +
                /*2*/"        " +
                /*1*/"        "), board);


        board.revertMove(c7xd8q, apply);

        Assertions.assertEquals(fromString("" +
                /*8*/"  ♝♞    " +
                /*7*/"  ♙♙    " +
                /*6*/"        " +
                /*5*/"        " +
                /*4*/"        " +
                /*3*/"        " +
                /*2*/"        " +
                /*1*/"        "), board);


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