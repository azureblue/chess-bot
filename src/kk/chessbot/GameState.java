package kk.chessbot;

public class GameState {
    private final Board board;
    private final Side side;

    public GameState(Board board, Side side) {
        this.board = board;
        this.side = side;
    }

    public Board getBoard() {
        return board;
    }

    public Side getSide() {
        return side;
    }
}