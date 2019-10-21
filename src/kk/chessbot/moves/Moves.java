package kk.chessbot.moves;

import kk.chessbot.Board;
import kk.chessbot.Piece;

import static kk.chessbot.wrappers.Move.*;

public class Moves {

    private static final MoveGenerator[] moveGenerators = new MoveGenerator[Piece.values().length];

    private static final MoveGenerator pawn = (board, x, y, white, out) -> {
        int partial = partial(Piece.Pawn, x, y);

        int dir = white ? 1 : -1;

        int forwardY = y + dir;
        int leftX = x - dir;
        int rightX = x + dir;

        //promotion
        if ((white && y == 6) || (!white && y == 1)) {
            if (board.isEmpty(x, forwardY)) {
                out.accept(compose(partial, Piece.Rook, x, forwardY));
                out.accept(compose(partial, Piece.Queen, x, forwardY));
                out.accept(compose(partial, Piece.Knight, x, forwardY));
                out.accept(compose(partial, Piece.Bishop, x, forwardY));
            }

            if (board.canTake(white, leftX, forwardY)) {
                out.accept(compose(partial, Piece.Rook, leftX, forwardY, FLAG_CAPTURE));
                out.accept(compose(partial, Piece.Queen, leftX, forwardY, FLAG_CAPTURE));
                out.accept(compose(partial, Piece.Knight, leftX, forwardY, FLAG_CAPTURE));
                out.accept(compose(partial, Piece.Bishop, leftX, forwardY, FLAG_CAPTURE));
            }

            if (board.canTake(white, rightX, forwardY)) {
                out.accept(compose(partial, Piece.Rook, rightX, forwardY, FLAG_CAPTURE));
                out.accept(compose(partial, Piece.Queen, rightX, forwardY, FLAG_CAPTURE));
                out.accept(compose(partial, Piece.Knight, rightX, forwardY, FLAG_CAPTURE));
                out.accept(compose(partial, Piece.Bishop, rightX, forwardY, FLAG_CAPTURE));
            }
            return;
        }

        if (board.isEmpty(x, forwardY)) {
            out.accept(compose(partial, x, forwardY));

            if ((y - dir) % 7 == 0 && board.isEmpty(x, y + dir * 2))
                out.accept(compose(partial, x, y + dir * 2));
        }


        if (board.canTake(white, leftX, forwardY))
            out.accept(compose(partial, leftX, forwardY, FLAG_CAPTURE));

        if (board.canTake(white, rightX, forwardY))
            out.accept(compose(partial, rightX, forwardY, FLAG_CAPTURE));
    };

    public static final MoveGenerator king = (board, x, y, white, out) -> {
        int partial = partial(Piece.King, x, y);
        for (int i = 0; i < 9; i++)
            acceptMoveIfPossible(out, board, white, partial,x - 1 + i % 3, y - 1 + i / 3);
    };

    public static final MoveGenerator bishop = (board, x, y, white, out) -> {
        int partial = partial(Piece.Bishop, x, y);
        for (int xx = x + 1, yy = y + 1; xx < 8 && yy < 8; xx++, yy++) {
            if (board.isEmpty(xx, yy))
                out.accept(compose(partial, xx, yy));
            else {
                if (board.isPieceInColor(xx, yy, !white))
                    out.accept(compose(partial, xx, yy, FLAG_CAPTURE));
                break;
            }
        }

        for (int xx = x - 1, yy = y + 1; xx >= 0 && yy < 8; xx--, yy++) {
            if (board.isEmpty(xx, yy))
                out.accept(compose(partial, xx, yy));
            else {
                if (board.isPieceInColor(xx, yy, !white))
                    out.accept(compose(partial, xx, yy, FLAG_CAPTURE));
                break;
            }
        }

        for (int xx = x - 1, yy = y - 1; xx >= 0 && yy >= 0; xx--, yy--) {
            if (board.isEmpty(xx, yy))
                out.accept(compose(partial, xx, yy));
            else {
                if (board.isPieceInColor(xx, yy, !white))
                    out.accept(compose(partial, xx, yy, FLAG_CAPTURE));
                break;
            }
        }

        for (int xx = x + 1, yy = y - 1; xx < 8 && yy >= 0; xx++, yy--) {
            if (board.isEmpty(xx, yy))
                out.accept(compose(partial, xx, yy));
            else {
                if (board.isPieceInColor(xx, yy, !white))
                    out.accept(compose(partial, xx, yy, FLAG_CAPTURE));
                break;
            }
        }
    };

    public static final MoveGenerator rook = (board, x, y, white, out) -> {
        int partial = partial(Piece.Rook, x, y);
        for (int xx = x + 1; xx < 8; xx++) {
            if (board.isEmpty(xx, y))
                out.accept(compose(partial, xx, y));
            else {
                if (board.isPieceInColor(xx, y, !white))
                    out.accept(compose(partial, xx, y, FLAG_CAPTURE));
                break;
            }
        }

        for (int xx = x - 1; xx >= 0; xx--) {
            if (board.isEmpty(xx, y))
                out.accept(compose(partial, xx, y));
            else {
                if (board.isPieceInColor(xx, y, !white))
                    out.accept(compose(partial, xx, y, FLAG_CAPTURE));
                break;
            }
        }

        for (int yy = y + 1; yy < 8; yy++) {
            if (board.isEmpty(x, yy))
                out.accept(compose(partial, x, yy));
            else {
                if (board.isPieceInColor(x, yy, !white))
                    out.accept(compose(partial, x, yy, FLAG_CAPTURE));
                break;
            }
        }

        for (int yy = y - 1; yy >= 0; yy--) {
            if (board.isEmpty(x, yy))
                out.accept(compose(partial, x, yy));
            else {
                if (board.isPieceInColor(x, yy, !white))
                    out.accept(compose(partial, x, yy, FLAG_CAPTURE));
                break;
            }
        }
    };


    public static final MoveGenerator queen = (board, x, y, white, out) -> {
        rook.generateMoves(board, x, y, white, out);
        bishop.generateMoves(board, x, y, white, out);
    };

    public static final MoveGenerator knight = (board, x, y, white, out) -> {
        int partial = partial(Piece.Knight, x, y);
        acceptMoveIfPossible(out, board, white, partial, x + 2, y + 1);
        acceptMoveIfPossible(out, board, white, partial, x + 2, y - 1);
        acceptMoveIfPossible(out, board, white, partial, x - 2, y + 1);
        acceptMoveIfPossible(out, board, white, partial, x - 2, y - 1);

        acceptMoveIfPossible(out, board, white, partial, x + 1, y + 2);
        acceptMoveIfPossible(out, board, white, partial, x - 1, y + 2);
        acceptMoveIfPossible(out, board, white, partial, x + 1, y - 2);
        acceptMoveIfPossible(out, board, white, partial, x - 1, y - 2);
    };

    private static void acceptMoveIfPossible(MoveConsumer out, Board board, boolean playerIsWhite, int partial, int x, int y) {
        if (board.canMoveOrTake(playerIsWhite, x, y))
            out.accept(compose(partial, x, y, board.isEmpty(x, y) ? 0 : FLAG_CAPTURE));
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
