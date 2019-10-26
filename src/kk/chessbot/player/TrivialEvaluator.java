package kk.chessbot.player;

import kk.chessbot.Board;
import kk.chessbot.Piece;
import kk.chessbot.wrappers.Move;

public class TrivialEvaluator {
    public static final int KING_VAL = 10000;
    private int[] valueByPieceBits = new int[8];
    {
        valueByPieceBits[0] = 0;
        valueByPieceBits[Piece.Pawn.bits] = 1;
        valueByPieceBits[Piece.Bishop.bits] = 3;
        valueByPieceBits[Piece.Knight.bits] = 3;
        valueByPieceBits[Piece.Rook.bits] = 4;
        valueByPieceBits[Piece.Queen.bits] = 7;
        valueByPieceBits[Piece.King.bits] = KING_VAL;
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

        int capturedValue = valueByPieceBits[board.pieceBits(posTo) & Piece.PIECE_BIT_MASK];

        if (promoted != 0) {
            capturedValue -= valueByPieceBits[Piece.Pawn.bits];
            capturedValue += valueByPieceBits[promoted];
        }

        if (board.isWhite(Move.posFrom(rawMove)))
            return capturedValue;
        else
            return -capturedValue;

    }
}
