public class Rook extends Piece{
    boolean hasMoved; // castling

    public Rook(int file, int player){ // new rook
        super((player == 0) ? 0 : 7, file, player);
        hasMoved = false;
    }

    public Rook(int rank, int file, int player){ // pawn promotion
        super(rank, file, player);
        hasMoved = false;

    }

    public Rook(int rank, int file, int player, boolean promoted){
        super(rank, file, player);
        hasMoved = promoted;
    }
}
