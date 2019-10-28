package kk.chessbot;

import kk.chessbot.moves.BoardUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FenTest {

    @Test
    void parse_fen_board_position() {
        Fen fen = Fen.fen("r1bqkbnr/n2p3p/1p4p1/p1pPB3/2B5/2P3N1/PP4PP/RN1Q1RK1 b kq - 0 13");
        Board board = fen.createBoard();

        assertEquals(BoardUtils.fromString("" +
                "♜ ♝♛♚♝♞♜" +
                "♞  ♟   ♟" +
                " ♟    ♟ " +
                "♟ ♟♙♗   " +
                "  ♗     " +
                "  ♙   ♘ " +
                "♙♙    ♙♙" +
                "♖♘ ♕ ♖♔"), board);

        assertEquals(Side.Black, fen.activeSide());
    }

}