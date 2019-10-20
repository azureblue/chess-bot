package kk.chessbot.moves;

import com.carrotsearch.hppc.IntHashSet;
import com.carrotsearch.hppc.procedures.IntProcedure;
import kk.chessbot.BitBoard;
import kk.chessbot.Board;
import kk.chessbot.Piece;
import kk.chessbot.wrappers.Move;
import kk.chessbot.wrappers.Position;

import java.util.HashSet;
import java.util.function.IntConsumer;

public class MoveCollector {
    private IntHashSet moves = new IntHashSet(50);

    public MoveCollector() {
    }

    public void generateMoves(Board board, Piece piece, boolean white, int x, int y) {
        Moves.getGenerator(piece).generateMoves(board, x, y, white, (dx, dy)
                -> moves.add(Move.raw(piece, null, x, y, dx, dy, board.isEmpty(dx, dy) ? 0 : Move.FLAG_CAPTURE)));
    }

    public void generateMoves(Board board, BitBoard mask) {
        moves.clear();
        long boardBits = mask.getBoardBits();
        for (int pos = 0; pos < 64; pos++) {
            if ((boardBits & 1) != 0)
                generateMovesAt(board, pos, moves::add);
            boardBits >>= 1;
        }
    }


    public void generateMovesAt(Board board, int pos, IntConsumer moveConsumer) {
        Piece piece = board.piece(pos);
        if (piece == null)
            return;
        int y = Position.y(pos);
        int x = Position.x(pos);
        Moves.getGenerator(piece).generateMoves(board, x, y, board.isWhite(pos),
                (xx, yy) -> moveConsumer.accept(Move.raw(piece, null, pos, xx | yy << 3,
                        board.isEmpty(xx, yy) ? 0 : Move.FLAG_CAPTURE)));

    }



    public IntHashSet get() {
        return moves;
    }
}
