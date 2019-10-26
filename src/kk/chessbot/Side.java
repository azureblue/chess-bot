package kk.chessbot;

public enum Side {
    White(true), Black(false);
    public final boolean isWhite;

    Side(boolean isWhite) {
        this.isWhite = isWhite;
    }
}
