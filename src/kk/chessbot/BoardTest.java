package kk.chessbot;

import kk.chessbot.wrappers.Move;
import org.junit.jupiter.api.Test;

import static kk.chessbot.moves.BoardUtils.board;
import static kk.chessbot.moves.BoardUtils.fromString;
import static kk.chessbot.wrappers.Position.position;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BoardTest {

    @Test
    void test_castling_queenside_w() {
        Board board = new Board();

        board.clearAll();

        Move move = Move.from("Ke1c1");
        int apply = board.apply(move.raw());

        assertEquals(board()
                .with(Piece.King, true, position("c1"))
                .with(Piece.Rook, true, position("d1"))
                .build(), board);

        board.revertMove(move.raw(), apply);

        assertEquals(board()
                .with(Piece.King, true, position("e1"))
                .with(Piece.Rook, true, position("a1"))
                .build(), board);
    }

    @Test
    void test_castling_kingside_w() {
        Board board = new Board();

        board.clearAll();

        Move move = Move.from("Ke1g1");
        int apply = board.apply(move.raw());

        assertEquals(board()
                .with(Piece.King, true, position("g1"))
                .with(Piece.Rook, true, position("f1"))
                .build(), board);

        board.revertMove(move.raw(), apply);

        assertEquals(board()
                .with(Piece.King, true, position("e1"))
                .with(Piece.Rook, true, position("h1"))
                .build(), board);
    }

    @Test
    void test_castling_queenside_b() {
        Board board = new Board();

        board.clearAll();

        Move move = Move.from("Ke8c8");
        int apply = board.apply(move.raw());

        assertEquals(board()
                .with(Piece.King, false, position("c8"))
                .with(Piece.Rook, false, position("d8"))
                .build(), board);

        board.revertMove(move.raw(), apply);

        assertEquals(board()
                .with(Piece.King, false, position("e8"))
                .with(Piece.Rook, false, position("a8"))
                .build(), board);
    }

    @Test
    void test_castling_kingside_b() {
        Board board = new Board();

        board.clearAll();

        Move move = Move.from("Ke8g8");

        int apply = board.apply(move.raw());

        assertEquals(board()
                .with(Piece.King, false, position("g8"))
                .with(Piece.Rook, false, position("f8"))
                .build(), board);

        board.revertMove(move.raw(), apply);

        assertEquals(board()
                .with(Piece.King, false, position("e8"))
                .with(Piece.Rook, false, position("h8"))
                .build(), board);
    }

    @Test
    void test_promotion_white() {
        Board board = fromString("" +
                /*8*/"  ♝♞    " +
                /*7*/"  ♙♙    " +
                /*6*/"        " +
                /*5*/"        " +
                /*4*/"        " +
                /*3*/"        " +
                /*2*/"        " +
                /*1*/"        ");


        Move c7xd8q = Move.from("c7xd8q");
        int apply = board.apply(c7xd8q.raw());

        assertEquals(fromString("" +
                /*8*/"  ♝♕    " +
                /*7*/"   ♙    " +
                /*6*/"        " +
                /*5*/"        " +
                /*4*/"        " +
                /*3*/"        " +
                /*2*/"        " +
                /*1*/"        "), board);


        board.revertMove(c7xd8q.raw(), apply);

        assertEquals(fromString("" +
                /*8*/"  ♝♞    " +
                /*7*/"  ♙♙    " +
                /*6*/"        " +
                /*5*/"        " +
                /*4*/"        " +
                /*3*/"        " +
                /*2*/"        " +
                /*1*/"        "), board);
    }

    @Test
    void test_promotion_black() {
        Board board = fromString("" +
                /*8*/"        " +
                /*7*/"        " +
                /*6*/"        " +
                /*5*/"        " +
                /*4*/"        " +
                /*3*/"        " +
                /*2*/"  ♟♟    " +
                /*1*/"  ♗♘    ");


        Move c7xd8q = Move.from("c2xd1q");
        int apply = board.apply(c7xd8q.raw());

        assertEquals(fromString("" +
                /*8*/"        " +
                /*7*/"        " +
                /*6*/"        " +
                /*5*/"        " +
                /*4*/"        " +
                /*3*/"        " +
                /*2*/"   ♟    " +
                /*1*/"  ♗♛    "), board);


        board.revertMove(c7xd8q.raw(), apply);

        assertEquals(fromString("" +
                /*8*/"        " +
                /*7*/"        " +
                /*6*/"        " +
                /*5*/"        " +
                /*4*/"        " +
                /*3*/"        " +
                /*2*/"  ♟♟    " +
                /*1*/"  ♗♘    "), board);
    }

    @Test
    void test_white_move_revert() {
        Board startBoard = fromString("" +
                /*8*/"        " +
                /*7*/"        " +
                /*6*/"        " +
                /*5*/"        " +
                /*4*/"♖♖♖     " +
                /*3*/"      ♜ " +
                /*2*/" ♔  ♜   " +
                /*1*/"        ");

        Board boardSUT = new Board();
        boardSUT.set(startBoard);

        int rg3a3 = Move.from("Rc4c2").raw();
        int apply = boardSUT.apply(rg3a3);

        assertEquals(fromString("" +
                /*8*/"        " +
                /*7*/"        " +
                /*6*/"        " +
                /*5*/"        " +
                /*4*/"♖♖      " +
                /*3*/"      ♜ " +
                /*2*/" ♔♖ ♜   " +
                /*1*/"        "), boardSUT);

        boardSUT.revertMove(rg3a3, apply);

        assertEquals(startBoard, boardSUT);
    }

    @Test
    void test_black_move_revert() {
        Board startBoard = fromString("" +
                /*8*/"        " +
                /*7*/"        " +
                /*6*/"        " +
                /*5*/"        " +
                /*4*/"♖♖♖     " +
                /*3*/"      ♜ " +
                /*2*/" ♚  ♜   " +
                /*1*/"        ");

        Board boardSUT = new Board();
        boardSUT.set(startBoard);

        int rg3a3 = Move.from("Rg3a3").raw();
        int apply = boardSUT.apply(rg3a3);

        assertEquals(fromString("" +
                /*8*/"        " +
                /*7*/"        " +
                /*6*/"        " +
                /*5*/"        " +
                /*4*/"♖♖♖     " +
                /*3*/"♜       " +
                /*2*/" ♚  ♜   " +
                /*1*/"        "), boardSUT);

        boardSUT.revertMove(rg3a3, apply);

        assertEquals(startBoard, boardSUT);
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