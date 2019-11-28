package kk.chessbot;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BitBoardTest {

    @Test
    void properRawPos() {
        BitBoard bitBoard = new BitBoard();
        bitBoard.set(2 << 3 | 7);

        assertTrue(bitBoard.get(7, 2));
        assertFalse(bitBoard.get(2, 7));

        bitBoard.clear(2 << 3 | 7);

        assertFalse(bitBoard.get(7, 2));

//        System.out.println(bitBoard);
    }

}