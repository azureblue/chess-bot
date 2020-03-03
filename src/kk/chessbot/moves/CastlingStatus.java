package kk.chessbot.moves;

import kk.chessbot.wrappers.Position;

public class CastlingStatus {
    private int castling = 0b1111;

    public CastlingStatus(int bits) {
        castling = bits;
    }

    public CastlingStatus() {
    }

    public int bits() {
        return castling;
    }

    public void setBits(int castlingBits) {
        this.castling = castlingBits;
    }

    public enum Side {
        K(Position.position("g1"), 0b1, Position.position("g8"), 0b10),
        Q(Position.position("c1"), 0b100, Position.position("c8"), 0b1000);

        private final Position kingDstWhite;
        private final int bitMaskWhite;
        private final Position kingDstBlack;
        private final int bitMaskBlack;

        Side(Position kingDstWhite, int bitMaskWhite,  Position kingDstBlack, int bitMaskBlack) {
            this.kingDstWhite = kingDstWhite;
            this.bitMaskWhite = bitMaskWhite;
            this.kingDstBlack = kingDstBlack;
            this.bitMaskBlack = bitMaskBlack;
        }

        public final Position getKingDstFor(boolean white) {
            return white ? kingDstWhite : kingDstBlack;
        }
    }

    public boolean canCastle(Side side, boolean white) {
        return (castling & (white ? side.bitMaskWhite : side.bitMaskBlack)) != 0;
    }

    public void removeCastling(Side side, boolean white) {
        castling &= (white ? ~side.bitMaskWhite : ~side.bitMaskBlack);
    }

    public void setCastling(Side side, boolean white, boolean canCastle) {
        if (canCastle)
            castling |= (white ? side.bitMaskWhite : side.bitMaskBlack);
        else
            castling &= (white ? ~side.bitMaskWhite : ~side.bitMaskBlack);
    }

}
