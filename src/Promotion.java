import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

public class Promotion extends JPanel implements MouseListener {
    ImageIcon[] src;
    JLabel[] options;
    int rank, file, player;
    int oldRank, oldFile;
    ChessBoard chessBoard;
    public Promotion(ImageIcon[] src, int oldRank, int oldFile, int rank, int file, int player, ChessBoard chessBoard){
        this.setLayout(new GridBagLayout());
        options = new JLabel[5];
        this.oldRank = oldRank;
        this.oldFile = oldFile;
        this.rank = rank;
        this.file = file;
        this.player = player;
        this.chessBoard = chessBoard;

        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.VERTICAL;

        if(player == 0) {
            JLabel queen = new JLabel();
            queen.putClientProperty("promotionName", "Q");
            queen.setIcon(src[4]);
            queen.addMouseListener(this);
            c.gridx = 0;
            c.gridy = 0;
            c.gridwidth = 2;
            c.gridheight = 2;
            this.add(queen, c);

            JLabel rook = new JLabel();
            rook.putClientProperty("promotionName", "R");
            rook.setIcon(src[5]);
            rook.addMouseListener(this);
            c.gridy = 2;
            this.add(rook, c);

            JLabel bishop = new JLabel();
            bishop.putClientProperty("promotionName", "B");
            bishop.setIcon(src[0]);
            bishop.addMouseListener(this);
            c.gridy = 4;
            this.add(bishop, c);

            JLabel knight = new JLabel();
            knight.putClientProperty("promotionName", "N");
            knight.setIcon(src[1]);
            knight.addMouseListener(this);
            c.gridy = 6;
            this.add(knight, c);

            JLabel cancel = new JLabel();
            cancel.putClientProperty("promotionName", "CANCEL");
            cancel.setText("CANCEL");
            cancel.addMouseListener(this);
            c.gridy = 8;
            c.gridheight = 1;
            this.add(cancel, c);
        }else{
            JLabel cancel = new JLabel();
            cancel.putClientProperty("promotionName", "CANCEL");
            cancel.setText("CANCEL");
            cancel.addMouseListener(this);
            c.gridx = 0;
            c.gridy = 0;
            c.gridwidth = 2;
            c.gridheight = 1;
            this.add(cancel, c);

            JLabel knight = new JLabel();
            knight.putClientProperty("promotionName", "N");
            knight.setIcon(src[1]);
            knight.addMouseListener(this);
            c.gridy = 1;
            c.gridheight = 1;
            this.add(knight, c);

            JLabel bishop = new JLabel();
            bishop.putClientProperty("promotionName", "B");
            bishop.setIcon(src[0]);
            bishop.addMouseListener(this);
            c.gridy = 3;
            this.add(bishop, c);

            JLabel rook = new JLabel();
            rook.putClientProperty("promotionName", "R");
            rook.setIcon(src[5]);
            rook.addMouseListener(this);
            c.gridy = 5;
            this.add(rook, c);

            JLabel queen = new JLabel();
            queen.putClientProperty("promotionName", "Q");
            queen.setIcon(src[4]);
            queen.addMouseListener(this);
            c.gridy = 7;
            this.add(queen, c);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //System.out.println(((JComponent)e.getSource()).getClientProperty("promotionName"));
        String input = (String)((JComponent)e.getSource()).getClientProperty("promotionName");
        //System.out.println("promotion click");
        switch (input) {
            case "Q" -> {
                //System.out.println("PROMOTE TO QUEEN");
                chessBoard.chessGrid[8-rank][file].setIcon(chessBoard.getIcon(input, player));
                chessBoard.grid[8-rank][file].setPiece(chessBoard.grid[8-oldRank][oldFile].movePiece());
                chessBoard.grid[8-rank][file].setPiece(new Queen(rank, file, player));
            }
            case "R" -> {
                //System.out.println("PROMOTE TO ROOK");
                chessBoard.chessGrid[8-rank][file].setIcon(chessBoard.getIcon(input, player));
                chessBoard.grid[8-rank][file].setPiece(chessBoard.grid[8-oldRank][oldFile].movePiece());
                chessBoard.grid[8-rank][file].setPiece(new Rook(rank, file, player));
            }
            case "B" -> {
                //System.out.println("PROMOTE TO BISHOP");
                chessBoard.chessGrid[8-rank][file].setIcon(chessBoard.getIcon(input, player));
                chessBoard.grid[8-rank][file].setPiece(chessBoard.grid[8-oldRank][oldFile].movePiece());
                chessBoard.grid[8-rank][file].setPiece(new Bishop(rank, file, player));
            }
            case "N" -> {
                //System.out.println("PROMOTE TO KNIGHT");
                chessBoard.chessGrid[8-rank][file].setIcon(chessBoard.getIcon(input, player));
                chessBoard.grid[8-rank][file].setPiece(chessBoard.grid[8-oldRank][oldFile].movePiece());
                chessBoard.grid[8-rank][file].setPiece(new Knight(rank, file, player));
            }
            case "CANCEL" -> chessBoard.chessGrid[8 - oldRank][oldFile].setIcon(chessBoard.dragLabel.getIcon());
            default -> System.out.println("Pawn promotion broke.");
        }
    //System.out.println(chessBoard.grid[8-oldRank][file].getPiece());
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
