package kk.chessbot;

import kk.chessbot.wrappers.Move;
import kk.chessbot.wrappers.Position;

import java.util.Arrays;

public class Board {

    public static final byte COLOR_MASK = 1 << 6;

    private final byte[] board = new byte[8 * 8];

    public static boolean inside(int x, int y) {
        return (x & 7) == x && (y & 7) == y;
    }

    private static byte pieceToRaw(Piece piece, boolean white) {
        return (byte) (piece.bits | (white ? 0 : COLOR_MASK));
    }

    private static Piece getPiece(int raw) {
        raw &= ~COLOR_MASK;
        if (raw == 0)
            return null;
        return Piece.byBits(raw);
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
        board[y * 8 + x] = pieceToRaw(piece, white);
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

    public String toString() {
        return toUnicodeMultiline();
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

    public int apply(Move move) {
        int rawPosFrom = move.posFrom();
        int rawPosTo = move.posTo();
        if (move.getPiece() == Piece.King) {
            int dx = Position.dx(rawPosFrom, rawPosTo);
            if (Math.abs(dx) > 1) {
                int row = Position.y(rawPosFrom);
                boolean queenSide = dx == -2;
                if (queenSide) {
                    clear(4, row);
                    clear(0, row);
                    set(Piece.King, 2, row, row == 0);
                    set(Piece.Rook, 3, row, row == 0);
                } else {
                    clear(4, row);
                    clear(7, row);
                    set(Piece.King, 6, row, row == 0);
                    set(Piece.Rook, 5, row, row == 0);
                }
                return 0;
            }
        }
        byte temp = board[rawPosTo];
        set(rawPosTo, get(rawPosFrom));
        if (move.isPromotion())
            set(rawPosTo, pieceToRaw(move.getPiecePromoted(), isWhite(rawPosFrom)));
        clear(rawPosFrom);
        return temp;
    }

    public void revertMove(Move move, int stateData) {
        int rawPosFrom = move.posFrom();
        int rawPosTo = move.posTo();
        if (move.getPiece() == Piece.King) {
            int dx = Position.dx(rawPosFrom, rawPosTo);
            if (Math.abs(dx) > 1) {
                int row = Position.y(rawPosFrom);
                boolean queenSide = dx == -2;
                if (queenSide) {
                    clear(2, row);
                    clear(3, row);
                    set(Piece.King, 4, row, row == 0);
                    set(Piece.Rook, 0, row, row == 0);
                } else {
                    clear(5, row);
                    clear(6, row);
                    set(Piece.King, 4, row, row == 0);
                    set(Piece.Rook, 7, row, row == 0);
                }
                return;
            }
        }
        set(move.getPiece(), move.sx(), move.sy(), isWhite(move.posTo()));
        set(move.posTo(), (byte) (stateData & 0xFF));
    }

    private void clear(int pos) {
        board[pos] = 0;
    }

    public void clearAll() {
        Arrays.fill(board, (byte) 0);
    }

    @Override
    public boolean equals(Object o) {
        Board board1 = (Board) o;
        return Arrays.equals(board, board1.board);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(board);
    }

    public void set(Board board) {
        System.arraycopy(board.board, 0, this.board, 0, 64);
    }
}
