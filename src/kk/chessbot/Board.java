package kk.chessbot;

public class Board {

    public static final byte COLOR_MASK = 1 << 6;

    private static Piece[] pieces = Piece.values();

    public boolean canMove(int x, int y) {
        return inside(x, y) && isEmpty(x, y);
    }

    public boolean canTake(boolean playerColor, int x, int y) {
        return inside(x, y) && isPieceInColor(x, y, !playerColor);
    }

    public boolean canMoveOrTake(boolean white, int x, int y) {
        if (!inside(x, y))
            return false;

        byte raw = raw(x, y);

        return raw == 0 || (raw & Board.COLOR_MASK) == 0 ^ white;
    }

    public boolean isPieceInColor(int x, int y, boolean white) {
        byte raw = raw(x, y);
        if (raw == 0)
            return false;
        return isWhite(raw) == white;
    }

    private final byte[] board = new byte[8 * 8];

    public void set(Piece piece, int x, int y, boolean white) {
        board[y * 8 + x] = pieceToInt(piece, white);
    }

    public final byte raw(int x, int y) {
        return board[y * 8 + x];
    }

    public Piece getPiece(int x, int y) {
        return getPiece(raw(x, y));
    }

    public boolean isEmpty(int x, int y) {
        return isEmpty(raw(x, y));
    }

    public boolean isEmpty(int raw) {
        return raw == 0;
    }

    public final boolean isWhite(int x, int y) {
        return isWhite(raw(x, y));
    }

    public final boolean isWhite(int raw) {
        return (raw & COLOR_MASK) == 0;
    }

    public static boolean inside(int x, int y) {
        return (x & 7) == x && (y & 7) == y;
    }

    private static byte pieceToInt(Piece piece, boolean white) {
        return (byte) (piece.ordinal() + 1 | (white ? 0 : COLOR_MASK));
    }

    private static Piece getPiece(int raw) {
        raw &= ~COLOR_MASK;
        if (raw == 0)
            return null;
        return pieces[raw - 1];
    }

    private static Player player(int i) {
        return (i & 1) == 0 ? Player.While : Player.Black;
    }

    public String toUnicodeMultiline() {
        StringBuilder sb = new StringBuilder(64 + 8);
        for (int y = 7; y >= 0; y--) {
            for (int x = 0; x < 8; x++) {
                Piece piece = getPiece(x, y);
                if (piece == null)
                    sb.append(" ");
                else
                    sb.append(piece.getUnicodeSymbol(isWhite(x, y)));
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    public void forEachPiece(BoardPieceConsumer consumer) {
        for (int i = 0; i < 64; i++) {
            int x = i % 8;
            int y = i / 8;
            int raw = raw(x, y);
            if (isEmpty(raw))
                continue;
            consumer.accept(this, getPiece(raw), isWhite(raw), x, y);
        }
    }

}
