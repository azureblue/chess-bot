package kk.chessbot;

import com.carrotsearch.hppc.IntHashSet;
import com.carrotsearch.hppc.procedures.IntProcedure;
import kk.chessbot.moves.MoveCollector;
import kk.chessbot.wrappers.Move;

import java.util.Random;

public class RandomPlayer {
    private Board board;
    private boolean white;
    Random random;

    public RandomPlayer(Board board, boolean white) {
        this(board, white, new Random());
    }

    public RandomPlayer(Board board, boolean white, Random random) {
        this.board = board;
        this.white = white;
        this.random = random;
    }

    public void applyMove(Move move) {
        board.apply(move);
    }

    public Move move() {
        BitBoard me = new BitBoard();
        BitBoard op = new BitBoard();

        board.getPlayerMask(white, me);
        board.getPlayerMask(!white, op);


        MoveCollector myMoves = new MoveCollector();
        MoveCollector opMoves = new MoveCollector();
        myMoves.generateMoves(board, me);
        opMoves.generateMoves(board, op);

        int[] myMovesAr = myMoves.get().toArray();

        boolean kingChecked = isKingChecked(opMoves, PiecePositions.fromBoard(board).getKing(white));

        IntHashSet possibleMoves = new IntHashSet();

        for (int m : myMovesAr) {
            Move move = new Move(m);
            int prev = board.apply(move);
//            System.out.println(board.toUnicodeMultiline());
            opMoves.generateMoves(board, op);
            if (!isKingChecked(opMoves, PiecePositions.fromBoard(board).getKing(white)))
                possibleMoves.add(m);
            board.revertMove(move, prev);
//            System.out.println(board.toUnicodeMultiline());
        }

        if (possibleMoves.size() == 0)
            return null;

        int[] possibleMoveAr = possibleMoves.toArray();

        return new Move(possibleMoveAr[random.nextInt(possibleMoves.size())]);
    }

    public boolean isKingChecked(MoveCollector opMoves, int kingPos) {


        if (kingPos == -1)
            return false;
        return opMoves.get().forEach(new IntProcedure() {
            boolean kingChecked = false;

            @Override
            public void apply(int move) {
                if (new Move(move).posTo() == kingPos)
                    kingChecked = true;
            }
        }).kingChecked;
    }

}
