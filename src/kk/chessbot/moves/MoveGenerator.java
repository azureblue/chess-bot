package kk.chessbot.moves;

import kk.chessbot.BitBoard;
import kk.chessbot.Board;
import kk.chessbot.Piece;
import kk.chessbot.wrappers.Position;

import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

public class MoveGenerator {

    private final Moves moves = new Moves();
    private final ArrayFiller filler = new ArrayFiller(null);

    public int generateMoves(Board board, BitBoard mask, int[] out) {
        filler.wrap(out);
        long boardBits = mask.getBoardBits();
        for (int pos = 0; pos < 64; pos++) {
            if ((boardBits & 1) != 0)
                generateMovesAt(board,  pos, filler);
            boardBits >>= 1;
        }
        return filler.getIdx();
    }

    public IntStream moveStream(Board board, BitBoard mask) {
        Spliterator.OfInt moveSpliterator = Spliterators.spliteratorUnknownSize(new PrimitiveIterator.OfInt() {
            long boardBits = mask.getBoardBits();
            int[] localMoveArray = new int[500];
            ArrayFiller filler = new ArrayFiller(localMoveArray);
            int moveCount = 0;
            int pos = 0;

            @Override
            public boolean hasNext() {
                if (moveCount > 0)
                    return true;
                filler.clear();

                for (; pos < 64 && moveCount == 0; pos++) {
                    if ((boardBits & 1) != 0) {
                        generateMovesAt(board, pos, filler);
                        moveCount = filler.getIdx();
                    }
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

    private void generateMovesAt(Board board, int pos, ArrayFiller out) {
        Piece piece = board.piece(pos);
        if (piece == null)
            return;
        int y = Position.y(pos);
        int x = Position.x(pos);
        moves.genMovesFor(piece, board, x, y, board.isWhite(pos), out);
    }
}
