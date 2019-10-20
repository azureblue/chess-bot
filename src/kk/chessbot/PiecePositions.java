package kk.chessbot;

public class PiecePositions {

    private int[] rawPosition = new int[6];

    private PiecePositions() {
    }

    public int getKing(boolean white) {
        int kings = rawPosition[Piece.King.ordinal()];
        if (!white)
            kings >>= 8;
        if ((kings & 64) != 64)
            return -1;
        return kings & 63;
    }

    public void setKing(boolean white, int position) {
        int bits = (position | 64);
        if (!white)
            bits <<= 8;
        rawPosition[Piece.King.ordinal()] |= bits;
    }

    public static PiecePositions fromBoard(Board board) {
        PiecePositions positions = new PiecePositions();
        for (int pos = 0; pos < 64; pos++) {
            if (board.piece(pos) == Piece.King)
                positions.setKing(board.isWhite(pos), pos);
        }
        return positions;
    }
}
