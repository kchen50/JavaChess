public class Knight extends Piece{

    public Knight(int file, int player){ // new knight
        super((player == 0) ? 0 : 7, file, player);
    }

    public Knight(int rank, int file, int player){ // pawn promotion
        super(rank, file, player);
    }
}
