package kk.chessbot.player;

import kk.chessbot.Board;
import kk.chessbot.Piece;
import kk.chessbot.wrappers.Move;

public class TrivialEvaluator {
    public static final int PIECE_MULTIPLIER = 1000;
    public static final int KING_CAPUTRE_FLAG = 1 << 28;
    private int[] valueByPieceBits = new int[8];

    {
        valueByPieceBits[0] = 0;
        valueByPieceBits[Piece.Pawn.bits] = PIECE_MULTIPLIER;
        valueByPieceBits[Piece.Bishop.bits] = 3 * PIECE_MULTIPLIER;
        valueByPieceBits[Piece.Knight.bits] = 3 * PIECE_MULTIPLIER;
        valueByPieceBits[Piece.Rook.bits] = 5 * PIECE_MULTIPLIER;
        valueByPieceBits[Piece.Queen.bits] = 9 * PIECE_MULTIPLIER;
        valueByPieceBits[Piece.King.bits] = 10000 * PIECE_MULTIPLIER;
    }

    public int evaluate(Board board) {
        int current = 0;
        for (int pos = 0; pos < 64; pos++) {
            int fieldBits = board.raw(pos);
            current += valueByPieceBits[fieldBits & Piece.PIECE_BIT_MASK] * ((fieldBits & Board.COLOR_MASK) == 0 ? 1 : -1);
        }
        return current;
    }

    public int evaluateMove(int rawMove, Board board) {
        int posTo = Move.posTo(rawMove);
        int promoted = Move.piecePromoted(rawMove);

        if ((rawMove & Move.FLAG_CAPTURE) == 0 && promoted == 0)
            return 0;

        int captured = board.pieceBits(posTo) & Piece.PIECE_BIT_MASK;

        int moveEv = valueByPieceBits[captured];

        if (promoted != 0) {
            moveEv -= valueByPieceBits[Piece.Pawn.bits];
            moveEv += valueByPieceBits[promoted];
        }

        if (captured == Piece.King.bits)
            moveEv |= KING_CAPUTRE_FLAG;

        return moveEv;
    }
}
