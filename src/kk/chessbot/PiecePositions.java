package kk.chessbot;

public class PiecePositions {

    public static int countPieces(Board board, Side side) {
        int count = 0;
        for (int pos = 0; pos < 64; pos++)
            if (board.isPieceInColor(pos, side.isWhite))
                count++;
        return count;
    }

    private int[] rawPosition = new int[8];

    public PiecePositions() {
    }

    public void clear() {
//        rawPosition[0] = 0;
        rawPosition[1] = 0;
        rawPosition[2] = 0;
        rawPosition[3] = 0;
        rawPosition[4] = 0;
        rawPosition[5] = 0;
        rawPosition[6] = 0;
        rawPosition[7] = 0;

    }

    public int piecePos(Piece piece, int number, Side side) {
        int positions = rawPosition[piece.bits];
        if (!side.isWhite)
            positions >>= 12;
        positions >>= (number * 6);
        return positions & 63;
    }

    private void setPiece(Piece piece, Side side, int pos) {
        int positions = rawPosition[piece.bits];
        if (!side.isWhite)
            positions >>= 12;

        //if ()
    }


    public void clearAndUpdate(Board board) {
        //TODO
        clear();
        PiecePositions positions = new PiecePositions();
        for (int pos = 0; pos < 64; pos++) {
            Piece piece = board.piece(pos);
            if (piece == Piece.Pawn)
                continue;
//
//            if (piece == Piece.King)
//                positions.setKing(board.isWhite(pos), pos);
        }
    }

    public static PiecePositions fromBoard(Board board) {
        PiecePositions positions = new PiecePositions();
        positions.clearAndUpdate(board);
        return positions;
    }
}
