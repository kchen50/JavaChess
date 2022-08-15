public class King extends Piece{
    boolean hasMoved;
    public King(int player){
        super((player == 0) ? 7 : 0, 4, player);
        hasMoved = false;
    }
}
