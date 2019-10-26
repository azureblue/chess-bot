package kk.chessbot;

public class Fen {

    private final String data;
    private final int boardDescLen;

    private Fen(String data) {
        this.data = data.trim();
        boardDescLen = data.indexOf(' ');
        if (boardDescLen == -1)
            throw new IllegalArgumentException("invalid fen: " + data);
    }

    public static Fen fen(String fen) {
        return new Fen(fen);
    }

    public Side activeSide() {
        return data.charAt(boardDescLen + 1) == 'w' ? Side.White : Side.Black;
    }

    public Board createBoard() {
        Board board = new Board();
        int boardX = 0;
        int boardY = 7;
        for (int fenCur = 0; fenCur < boardDescLen; fenCur++) {
            char c = data.charAt(fenCur);
            if (c == '/') {
                boardY--;
                boardX = 0;
            } else if (Character.isDigit(c)){
                boardX += Character.getNumericValue(c);
            } else {
                board.set(Piece.pieceByChar(c), boardX++, boardY, Character.isUpperCase(c));
            }
        }
        return board;
    }
}
