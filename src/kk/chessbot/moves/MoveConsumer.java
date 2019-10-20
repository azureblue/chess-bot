package kk.chessbot.moves;

@FunctionalInterface
public interface MoveConsumer {

    void accept(int x, int y);
}
