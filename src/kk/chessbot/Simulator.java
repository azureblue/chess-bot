package kk.chessbot;

import kk.chessbot.player.AlphaBetaPlayer;
import kk.chessbot.wrappers.Move;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import static kk.chessbot.moves.BoardUtils.fromString;

public class Simulator {
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

        AlphaBetaPlayer white = new AlphaBetaPlayer(startingBoard, Side.White, r1, 6);
        AlphaBetaPlayer black = new AlphaBetaPlayer(startingBoard, Side.Black, r2, 4);

        Scanner sc = new Scanner(System.in);
        sc.nextLine();

        Board temp = new Board();

        Map<String, Integer> positions = new HashMap<>();
        while (true) {
            white.getBoard(temp);
            int blackPieces = PiecePositions.countPieces(temp, Side.Black);
            black.getBoard(temp);
            int whitePieces = PiecePositions.countPieces(temp, Side.White);
//            System.out.println("" + blackPieces + " " + whitePieces);
            if (blackPieces == 1 || whitePieces == 1) {
                System.out.println("end game");
                break;
            }

            Move whiteMove = white.makeMove(12314);
            System.out.println("white going " + whiteMove.toLongNotation());
            if (temp.piece(whiteMove.posTo()) == Piece.King) {
                System.out.println("white wins");
                break;
            }


            white.applyMove(whiteMove);
            black.applyMove(whiteMove);

//                System.out.println(white.board);
            white.getBoard(temp);
            String boardString = temp.toUnicodeMultiline();
            positions.put(boardString, positions.getOrDefault(boardString, 0) + 1);
            if (positions.get(boardString) == 3){
                System.out.println("end game draw");
                break;
            }

            System.out.println(temp);
//                sc.nextLine();
            Move blackMove = black.makeMove(12314);
            System.out.println("black going " + blackMove.toLongNotation());
            if (temp.piece(blackMove.posTo()) == Piece.King) {
                System.out.println("black wins");
                break;
            }


            white.applyMove(blackMove);
            black.applyMove(blackMove);




            white.getBoard(temp);
            boardString = temp.toUnicodeMultiline();
            positions.put(boardString, positions.getOrDefault(boardString, 0) + 1);
            if (positions.get(boardString) == 3){
                System.out.println("end game draw");
                break;
            }
            System.out.println(temp);
//                sc.nextLine();
        }


    }
}
