package kk.chessbot.moves;

import kk.chessbot.Board;
import kk.chessbot.Piece;

public class Moves {

    private static final MoveGenerator moveGenerators[] = new MoveGenerator[Piece.values().length];

    public static final MoveGenerator pawn = (board, x, y, white, out) -> {
        int dir = white ? 1 : -1;

        int forwardY = y + dir;
        int leftX = x - dir;
        int rightX = x + dir;

        if (board.isEmpty(x, forwardY)) {
            out.accept(x, forwardY);

            if ((y - dir) % 7 == 0)
                acceptMoveIfEmpty(out, board, x, y + dir * 2);
        }

        if (board.canTake(white, leftX, forwardY))
            out.accept(leftX, forwardY);

        if (board.canTake(white, rightX, forwardY))
            out.accept(rightX, forwardY);
    };

    public static final MoveGenerator king = (board, x, y, white, out) -> {
        for (int i = 0; i < 9; i++)
            acceptMoveIfPossible(out, board, white, x - 1 + i % 3, y - 1 + i / 3);
    };

    public static final MoveGenerator bishop = (board, x, y, white, out) -> {
        for (int xx = x + 1, yy = y + 1; xx < 8 && yy < 8; xx++, yy++) {
            if (board.isEmpty(xx, yy))
                out.accept(xx, yy);
            else {
                if (board.isPieceInColor(xx, yy, !white))
                    out.accept(xx, yy);
                break;
            }
        }

        for (int xx = x - 1, yy = y + 1; xx >= 0 && yy < 8; xx--, yy++) {
            if (board.isEmpty(xx, yy))
                out.accept(xx, yy);
            else {
                if (board.isPieceInColor(xx, yy, !white))
                    out.accept(xx, yy);
                break;
            }
        }

        for (int xx = x - 1, yy = y - 1; xx >= 0 && yy >= 0; xx--, yy--) {
            if (board.isEmpty(xx, yy))
                out.accept(xx, yy);
            else {
                if (board.isPieceInColor(xx, yy, !white))
                    out.accept(xx, yy);
                break;
            }
        }

        for (int xx = x + 1, yy = y - 1; xx < 8 && yy >= 0; xx++, yy--) {
            if (board.isEmpty(xx, yy))
                out.accept(xx, yy);
            else {
                if (board.isPieceInColor(xx, yy, !white))
                    out.accept(xx, yy);
                break;
            }
        }
    };

    public static final MoveGenerator rook = (board, x, y, white, out) -> {
        for (int xx = x + 1; xx < 8; xx++) {
            if (board.isEmpty(xx, y))
                out.accept(xx, y);
            else {
                if (board.isPieceInColor(xx, y, !white))
                    out.accept(xx, y);
                break;
            }
        }

        for (int xx = x - 1; xx >= 0; xx--) {
            if (board.isEmpty(xx, y))
                out.accept(xx, y);
            else {
                if (board.isPieceInColor(xx, y, !white))
                    out.accept(xx, y);
                break;
            }
        }

        for (int yy = y + 1; yy < 8; yy++) {
            if (board.isEmpty(x, yy))
                out.accept(x, yy);
            else {
                if (board.isPieceInColor(x, yy, !white))
                    out.accept(x, yy);
                break;
            }
        }

        for (int yy = y - 1; yy >= 0; yy--) {
            if (board.isEmpty(x, yy))
                out.accept(x, yy);
            else {
                if (board.isPieceInColor(x, yy, !white))
                    out.accept(x, yy);
                break;
            }
        }
    };


    public static final MoveGenerator queen = (board, x, y, white, out) -> {
        rook.generateMoves(board, x, y, white, out);
        bishop.generateMoves(board, x, y, white, out);
    };

    public static final MoveGenerator knight = (board, x, y, white, out) -> {
        acceptMoveIfPossible(out, board, white,x + 2, y + 1);
        acceptMoveIfPossible(out, board, white,x + 2, y - 1);
        acceptMoveIfPossible(out, board, white,x - 2, y + 1);
        acceptMoveIfPossible(out, board, white,x - 2, y - 1);

        acceptMoveIfPossible(out, board, white,x + 1, y + 2);
        acceptMoveIfPossible(out, board, white,x - 1, y + 2);
        acceptMoveIfPossible(out, board, white,x + 1, y - 2);
        acceptMoveIfPossible(out, board, white,x - 1, y - 2);
    };


    public static void main(String[] args) {
        final byte COLOR_MASK = (byte) 0b11000000;
        System.out.println(COLOR_MASK);
    }

    private static void acceptMoveIfPossible(MoveConsumer out, Board board, boolean playerIsWhite, int x, int y) {
        if (board.canMoveOrTake(playerIsWhite, x, y))
            out.accept(x, y);
    }

    private static void acceptMoveIfEmpty(MoveConsumer out, Board board, int x, int y) {
        if (board.canMove(x, y))
            out.accept(x, y);
    }

    static {
        moveGenerators[Piece.Pawn.ordinal()] = pawn;
        moveGenerators[Piece.Knight.ordinal()] = knight;
        moveGenerators[Piece.Bishop.ordinal()] = bishop;
        moveGenerators[Piece.Rook.ordinal()] = rook;
        moveGenerators[Piece.Queen.ordinal()] = queen;
        moveGenerators[Piece.King.ordinal()] = king;
    }

    public static MoveGenerator getGenerator(Piece piece) {
        return moveGenerators[piece.ordinal()];
    }



}
