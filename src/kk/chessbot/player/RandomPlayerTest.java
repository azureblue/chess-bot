package kk.chessbot.player;

import kk.chessbot.Side;
import org.junit.jupiter.api.Test;

import static kk.chessbot.moves.BoardUtils.fromString;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RandomPlayerTest {

    @Test
    void escapeFromMate() {

        assertEquals("c2xb3", new RandomPlayer(fromString("" +
                /*8*/"        " +
                /*7*/"        " +
                /*6*/"        " +
                /*5*/"        " +
                /*4*/"        " +
                /*3*/"♜♜      " +
                /*2*/"  ♙     " +
                /*1*/" ♔ ♚    "),
                Side.White).makeMove().toLongNotation());

        assertEquals("Kb1c2", new RandomPlayer(fromString("" +
                /*8*/"        " +
                /*7*/"        " +
                /*6*/"        " +
                /*5*/"        " +
                /*4*/"        " +
                /*3*/"♜♜♙     " +
                /*2*/"        " +
                /*1*/" ♔ ♜    "),
                Side.White).makeMove().toLongNotation());

        assertEquals("Rg3b3", new RandomPlayer(fromString("" +
                /*8*/"        " +
                /*7*/"        " +
                /*6*/"        " +
                /*5*/"        " +
                /*4*/"♜♜♜     " +
                /*3*/"      ♖ " +
                /*2*/" ♔  ♖   " +
                /*1*/"        "),
                Side.White).makeMove().toLongNotation());

        //black
        assertEquals("Rg3b3", new RandomPlayer(fromString("" +
                /*8*/"        " +
                /*7*/"        " +
                /*6*/"        " +
                /*5*/"        " +
                /*4*/"♖♖♖     " +
                /*3*/"      ♜ " +
                /*2*/" ♚  ♜   " +
                /*1*/"        "),
                Side.Black).makeMove().toLongNotation());

        assertEquals("c4xb3", new RandomPlayer(fromString("" +
                /*8*/"        " +
                /*7*/"        " +
                /*6*/"        " +
                /*5*/"        " +
                /*4*/"  ♟     " +
                /*3*/"♖♖      " +
                /*2*/"        " +
                /*1*/" ♚ ♔    "),
                Side.Black).makeMove().toLongNotation());

        assertEquals("Kb1c2", new RandomPlayer(fromString("" +
                /*8*/"        " +
                /*7*/"        " +
                /*6*/"        " +
                /*5*/"        " +
                /*4*/"        " +
                /*3*/"♖♖♟     " +
                /*2*/"        " +
                /*1*/" ♚ ♖    "),
                Side.Black).makeMove().toLongNotation());
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