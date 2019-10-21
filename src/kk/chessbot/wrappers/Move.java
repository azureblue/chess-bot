package kk.chessbot.wrappers;

import kk.chessbot.Piece;

import static kk.chessbot.wrappers.Position.position;

public class Move {
    public static final int FLAG_CAPTURE = 1 << 18;
    private final int moveData;

    public Move(int rawMoveData) {
        moveData = rawMoveData;
    }

    public Move(Piece piece, Piece promoted, int sx, int sy, int dx, int dy, int flags) {
        this(raw(piece, promoted, sx, sy, dx, dy, flags));
    }

    public static int raw(Piece piece, int posFrom, int posTo, int flags) {
        return piece.bits
                | posFrom << 6
                | posTo << 12
                | flags;
    }


    public static int raw(Piece piece, Piece promoted, int posFrom, int posTo, int flags) {
        return piece.bits
                | (promoted != null ? (promoted.bits << 3) : 0)
                | posFrom << 6
                | posTo << 12
                | flags;
    }

    public static int raw(Piece piece, int sx, int sy, int dx, int dy, int flags) {
        return piece.bits
                | sx << 6
                | sy << 9
                | dx << 12
                | dy << 15
                | flags;
    }

    public static int raw(Piece piece, Piece promoted, int sx, int sy, int dx, int dy, int flags) {
        return piece.bits
                | (promoted != null ? (promoted.bits << 3) : 0)
                | sx << 6
                | sy << 9
                | dx << 12
                | dy << 15
                | flags;
    }
    public static int partial(Piece piece, int sx, int sy) {
        return piece.bits
                | sx << 6
                | sy << 9;
    }


    public static int compose(int partial, int dx, int dy, int flags) {
        return partial
                | dx << 12
                | dy << 15
                | flags;
    }

    public static int compose(int partial, Piece promoted, int dx, int dy, int flags) {
        return partial
                | promoted.bits << 3
                | dx << 12
                | dy << 15
                | flags;
    }

    public static int compose(int partial, Piece promoted, int dx, int dy) {
        return partial
                | promoted.bits << 3
                | dx << 12
                | dy << 15;
    }

    public static int compose(int partial, int dx, int dy) {
        return partial
                | dx << 12
                | dy << 15;
    }


    public Move(Piece piece, Piece promoted, int rawSrcPos, int rawDstPos, int flags) {
        moveData = raw(piece, promoted, rawSrcPos, rawDstPos, flags);
    }

    public static Move move(String move) {
        boolean isPawn = move.charAt(1) <= '9';
        Piece piece = isPawn ? Piece.Pawn : Piece.pieceByChar(move.charAt(0));
        int current = isPawn ? 0 : 1;
        int rawFrom = position(move, current).raw();
        current += 2;
        int flags = 0;
        if (move.charAt(current) == 'x') {
            flags |= FLAG_CAPTURE;
            current++;
        }
        int rawTo = position(move, current).raw();
        current+=2;
        Piece promoted = null;
        if (current < move.length())
            promoted = Piece.pieceByChar(move.charAt(current));

        return new Move(piece, promoted, rawFrom, rawTo, flags);
    }

    public String toLongNotation() {
        StringBuilder sb = new StringBuilder(8);
        Piece piece = getPiece();
        if (piece != Piece.Pawn)
            sb.append(piece.symbol);
        sb.append(Position.toNotation(sx(), sy()));
        if (flag(FLAG_CAPTURE))
            sb.append('x');
        sb.append(Position.toNotation(dx(), dy()));
        Piece piecePromoted = getPiecePromoted();
        if (piecePromoted != null)
            sb.append(piecePromoted.symbol);
        return sb.toString();
    }

    public final Piece getPiece() {
        return Piece.byBits(moveData & 0x7);
    }

    public final Piece getPiecePromoted() {
        return Piece.byBits(moveData >> 3 & 0x7);
    }

    public final int sx() {
        return (moveData >> 6 & 0x7);
    }

    public final int sy() {
        return (moveData >> 9 & 0x7);
    }

    public final int dx() {
        return (moveData >> 12 & 0x7);
    }

    public final int dy() {
        return (moveData >> 15 & 0x7);
    }

    public final boolean flag(int flag) {
        return (moveData & flag) != 0;
    }

    @Override
    public boolean equals(Object o) {
        return moveData == ((Move) o).moveData;
    }

    @Override
    public int hashCode() {
        return moveData;
    }

    @Override
    public String toString() {
        return toLongNotation();
    }

    public int posFrom() {
        return moveData >> 6 & 63;
    }

    public int posTo() {
        return moveData >> 12 & 63;
    }

    public boolean isPromotion() {
        return (moveData >> 3 & 7) != 0;
    }
}
