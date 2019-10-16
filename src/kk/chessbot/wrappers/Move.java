package kk.chessbot.wrappers;

import kk.chessbot.Piece;

import static kk.chessbot.wrappers.Position.position;

public class Move {
    public static final int FLAG_CAPTURE = 1 << 18;
    private final int moveData;

    public Move(Piece piece, Piece promoted, int sx, int sy, int dx, int dy, int flags) {
        moveData = piece.ordinal()
                | (promoted != null ? (promoted.ordinal() << 3) : 0)
                | sx << 6
                | sy << 9
                | dx << 12
                | dy << 15
                | flags;
    }

    public Move(Piece piece, Piece promoted, int rawSrcPos, int rawDstPos, int flags) {
        moveData = piece.ordinal()
                | (promoted != null ? (promoted.ordinal() << 3) : 0)
                | rawSrcPos << 6
                | rawDstPos << 12
                | flags;
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
        return sb.toString();
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
        return new Move(piece, null, rawFrom, rawTo, flags);

    }

    public final Piece getPiece() {
        return Piece.byOrdinal(moveData & 0x7);
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
}
