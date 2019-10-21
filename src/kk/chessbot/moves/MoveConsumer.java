package kk.chessbot.moves;

@FunctionalInterface
public interface MoveConsumer {

    void accept(int rawMove);

}
