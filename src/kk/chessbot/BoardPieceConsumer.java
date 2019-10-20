package kk.chessbot;

@FunctionalInterface
public interface BoardPieceConsumer {
    void accept(Board board, Piece piece, boolean white, int x, int y);

}
