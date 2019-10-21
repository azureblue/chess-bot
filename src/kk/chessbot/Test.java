package kk.chessbot;

import kk.chessbot.wrappers.Move;

import java.util.Random;
import java.util.Scanner;

import static kk.chessbot.moves.BoardUtils.fromString;

public class Test {
    public static void main(String[] args) {

            Random r1 = new Random(1234);
            Random r2 = new Random(12345);

            Board p1Board = fromString("" +
                    "♜♞♝♛♚♝♞♜" +
                    "♟♟♟♟♟♟♟♟" +
                    "        " +
                    "        " +
                    "        " +
                    "        " +
                    "♙♙♙♙♙♙♙♙" +
                    "♖♘♗♕♔♗♘♖");

            Board p2Board = fromString("" +
                    "♜♞♝♛♚♝♞♜" +
                    "♟♟♟♟♟♟♟♟" +
                    "        " +
                    "        " +
                    "        " +
                    "        " +
                    "♙♙♙♙♙♙♙♙" +
                    "♖♘♗♕♔♗♘♖");

            RandomPlayer white = new RandomPlayer(p1Board, true, r1);
            RandomPlayer black = new RandomPlayer(p2Board, false, r2);

            Scanner sc = new Scanner(System.in);
            while (true) {

                Move whiteMove = white.move();
                System.out.println("white going " + whiteMove.toLongNotation());
                white.applyMove(whiteMove);
                black.applyMove(whiteMove);
                System.out.println(p1Board.toUnicodeMultiline());
                Move blackMove = black.move();
                System.out.println("black going " + blackMove.toLongNotation());
                black.applyMove(blackMove);
                white.applyMove(blackMove);
                System.out.println(p1Board.toUnicodeMultiline());
            }


    }
}
