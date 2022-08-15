public class Tile {
    int color; // 0 = white, 1 = black
    private Piece piece;
    boolean occupied;

    public Tile(int color){
        this.color = color;
        occupied = false;
    }

    public Tile(int color, Piece piece){
        this.color = color;
        this.piece = piece;
        occupied = true;
    }

    public Piece movePiece(){
        occupied = false;
        Piece temp = piece;
        piece = null;
        return temp;
    }

    public Piece getPiece(){
        return piece;
    }

    public void setPiece(Piece newPiece){
        this.piece = newPiece;
        occupied = true; // possibly return old piece
    }
}
