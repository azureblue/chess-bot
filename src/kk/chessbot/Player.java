package kk.chessbot;

public enum Player {
    While(true), Black(false);
    public final boolean isWhite;

    Player(boolean isWhite) {
        this.isWhite = isWhite;
    }
}
