package kk.chessbot;

import kk.chessbot.player.NotSoRandomPlayer;
import kk.chessbot.player.RandomPlayer;
import kk.chessbot.wrappers.Move;

import java.util.Random;
import java.util.Scanner;

import static kk.chessbot.moves.BoardUtils.fromString;

public class Test {
    public static void main(String[] args) {

//            Random r1 = new Random(12344);
//            Random r2 = new Random(12344);
        Random seeder = new Random();

        long seed1 = seeder.nextLong();
        long seed2 = seeder.nextLong();

        System.out.println(seed1);
        System.out.println(seed2);

        Random r1 = new Random(seed1);
        Random r2 = new Random(seed2);


        Board startingBoard = fromString("" +
                "♜♞♝♛♚♝♞♜" +
                "♟♟♟♟♟♟♟♟" +
                "        " +
                "        " +
                "        " +
                "        " +
                "♙♙♙♙♙♙♙♙" +
                "♖♘♗♕♔♗♘♖");

        RandomPlayer white = new RandomPlayer(startingBoard, Side.Black, r1);
        NotSoRandomPlayer black = new NotSoRandomPlayer(startingBoard, Side.White, r2);

        Scanner sc = new Scanner(System.in);
        Board temp = new Board();
        while (true) {
            white.getBoard(temp);
            int blackPieces = PiecePositions.countPieces(temp, Side.Black);
            black.getBoard(temp);
            int whitePieces = PiecePositions.countPieces(temp, Side.White);
            System.out.println("" + blackPieces + " " + whitePieces);
            if (blackPieces == 1 || whitePieces == 1) {
                System.out.println("end game");
                break;
            }

            Move whiteMove = white.makeMove(12314);

            white.applyMove(whiteMove);
            black.applyMove(whiteMove);

            System.out.println("white going " + whiteMove.toLongNotation());
//                System.out.println(white.board);
//                sc.nextLine();
            Move blackMove = black.makeMove(12314);

            white.applyMove(blackMove);
            black.applyMove(blackMove);


            System.out.println("black going " + blackMove.toLongNotation());
            white.getBoard(temp);
            System.out.println(temp);
//                sc.nextLine();
        }


    }
}
