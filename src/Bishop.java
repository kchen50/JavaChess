public class Bishop extends Piece{

    public Bishop(int file, int player){ // new bishop
        super((player == 0) ? 0 : 7, file, player);
    }

    public Bishop(int rank, int file, int player){ // pawn promotion
        super(rank, file, player);
    }
}
