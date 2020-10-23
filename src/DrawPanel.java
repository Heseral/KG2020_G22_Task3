

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

public class DrawPanel extends JPanel implements MouseListener, MouseMotionListener {

    public DrawPanel() {
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    }

    private ScreenConverter sc = new ScreenConverter(-2, 2, 4, 4, 800, 600);

    private Line axisX = new Line(-1, 0, 1, 0);
    private Line axisY = new Line(0, -1, 0, 1);

    @Override
    public void paint(Graphics g) {
        sc.sethS(getHeight()); sc.setwS(getWidth());
        BufferedImage bi = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D gr = bi.createGraphics();
        gr.setColor(Color.WHITE);
        gr.fillRect(0, 0, getWidth(), getHeight());
        gr.dispose();
        PixelDrawer pd = new BufferedImagePixelDrawer(bi);
        LineDrawer ld = new DDALineDrawer(pd);
        /**/
        ld.drawLine(sc.r2s(axisX.getP1()), sc.r2s(axisX.getP2()));
        ld.drawLine(sc.r2s(axisY.getP1()), sc.r2s(axisY.getP2()));
        /**/
        g.drawImage(bi, 0, 0, null);
    }

    private ScreenPoint lastPosition = null;

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {

    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        lastPosition = new ScreenPoint(mouseEvent.getX(), mouseEvent.getY());
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        lastPosition = null;
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        ScreenPoint newPosition = new ScreenPoint(mouseEvent.getX(), mouseEvent.getY());
        if (lastPosition != null) {
            ScreenPoint screenDelta = new ScreenPoint(newPosition.getX() - lastPosition.getX(), newPosition.getY() - lastPosition.getY());
            RealPoint delta = sc.s2r(screenDelta);
            sc.setxR( sc.getxR() - delta.getX());
            sc.setyR( sc.getyR() - delta.getY());
            repaint();
        }
        //lastPosition = newPosition;
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {

    }
}
