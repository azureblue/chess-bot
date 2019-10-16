package kk.chessbot.moves;

import kk.chessbot.Board;

public interface MoveGenerator {
    void generateMoves(Board board, int x, int y, boolean white, PositionConsumer moveConsumer);
}
