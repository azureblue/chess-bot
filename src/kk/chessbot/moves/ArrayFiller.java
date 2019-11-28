package kk.chessbot.moves;

import java.util.function.IntConsumer;

public final class ArrayFiller implements IntConsumer {
    private int[] ar;
    private int idx;

    public ArrayFiller(int[] ar) {
        this.ar = ar;
    }

    public final void put(int val) {
        ar[idx++] = val;
    }

    public int getIdx() {
        return idx;
    }

    @Override
    public final void accept(int val) {
        put(val);
    }

    public final void clear() {
        idx = 0;
    }

    public final void wrap(int[] ar) {
        this.ar = ar;
        idx = 0;
    }
}
