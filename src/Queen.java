public class Queen extends Piece{

    public Queen(int player){ // new queen
        super((player == 0) ? 7 : 0, 3, player);
    }

    public Queen(int file, int player){ // new queen
        super((player == 0) ? 0 : 7, file, player); // white promotes queen = 0 (top) & vice versa for black
    }

    public Queen(int rank, int file, int player){
        super(rank, file, player);
    }
}
