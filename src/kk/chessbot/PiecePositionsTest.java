package kk.chessbot;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static kk.chessbot.moves.BoardUtils.board;
import static kk.chessbot.wrappers.Position.position;

class PiecePositionsTest {

    @Test
    void kingPositions() {

        Board board = board()
                .with(Piece.King, true, position("a1"))
                .with(Piece.King, false, position("b4")).build();

        System.out.println(board.toUnicodeMultiline());

        //when
        PiecePositions positions = PiecePositions.fromBoard(board);

        //then
        Assertions.assertEquals(position("a1"), position(positions.getKing(true)));
        Assertions.assertEquals(position("b4"), position(positions.getKing(false)));

    }

    @Test
    void singleKingPosition() {

        PiecePositions blackKing = PiecePositions.fromBoard(board()
                .with(Piece.King, false, position("b4")).build());

        Assertions.assertEquals(-1, blackKing.getKing(true));
        Assertions.assertEquals(position("b4"), position(blackKing.getKing(false)));

        PiecePositions whiteKing = PiecePositions.fromBoard(board()
                .with(Piece.King, true, position("h7")).build());

        Assertions.assertEquals(-1, whiteKing.getKing(false));
        Assertions.assertEquals(position("h7"), position(whiteKing.getKing(true)));

    }

}