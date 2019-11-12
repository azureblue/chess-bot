package kk.chessbot.player;

import kk.chessbot.BitBoard;
import kk.chessbot.Board;
import kk.chessbot.Piece;
import kk.chessbot.Side;
import kk.chessbot.moves.MoveGenerator;
import kk.chessbot.moves.Moves;
import kk.chessbot.wrappers.Move;

import java.util.*;
import java.util.stream.IntStream;

public class NotSoRandomPlayer implements Player {

    static {
        int v = 5;
        System.out.println("NotSoRandomPlayer V" + v);
    }

    private final Board board;
    private final boolean white;
    private final Random rand;

    private int[][] moves = new int[7][Moves.MAX_MOVES_IN_TURN];
    private int[][] partialPaths = new int[7][7];

    private MoveGenerator moveGenerator = new MoveGenerator();

    private BitBoard bitBoard = new BitBoard();
    private int maxLevel = 0;

    private ArrayList<MoveValuePair> moveList = new ArrayList<>(Moves.MAX_MOVES_IN_TURN);
    private HashMap<Integer, int[]> paths = new HashMap<>();
    private int[] currentPath = new int[8];
    private TrivialEvaluator evaluator = new TrivialEvaluator();

    private Move myLastMove = null;

    public NotSoRandomPlayer(Board board, Side side) {
        this(board, side, new Random());
    }

    public NotSoRandomPlayer(Board board, Side side, Random rand) {
        this.board = new Board(board);
        this.white = side.isWhite;
        this.rand = rand;
    }

    public void applyMove(Move move) {
        if (board.isWhite(move.posFrom()) == white)
            myLastMove = move;
        board.apply(move.raw());
    }

    @Override
    public void getBoard(Board board) {
        board.set(this.board);
    }

    private int findMoves(int level, boolean currentColor, int currentVal) {
        int multiplier = currentColor ? 1 : -1;
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
            moveEv = moveEv * (10 - level) / 10;
            int localVal = currentVal + moveEv;
            if (Math.abs(moveEv) > 1000000)
                valueAfterMove = localVal;
            else {
                int piece = Move.piece(move);
                if (piece == Piece.King.bits)
                    localVal -= 100 * multiplier;

                if (level == 0 && myLastMove != null && Move.piece(myLastMove.raw()) == piece) {
//                    System.out.println(Move.wrap(move));
//                    System.out.println("asd");
                    localVal -= 100 * multiplier;
                } else if (level > 1 && Move.piece(currentPath[level - 2]) == piece)
                    localVal -= 100 * multiplier;

                int apply = board.apply(move);
                currentPath[level] = move;
                valueAfterMove = findMoves(level + 1, !currentColor, localVal);
                board.revertMove(move, apply);
            }

            if (currentColor && myVal < valueAfterMove || !currentColor && myVal > valueAfterMove) {
                myVal = valueAfterMove;
//                copyArr(partialPaths[level + 1], partialPaths[level], level + 1, maxLevel);
//                partialPaths[level][level] = move;
            }

            if (level == 0) {
                moveList.add(new MoveValuePair(Move.wrap(move), valueAfterMove));
//                paths.put(move, partialPaths[0].clone());
            }
        }

        return myVal;
    }

    private void copyArr(int[] src, int[] dst, int from, int to) {
        while (from < to) {
            dst[from] = src[from];
            from++;
        }
    }

    private void setDepth(int level) {
        maxLevel = level;
    }

    public Move makeMove(int time) {
        moveList.clear();
        paths.clear();

        setDepth(4);
        int myVal = findMoves(0, white, evaluator.evaluate(board));
//        if ((white && myVal < -2000) || (!white && myVal > 2000)) {
//            moveList.clear();
//
//            setDepth(2);
//            findMoves(0, white, evaluator.evaluate(board));
//        }

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

//        for (MoveValuePair moveValuePair : moveList) {
//            System.out.print(moveValuePair.getMove() + ": " + moveValuePair.getValue() + " ");
//            IntStream.of(paths.getOrDefault(moveValuePair.getMove().raw(), new int[0])).forEachOrdered(i -> {
//                if (i == 0)
//                    return;
//                System.out.print(Move.wrap(i).toLongNotation() + "->");
//            });
//            System.out.println();
//        }

        return moveList.get(rand.nextInt(nextBestMoveIdx)).getMove();
    }

}
