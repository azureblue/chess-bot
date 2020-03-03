package kk.chessbot.player;

import kk.chessbot.BitBoard;
import kk.chessbot.Board;
import kk.chessbot.Piece;
import kk.chessbot.Side;
import kk.chessbot.moves.CastlingStatus;
import kk.chessbot.moves.MoveGenerator;
import kk.chessbot.moves.Moves;
import kk.chessbot.wrappers.Move;
import kk.chessbot.wrappers.Position;

import java.util.*;

public class AlphaBetaPlayer implements Player {

    static {
        String v = "1911300110";
        System.out.println("AlphaBetaPlayer v" + v);
    }

    private final Board board;
    private final boolean white;

    private final Random rand;
    private final int depth;

    private int[][] moves = new int[7][Moves.MAX_MOVES_IN_TURN];
    private int[][] partialPaths = new int[7][7];

    private MoveGenerator moveGenerator = new MoveGenerator();
    private boolean timeout = false;
    private CastlingStatus castlingStatus = new CastlingStatus();

    private BitBoard[] bitBoards = {new BitBoard(), new BitBoard()};
    private int maxLevel = 0;

    private ArrayList<MoveValuePair> moveList = new ArrayList<>(Moves.MAX_MOVES_IN_TURN);
    private HashMap<Integer, int[]> paths = new HashMap<>();
    private int[] currentPath = new int[8];
    private TrivialEvaluator evaluator = new TrivialEvaluator();

    private Move myLastMove = null;
    private long startTime;
    private int timeLimit;

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

//        if (board.piece(Position.position("e1").raw()) != Piece.King) {
//            castlingStatus.removeCastling(CastlingStatus.Side.K, true);
//            castlingStatus.removeCastling(CastlingStatus.Side.Q, true);
//        }
//
//        if (board.piece(Position.position("e8").raw()) != Piece.King) {
//            castlingStatus.removeCastling(CastlingStatus.Side.K, false);
//            castlingStatus.removeCastling(CastlingStatus.Side.Q, false);
//        }
//
//        if (board.piece(Position.position("a8").raw()) != Piece.Rook)
//            castlingStatus.removeCastling(CastlingStatus.Side.Q, false);
//
//        if (board.piece(Position.position("h8").raw()) != Piece.Rook)
//            castlingStatus.removeCastling(CastlingStatus.Side.K, false);
//
//        if (board.piece(Position.position("a1").raw()) != Piece.Rook)
//            castlingStatus.removeCastling(CastlingStatus.Side.Q, true);
//
//        if (board.piece(Position.position("h1").raw()) != Piece.Rook)
//            castlingStatus.removeCastling(CastlingStatus.Side.K, true);
//

    }

    public void applyMove(Move move) {
        if (board.isWhite(move.posFrom().raw()) == white)
            myLastMove = move;
        board.apply(move.raw());

//        if (move.getPiece() == Piece.King) {
//            castlingStatus.setCastling(CastlingStatus.Side.K, white, false);
//            castlingStatus.setCastling(CastlingStatus.Side.Q, white, false);
//        }
//
//        if (move.getPiece() == Piece.Rook) {
//            if (white && move.posFrom().equals(Position.position("a1")))
//                castlingStatus.setCastling(CastlingStatus.Side.Q, white, false);
//
//            if (white && move.posFrom().equals(Position.position("h1")))
//                castlingStatus.setCastling(CastlingStatus.Side.K, white, false);
//
//            if (!white && move.posFrom().equals(Position.position("a8")))
//                castlingStatus.setCastling(CastlingStatus.Side.Q, white, false);
//
//            if (!white && move.posFrom().equals(Position.position("h8")))
//                castlingStatus.setCastling(CastlingStatus.Side.K, white, false);
//        }
    }

    @Override
    public void getBoard(Board board) {
        board.set(this.board);
    }

    private void shuffleArrayALittle(int[] array, int n)
    {
        int index, temp;
        for (int i = 0; i < n; i+=4)
        {
            index = rand.nextInt(n - i) + i;
            temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
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
        shuffleArrayALittle(moves[level], moveCount);
        int myVal = currentColor ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        for (int i = 0; i < moveCount; i++) {
            int move = moves[level][i];
            int valueAfterMove;
            int ev = evaluator.evaluateMove(move, board);
            boolean kingCaptured = (ev & TrivialEvaluator.KING_CAPUTRE_FLAG) != 0;
            int localVal = currentVal + ev * multiplier / 10 * (10 - level);
            localVal += tuneMoveEv(level, move) * multiplier;
            if (kingCaptured)
                valueAfterMove = localVal;
            else {
                //currentPath[level] = move;

                if (level + 1 == maxLevel)
                    valueAfterMove = localVal;
                else if (level == 3 && (System.currentTimeMillis() - startTime > timeLimit)) {
                    valueAfterMove = localVal;
                } else {
                    int apply = board.apply(move);
                    updateBitBoard(level, move);
                    valueAfterMove = findMoves(level + 1, alpha, beta, !currentColor, localVal);
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
//
//    private void updateCastlings(int move, boolean isWhiteTurn) {
//        if (Move.piece(move) == Piece.King.bits) {
//            castlingStatus.removeCastling(CastlingStatus.Side.K, isWhiteTurn);
//            castlingStatus.removeCastling(CastlingStatus.Side.Q, isWhiteTurn);
//        }
//
//        if (Move.piece(move) == Piece.Rook.bits) {
//            if (Position.x(Move.posFrom(move)) == 0)
//                castlingStatus.removeCastling(CastlingStatus.Side.Q, isWhiteTurn);
//            else if (Position.x(Move.posFrom(move)) == 7)
//                castlingStatus.removeCastling(CastlingStatus.Side.K, isWhiteTurn);
//        }
//    }

    private int tuneMoveEv(int level, int move) {
        int tune = 0;
        tune += rand.nextInt(5);
        int from = Move.posFrom(move);
//        int to = Move.posTo(move);
        int piece = Move.piece(move);
        int sy = Position.y(from);
//        int dy = Position.y(to);
//        int dx = Position.x(to);
//        if (sy % 7 == 0) {
//            if (piece == Piece.Knight.bits || piece == Piece.Bishop.bits)
//                tune += 20;
//        }
//
//        if (dx % 7 == 0) {
//            if (piece == Piece.Knight.bits || piece == Piece.Bishop.bits)
//                tune -= 50;
//        }
//
//            if (Position.x(from) == 6 || Position.x(from) == 5)
//
//        }

        if (Move.isCastling(move))
            tune += 800;

        if (piece == Piece.King.bits) {
            tune -= 200;
        }

        if (piece == Piece.Pawn.bits) {
            if (sy == 1 || sy == 6)
                tune += 70;
            else
                tune += 40;
        }

        if (level == 0 && myLastMove != null && Move.piece(myLastMove.raw()) == piece && piece != Piece.Pawn.bits) {
            tune -= 10;
        } else if (level > 1 && Move.piece(currentPath[level - 2]) == piece && piece != Piece.Pawn.bits)
            tune -= 10;

        return tune;
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
        this.startTime = System.currentTimeMillis();
        moveList.clear();
        paths.clear();
        bitBoards[0].clear();
        bitBoards[1].clear();
        board.getPlayerMask(white, bitBoards[0]);
        board.getPlayerMask(!white, bitBoards[1]);

        this.timeLimit = Math.min(4000, time / 20);

        setDepth(depth);

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
