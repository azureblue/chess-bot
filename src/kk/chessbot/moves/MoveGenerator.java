package kk.chessbot.moves;

import kk.chessbot.BitBoard;
import kk.chessbot.Board;
import kk.chessbot.Piece;
import kk.chessbot.wrappers.Position;

import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

public class MoveGenerator {

    public MoveGenerator() {
    }

    static class ArrayFiller {
        private final int[] ar;
        private int idx;

        ArrayFiller(int[] ar) {
            this.ar = ar;
        }

        public final void put(int val) {
            ar[idx++] = val;
        }

        public int getIdx() {
            return idx;
        }
    }

    public int countMoves(Board board, BitBoard mask) {
        int[] count = new int[1];
        long boardBits = mask.getBoardBits();
        for (int pos = 0; pos < 64; pos++) {
            if ((boardBits & 1) != 0)
                generateMovesAt(board, pos, rawMove -> count[0]++);
            boardBits >>= 1;
        }
        return count[0];
    }

    public int generateMoves(Board board, BitBoard mask, int[] out) {
        long boardBits = mask.getBoardBits();
        ArrayFiller filler = new ArrayFiller(out);
        for (int pos = 0; pos < 64; pos++) {
            if ((boardBits & 1) != 0)
                generateMovesAt(board, pos, filler::put);
            boardBits >>= 1;
        }
        return filler.getIdx();
    }

    public IntStream moveStream(Board board, BitBoard mask) {

        Spliterator.OfInt moveSpliterator = Spliterators.spliteratorUnknownSize(new PrimitiveIterator.OfInt() {
            long boardBits = mask.getBoardBits();
            int[] localMoveArray = new int[500];
            int moveCount = 0;
            int pos = 0;

            @Override
            public boolean hasNext() {
                if (moveCount > 0)
                    return true;

                for (; pos < 64 && moveCount == 0; pos++) {
                    if ((boardBits & 1) != 0)
                        generateMovesAt(board, pos, rawMove -> localMoveArray[moveCount++] = rawMove);
                    boardBits >>= 1;
                }

                return moveCount > 0;
            }

            @Override
            public int nextInt() {
                return localMoveArray[--moveCount];
            }
        }, Spliterator.IMMUTABLE);

        return StreamSupport.intStream(moveSpliterator, false);

    }

    public void generateMovesAt(Board board, int pos, MoveConsumer moveConsumer) {
        Piece piece = board.piece(pos);
        if (piece == null)
            return;
        int y = Position.y(pos);
        int x = Position.x(pos);
        Moves.getGenerator(piece).generateMoves(board, x, y, board.isWhite(pos), moveConsumer);

    }
}
