package kk.chessbot.player;

import kk.chessbot.Board;
import kk.chessbot.wrappers.Move;

public interface Player {
    Move makeMove(int msTimeLimit);
    void applyMove(Move move);
    void getBoard(Board board);

    default Move makeMove() {
        return makeMove(5000);
    }
}
