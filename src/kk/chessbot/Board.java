package kk.chessbot;

import kk.chessbot.wrappers.Move;

public class Board {

    public static final byte COLOR_MASK = 1 << 6;

    private static Piece[] pieces = Piece.values();

    private final byte[] board = new byte[8 * 8];

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
        return rawIsWhite(raw) == white;
    }

    public void clear(int x, int y) {
        board[y * 8 + x] = 0;
    }
    public byte set(Piece piece, int x, int y, boolean white) {
        byte raw = board[y * 8 + x];
        board[y * 8 + x] = pieceToInt(piece, white);
        return raw;
    }

    public final byte raw(int x, int y) {
        return board[y * 8 + x];
    }

    public Piece piece(int pos) {
        return getPiece(board[pos]);
    }

    public Piece piece(int x, int y) {
        return Board.getPiece(raw(x, y));
    }

    public boolean isEmpty(int x, int y) {
        return isEmptyRaw(raw(x, y));
    }

    public boolean isEmpty(int pos) {
        return board[pos] == 0;
    }
    public boolean isEmptyRaw(int raw) {
        return raw == 0;
    }

    public final boolean isWhite(int x, int y) {
        return rawIsWhite(raw(x, y));
    }

    public final boolean isWhite(int pos) {
        return rawIsWhite(board[pos]);
    }

    public final boolean rawIsWhite(int raw) {
        return (raw & COLOR_MASK) == 0;
    }

    public String toUnicodeMultiline() {
        StringBuilder sb = new StringBuilder(64 + 8);
        for (int y = 7; y >= 0; y--) {
            for (int x = 0; x < 8; x++) {
                Piece piece = piece(x, y);
                if (piece == null)
                    sb.append("\u00B7");
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
            if (isEmptyRaw(raw))
                continue;
            consumer.accept(this, getPiece(raw), rawIsWhite(raw), x, y);
        }
    }

    public void getPlayerMask(boolean white, BitBoard bitBoard) {
        for (int pos = 0; pos < 64; pos++) {
            if (isEmpty(pos))
                continue;
            if (isWhite(pos) == white)
                bitBoard.set(pos);
        }
    }

    public byte set(int pos, byte raw) {
        byte temp = board[pos];
        board[pos] = raw;
        return temp;
    }

    public byte get(int pos) {
        return board[pos];
    }

    public byte apply(Move move) {
        byte temp = board[move.posDst()];
        set(move.posDst(), get(move.posSrc()));
        clear(move.posSrc());
        return temp;
    }

    public void revertMove(Move move, byte previous) {
        set(move.getPiece(), move.sx(), move.sy(), isWhite(move.posDst()));
        set(move.posDst(), previous);
    }

    private void clear(int pos) {
        board[pos] = 0;
    }
}
