package kk.chessbot.player;

import kk.chessbot.wrappers.Move;

public class MoveValuePair {
    private final Move move;
    private final int value;

    MoveValuePair(Move move, int value) {
        this.move = move;
        this.value = value;
    }

    public Move getMove() {
        return move;
    }

    public int getValue() {
        return value;
    }
}
