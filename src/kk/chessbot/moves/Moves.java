package kk.chessbot.moves;

import kk.chessbot.Board;
import kk.chessbot.Piece;

import static kk.chessbot.wrappers.Move.*;

public class Moves {

    public static final int MAX_MOVES_IN_TURN = 218;

    private static void acceptMoveIfPossible(ArrayFiller out, Board board, boolean playerIsWhite, int partial, int x, int y) {
        if (board.canMoveOrTake(playerIsWhite, x, y))
            out.put(compose(partial, x, y, board.isEmpty(x, y) ? 0 : FLAG_CAPTURE));
    }

    private final void pawn(Board board, int x, int y, boolean white, ArrayFiller out) {
        int partial = partial(Piece.Pawn, x, y);

        int dir = white ? 1 : -1;

        int forwardY = y + dir;
        int leftX = x - dir;
        int rightX = x + dir;

        //promotion
        if ((white && y == 6) || (!white && y == 1)) {
            if (board.isEmpty(x, forwardY)) {
                out.put(compose(partial, Piece.Rook, x, forwardY));
                out.put(compose(partial, Piece.Queen, x, forwardY));
                out.put(compose(partial, Piece.Knight, x, forwardY));
                out.put(compose(partial, Piece.Bishop, x, forwardY));
            }

            if (board.canTake(white, leftX, forwardY)) {
                out.put(compose(partial, Piece.Rook, leftX, forwardY, FLAG_CAPTURE));
                out.put(compose(partial, Piece.Queen, leftX, forwardY, FLAG_CAPTURE));
                out.put(compose(partial, Piece.Knight, leftX, forwardY, FLAG_CAPTURE));
                out.put(compose(partial, Piece.Bishop, leftX, forwardY, FLAG_CAPTURE));
            }

            if (board.canTake(white, rightX, forwardY)) {
                out.put(compose(partial, Piece.Rook, rightX, forwardY, FLAG_CAPTURE));
                out.put(compose(partial, Piece.Queen, rightX, forwardY, FLAG_CAPTURE));
                out.put(compose(partial, Piece.Knight, rightX, forwardY, FLAG_CAPTURE));
                out.put(compose(partial, Piece.Bishop, rightX, forwardY, FLAG_CAPTURE));
            }
            return;
        }

        if (board.isEmpty(x, forwardY)) {
            out.put(compose(partial, x, forwardY));

            if ((y - dir) % 7 == 0 && board.isEmpty(x, y + dir * 2))
                out.put(compose(partial, x, y + dir * 2));
        }


        if (board.canTake(white, leftX, forwardY))
            out.put(compose(partial, leftX, forwardY, FLAG_CAPTURE));

        if (board.canTake(white, rightX, forwardY))
            out.put(compose(partial, rightX, forwardY, FLAG_CAPTURE));
    }

    public final void king(Board board, int x, int y, boolean white, ArrayFiller out) {
        int partial = partial(Piece.King, x, y);
        for (int i = 0; i < 9; i++)
            acceptMoveIfPossible(out, board, white, partial, x - 1 + i % 3, y - 1 + i / 3);
    }

    private void diagonals(Piece piece, Board board, int x, int y, boolean white, ArrayFiller out) {
        int partial = partial(piece, x, y);
        for (int xx = x + 1, yy = y + 1; xx < 8 && yy < 8; xx++, yy++) {
            if (board.isEmpty(xx, yy))
                out.put(compose(partial, xx, yy));
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
    }

    private void lines(Piece piece, Board board, int x, int y, boolean white, ArrayFiller out) {
        int partial = partial(piece, x, y);
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
    }

    ;

    public final void knight(Board board, int x, int y, boolean white, ArrayFiller out) {
        int partial = partial(Piece.Knight, x, y);
        acceptMoveIfPossible(out, board, white, partial, x + 2, y + 1);
        acceptMoveIfPossible(out, board, white, partial, x + 2, y - 1);
        acceptMoveIfPossible(out, board, white, partial, x - 2, y + 1);
        acceptMoveIfPossible(out, board, white, partial, x - 2, y - 1);

        acceptMoveIfPossible(out, board, white, partial, x + 1, y + 2);
        acceptMoveIfPossible(out, board, white, partial, x - 1, y + 2);
        acceptMoveIfPossible(out, board, white, partial, x + 1, y - 2);
        acceptMoveIfPossible(out, board, white, partial, x - 1, y - 2);
    }

    public final void genMovesFor(Piece piece, Board board, int x, int y, boolean white, ArrayFiller out) {
        switch (piece) {
            case Pawn:
                pawn(board, x, y, white, out);
                break;
            case Knight:
                knight(board, x, y, white, out);
                break;
            case Bishop:
                diagonals(piece, board, x, y, white, out);
                break;
            case Rook:
                lines(piece, board, x, y, white, out);
                break;
            case Queen:
                lines(piece, board, x, y, white, out);
                diagonals(piece, board, x, y, white, out);
                break;
            case King:
                king(board, x, y, white, out);
                break;
        }
    }

}

