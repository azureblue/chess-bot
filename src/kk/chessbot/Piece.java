package kk.chessbot;

public enum Piece {
    Pawn('P', 0b001),
    Knight('N', 0b010),
    Bishop('B', 0b011),
    Rook('R', 0b100),
    Queen('Q', 0b101),
    King('K', 0b110);

    public final char symbol;
    public final int bits;

    private static final Piece[] lookup = new Piece[127];
    private static final Piece[] valuesByButs = new Piece[8];

    static {
        for (Piece piece : values()) {
            lookup[Character.toUpperCase(piece.symbol)] = piece;
            lookup[Character.toLowerCase(piece.symbol)] = piece;
            valuesByButs[piece.bits] = piece;
        }
    }

    public static Piece byBits(int bits) {
        return valuesByButs[bits];
    }

    public static Piece pieceByChar(char ch) {
        return lookup[ch];
    }

    public static Piece pieceByUnicodeSymbol(char unicodeSymbol) {
        if (unicodeSymbol < '\u2654' || unicodeSymbol > '\u265F')
            throw new IllegalStateException("no matching piece for character: " + unicodeSymbol);

        if (unicodeSymbol > '\u2659')
            return valuesByButs['\u265F' - unicodeSymbol + 1];
        return valuesByButs['\u2659' - unicodeSymbol + 1];
    }

    public char getUnicodeSymbol(boolean white) {
        char code = '\u2659';
        code -= ordinal();
        if (!white)
            code += 6;
        return code;
    }

    Piece(char symbol, int bits) {
        this.symbol = symbol;
        this.bits = bits;
    }

}
