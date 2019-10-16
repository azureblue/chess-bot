package kk.chessbot.moves;

import kk.chessbot.Board;
import kk.chessbot.Piece;
import kk.chessbot.wrappers.Position;

public class BoardUtils {

    public static class BoardBuilder {

        private BoardBuilder() {
        }

        private final Board board = new Board();
        public BoardBuilder with(Piece piece, boolean white, Position position) {
            board.set(piece, position.x(), position.y(), white);
            return this;
        }
        public Board build() {
            return board;
        }

    }

    public static BoardBuilder board() {
        return new BoardBuilder();
    }

    public static Board boardFromString(String str) {
        Board board = new Board();
        int pos = 0;
        for (char ch : str.toCharArray()) {
            if (Character.isWhitespace(ch)) {
                if (ch == ' ')
                    pos++;
                continue;
            }
            Piece piece;
            boolean white = true;
            if (ch >= '\u2654' && ch <= '\u265F') {
                piece = Piece.pieceByUnicodeSymbol(ch);
                if (ch > '\u2659')
                    white = false;
            }
            else {
                piece = Piece.pieceByChar(Character.toUpperCase(ch));
                if (Character.isLowerCase(ch))
                    white = false;
            }

            board.set(piece, pos % 8, 7 - pos / 8, white);
            pos++;
        }
        return board;
    }
}
