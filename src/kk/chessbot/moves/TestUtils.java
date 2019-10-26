package kk.chessbot.moves;

import kk.chessbot.BitBoard;
import kk.chessbot.Board;
import kk.chessbot.wrappers.Move;
import kk.chessbot.wrappers.Position;
import org.junit.jupiter.api.Assertions;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import static java.util.stream.StreamSupport.stream;

public class TestUtils {

    public static Set<Move> set(Move... expectedMoves) {
        return new HashSet<>(Arrays.asList(expectedMoves));
    }

    public static Set<String> set(String... expectedMoves) {
        return new HashSet<>(Arrays.asList(expectedMoves));
    }

    public static Set<Move> generateAllMoves(Board board, BitBoard bitBoard) {
        MoveGenerator moveGenerator = new MoveGenerator();
        Set<Move> set = new HashSet<>();
        moveGenerator.moveStream(board, bitBoard).mapToObj(Move::wrap).forEach(set::add);
        return set;
    }

    public static void testMoves(Set<Move> expectedMoves, Board board) {
        testMoves(expectedMoves, board, move -> true);
    }

    public static void testMoves(Set<Move> expected, Board board, Predicate<Move> filter) {
        Set<Move> moves = generateAllMoves(board, BitBoard.MASK_ALL);
        moves.removeIf(filter.negate());
        testMoves(expected, moves);
    }

    public static void testMoves(Set<Move> expected, Board board, Position at) {
        Set<Move> moves = generateAllMoves(board, new BitBoard() {{
            set(at.raw());
        }});
        testMoves(expected, moves);
    }

    public static <T> void testMoves(Set<T> expected, Set<T> actual) {
        Assertions.assertEquals(expected, actual);
    }

    public static void testMoves(Set<Move> expectedPossible, Move actual) {
        Assertions.assertTrue(expectedPossible.contains(actual), "actual move: " + actual);
    }

    public static <T> void testMoves(Set<T> expectedPossible, T actual) {
        Assertions.assertTrue(expectedPossible.contains(actual), "actual move: " + actual);
    }

}
