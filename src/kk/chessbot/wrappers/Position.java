package kk.chessbot.wrappers;

public class Position {

    private final short position;

    private Position(short position) {
        this.position = position;
    }

    public static Position position(int pos) {
        return new Position((short) pos);
    }

    public static Position position(int x, int y) {
        return new Position(toRaw(x, y));
    }

    public static Position position(String position) {
        return position(position, 0);
    }

    public static Position position(String position, int offset) {
        char col = position.charAt(offset);
        if (col < 'a')
            col += 32;

        return position(col - 'a', position.charAt(offset + 1) - 49);
    }

    public static short toRaw(int x, int y) {
        return (short) (x | y << 3);
    }

    public static int dx(int rawPosFrom, int rawPosTo) {
        return (rawPosTo & 7) - (rawPosFrom & 7);
    }

    public String toString() {
        return toNotation(x(), y());
    }

    public final int x() {
        return position & 7;
    }

    public final int y() {
        return position >> 3 & 7;
    }

    public final int raw() {
        return position;
    }

    public static String toNotation(int x, int y) {
        return "" + (char) ('a' + x) + (y + 1);
    }

    public static int x(int position) {
        return position & 7;
    }

    public static int y(int position) {
        return position >> 3 & 7;
    }

    @Override
    public boolean equals(Object o) {
        return position == ((Position) o).position;
    }

    @Override
    public int hashCode() {
        return position;
    }
}
