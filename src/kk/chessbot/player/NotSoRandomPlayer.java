package kk.chessbot.player;

import kk.chessbot.BitBoard;
import kk.chessbot.Board;
import kk.chessbot.Piece;
import kk.chessbot.Side;
import kk.chessbot.moves.MoveGenerator;
import kk.chessbot.moves.Moves;
import kk.chessbot.wrappers.Move;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class NotSoRandomPlayer implements Player {
    private final Board board;
    private final boolean white;
    private final Random rand;

    private int[][] moves = new int[7][Moves.MAX_MOVES_IN_TURN];
    private MoveGenerator moveGenerator = new MoveGenerator();

    private BitBoard bitBoard = new BitBoard();
    private int maxLevel = 0;

    private ArrayList<MoveValuePair> moveList = new ArrayList<>(Moves.MAX_MOVES_IN_TURN);
    private TrivialEvaluator evaluator = new TrivialEvaluator();

    public NotSoRandomPlayer(Board board, Side side) {
        this(board, side, new Random());
    }

    public NotSoRandomPlayer(Board board, Side side, Random rand) {
        this.board = new Board(board);
        this.white = side.isWhite;
        this.rand = rand;
    }

    public void applyMove(Move move) {
        board.apply(move.raw());
    }

    @Override
    public void getBoard(Board board) {
        board.set(this.board);
    }

    private int findMoves(int level, boolean currentColor, int currentVal) {
        if (level == maxLevel) {
            return currentVal;
        }

        bitBoard.clear();
        board.getPlayerMask(currentColor, bitBoard);
        int moveCount = moveGenerator.generateMoves(board, bitBoard, moves[level]);
        int myVal = currentColor ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        for (int i = 0; i < moveCount; i++) {
            int move = moves[level][i];
            int valueAfterMove;
            int moveEv = evaluator.evaluateMove(move, board);
            int localVal = currentVal + moveEv;
            if (Math.abs(moveEv) > 1000)
                valueAfterMove = localVal;
            else {
                int apply = board.apply(move);
                valueAfterMove = findMoves(level + 1, !currentColor, localVal);
                board.revertMove(move, apply);
            }

            if (currentColor && myVal < valueAfterMove)
                myVal = valueAfterMove;
            else if (!currentColor && myVal > valueAfterMove)
                myVal = valueAfterMove;

            if (level == 0) {
                moveList.add(new MoveValuePair(Move.wrap(move), valueAfterMove));
            }
        }
        return myVal;

    }

    private void setDepth(int level) {
        maxLevel = level;
    }

    public Move makeMove(int time) {
        moveList.clear();

        setDepth(4);
        int myVal = findMoves(0, white, evaluator.evaluate(board));
        if ((white && myVal < -2000) || (!white && myVal > 2000)) {
            moveList.clear();

            setDepth(2);
            findMoves(0, white, evaluator.evaluate(board));
        }

        if (moveList.isEmpty())
            return null;

        moveList.sort(Comparator.comparingInt(MoveValuePair::getValue));

        if (white)
            Collections.reverse(moveList);

        int bestValue = moveList.get(0).getValue();
        int nextBestMoveIdx = 1;
        for (; nextBestMoveIdx < moveList.size(); nextBestMoveIdx++) {
            if (moveList.get(nextBestMoveIdx).getValue() != bestValue)
                break;
        }

        return moveList.get(rand.nextInt(nextBestMoveIdx)).getMove();
    }

}
