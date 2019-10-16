package kk.chessbot.moves;

@FunctionalInterface
public interface PositionConsumer {

    void accept(int x, int y);
}
