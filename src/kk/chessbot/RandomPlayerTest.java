package kk.chessbot;

import kk.chessbot.wrappers.Move;
import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.Scanner;

import static kk.chessbot.moves.BoardUtils.boardFromString;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RandomPlayerTest {

    @Test
    void escapeFromMate() {

        assertEquals("c2xb3", new RandomPlayer(boardFromString("" +
                /*8*/"        " +
                /*7*/"        " +
                /*6*/"        " +
                /*5*/"        " +
                /*4*/"        " +
                /*3*/"♜♜      " +
                /*2*/"  ♙     " +
                /*1*/" ♔ ♚    "),
                true).move().toLongNotation());

        assertEquals("Kb1c2", new RandomPlayer(boardFromString("" +
                /*8*/"        " +
                /*7*/"        " +
                /*6*/"        " +
                /*5*/"        " +
                /*4*/"        " +
                /*3*/"♜♜♙     " +
                /*2*/"        " +
                /*1*/" ♔ ♜    "),
                true).move().toLongNotation());

        assertEquals("Rg3b3", new RandomPlayer(boardFromString("" +
                /*8*/"        " +
                /*7*/"        " +
                /*6*/"        " +
                /*5*/"        " +
                /*4*/"♜♜♜     " +
                /*3*/"      ♖ " +
                /*2*/" ♔  ♖   " +
                /*1*/"        "),
                true).move().toLongNotation());

        //black
        assertEquals("Rg3b3", new RandomPlayer(boardFromString("" +
                /*8*/"        " +
                /*7*/"        " +
                /*6*/"        " +
                /*5*/"        " +
                /*4*/"♖♖♖     " +
                /*3*/"      ♜ " +
                /*2*/" ♚  ♜   " +
                /*1*/"        "),
                false).move().toLongNotation());

        assertEquals("c4xb3", new RandomPlayer(boardFromString("" +
                /*8*/"        " +
                /*7*/"        " +
                /*6*/"        " +
                /*5*/"        " +
                /*4*/"  ♟     " +
                /*3*/"♖♖      " +
                /*2*/"        " +
                /*1*/" ♚ ♔    "),
                false).move().toLongNotation());

        assertEquals("Kb1c2", new RandomPlayer(boardFromString("" +
                /*8*/"        " +
                /*7*/"        " +
                /*6*/"        " +
                /*5*/"        " +
                /*4*/"        " +
                /*3*/"♖♖♟     " +
                /*2*/"        " +
                /*1*/" ♚ ♖    "),
                false).move().toLongNotation());
    }



}

    /*
                "♜♞♝♛♚♝♞♜" +
                "♟♟♟♟♟♟♟♟" +
                "             " +
                "             " +
                "             " +
                "             " +
                "♙♙♙♙♙♙♙♙" +
                "♖♘♗♕♔♗♘♖");
     */