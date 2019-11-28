package kk.chessbot;

import kk.chessbot.player.AlphaBetaPlayer;

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

        AlphaBetaPlayer black = new AlphaBetaPlayer(board, Side.Black, r1);
        Scanner sc = new Scanner(System.in);
         sc.nextLine();
        System.out.println(black.makeMove());

    }
}
