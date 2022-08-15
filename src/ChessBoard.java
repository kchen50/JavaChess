import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class ChessBoard extends JPanel implements MouseListener, MouseMotionListener {
    JFrame frame;
    JPanel gridPanel;

    //Polygon[][] grid;
    JLabel[][] chessGrid;
    JLabel[][] flippedChessGrid;
    Tile[][] grid;
    JLayeredPane pane;
    GlassPane glassPane;

    int dimR = 8, dimC = 8;
    int buttonSize = 80;
    Color light = new Color(222,247,255);
    Color dark = new Color(35, 126, 153);

    String[] whiteSources = {"res/WhiteBishop.png", "res/WhiteKnight.png", "res/WhiteKing.png", "res/WhitePawn.png", "res/WhiteQueen.png", "res/WhiteRook.png"};
    String[] blackSources = {"res/BlackBishop.png", "res/BlackKnight.png", "res/BlackKing.png", "res/BlackPawn.png", "res/BlackQueen.png", "res/BlackRook.png"};
    ImageIcon[] whiteIcons;
    ImageIcon[] blackIcons;

    boolean mouse = false;
    boolean pieceHeld = false;
    boolean flipped = false;
    JLabel clicked;
    int rank = -1, file = -1;
    int moveNumber = 0;
    boolean whiteToMove = true;
    JLabel dragTest;

    int whiteKingRank = 1;
    int whiteKingFile = 4;
    int blackKingRank = 8;
    int blackKingFile = 4;

    int aRookFile = 0;
    int hRookFile = 7;
    int castles = -1;
    int pawnPromotion = -1;
    boolean whiteChecked = false;
    boolean blackChecked = false;

    public ChessBoard(){
        frame = new JFrame("Chess");
        frame.add(this);
        gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(dimR, dimC));
        //gridPanel.addMouseMotionListener(this);

        frame.setSize(buttonSize*(dimR+1), buttonSize*(dimC+1));
        frame.setPreferredSize(new Dimension(buttonSize*dimR, buttonSize*dimC));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JMenuBar menuBar = new JMenuBar();
        JMenu optionsMenu = new JMenu("Options");
        menuBar.add(optionsMenu);
        frame.setJMenuBar(menuBar);

        glassPane = new GlassPane(false, menuBar, frame.getContentPane());
        frame.setGlassPane(glassPane);
        //glassPane.setEnabled(true);
        //glassPane.setVisible(true);

        pane = new JLayeredPane();
        pane.setBounds(0, 0, buttonSize*dimR, buttonSize*dimC);

        whiteIcons = new ImageIcon[6];
        blackIcons = new ImageIcon[6];

        //frame.setContentPane(pane);

        setGrid();
        setPieces();

/*
        /*ImageIcon blackBishop = new ImageIcon("res/BlackBishop.png");
        ImageIcon blackHorse = new ImageIcon("res/BlackHorse.png");
        ImageIcon blackKing = new ImageIcon("res/BlackKing.png");
        ImageIcon blackPawn = new ImageIcon("res/BlackPawn.png");
        ImageIcon blackQueen = new ImageIcon("res/BlackQueen.png");
        ImageIcon blackRook = new ImageIcon(new ImageIcon("res/BlackRook.png").getImage().getScaledInstance(buttonSize, buttonSize, Image.SCALE_DEFAULT));
        blackRook.setDescription("Black Rook");
        ImageIcon whiteBishop = new ImageIcon("res/WhiteBishop.png");
        ImageIcon whiteHorse = new ImageIcon("res/WhiteHorse.png");
        ImageIcon whiteKing = new ImageIcon("res/WhiteKing.png");
        ImageIcon whitePawn = new ImageIcon("res/WhitePawn.png");
        ImageIcon whiteQueen = new ImageIcon("res/WhiteQueen.png");
        ImageIcon whiteRook = new ImageIcon("res/WhiteRook.png");

        //chessGrid[0][0].setIcon(blackRook);
*/
        //JLabel label = new JLabel(blackRook, JLabel.CENTER);
        //label.setSize(buttonSize, buttonSize);
        //frame.add(label);

        //pane = new JLayeredPane();
        //pane.setOpaque(true);
        /*frame.add(pane, BorderLayout.CENTER);

        //pane.add(gridPanel, JLayeredPane.DEFAULT_LAYER);
        if(1==2) {
            Promotion p = new Promotion(whiteIcons);
            pane.add(p, JLayeredPane.MODAL_LAYER);
            p.setVisible(true);
            p.setBounds(0, 0, 80, 360);
            //System.out.println("HI");
        }
        else */
        frame.add(pane, BorderLayout.CENTER);
        pane.add(gridPanel, JLayeredPane.DEFAULT_LAYER);
        gridPanel.setBounds(0, 0, buttonSize*dimR, buttonSize*dimC);

        dragTest = new JLabel();

        pane.add(dragTest, JLayeredPane.DRAG_LAYER);
        dragTest.setBounds(0, 0, 100, 100);
        dragTest.setVisible(false);
        //label.setBounds(0, 0, 100, 100);
        //dragTest.setVisible(true);
        //dragTest.setOpaque(true);
        //pane.setBounds(0, 0, frame.getWidth(), frame.getHeight()); // must change later on
        //pane.add(gridPanel, 1, 0);
        //pane.setBackground(Color.PINK);
        //pane.setBorder(BorderFactory.createTitledBorder("Chess"));
        //frame.pack();



        frame.setLayout(null);
        frame.setVisible(true);
        //System.out.println(getScaledImage(whiteSources[3]).getDescription());

    }

    public void setGrid(){
        //grid = new Polygon[dimR][dimC];
        chessGrid = new JLabel[dimR][dimC];
        grid = new Tile[dimR][dimC];

        for(int i = 0; i < dimR; i++){
            for(int j = 0; j < dimC; j++){
                //int[] xPoints = {i*buttonSize, (i+1)*buttonSize, (i+1)*buttonSize, i*buttonSize};
                //int[] yPoints = {j*buttonSize, j*buttonSize, (j+1)*buttonSize, (j+1)*buttonSize};
                //grid[i][j] = new Polygon(xPoints, yPoints, 4);
                chessGrid[i][j] = new JLabel();//new ImageIcon("res/BlackRook.png")); // create tile class to store vertices
                chessGrid[i][j].setBackground((i+j)%2==0 ? light : dark); // create method that takes in x, y and returns tile
                chessGrid[i][j].setOpaque(true);
                chessGrid[i][j].addMouseListener(this);
                chessGrid[i][j].addMouseMotionListener(this);
                chessGrid[i][j].putClientProperty("rank", 8-i);
                chessGrid[i][j].putClientProperty("file", (char)(j+65));

                grid[i][j] = new Tile((i+j)%2==0 ? 0 : 1);

                gridPanel.add(chessGrid[i][j]);
                //gridPanel.add(new JLabel());
            }
        }
        repaint();
    }

    public void setPieces(){
        // 0 = bishop
        whiteIcons[0] = getScaledImage(whiteSources[0]);
        blackIcons[0] = getScaledImage(blackSources[0]);
        // 1 = knight
        whiteIcons[1] = getScaledImage(whiteSources[1]);
        blackIcons[1] = getScaledImage(blackSources[1]);
        // 2 = king
        whiteIcons[2] = getScaledImage(whiteSources[2]);
        blackIcons[2] = getScaledImage(blackSources[2]);
        // 3 = pawn
        whiteIcons[3] = getScaledImage(whiteSources[3]);
        blackIcons[3] = getScaledImage(blackSources[3]);
        // 4 = queen
        whiteIcons[4] = getScaledImage(whiteSources[4]);
        blackIcons[4] = getScaledImage(blackSources[4]);
        // 5 = rook
        whiteIcons[5] = getScaledImage(whiteSources[5]);
        blackIcons[5] = getScaledImage(blackSources[5]);

        int iconInd = -1;
        for(int i = 0; i < chessGrid[0].length; i++){
            switch(i){
                case 0:
                case 7:
                    iconInd = 5;
                    break;
                case 1:
                case 6:
                    iconInd = 1;
                    break;
                case 2:
                case 5:
                    iconInd = 0;
                    break;
                case 3:
                    iconInd = 4;
                    break;
                case 4:
                    iconInd = 2;
                    break;
                default:
                    System.out.println("something broke and i have no idea what");

            }

            chessGrid[0][i].setIcon(blackIcons[iconInd]);
            chessGrid[7][i].setIcon(whiteIcons[iconInd]);
            chessGrid[1][i].setIcon(blackIcons[3]);
            chessGrid[6][i].setIcon(whiteIcons[3]);

            grid[6][i].setPiece(new Pawn(i, 0));
            grid[1][i].setPiece(new Pawn(i, 1));

            switch(iconInd){
                case 0: grid[0][i].setPiece(new Bishop(i, 1));
                        grid[7][i].setPiece(new Bishop(i, 0));
                    break; // bishop
                case 1: grid[0][i].setPiece(new Knight(i, 1));
                        grid[7][i].setPiece(new Knight(i, 0));
                    break; // knight
                case 2: grid[0][i].setPiece(new King(1));
                        grid[7][i].setPiece(new King(0));
                    break; // king
                case 3:
                    break; // pawns already set above
                case 4: grid[0][i].setPiece(new Queen(1));
                        grid[7][i].setPiece(new Queen(0));
                    break; // queen
                case 5: grid[0][i].setPiece(new Rook(i, 1));
                        grid[7][i].setPiece(new Rook(i, 0));
                    break; // rook
                default:
                    System.out.println("something else broke");
            }
        }
        /*
        for(int i = 0; i < grid.length; i++){
            for(int j = 0; j < grid[i].length; j++){
                if(grid[i][j].occupied)
                    System.out.println(i + " " + j + " " + grid[i][j].getPiece().getClass().getSimpleName() + grid[i][j].getPiece().player);
                else System.out.println(i + " " + j + " unoccupied");
            }
        }*/
    }

    public ImageIcon getScaledImage(String path){
        ImageIcon img = new ImageIcon(new ImageIcon(path).getImage().getScaledInstance(buttonSize, buttonSize, Image.SCALE_DEFAULT));
        img.setDescription(path.substring(4, 9) + " " + path.substring(9, path.length()-4));
        return img;
    }

    public boolean validMove(@NotNull JLabel label){
        int oldFile = (int)((char)label.getClientProperty("file")) - 65;
        int oldRank = (int)label.getClientProperty("rank");

        Piece piece = grid[8-oldRank][oldFile].getPiece();
        String pieceType = piece.getClass().getSimpleName();
        int player = piece.player;
        //System.out.println(pieceType);

        if(oldFile == file && oldRank == rank)
            return false;

        int kingRank = player == 0 ? whiteKingRank : blackKingRank;
        int kingFile = player == 0 ? whiteKingFile : blackKingFile;

        // move conditions: can't "take" friendly piece, can't be pinned, can't jump over other pieces (unless knight)
        //boolean condition0 = grid[8-rank][file].occupied && !grid[8-rank][file].getPiece().getClass().getSimpleName().equalsIgnoreCase("King");
        boolean condition0 = !(whiteChecked && piece.player == 0) && !(blackChecked && piece.player == 1);
        boolean condition1 = !grid[8-rank][file].occupied || ((grid[8-rank][file].getPiece().player != player) && !grid[8-rank][file].getPiece().getClass().getSimpleName().equalsIgnoreCase("King"));
        boolean condition2 = pieceType.equalsIgnoreCase("King") || !isPinned(oldFile, oldRank, player);

        if (condition1) {
            if (condition0) {
                if (condition2) {
                    return pieceMoveCheck(pieceType, oldFile, oldRank, player);
                } else { // check that piece is moving along file, rank, or diagonal
                    System.out.println("PINNED");

                    //System.out.println("OLD: " + oldFile + " " + oldRank);
                    //System.out.println("NEW: " + file + " " + rank);
                    //System.out.println("KING: " + kingFile + " " +kingRank);
                    return movingInBetween(pieceType, player, oldRank, oldFile, kingRank, kingFile);
                    // temporary false return to make testing easier
                }
            } else {
                System.out.println("deal with check");

                if(pieceType.equalsIgnoreCase("King")) { // king
                    return kingMove(oldFile, oldRank, player, true);
                }else{
                    HashMap<String, ArrayList<Point>> atk = attackSources(kingRank, kingFile, player);
                    System.out.println(atk.keySet());
                    Set<String> piecesAttacking = atk.keySet();
                    if(piecesAttacking.size() == 1 && !isPinned(oldFile, oldRank, player)){ // non-king piece, not pinned
                        Iterator<String> it = piecesAttacking.iterator();
                        String key = it.next();
                        String pieceAttacking = key.substring(0, key.length()-1);
                        ArrayList<Point> loc = atk.get(key);
                        switch(pieceAttacking){
                            case "KNIGHT": // check that newFile, newRank are identical; i.e. the attacking knight/pawn is being taken
                            case "PAWN":
                                if((!isPinned(oldFile, oldRank, player) || movingInBetween(pieceType, player, oldRank, oldFile, kingRank, kingFile)) && loc.get(0).x == rank && loc.get(0).y == file){
                                    if(piece.player == 0)
                                        whiteChecked = false;
                                    else blackChecked = false;
                                    return true;
                                }

                            default: // rook, bishop, queen
                                //System.out.println("default");
                                if(!isPinned(oldFile, oldRank, player) || movingInBetween(pieceType, player, oldRank, oldFile, kingRank, kingFile)){
                                    if(loc.get(0).x == rank && loc.get(0).y == file) { // take attacking piece
                                        if(piece.player == 0)
                                            whiteChecked = false;
                                        else blackChecked = false;
                                        return true;
                                    }
                                    //System.out.println(movingInBetween(pieceType, player, loc.get(0).x, loc.get(0).y, kingRank, kingFile));
                                    boolean rankInBetween = (rank >= (8-loc.get(0).x) && rank <= kingRank) || (rank <= (8-loc.get(0).x) && rank >= kingRank);
                                    boolean fileInBetween = (file >= loc.get(0).y && file <= kingFile) || (file <= loc.get(0).y && file >= kingFile);
                                    //System.out.println(movingToBlock(pieceType, player, loc.get(0).x, loc.get(0).y, kingRank, kingFile, oldRank, oldFile));
                                    if(rankInBetween && fileInBetween)
                                        if(movingToBlock(pieceType, player, loc.get(0).x, loc.get(0).y, kingRank, kingFile, oldRank, oldFile)){
                                            if(piece.player == 0)
                                                whiteChecked = false;
                                            else blackChecked = false;
                                            return true;
                                        }
                                    return false;
                                    // block attacking piece
                                }
                                else return false;

                        }
                    }else return false;
                }
            }
        }else if (pieceType.equalsIgnoreCase("King") && !((King) piece).hasMoved && oldRank == rank && Math.abs(oldFile - file) > 1) {
            return kingMove(oldFile, oldRank, player, false);
        }

        return false;
        //ImageIcon imageIcon = (ImageIcon)piece;
        //System.out.println(imageIcon.getDescription());
        //need to find way to get identity of piece
        //return true;
    }

    public boolean movingToBlock(String pieceType, int player, int attackingRank, int attackingFile, int kingRank, int kingFile, int oldRank, int oldFile){
        try {
            if ((attackingRank == rank && rank == kingRank) || (attackingFile == file && file == kingFile)) {
                if ((attackingRank <= rank && rank < kingRank) || (attackingRank >= rank && rank > kingRank) || (attackingFile <= file && file < kingFile) || (attackingFile >= file & file > kingFile))
                    return pieceMoveCheck(pieceType, oldFile, oldRank, player);
                return false;
            } else if (((double)attackingRank - (double)rank)/(attackingFile - file) == ((double)rank - (double)kingRank)/(file - kingFile)){
                //System.out.println("bishop block check");
                if((attackingRank <= rank && rank < kingRank) || (attackingRank >= rank && rank > kingRank))
                    if((attackingFile <= file && file < kingFile) || (attackingFile >= file & file > kingFile))
                        return pieceMoveCheck(pieceType, oldFile, oldRank, player);
            }
        }catch(Exception e){
            return false;
        }
        return false;
    }

    public boolean movingInBetween(String pieceType, int player, int oldRank, int oldFile, int kingRank, int kingFile){
        if ((oldRank == rank && rank == kingRank) || (oldFile == file && file == kingFile)) {
            return pieceMoveCheck(pieceType, oldFile, oldRank, player);
        } else if (((double) oldRank - (double) kingRank) / (oldFile - kingFile) == ((double) rank - (double) kingRank) / (file - kingFile)) {
            return pieceMoveCheck(pieceType, oldFile, oldRank, player);
        }
        System.out.println("doesn't meet requirements for moving in between");
        return false;
    }

    public boolean pieceMoveCheck(@NotNull String pieceType, int oldFile, int oldRank, int player){
        switch (pieceType) {
            case "Rook":
                return rookMove(oldFile, oldRank);
            case "Knight":
                return knightMove(oldFile, oldRank);
            case "Bishop":
                return bishopMove(oldFile, oldRank);
            case "Queen":
                return queenMove(oldFile, oldRank);
            case "King":
                return kingMove(oldFile, oldRank, player, false);
            case "Pawn":
                return pawnMove(oldFile, oldRank, player);
        }

        System.out.println("Piece not recognized.");
        return false;
    }

    public boolean isPinned(int oldFile, int oldRank, int player){
        //System.out.println("PIN CHECK");
        // make methods that check if a space is attacked by a rook or bishop
        // make methods to check that there is nothing between two spaces (the piece & the king)
        int tempFile = player == 0 ? whiteKingFile : blackKingFile;
        int tempRank = 8-(player == 0 ? whiteKingRank : blackKingRank);
        //System.out.println(tempFile + " " + tempRank);
        //System.out.println("RANKS: " + oldRank + " " + (8-oldRank) + " " + tempRank);
        //System.out.println((8-oldRank) == tempRank);
        boolean checkRookPin = oldFile != tempFile && 8-oldRank != tempRank;
        boolean checkBishopPin = Math.abs(oldFile - tempFile) != Math.abs((8-oldRank) - tempRank);
        //System.out.println("PIN CHECKS: " + checkRookPin + " " + checkBishopPin);

        if(checkRookPin && checkBishopPin) {
            //System.out.println("NO PIN DETECTED");
            return false;
        }

        // check that there is no piece between (oldFile, oldRank) and the king and the first piece on the other side is a rook or bishop

        if(!checkRookPin) {
            System.out.println("ROOK PIN CHECK");
            if (oldFile == tempFile) { // UP, DOWN
                boolean kingConnected = openFile(oldFile, oldRank, tempRank);
                boolean rookPin = coveredByRook(oldRank, oldFile, tempRank > (8-oldRank) ? 1 : 2, player).size() != 0;
                //System.out.println("RANKS: " + tempRank + " " + oldRank);
                //System.out.println((8-oldRank) > oldRank ? "DOWN" : "UP");
                //System.out.println("PIN CONDITIONS: " + kingConnected + " " + rookPin);
                return kingConnected && rookPin;
                //System.out.println("CONNECTED: " + kingConnected);
            } else{ // LEFT, RIGHT
                boolean kingConnected = openRank(oldRank, oldFile, tempFile);
                boolean rookPin = coveredByRook(oldRank, oldFile, tempFile < oldFile ? 3 : 4, player).size() != 0;
                //System.out.println(tempFile < oldFile ? "LEFT" : "RIGHT");
                //System.out.println("PIN CONDITIONS: " + kingConnected + " " + rookPin);
                return kingConnected && rookPin;
                //System.out.println("CONNECTED: " + kingConnected);
            }
        }if (!checkBishopPin){
            if (oldFile - tempFile == tempRank - (8-oldRank)) { // NE, SW
                //System.out.println("BISHOP PIN CHECK");
                boolean kingConnected = openPosDiag(8-oldRank, oldFile, tempRank, tempFile);
                boolean bishopPin = coveredByBishop(oldRank, oldFile, tempRank > (8-oldRank) ? 1 : 2, player).size() != 0;
                //System.out.println((8-oldRank) > tempRank ? "NE" : "SW");
                //System.out.println("PIN CONDITIONS: " + kingConnected + " " + bishopPin);
                return kingConnected && bishopPin;
                //System.out.println("CONNECTED: " + kingConnected);
            } else if (oldFile - tempFile == (8-oldRank) - tempRank) { // NW, SE
                //System.out.println("BISHOP PIN CHECK");
                boolean kingConnected = openNegDiag(8-oldRank, oldFile, tempRank, tempFile);
                boolean bishopPin = coveredByBishop(oldRank, oldFile, tempRank > (8-oldRank) ? 3 : 4, player).size() != 0;
                //System.out.println((8-oldRank) > tempRank ? "NW" : "SE");
                //System.out.println("PIN CONDITIONS: " + kingConnected + " " + bishopPin);
                return kingConnected && bishopPin;
                //System.out.println("CONNECTED: " + kingConnected);
            }
        }
        return false;
        //return true;
    }

    public boolean openRank(int rank, int startFile, int endFile){ // viewed indices
        //System.out.println("FILES: " + startFile + " " + endFile);
        //System.out.println("OPEN RANK CHECK");
        if(startFile > endFile){
            for(int i = startFile - 1; i > endFile; i--) {
                //System.out.println(i + " " + rank);
                //System.out.println("LOCATION CHECKED:" + (8-rank) + " " + file);
                if (grid[8 - rank][i].occupied) {
                    //System.out.println(grid[8-rank][i].getPiece().getClass().getSimpleName());
                    return false;
                }
            }
        }
        else{
            for(int i = startFile + 1; i < endFile; i++) {
                //System.out.println(i + " " + rank);
                //System.out.println("LOCATION CHECKED:" + (8-rank) + " " + file);
                if (grid[8 - rank][i].occupied){
                    //System.out.println(grid[8-rank][i].getPiece().getClass().getSimpleName());
                    return false;
                }
            }
        }
        return true;
    }

    public boolean openFile(int file, int startRank, int endRank){ // viewed indices
        //System.out.println("RANKS: " + (8-startRank) + " " + (8-endRank));
        //System.out.println("WHITE KING: " + whiteKingFile + " " + whiteKingRank);
        if(startRank > endRank){
            for(int i = (8-startRank) - 1; i > (8-endRank); i--) {
                //System.out.println("OPEN FILE CHECK: " + file + " " + i);
                if (grid[8 - i][file].occupied)
                    return false;
            }
        }
        else{
            for(int i = (8-startRank) + 1; i < (8-endRank); i++) {
                //System.out.println("OPEN FILE CHECK: " + file + " " + i);
                if (grid[8 - i][file].occupied)
                    return false;
            }
        }
        return true;
    }

    public boolean openPosDiag(int startRank, int startFile, int endRank, int endFile){ // proper indices
        //System.out.println((endRank - startRank) + " " + (startFile - endFile));
        //System.out.println(startFile + " " + startRank + "\t" + endFile + " " + endRank);
        if(endRank - startRank != startFile - endFile || startRank == endRank)
            return false;

        //System.out.println("POS DIAG CHECK");
        int len = Math.abs(endRank - startRank);

        if(startRank < endRank){
            for (int i = 1; i < len && startRank + i < endRank && startFile - i > endFile; i++){ // checks SW
                if(grid[startRank+i][startFile-i].occupied)
                    return false;
            }
        }else{
            for (int i = 1; i < len && startRank - i > endRank && startFile + i < endFile; i++){ // checks SW
                if(grid[startRank-i][startFile+i].occupied)
                    return false;
            }
        }

        return true;
    }

    public boolean openNegDiag(int startRank, int startFile, int endRank, int endFile){ // proper indices
        //System.out.println((endRank - startRank) + " " + (endFile - startFile));
        //System.out.println(startFile + " " + startRank + "\t" + endFile + " " + endRank);
        if(endRank - startRank != endFile - startFile || startRank == endRank)
            return false;

        //System.out.println("NEG DIAG CHECK");
        int len = Math.abs(endRank - startRank);

        if(startRank < endRank){
            for (int i = 1; i < len && startRank + i < endRank && startFile + i < endFile; i++){ // checks SW
                if(grid[startRank+i][startFile+i].occupied)
                    return false;
            }
        }else{
            for (int i = 1; i < len && startRank - i > endRank && startFile - i > endFile; i++){ // checks SW
                if(grid[startRank-i][startFile-i].occupied)
                    return false;
            }
        }

        return true;
    }

    public ArrayList<Point> coveredByRook(int rank, int file, int dir, int fPlayer){
        ArrayList<Point> points = new ArrayList<>();
        if(dir == 1) {
            for (int i = rank + 1; i <= 8 && i >= 0; i++) { // checks upward
                //System.out.println(8-i);
                if (grid[8 - i][file].occupied) {
                    Piece piece = grid[8 - i][file].getPiece();
                    String type = piece.getClass().getSimpleName();
                    int opPlayer = piece.player;
                    //System.out.println(type + " " + opPlayer + " " + file + " " + (8-i));
                    if(fPlayer == opPlayer || !(type.equalsIgnoreCase("Rook") || type.equalsIgnoreCase("Queen")))
                        return points;
                    if(fPlayer != opPlayer && (type.equalsIgnoreCase("Rook") || type.equalsIgnoreCase("Queen"))) {
                        //System.out.println("ROOK " + file + " " + (8-i) + " " + opPlayer);
                        points.add(new Point(i, file));
                    }
                }
            }
        }else if(dir == 2){
            for(int i = rank - 1; i > 0 && i <= 8; i--){ // checks downward
                if(grid[8 - i][file].occupied) {
                    Piece piece = grid[8 - i][file].getPiece();
                    String type = piece.getClass().getSimpleName();
                    int opPlayer = piece.player;
                    //System.out.println(type + " " + opPlayer + " " + file + " " + (8-i));
                    if(fPlayer == opPlayer || !(type.equalsIgnoreCase("Rook") || type.equalsIgnoreCase("Queen")))
                        return points;
                    if(fPlayer != opPlayer && (type.equalsIgnoreCase("Rook") || type.equalsIgnoreCase("Queen"))) {
                        //System.out.println("ROOK " + file + " " + (i) + " " + opPlayer);
                        points.add(new Point(i, file));
                    }
                }
            }
        }else if(dir == 3) {
            for (int i = file + 1; i <= 7 && i >= 0; i++) { // checks rightward
                if (grid[8 - rank][i].occupied) {
                    Piece piece = grid[8 - rank][i].getPiece();
                    String type = piece.getClass().getSimpleName();
                    int opPlayer = piece.player;
                    //System.out.println(type + " " + opPlayer + " " + file + " " + (8-i));
                    if(fPlayer == opPlayer || !(type.equalsIgnoreCase("Rook") || type.equalsIgnoreCase("Queen")))
                        return points;
                    if(fPlayer != opPlayer && (type.equalsIgnoreCase("Rook") || type.equalsIgnoreCase("Queen"))) {
                        //System.out.println("ROOK " + file + " " + (8-i) + " " + opPlayer);
                        points.add(new Point(rank, i));
                    }
                }
            }
        }else if(dir == 4){
            for(int i = file - 1; i >= 0 && i <= 7; i--){ // checks leftward
                if(grid[8-rank][i].occupied) {
                    Piece piece = grid[8 - rank][i].getPiece();
                    String type = piece.getClass().getSimpleName();
                    int opPlayer = piece.player;
                    //System.out.println(type + " " + opPlayer + " " + file + " " + (8-i));
                    if(fPlayer == opPlayer || !(type.equalsIgnoreCase("Rook") || type.equalsIgnoreCase("Queen")))
                        return points;
                    if(fPlayer != opPlayer && (type.equalsIgnoreCase("Rook") || type.equalsIgnoreCase("Queen"))) {
                        //System.out.println("ROOK " + file + " " + (8-i) + " " + opPlayer);
                        points.add(new Point(rank, i));
                    }
                }
            }
        }
        //if(points.size() != 0)
        //    System.out.println("COVERED BY ROOK " + points.size() + " TIMES" + " " + dir);
        return points;
    }

    /*public HashMap<Integer, ArrayList<Point>> coveredByRook(int rank, int file, boolean isRank, int player){
        HashMap<Integer, ArrayList<Point>> attacked = new HashMap<>();
        if(isRank) {
            ArrayList<Point> dir1 = coveredByRook(rank, file, 1, player);
            ArrayList<Point> dir2 = coveredByRook(rank, file, 2, player);
            if(dir1.size() > 0)
                attacked.put(1, )
            return coveredByRook(rank, file, 1, player) + coveredByRook(rank, file, 2, player);
        }
        else
            return coveredByRook(rank, file, 3, player) + coveredByRook(rank, file, 4, player);
    }*/

    /*public boolean coveredByRook(int rank, int file, boolean isRank){
        if(isRank){
            for(int i = rank + 1; i <= 7; i++){
                if(grid[8-i][file].occupied)
                    return grid[8-i][file].getPiece().getClass().getSimpleName().equalsIgnoreCase("Rook");
            }
            for(int i = rank - 1; i >= 0; i--){
                if(grid[8-i][file].occupied)
                    return grid[8-i][file].getPiece().getClass().getSimpleName().equalsIgnoreCase("Rook");
            }
        }else{
            for(int i = file + 1; i <= 7; i++){
                System.out.println(i + " " + rank);
                if(grid[8-rank][i].occupied)
                    return grid[8-rank][i].getPiece().getClass().getSimpleName().equalsIgnoreCase("Rook");
            }
            for(int i = file - 1; i >= 0; i--){
                System.out.println(i + " " + rank);
                if(grid[8-rank][i].occupied)
                    return grid[8-rank][i].getPiece().getClass().getSimpleName().equalsIgnoreCase("Rook");
            }
        }
        return false;
    }*/

    public HashMap<Integer, ArrayList<Point>> coveredByRook(int rank, int file, int player){
        HashMap<Integer, ArrayList<Point>> attacked = new HashMap<>();
        for(int i = 1; i <= 4; i++){
            ArrayList<Point> temp = coveredByRook(rank, file, i, player);
            if(temp.size() > 0)
                attacked.put(i, temp);
        }
        return attacked;
    }

    public ArrayList<Point> coveredByBishop(int rank, int file, int dir, int fPlayer){
        //System.out.println("RANK: " + rank + " FILE: " + file + " DIR: " + dir);
        ArrayList<Point> points = new ArrayList<>();
        if(dir == 1) { // SW
            for (int i = 1; (8 - (rank + i) >= 0) && (8 - (rank + i) <= 7) && (file + i >= 0) && (file + i <= 7); i++) { // checks NE
                if (grid[8 - (rank + i)][file + i].occupied) {
                    Piece piece = grid[8 - (rank + i)][file + i].getPiece();
                    String type = piece.getClass().getSimpleName();
                    int opPlayer = piece.player;
                    if(fPlayer == opPlayer || !(type.equalsIgnoreCase("Bishop") || type.equalsIgnoreCase("Queen")))
                        return points;
                    //System.out.println("PLAYERS: " + opPlayer + " " + fPlayer);
                    if(fPlayer != opPlayer && (type.equalsIgnoreCase("Bishop") || type.equalsIgnoreCase("Queen"))) {
                        points.add(new Point(rank + i, file + i));
                    }
                }
            }
        }else if(dir == 2){ // NE
            for (int i = 1; (8 - (rank - i) >= 0) && (8 - (rank - i) <= 7) && (file - i >= 0) && (file - i <= 7); i++){ // checks SW
                if(grid[8-(rank-i)][file-i].occupied) {
                    Piece piece = grid[8-(rank-i)][file-i].getPiece();
                    String type = piece.getClass().getSimpleName();
                    int opPlayer = piece.player;
                    if(fPlayer == opPlayer || !(type.equalsIgnoreCase("Bishop") || type.equalsIgnoreCase("Queen")))
                        return points;
                    //System.out.println("PLAYERS: " + opPlayer + " " + fPlayer);
                    if(fPlayer != opPlayer && (type.equalsIgnoreCase("Bishop") || type.equalsIgnoreCase("Queen"))) {
                        points.add(new Point(rank - i, file - i));
                    }
                }
            }
        }else if(dir == 3) { // SE
            for (int i = 1; (8 - (rank + i) >= 0) && (8 - (rank + i) <= 7) && (file - i >= 0) && (file - i <= 7); i++) { // checks NW
                if (grid[8 - (rank + i)][file - i].occupied) {
                    Piece piece = grid[8 - (rank + i)][file - i].getPiece();
                    String type = piece.getClass().getSimpleName();
                    int opPlayer = piece.player;
                    if(fPlayer == opPlayer || !(type.equalsIgnoreCase("Bishop") || type.equalsIgnoreCase("Queen")))
                        return points;
                    //System.out.println("PLAYERS: " + opPlayer + " " + fPlayer);
                    if(fPlayer != opPlayer && (type.equalsIgnoreCase("Bishop") || type.equalsIgnoreCase("Queen"))) {
                        points.add(new Point(rank + i, file - i));
                    }
                }
            }
        }else if(dir == 4){ // NW
            for (int i = 1; (8 - (rank - i) >= 0) && (8 - (rank - i) <= 7) && (file + i >= 0) && (file + i <= 7); i++){ // checks SE
                if(grid[8-(rank-i)][file+i].occupied) {
                    Piece piece = grid[8 - (rank - i)][file + i].getPiece();
                    String type = piece.getClass().getSimpleName();
                    int opPlayer = piece.player;
                    if(fPlayer == opPlayer || !(type.equalsIgnoreCase("Bishop") || type.equalsIgnoreCase("Queen")))
                        return points;
                    //System.out.println("PLAYERS: " + opPlayer + " " + fPlayer);
                    if(fPlayer != opPlayer && (type.equalsIgnoreCase("Bishop") || type.equalsIgnoreCase("Queen"))) {
                        points.add(new Point(rank - i, file + i));
                    }
                }
            }
        }
        //System.out.println("weird coveredByBishop return");
        //System.out.println("COVERED BY BISHOP " + count + " TIMES" + " " + dir);
        return points;
    }

    /*public int coveredByBishop(int rank, int file, boolean positiveSlope, int player){
        if(positiveSlope)
            return coveredByBishop(rank, file, 1, player) + coveredByBishop(rank, file, 2, player);
        return coveredByBishop(rank, file, 3, player) + coveredByBishop(rank, file, 4, player);

    }*/

    /*public boolean coveredByBishop(int rank, int file, boolean positiveSlope){
        if(positiveSlope){
            for(int i = 1; (rank + i <= 7) && (file + i <= 7); i++){ // checks NE
                if(grid[8-(rank+i)][file+i].occupied)
                    return grid[8-(rank+i)][file+i].getPiece().getClass().getSimpleName().equalsIgnoreCase("Bishop");
            }
            for(int i = 1; (rank - i >= 0) && (file - i >= 0); i++){ // checks SW
                if(grid[8-(rank-i)][file-i].occupied)
                    return grid[8-(rank-i)][file-i].getPiece().getClass().getSimpleName().equalsIgnoreCase("Bishop");
            }
        }else{
            for(int i = 1; (rank + i <= 7) && (file - i >= 0); i++){ // checks NW
                if(grid[8-(rank+i)][file-i].occupied)
                    return grid[8-(rank+i)][file-i].getPiece().getClass().getSimpleName().equalsIgnoreCase("Bishop");
            }
            for(int i = 1; (rank - i >= 0) && (file + i <= 7); i++){ // checks SE
                if(grid[8-(rank-i)][file+i].occupied)
                    return grid[8-(rank-i)][file+i].getPiece().getClass().getSimpleName().equalsIgnoreCase("Bishop");
            }
        }
        return false;
    }*/

    public HashMap<Integer, ArrayList<Point>> coveredByBishop(int rank, int file, int player){
        //System.out.println("ASDF"+coveredByBishop(rank, file, true, player) + "" + coveredByBishop(rank, file, false, player));
        HashMap<Integer, ArrayList<Point>> attacked = new HashMap<>();
        for(int i = 1; i <= 4; i++){
            ArrayList<Point> temp = coveredByBishop(rank, file, i, player);
            if(temp.size() > 0)
                attacked.put(i, temp);
        }
        return attacked;
    }

    public ArrayList<Point> coveredByKnight(int rank, int file, int player){
        int[] knightFileAdd = {1, 2, 2, 1, -1, -2, -2, -1};
        int[] knightRankAdd = {2, 1, -1, -2, -2, -1, 1, 2};
        ArrayList<Point> points = new ArrayList<>();
        for(int i = 0; i < 8; i++){
            try {
                if (grid[8 - (rank+knightRankAdd[i])][file+knightFileAdd[i]].occupied) {
                    Piece piece = grid[8 - (rank+knightRankAdd[i])][file+knightFileAdd[i]].getPiece();
                    if(piece.player != player && piece.getClass().getSimpleName().equalsIgnoreCase("Knight"))
                        points.add(new Point(rank+knightRankAdd[i],file+knightFileAdd[i]));
                }
            }catch(ArrayIndexOutOfBoundsException ignored){};
        }
        //System.out.println("COVERED BY KNIGHT " + count + " TIMES");
        return points;
    }

    public ArrayList<Point> coveredByPawn(int rank, int file, int player){
        //System.out.println(rank);
        //System.out.println(player == 0 ? -1 : 1);
        int pawnRank = 8-(rank + (player == 0 ? 1 : -1));
        ArrayList<Point> points = new ArrayList<>();
        //System.out.println(pawnRank);
        int count = 0;
        if(pawnRank >= 0 && pawnRank <= 7) {
            if (file != 0 && (grid[pawnRank][file - 1].occupied && grid[pawnRank][file - 1].getPiece().player != player && grid[pawnRank][file - 1].getPiece().getClass().getSimpleName().equalsIgnoreCase("Pawn")))
                points.add(new Point(pawnRank, file - 1));
            if (file != 7 && (grid[pawnRank][file + 1].occupied && grid[pawnRank][file + 1].getPiece().player != player && grid[pawnRank][file + 1].getPiece().getClass().getSimpleName().equalsIgnoreCase("Pawn")))
                points.add(new Point(pawnRank, file + 1));
        }
        //System.out.println(wOccupied + "" + eOccupied);
        //System.out.println("COVERED BY PAWN " + count + " TIMES");
        return points;
    }

    public boolean coveredByKing(int rank, int file, int player){
        int opKingRank = player == 0 ? blackKingRank : whiteKingRank;
        int opKingFile = player == 0 ? blackKingFile : whiteKingFile;
        return Math.max(Math.abs(opKingFile - file), Math.abs(opKingRank - rank)) <= 1;
    }

    public boolean notCovered(int rank, int file, int player){
        /*System.out.println("rook " + coveredByRook(rank, file, player));
        System.out.println("bishop " + coveredByBishop(rank, file, player));
        System.out.println("knight " + coveredByKnight(rank, file, player));
        System.out.println("pawn " + coveredByPawn(rank, file, player));
        System.out.println("king " + coveredByKing(rank, file, player));*/
        return coveredByRook(rank, file, player).keySet().size() == 0 && coveredByBishop(rank, file, player).keySet().size() == 0 && coveredByKnight(rank, file, player).size() == 0 && coveredByPawn(rank, file, player).size() == 0 && !coveredByKing(rank, file, player);
    }

    public HashMap<String, ArrayList<Point>> attackSources(int rank, int file, int player){
        /*System.out.println("rook " + coveredByRook(rank, file, player));
        System.out.println("bishop " + coveredByBishop(rank, file, player));
        System.out.println("knight " + coveredByKnight(rank, file, player));
        System.out.println("pawn " + coveredByPawn(rank, file, player));
        System.out.println("king " + coveredByKing(rank, file, player));*/
        HashMap<String, ArrayList<Point>> attacked = new HashMap<>();

        HashMap<Integer, ArrayList<Point>> attackedByRook = coveredByRook(rank, file, player);
        for(int rookDir : attackedByRook.keySet())
            attacked.put("ROOK" + rookDir, attackedByRook.get(rookDir));

        HashMap<Integer, ArrayList<Point>> attackedByBishop = coveredByBishop(rank, file, player);
        for(int bishopDir : attackedByBishop.keySet())
            attacked.put("BISHOP" + bishopDir, attackedByBishop.get(bishopDir));

        ArrayList<Point> attackedByPawn = coveredByPawn(rank, file, player);
        if(attackedByPawn.size() > 0)
            attacked.put("PAWN1", attackedByPawn);

        ArrayList<Point> attackedByKnight = coveredByKnight(rank, file, player);
        if(attackedByKnight.size() > 0)
            attacked.put("KNIGHT1", attackedByKnight);

        //int attackedByKing = coveredByKing(rank, file, player) ? 1 : 0;
        if(coveredByKing(rank, file, player)) {
            ArrayList<Point> king = new ArrayList<>();
            king.add(player == 0 ? new Point(blackKingRank, blackKingFile) : new Point(whiteKingRank, whiteKingFile));
            attacked.put("KING1", king);
        }
        return attacked;
    }

    public boolean rookMove(int oldFile, int oldRank){
        if(oldFile != file && oldRank != rank)
            return false;

        if(oldFile != file){ // oldRank == rank
            if(oldFile > file){
                for(int i = oldFile - 1; i > file && i >= 0; i--)
                    if(grid[8-rank][i].occupied)
                        return false;
            }else{ // oldFile < file
                for(int i = oldFile + 1; i < file && i <= 7; i++)
                    if(grid[8-rank][i].occupied)
                        return false;
            }
        }else{ // oldRank != rank, oldFile == file
            if(oldRank > rank){
                for(int i = oldRank - 1; i > rank && i >= 0; i--)
                    if(grid[8-i][file].occupied)
                        return false;
            }else{ // oldRank < rank
                for(int i = oldRank +1; i < rank && i <= 7; i++)
                    if(grid[8-i][file].occupied)
                        return false;
            }
        }
        return true;
    }

    public boolean knightMove(int oldFile, int oldRank){
        return (Math.abs(file - oldFile) == 2 && Math.abs(rank - oldRank) == 1) || (Math.abs(file - oldFile) == 1 && Math.abs(rank - oldRank) == 2);
    }

    public boolean bishopMove(int oldFile, int oldRank){
        //System.out.println("bishopMove " + rank + " " + file + " " + oldRank + " " + oldFile);
        if(Math.abs(file - oldFile) != Math.abs(rank - oldRank))
            return false;
        else{ // destination cannot be occupied by friendly piece; no piece in between original and destination can be occupied whatsoever
            System.out.println("made it past initial bishop move check");
            int lateralDist = Math.abs(file - oldFile);
            boolean aFile = file > oldFile;
            boolean aRank = rank > oldRank;
            for(int i = 1; i < lateralDist; i++){
                int iFile = oldFile + (aFile ? i : -i);
                int iRank = oldRank + (aRank ? i : -i);
                //System.out.println("intermediate: " + iFile + " " + iRank);
                if(grid[8-iRank][iFile].occupied)
                    return false;
            }
        }
        return true;
    }

    public boolean queenMove(int oldFile, int oldRank){
        return rookMove(oldFile, oldRank) || bishopMove(oldFile, oldRank);
    }

    public void setKingLoc(int rank, int file, int player){
        if(player == 0){
            whiteKingRank = rank;
            whiteKingFile = file;
            //System.out.println("WHITE KING: " + whiteKingFile + " " + whiteKingRank);
        }else{
            blackKingRank = rank;
            blackKingFile = file;
            //System.out.println("BLACK KING: " + blackKingFile + " " + blackKingRank);
        }
    }

    public boolean kingMove(int oldFile, int oldRank, int player, boolean checked){ // square to move to cannot be occupied or covered by an opposing piece
        boolean valid = Math.max(Math.abs(file - oldFile), Math.abs(rank - oldRank)) <= 1 && notCovered(rank, file, player);
        King king = ((King)grid[8-oldRank][oldFile].getPiece());
        if(valid){
            king.hasMoved = true;
            setKingLoc(rank, file, player);
        }else if(!king.hasMoved && !checked){
            if(player == 0){
                if(rank == 1){
                    //System.out.println("WHITE");
                    if(file > 5 && openRank(1, whiteKingFile, 7)){ // may need to adjust to be usable in chess960
                        if(grid[8-whiteKingRank][hRookFile].occupied){
                            Piece rook = grid[8-whiteKingRank][hRookFile].getPiece();
                            if(rook.player == 0 && rook.getClass().getSimpleName().equalsIgnoreCase("Rook") && !((Rook) rook).hasMoved){
                                castles = 2;
                                if(notCovered(whiteKingRank, whiteKingFile, 0)){
                                    setKingLoc(rank, file, player);
                                    return true;
                                }
                            }
                            else return false;
                        }
                        //System.out.println("WHITE SHORT CASTLES");
                    }else if(file < 3 && openRank(1, whiteKingFile, 0)){ // may need to adjust to be usable in chess960
                        if(grid[8-whiteKingRank][aRookFile].occupied){
                            Piece rook = grid[8-whiteKingRank][aRookFile].getPiece();
                            if(rook.player == 0 && rook.getClass().getSimpleName().equalsIgnoreCase("Rook") && !((Rook) rook).hasMoved){
                                castles = 0;
                                if(notCovered(whiteKingRank, whiteKingFile, 0)){
                                    setKingLoc(rank, file, player);
                                    return true;
                                }
                            }
                            else return false;
                        }
                        //System.out.println("WHITE LONG CASTLES");
                    }else return false;
                }else return false;
            }else{
                if(rank == 8){
                    //System.out.println("BLACK");
                    if(file > 5 && openRank(8, blackKingFile, 7)){ // may need to adjust to be usable in chess960
                        if(grid[8-blackKingRank][hRookFile].occupied){
                            Piece rook = grid[8-blackKingRank][hRookFile].getPiece();
                            if(rook.player == 1 && rook.getClass().getSimpleName().equalsIgnoreCase("Rook") && !((Rook) rook).hasMoved){
                                castles = 3;
                                if(notCovered(blackKingRank, blackKingFile, 1)){
                                    setKingLoc(rank, file, player);
                                    return true;
                                }
                            }
                            else return false;
                        }
                        //System.out.println("BLACK SHORT CASTLES");
                    }else if(file < 3 && openRank(8, blackKingFile, 0)){ // may need to adjust to be usable in chess960
                        if(grid[8-blackKingRank][aRookFile].occupied){
                            Piece rook = grid[8-blackKingRank][aRookFile].getPiece();
                            if(rook.player == 1 && rook.getClass().getSimpleName().equalsIgnoreCase("Rook") && !((Rook) rook).hasMoved){
                                castles = 1;
                                if(notCovered(blackKingRank, blackKingFile, 1)){
                                    setKingLoc(rank, file, player);
                                    return true;
                                }
                            }
                            else return false;
                        }
                        //System.out.println("BLACK LONG CASTLES");
                    }else return false;
                }else return false;
            }
        }
        return valid;
    }

    public boolean pawnMove(int oldFile, int oldRank, int player){
        if(Math.abs(file - oldFile) > 1 || Math.abs(rank - oldRank) > 2) // if change in file > 1 or change in rank > 2 (first move), return false
            return false;

        boolean forwardOne = oldRank + (grid[8-oldRank][oldFile].getPiece().player == 0 ? 1 : -1)  == rank;

        if(file == oldFile){
            if(Math.abs(rank - oldRank) == 1){
                if(forwardOne) {
                    if(!grid[8-rank][file].occupied){
                        ((Pawn)grid[8-oldRank][oldFile].getPiece()).hasMoved = true;
                        if(rank == (player == 0 ? 8 : 1)) {
                            //chessGrid[8-oldRank][oldFile].setIcon(dragTest.getIcon());
                            initiatePromotion(oldRank, oldFile, rank, file, player);
                            pawnPromotion = player;
                        }
                        return true;
                    }else return false;
                }// DEPENDS ON FLIPPED BOOLEAN
            }else if(Math.abs(rank - oldRank) == 2){ // 2 spaces in front are both unoccupied, piece.hasMoved == false
                if(oldRank + (grid[8-oldRank][oldFile].getPiece().player == 0 ? 2 : -2)  == rank) {
                    if(((Pawn)grid[8-oldRank][oldFile].getPiece()).hasMoved) {
                        return false;
                    }
                    else{
                        if(!grid[8-rank][file].occupied && !grid[8-((oldRank + rank)/2)][file].occupied){
                            ((Pawn)grid[8-oldRank][oldFile].getPiece()).hasMoved = true;
                            ((Pawn)grid[8-oldRank][oldFile].getPiece()).leapMoveNumber = moveNumber;
                            //System.out.println(moveNumber);
                            if(rank == (player == 0 ? 8 : 1))
                                pawnPromotion = player;
                            return true;
                        } else return false;
                    }
                }// DEPENDS ON FLIPPED BOOLEAN
            }else{
                System.out.println("Error occurred with pawn move");
                return false;
            }
        }else if(forwardOne){ // Math.abs(rank - oldRank) != 1: pawn captures - normal and en passant
            //System.out.println(file + " " + rank);
            //System.out.println(oldFile + " " + oldRank);
            if(grid[8-rank][file].occupied) {
                if(rank == (player == 0 ? 8 : 1)) {
                    //chessGrid[8-oldRank][oldFile].setIcon(dragTest.getIcon());
                    initiatePromotion(oldRank, oldFile, rank, file, player);
                    pawnPromotion = player;
                }
                return true;
            }
            else{
                if(!(grid[8-oldRank][file].occupied && ((Pawn)grid[8-oldRank][file].getPiece()).leapMoveNumber + 1 == moveNumber))
                    return false;
                else{
                    System.out.println("EN PEASANT");
                    grid[8-oldRank][file].movePiece();
                    chessGrid[8-oldRank][file].setIcon(null);
                    return true;
                }
            }
            //if(occupied) // then normal capture
            //else // check hashmap using move # - 1 for the pawn in the correct position
        }

        // FINISH PAWN
        return false;
    }



    public void initiatePromotion(int oldRank, int oldFile, int rank, int file, int player){
        Promotion p = new Promotion(player == 0 ? whiteIcons : blackIcons, oldRank, oldFile, rank, file, player, this);
        pane.add(p, JLayeredPane.MODAL_LAYER);
        p.setVisible(true);
        if((player == 0 && !flipped) || (player == 1 && flipped))
            p.setBounds(chessGrid[8-rank][file].getLocation().x, chessGrid[8-rank][file].getLocation().y, 80, 360);
        else
            p.setBounds(chessGrid[8-rank][file].getLocation().x, chessGrid[8-rank][file].getLocation().y-280, 80, 360);

        glassPane.setPromotion(this, p, oldRank, oldFile);
        glassPane.setVisible(true);
        glassPane.setEnabled(true);

    }

    public boolean checkmate(int player){
        // look for space that king can move to
        //System.out.println(player + " king move check");
        int kingRank = player == 0 ? whiteKingRank : blackKingRank;
        int kingFile = player == 0 ? whiteKingFile : blackKingFile;
        for(int rShift = -1; rShift <= 1; rShift++){
            for(int fShift = -1; fShift <= 1; fShift++){
                try{
                    if(notCovered(kingRank + rShift, kingFile + fShift, player) && !grid[8-(kingRank + rShift)][kingFile + fShift].occupied)
                        return false;
                }catch(ArrayIndexOutOfBoundsException ignored){};
            }
        }

        // detect # of ways king is checked
        //System.out.println(player + " piece move check");
        int opPlayer = player == 0 ? 1 : 0;

        HashMap<String, ArrayList<Point>> atk = attackSources(kingRank, kingFile, player);
        System.out.println(atk.keySet());
        Set<String> piecesAttacking = atk.keySet();
        Iterator<String> it = piecesAttacking.iterator();
        String key = it.next();
        String pieceAttacking = key.substring(0, key.length()-1);
        int dir = Integer.parseInt(key.substring(key.length()-1));

        if(piecesAttacking.size() == 1){
            // knight, pawn can only have 1 attacker, whereas bishops/rooks/queens could be lined up
            switch(pieceAttacking){
                case "KNIGHT": // check that newFile, newRank are identical; i.e. the attacking knight/pawn is being taken
                case "PAWN":
                    Point attackPos = atk.get(key).get(0);
                    HashMap<String, ArrayList<Point>> takeAttacker = attackSources(8-attackPos.x, attackPos.y, opPlayer);
                    for(String takeAttackerType : takeAttacker.keySet()){
                        for(Point takeAttackerLoc : takeAttacker.get(takeAttackerType)){
                            if(!isPinned(8-takeAttackerLoc.x, takeAttackerLoc.y, player))
                                return false;
                        }
                    }
                    break;
                    // asdf
                case "ROOK": // cant use coveredbypawn; loop through all spaces in between along with the attacking piece, and check that at least 1 counterattacker isnt pinned
                    for(Point attackingRook : atk.get(key)) {
                        ArrayList<Point> blockOrTake = rookSpacesInBetween(kingRank, kingFile, attackingRook.x, attackingRook.y);
                        for(Point p : blockOrTake){
                            HashMap<String, ArrayList<Point>> blockersOrTakers = attackSources(8-p.x, p.y, opPlayer);
                            if(blockersOrTakers.keySet().size() > 0)
                                for(ArrayList<Point> blockerTakerList : blockersOrTakers.values())
                                    for(Point blockerTaker : blockerTakerList)
                                        if(!isPinned(8-blockerTaker.x, blockerTaker.y, player))
                                            return false;
                        }
                    }
                    break;
                case "BISHOP":
                    for(Point attackingBishop : atk.get(key)) {
                        ArrayList<Point> blockOrTake = bishopSpacesInBetween(kingRank, kingFile, attackingBishop.x, attackingBishop.y);
                        System.out.println(blockOrTake);
                        for (Point p : blockOrTake) {
                            System.out.println(p);
                            HashMap<String, ArrayList<Point>> blockersOrTakers = attackSources(8 - p.x, p.y, opPlayer);
                            System.out.println(blockersOrTakers);
                            if (blockersOrTakers.keySet().size() > 0)
                                for (ArrayList<Point> blockerTakerList : blockersOrTakers.values())
                                    for (Point blockerTaker : blockerTakerList)
                                        if (!isPinned(8 - blockerTaker.x, blockerTaker.y, player))
                                            return false;
                        }
                    }
                    break;
                default: // rook, bishop, queen

            }
        }

        //      if 1, check if any piece can block or take the attacking piece !! CANNOT BE PINNED
        //      else, king is checkmated
        return true;
    }

    public ArrayList<Point> rookSpacesInBetween(int kingRank, int kingFile, int rookRank, int rookFile){
        ArrayList<Point> points = new ArrayList<>();
        if(kingRank == rookRank){
            //for(int i = rookFile + 1; i <= kingFile; i++){
            if(kingFile > rookFile)
                for(int i = rookFile; i < kingFile; i++)
                    points.add(new Point(8 - rookRank, i));
            else if(kingFile < rookFile) {
                for (int i = kingFile + 1; i <= rookFile; i++)
                    points.add(new Point(8 - rookRank, i));
            }
        }else if(kingFile == rookFile){
            if(kingRank > rookRank)
                for(int i = rookRank; i < kingRank; i++)
                    points.add(new Point(8 - i, rookFile));
            else if(kingRank < rookRank)
                for(int i = kingRank + 1; i <= rookRank; i++)
                    points.add(new Point(8 - i, rookFile));
        }

        return points;
    }

    public ArrayList<Point> bishopSpacesInBetween(int kingRank, int kingFile, int bishopRank, int bishopFile){
        ArrayList<Point> points = new ArrayList<>();
        if(kingRank > bishopRank) { // king is above bishop
            //System.out.println(kingRank + " " + kingFile + " " + bishopRank + " " + bishopFile);
            for (int i = 1; i < kingRank - bishopRank; i++) {
                if (kingFile < bishopFile)  // king is left of bishop
                    points.add(new Point(8 - (bishopRank + i), bishopFile - i));
                else if (kingFile > bishopFile)  // king is to the right of rook
                    points.add(new Point(8 - (bishopRank + i), bishopFile + i));
            }
        }else if(kingRank < bishopRank){ // king is below bishop
            for(int i = 1; i < bishopRank - kingRank; i++) {
                if (kingFile > bishopFile)
                    points.add(new Point(8 - (bishopRank - i), bishopRank + i));
                else if (kingFile < bishopFile)
                    points.add(new Point(8 - (bishopRank - i), bishopRank - i));
            }
        }

        return points;
    }

    public ImageIcon getIcon(String input, int player){
        int ind = -1;
        switch(input){
            case "Q" -> ind = 4;
            case "R" -> ind = 5;
            case "B" -> ind = 0;
            case "N" -> ind = 1;
            default -> {
                return null;
            }
        }
        if(player == 0 || player == 1)
            return (player == 0) ? whiteIcons[ind] : blackIcons[ind];
        return null;
    }

    public void aCastles(int tempRank, int tempFile){
        //System.out.println("A CASTLES");
        ((King)grid[8-rank][tempFile].getPiece()).hasMoved = true;
        ((Rook)grid[8-rank][aRookFile].getPiece()).hasMoved = true;


        grid[8 - rank][tempFile-2].setPiece(grid[tempRank][tempFile].movePiece());
        chessGrid[8 - rank][tempFile-2].setIcon(dragTest.getIcon());
        grid[8 - rank][tempFile-1].setPiece(grid[tempRank][aRookFile].movePiece());
        chessGrid[8-rank][tempFile-1].setIcon(chessGrid[8-rank][aRookFile].getIcon());
        chessGrid[8-rank][aRookFile].setIcon(null);
    }

    public void hCastles(int tempRank, int tempFile){
        //System.out.println("H CASTLES");
        ((King)grid[8-rank][tempFile].getPiece()).hasMoved = true;
        ((Rook)grid[8-rank][hRookFile].getPiece()).hasMoved = true;

        grid[8 - rank][tempFile+2].setPiece(grid[tempRank][tempFile].movePiece());
        chessGrid[8 - rank][tempFile+2].setIcon(dragTest.getIcon());
        grid[8 - rank][tempFile+1].setPiece(grid[tempRank][hRookFile].movePiece());
        chessGrid[8-rank][tempFile+1].setIcon(chessGrid[8-rank][hRookFile].getIcon());
        chessGrid[8-rank][hRookFile].setIcon(null);
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        /*for(int i = 0; i < dimR; i++){
            for(int j = 0; j < dimC; j++){
                //g.setColor((i+j)%2==0 ? light : dark);
                //g.fillPolygon(grid[i][j]);
                //grid[i][j] = new Polygon(xPoints, yPoints, 4);
            }
        }*/
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        clicked = (JLabel)e.getSource();

        mouse = true;
        pieceHeld = clicked.getIcon() != null;

        dragTest.setIcon(clicked.getIcon());
        clicked.setIcon(null);
        dragTest.setLocation(new Point(e.getX()+clicked.getX()-buttonSize/2, e.getY()+clicked.getY()-buttonSize/2));
        dragTest.setVisible(true);

        //gridPanel.setVisible(false);
        //clicked = (JLabel)e.getSource();
        JComponent temp = (JComponent) (e.getSource());
        //System.out.println("CLICKED ON: " + temp.getClientProperty("file") + "" + temp.getClientProperty("rank"));

        //rank and file saved for valid move conditions later
        rank = (int)temp.getClientProperty("rank");
        file = (int)((char)temp.getClientProperty("file"))-65;
        //System.out.println("file: " + (file-10) + " rank: " + (8-rank));
        //System.out.println(temp.getX() + " " + temp.getY());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
// BUG: if mouse releases outside of bounds, pieces is placed at last square entered
        mouse = false;
        dragTest.setVisible(false);

        //System.out.println("RELEASED ON: " + (char)(file+65) + "" + rank + "\n");
        //System.out.println("RANK: " + (8 - (int)clicked.getClientProperty("rank")));

        int tempRank = 8 - (int) clicked.getClientProperty("rank");
        int tempFile = (int) ((char) clicked.getClientProperty("file")) - 65;

        if(pieceHeld){
            //System.out.println(grid[tempRank][tempFile].getPiece().getClass().getSimpleName());
            if(grid[tempRank][tempFile].getPiece().player == moveNumber % 2){
                if (validMove(clicked)) { // move Piece object
                    moveNumber++;
                    if (pawnPromotion != -1) {
                        //System.out.println("PAWN PROMOTION");

                        pawnPromotion = -1;
                    } else if (castles == -1) {
                        //System.out.println("VALID MOVE");
                        grid[8 - rank][file].setPiece(grid[tempRank][tempFile].movePiece());
                        //System.out.println(grid[8-rank][file].getPiece());
                        chessGrid[8 - rank][file].setIcon(dragTest.getIcon());
                    } else { // castles
                        switch (castles) {
                            case 0, 1 -> aCastles(tempRank, tempFile);
                            case 2, 3 -> hCastles(tempRank, tempFile);
                        }
                        castles = -1;
                    }
                    if(moveNumber % 2 == 0 && !notCovered(whiteKingRank, whiteKingFile, 0)){
                        whiteChecked = true;
                        if(checkmate(0))
                            System.out.println("Black checkmates white!");
                        else System.out.println("Black checks white!");
                    }else if(moveNumber % 2 == 1 && !notCovered(blackKingRank, blackKingFile, 1)){
                        blackChecked = true;
                        if(checkmate(1))
                            System.out.println("White checkmates black!");
                        else System.out.println("White checks black!");
                    }
                    //printGrid();
                } else {
                    //System.out.println("INVALID MOVE");
                    clicked.setIcon(dragTest.getIcon());
                }
            }else{
                clicked.setIcon(dragTest.getIcon());
                System.out.println("It isn't " + (moveNumber % 2 == 1 ? "white's" : "black's") + " turn!");
            }
        }

        pieceHeld = false;

        //gridPanel.setVisible(true);
        //JComponent temp = (JComponent) (e.getSource());
        //System.out.println("RELEASED ON: " + temp.getClientProperty("file") + "" + temp.getClientProperty("rank"));

        //System.out.println("file: " + (file-10) + " rank: " + (8-rank));
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if(mouse) {
            JComponent temp = (JComponent) (e.getSource());
            rank = (int)temp.getClientProperty("rank");
            file = (int)((char)temp.getClientProperty("file")) - 65;
            //System.out.println(rank);
            //System.out.println(temp.getClientProperty("file"));
            //System.out.println("file: " + file);
            //System.out.println(temp.getClientProperty("file") + "" + temp.getClientProperty("rank"));
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        dragTest.setLocation(new Point(e.getX()+clicked.getX()-buttonSize/2, e.getY()+clicked.getY()-buttonSize/2));
        //System.out.println(dragTest.getLocation());
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    public void printGrid(){
        for(int i = 0; i < grid.length; i++){
            for(int j = 0; j < grid[i].length; j++){
                if(grid[i][j].occupied){
                    System.out.print(grid[i][j].getPiece().getClass().getSimpleName().substring(0, 1));
                }else System.out.print("-");
            }
            System.out.println();
        }
        System.out.println();
    }
}
