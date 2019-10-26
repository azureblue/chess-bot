package kk.chessbot.moves;

import kk.chessbot.BitBoard;
import kk.chessbot.Piece;
import kk.chessbot.wrappers.Move;
import kk.chessbot.wrappers.Position;
import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static kk.chessbot.moves.BoardUtils.board;
import static kk.chessbot.moves.TestUtils.set;
import static kk.chessbot.moves.TestUtils.testMoves;

class MoveGeneratorTest {

    @Test
    void testMoveStream() {
        MoveGenerator moveGenerator = new MoveGenerator();
        IntStream moveStream = moveGenerator.moveStream(board()
                .with(Piece.Pawn, true, Position.position("a2"))
                .with(Piece.Pawn, true, Position.position("h2"))
                .with(Piece.Pawn, false, Position.position("a7"))
                .with(Piece.Pawn, false, Position.position("h7"))
                .build(), BitBoard.MASK_ALL);


        testMoves(set(Move.from("a2a3"),
                Move.from("a2a4"),
                Move.from("h2h3"),
                Move.from("h2h4"),
                Move.from("a7a6"),
                Move.from("a7a5"),
                Move.from("h7h6"),
                Move.from("h7h5")), moveStream.mapToObj(Move::wrap).collect(Collectors.toSet()));


    }

}