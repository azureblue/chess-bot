package kk.chessbot.moves;

import kk.chessbot.wrappers.Position;
import org.junit.jupiter.api.Test;

import static kk.chessbot.wrappers.Position.position;
import static org.junit.jupiter.api.Assertions.*;

class PositionTest {

    @Test
    void test_position_parsing_from_long_notation() {
        assertPositionXY(0, 7, position("a8"));
        assertPositionXY(7, 7, position("h8"));
        assertPositionXY(7, 0, position("h1"));
        assertPositionXY(0, 0, position("a1"));
    }


    private void assertPositionXY(int expectedX, int expectedY, Position pos) {
        assertEquals(expectedX, pos.x());
        assertEquals(expectedY, pos.y());
    }
}