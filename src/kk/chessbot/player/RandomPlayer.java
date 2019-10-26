package kk.chessbot.player;

import kk.chessbot.BitBoard;
import kk.chessbot.Board;
import kk.chessbot.Piece;
import kk.chessbot.Side;
import kk.chessbot.moves.MoveGenerator;
import kk.chessbot.moves.Moves;
import kk.chessbot.wrappers.Move;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class RandomPlayer implements Player {
    private final Board board;
    private final boolean white;
    private final Random random;

    public static class MoveArray {
        final int[] ar = new int[Moves.MAX_MOVES_IN_TURN];
        int size = 0;
    }

    public RandomPlayer(Board board, Side side) {
        this(board, side, new Random());
    }

    public RandomPlayer(Board board, Side side, Random random) {
        this.board = new Board(board);
        this.white = side.isWhite;
        this.random = random;
    }

    @Override
    public void applyMove(Move move) {
        board.apply(move.raw());
    }

    @Override
    public void getBoard(Board board) {
        board.set(this.board);
    }

    private final MoveArray myMoves = new MoveArray();
    private final MoveArray opMoves = new MoveArray();

    @Override
    public Move makeMove(int msTimeLimit) {
        BitBoard me = new BitBoard();
        BitBoard op = new BitBoard();

        board.getPlayerMask(white, me);
        board.getPlayerMask(!white, op);

        MoveGenerator moveGenerator = new MoveGenerator();
        myMoves.size = moveGenerator.generateMoves(board, me, myMoves.ar);

        Set<Integer> possibleMoves = new HashSet<>();

        for (int i = 0; i < myMoves.size; i++) {
            int move = myMoves.ar[i];

            int prev = board.apply(move);
//            System.out.println(board.toUnicodeMultiline());
            opMoves.size = moveGenerator.generateMoves(board, op, opMoves.ar);
            if (!isKingChecked(opMoves.ar, opMoves.size))
                possibleMoves.add(move);
            board.revertMove(move, prev);
//            System.out.println(board.toUnicodeMultiline());
        }

        if (possibleMoves.size() == 0)
            return null;

        Integer[] possibleMoveAr = new ArrayList<>(possibleMoves).toArray(new Integer[0]);

        Integer rawMoveData = possibleMoveAr[random.nextInt(possibleMoves.size())];

        return Move.wrap(rawMoveData);
    }

    private boolean isKingChecked(int[] moves, int size) {

        for (int i = 0; i < size; i++) {
            if (board.piece(Move.posTo(moves[i])) == Piece.King)
                return true;
        }
        return false;

    }
}
