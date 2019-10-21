package kk.chessbot;

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
                true).move().toLongNotation());

        assertEquals("Kb1c2", new RandomPlayer(fromString("" +
                /*8*/"        " +
                /*7*/"        " +
                /*6*/"        " +
                /*5*/"        " +
                /*4*/"        " +
                /*3*/"♜♜♙     " +
                /*2*/"        " +
                /*1*/" ♔ ♜    "),
                true).move().toLongNotation());

        assertEquals("Rg3b3", new RandomPlayer(fromString("" +
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
        assertEquals("Rg3b3", new RandomPlayer(fromString("" +
                /*8*/"        " +
                /*7*/"        " +
                /*6*/"        " +
                /*5*/"        " +
                /*4*/"♖♖♖     " +
                /*3*/"      ♜ " +
                /*2*/" ♚  ♜   " +
                /*1*/"        "),
                false).move().toLongNotation());

        assertEquals("c4xb3", new RandomPlayer(fromString("" +
                /*8*/"        " +
                /*7*/"        " +
                /*6*/"        " +
                /*5*/"        " +
                /*4*/"  ♟     " +
                /*3*/"♖♖      " +
                /*2*/"        " +
                /*1*/" ♚ ♔    "),
                false).move().toLongNotation());

        assertEquals("Kb1c2", new RandomPlayer(fromString("" +
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