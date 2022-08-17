import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import javax.swing.event.MouseInputAdapter;

public class GlassPane extends JComponent {
    Point p;
    boolean shatter;
    Container promotion;
    GPListener listener;

    public GlassPane(boolean shatter, JMenuBar jMenuBar, Container contentPane){
        this.shatter = shatter;
        listener = new GPListener(jMenuBar, this, contentPane);
        addMouseListener(listener);
        addMouseMotionListener(listener);
    }

    public void setPromotion(ChessBoard chessBoard, Container promotion, int oldRank, int oldFile){
        this.promotion = promotion;
        listener.promotion = promotion;
        shatter = true;
        listener.chessBoard = chessBoard;

        listener.oldRank = oldRank;
        listener.oldFile = oldFile;
        //System.out.println("PROMOTION" + shatter);
    }

    public class GPListener extends MouseInputAdapter{
        Toolkit toolkit;
        JMenuBar menuBar;
        GlassPane glassPane;
        Container contentPane;
        boolean promote;
        Container promotion;

        ChessBoard chessBoard;
        int oldRank, oldFile;

        public GPListener(JMenuBar menuBar, GlassPane glassPane, Container contentPane){
            toolkit = Toolkit.getDefaultToolkit();
            this.menuBar = menuBar;
            this.glassPane = glassPane;
            this.contentPane = contentPane;
        }


        @Override
        public void mouseReleased(MouseEvent e) {
            redirect(e);
        }

        private void redirect(MouseEvent e){
            //System.out.println("clicky");
            Point glassPanePoint = e.getPoint();
            Point containerPoint = SwingUtilities.convertPoint(glassPane, glassPanePoint, contentPane);
            if(containerPoint.y < 0){
                if(containerPoint.y + menuBar.getHeight() > 0){
                    //System.out.println("MenuBar clicked");
                    /*
                    containerPoint = SwingUtilities.convertPoint(glassPane, glassPanePoint, menuBar);
                    Component component = SwingUtilities.getDeepestComponentAt(menuBar, containerPoint.x, containerPoint.y);
                    JMenu mb = (JMenu)component;
                    //mb.doClick();
                    mb.setSelected(!mb.isSelected());
                    mb.setPopupMenuVisible(!mb.isPopupMenuVisible());
                    System.out.println(component.getClass().getSimpleName());
                    Point componentPt = SwingUtilities.convertPoint(glassPane, glassPanePoint, component);
                    //System.out.println(componentPt);
                    component.dispatchEvent(new MouseEvent(component, e.getID(), e.getWhen(), e.getModifiers(), componentPt.x, componentPt.y, e.getClickCount(), e.isPopupTrigger()));

                     */
                }else{

                }
            }else{
                Component component = SwingUtilities.getDeepestComponentAt(contentPane, containerPoint.x, containerPoint.y);

                //System.out.println(component);
                //System.out.println(component.getClass().getSimpleName() + ((JComponent)component).getClientProperty("promotionName"));
                if(((JComponent)component).getClientProperty("promotionName") == null) {
                    if(shatter) {
                        //System.out.println("GLASS PANE HAS SHATTERED");
                        chessBoard.chessGrid[8 - oldRank][oldFile].setIcon(chessBoard.dragLabel.getIcon());
                    }
                }
                else {
                    Point componentPt = SwingUtilities.convertPoint(glassPane, glassPanePoint, component);
                    component.dispatchEvent(new MouseEvent(component, e.getID(), e.getWhen(), e.getModifiers(), componentPt.x, componentPt.y, e.getClickCount(), e.isPopupTrigger()));
                    //System.out.println("post click");

                }
                if(shatter) {
                    if (promotion != null) {
                        promotion.setVisible(false);
                        promotion.setEnabled(false);
                    }

                    setEnabled(false);
                    setVisible(false);

                    shatter = false;
                    promotion = null;
                }
            }

        }
    }
}
