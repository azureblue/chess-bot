package kk.chessbot.moves;

import kk.chessbot.Board;

public interface PieceMoveGenerator {
    void generateMoves(Board board, int x, int y, boolean white, MoveConsumer moveConsumer);
}
