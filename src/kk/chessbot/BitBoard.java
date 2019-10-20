package kk.chessbot;

public class BitBoard {
    private long boardBits = 0;

    public void clear() {
        boardBits = 0;
    }

    public boolean get(int x, int y) {
        return (boardBits >> (y << 3 | x) & 1) == 1;
    }

    public void set(int x, int y) {
        boardBits |= 1 << (y << 3 | x);
    }

    public void set(int pos) {
        boardBits |= 1L << (long) pos;
    }

    public long getBoardBits() {
        return boardBits;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(64 + 8);
        for (int y = 7; y >= 0; y--) {
            for (int x = 0; x < 8; x++) {
                boolean b = get(x, y);
                if (b)
                    sb.append("#");
                else
                    sb.append(".");
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}
