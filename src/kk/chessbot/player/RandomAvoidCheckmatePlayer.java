package kk.chessbot.player;

import kk.chessbot.BitBoard;
import kk.chessbot.Board;
import kk.chessbot.Piece;
import kk.chessbot.moves.ArrayFiller;
import kk.chessbot.moves.MoveGenerator;
import kk.chessbot.wrappers.Move;

import java.util.ArrayList;
import java.util.Random;

public class RandomAvoidCheckmatePlayer implements Player{
    private final  Board board;
    private final boolean white;
    private final Random random;

    public RandomAvoidCheckmatePlayer(Board board, boolean white) {
        this(board, white, new Random());
    }

    public RandomAvoidCheckmatePlayer(Board board, boolean white, Random random) {
        this.board = new Board(board);
        this.white = white;
        this.random = random;
    }

    public void applyMove(Move move) {
        board.apply(move.raw());
    }

    @Override
    public void getBoard(Board board) {
        board.set(this.board);
    }

    private int[][][] moves = new int[2][2][500];

    private MoveGenerator moveGenerator = new MoveGenerator();
    private BitBoard bitBoard = new BitBoard();

    private ArrayList<Move> moveOut = new ArrayList<>(500);

    private boolean findMove(int level, boolean white, int maxLevel) {
//        System.out.println(board);
        if (level == maxLevel)
            return false;
        int turnLevel = level / 2;
        int player = level % 2;
        boolean opponent = (player == 1);
        bitBoard.clear();
        board.getPlayerMask(white, bitBoard);
        int moveCount = moveGenerator.generateMoves(board, bitBoard, moves[turnLevel][player]);
        if (opponent) {
            for (int i = 0; i < moveCount; i++) {
                int move = moves[turnLevel][player][i];
                if (board.piece(Move.posTo(move)) == Piece.King)
                    return true;
            }
            boolean kingDead = false;
            for (int i = 0; i < moveCount; i++) {
                int move = moves[turnLevel][player][i];
                int apply = board.apply(move);
                kingDead = findMove(level + 1, !white, maxLevel);
                board.revertMove(move, apply);
                if (kingDead)
                    break;
            }
            return kingDead;

        } else {
            boolean kingDead = true;
            for (int i = 0; i < moveCount; i++) {
                int move = moves[turnLevel][player][i];
                int apply = board.apply(move);
                boolean kingDeadCurrent = findMove(level + 1, !white, maxLevel);
                kingDead &= kingDeadCurrent;
                board.revertMove(move, apply);
                if (turnLevel == 0) {
                    if (!kingDeadCurrent)
                        moveOut.add(Move.wrap(move));
                } else if (!kingDead)
                    break;
            }
            return kingDead;
        }
    }

    public Move makeMove(int time) {
        moveOut.clear();
        boolean dead = findMove(0, white, 4);
        if (!dead) {
            return moveOut.get(random.nextInt(moveOut.size()));
        } else {
            findMove(0, white, 2);
            return moveOut.get(random.nextInt(moveOut.size()));
        }
    }

}
