package kk.chessbot;

import kk.chessbot.player.NotSoRandomPlayer;
import kk.chessbot.player.RandomPlayer;
import kk.chessbot.wrappers.Move;

import java.util.Random;
import java.util.Scanner;

import static kk.chessbot.moves.BoardUtils.fromString;

public class PerfTest {
    public static void main(String[] args) {

        Random r1 = new Random(12345);


        Board board = (fromString("" +
                "♜ ♝♛♚♝♞♜" +
                "♟♟   ♟♟♟" +
                "  ♞     " +
                "    ♟   " +
                "  ♗ ♙   " +
                "    ♕   " +
                "♙♙♙♙ ♙♙♙" +
                "♖♘♗ ♔ ♘♖"));

        NotSoRandomPlayer black = new NotSoRandomPlayer(board, Side.Black, r1);
        Scanner sc = new Scanner(System.in);
        sc.nextLine();
        System.out.println(black.makeMove());

    }
}
