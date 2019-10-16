package kk.chessbot;

public enum Piece {
    Pawn('P'), Knight('N'), Bishop('B'), Rook('R'), Queen('Q'), King('K');

    public final char symbol;

    private static final Piece[] lookup = new Piece[127];
    private static final Piece[] values = Piece.values();

    static {
        for (Piece piece : values()) {
            lookup[Character.toUpperCase(piece.symbol)] = piece;
            lookup[Character.toLowerCase(piece.symbol)] = piece;
        }
    }

    public static Piece byOrdinal(int ord) {
        return values[ord];
    }

    public static Piece pieceByChar(char ch) {
        return lookup[ch];
    }

    public static Piece pieceByUnicodeSymbol(char unicodeSymbol) {
        if (unicodeSymbol < '\u2654' || unicodeSymbol > '\u265F')
            throw new IllegalStateException("no matching piece for character: " + unicodeSymbol);

        if (unicodeSymbol > '\u2659')
            return values['\u265F' - unicodeSymbol];
        return values['\u2659' - unicodeSymbol];
    }

    public char getUnicodeSymbol(boolean white) {
        char code =  '\u2659';
        code -= ordinal();
        if (!white)
            code += 6;
        return code;
    }

    Piece(char symbol) {
        this.symbol = symbol;
    }

}
