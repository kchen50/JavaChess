public class Pawn extends Piece{
    boolean hasMoved;
    int leapMoveNumber = -1;
    public Pawn(int file, int player){ // default pawn
        super((player == 0) ? 1 : 6, file, player);
        hasMoved = false;
    }
}
