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
        Moves.getGenerator(piece).generateMoves(board, x, y, white, moves::add);
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


    public void generateMovesAt(Board board, int pos, MoveConsumer moveConsumer) {
        Piece piece = board.piece(pos);
        if (piece == null)
            return;
        int y = Position.y(pos);
        int x = Position.x(pos);
        Moves.getGenerator(piece).generateMoves(board, x, y, board.isWhite(pos), moveConsumer);

    }



    public IntHashSet get() {
        return moves;
    }
}
