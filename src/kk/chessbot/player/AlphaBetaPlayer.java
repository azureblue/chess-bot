package kk.chessbot.player;

import kk.chessbot.BitBoard;
import kk.chessbot.Board;
import kk.chessbot.Piece;
import kk.chessbot.Side;
import kk.chessbot.moves.MoveGenerator;
import kk.chessbot.moves.Moves;
import kk.chessbot.wrappers.Move;
import kk.chessbot.wrappers.Position;

import java.util.*;

public class AlphaBetaPlayer implements Player {

    static {
        int v = 5;
        System.out.println("AlphaBetaPlayer V" + v);
    }

    private final Board board;
    private final boolean white;
    private final Random rand;
    private final int depth;

    private int[][] moves = new int[7][Moves.MAX_MOVES_IN_TURN];
    private int[][] partialPaths = new int[7][7];

    private MoveGenerator moveGenerator = new MoveGenerator();

    private BitBoard[] bitBoards = {new BitBoard(), new BitBoard()};
    private int maxLevel = 0;

    private ArrayList<MoveValuePair> moveList = new ArrayList<>(Moves.MAX_MOVES_IN_TURN);
    private HashMap<Integer, int[]> paths = new HashMap<>();
    private int[] currentPath = new int[8];
    private TrivialEvaluator evaluator = new TrivialEvaluator();

    private Move myLastMove = null;

    public AlphaBetaPlayer(Board board, Side side) {
        this(board, side, new Random());
    }

    public AlphaBetaPlayer(Board board, Side side, Random rand) {
        this(board, side, rand, 6);
    }

    public AlphaBetaPlayer(Board startingBoard, Side side, Random random, int depth) {
        this.board = new Board(startingBoard);
        this.white = side.isWhite;
        this.rand = random;
        this.depth = depth;
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

    private int findMoves(int level, int alpha, int beta, boolean currentColor, int currentVal) {
        int multiplier = currentColor ? 1 : -1;
        if (level == maxLevel) {
            return currentVal;
        }
        BitBoard bitBoardMy = bitBoards[level % 2];
        BitBoard bitBoardOp = bitBoards[(level + 1) % 2];

//        bitBoard.clear();
//        board.getPlayerMask(currentColor, bitBoard);
        long boardBitsMy = bitBoardMy.getBoardBits();
        long boardBitsOp = bitBoardOp.getBoardBits();
        int moveCount = moveGenerator.generateMoves(board, bitBoardMy, moves[level]);
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
                    localVal -= 200 * multiplier;

                if (level == 0 && myLastMove != null && Move.piece(myLastMove.raw()) == piece) {
                    localVal -= 100 * multiplier;
                } else if (level > 1 && Move.piece(currentPath[level - 2]) == piece)
                    localVal -= 100 * multiplier;

                currentPath[level] = move;

                int nextLevel = level + 1;
                if (nextLevel == maxLevel)
                    valueAfterMove = localVal;
                else {
                    int apply = board.apply(move);
                    updateBitBoard(level, move);
                    valueAfterMove = findMoves(nextLevel, alpha, beta, !currentColor, localVal);
                    bitBoardMy.setBits(boardBitsMy);
                    bitBoardOp.setBits(boardBitsOp);
                    board.revertMove(move, apply);
                }
            }

            if (currentColor && myVal < valueAfterMove || !currentColor && myVal > valueAfterMove) {
                myVal = valueAfterMove;
//                copyArr(partialPaths[level + 1], partialPaths[level], level + 1, maxLevel);
//                partialPaths[level][level] = move;
            }

            if (currentColor && alpha < myVal)
                alpha = myVal;
            else if (!currentColor && beta > myVal) {
                beta = myVal;
            }

            if (alpha > beta) {
                //System.out.println("alpha beta cutoff");
                break;
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

    private void updateBitBoard(int level, int rawMove) {
        int posFrom = Move.posFrom(rawMove);
        bitBoards[level % 2].clear(posFrom);
        int posTo = Move.posTo(rawMove);
        bitBoards[level % 2].set(posTo);
        if (Move.isEnPassant(rawMove)) {
            bitBoards[(level + 1) % 2].clear(Position.toRaw(Position.x(posTo), Position.y(posFrom)));
        } else if (Move.isCapture(rawMove))
            bitBoards[(level + 1) % 2].clear(posTo);
    }

    private void setDepth(int level) {
        maxLevel = level;
    }

    public Move makeMove(int time) {
        moveList.clear();
        paths.clear();
        bitBoards[0].clear();
        bitBoards[1].clear();
        board.getPlayerMask(white, bitBoards[0]);
        board.getPlayerMask(!white, bitBoards[1]);

        setDepth(depth);
        if (time < 20000)
            setDepth(4);
        int myVal = findMoves(0, Integer.MIN_VALUE, Integer.MAX_VALUE, white, evaluator.evaluate(board));
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
