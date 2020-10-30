package gui;

import figures.ellipse.EllipseDrawer;
import figures.ellipse.Ellipse;
import figures.lines.DDALineDrawer;
import figures.lines.Line;
import figures.lines.LineDrawer;
import pixels.*;
import util.GlobalVar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class DrawPanel extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener {

    public DrawPanel() {
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addMouseWheelListener(this);
    }

    private ScreenConverter screenConverter = new ScreenConverter(
            -2,
            2,
            4,
            4,
            GlobalVar.SCREEN_WIDTH,
            GlobalVar.SCREEN_HEIGHT
    );

    private Line xAxis = new Line(-1, 0, 1, 0);
    private Line yAxis = new Line(0, -1, 0, 1);
    private ScreenPoint previousPoint = null;
    private ArrayList<Ellipse> allEllipses = new ArrayList<>();
    private Ellipse newEllipse;

    @Override
    public void paint(Graphics graphics) {
        screenConverter.setScreenHeight(getHeight());
        screenConverter.setScreenWidth(getWidth());
        BufferedImage bufferedImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setColor(Color.WHITE);
        graphics2D.fillRect(0, 0, getWidth(), getHeight());
        graphics2D.dispose();
        PixelDrawer pixelDrawer = new BufferedImagePixelDrawer(bufferedImage);
        LineDrawer lineDrawer = new DDALineDrawer(pixelDrawer);
        drawAll(lineDrawer, new EllipseDrawer(pixelDrawer));
        /**/
        lineDrawer.drawLine(screenConverter.realToScreen(xAxis.getFirstPoint()), screenConverter.realToScreen(xAxis.getSecondPoint()));
        lineDrawer.drawLine(screenConverter.realToScreen(yAxis.getFirstPoint()), screenConverter.realToScreen(yAxis.getSecondPoint()));
        /**/
        graphics.drawImage(bufferedImage, 0, 0, null);
    }

    public void drawAll(LineDrawer lineDrawer, EllipseDrawer ellipseDrawer) {
        drawLine(lineDrawer, xAxis);
        drawLine(lineDrawer, yAxis);
        for (Ellipse ellipse : allEllipses) {
            drawEllipse(ellipseDrawer, ellipse, Color.BLACK);
        }
        if (newEllipse != null) {
            drawEllipse(ellipseDrawer, newEllipse, Color.BLACK);
            //System.out.println("repainted at " + newEllipse.getWidth() + " and " + newEllipse.getHeight());
        }

    }

    public void drawEllipse(EllipseDrawer ellipseDrawer, Ellipse ellipse, Color color) {
        ScreenPoint screenedFromPoint = screenConverter.realToScreen(ellipse.getFrom());
        ellipseDrawer.drawEllipse(
                screenedFromPoint,
                screenConverter.realToScreen(ellipse.getWidthVector()).getX() - screenedFromPoint.getX(),
                screenConverter.realToScreen(ellipse.getHeightVector()).getY() - screenedFromPoint.getY(),
                color
        );
        System.out.println("from: " + ellipse.getFrom() + "; width: " + ellipse.getWidthVector().getX() + "; height: " + ellipse.getHeightVector().getY());
        System.out.println("from: " + screenConverter.realToScreen(ellipse.getFrom()) + "; width: " + screenConverter.realToScreen(ellipse.getWidthVector()).getX() + "; height: " + screenConverter.realToScreen(ellipse.getHeightVector()).getY());
        System.out.println();
    }

    public void drawLine(LineDrawer lineDrawer, Line line) {
        lineDrawer.drawLine(screenConverter.realToScreen(line.getFirstPoint()), screenConverter.realToScreen(line.getSecondPoint()));
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {

    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        switch (mouseEvent.getButton()) {
            case MouseEvent.BUTTON3:
                previousPoint = new ScreenPoint(mouseEvent.getX(), mouseEvent.getY());
                break;
            case MouseEvent.BUTTON1:
                newEllipse = new Ellipse(
                        screenConverter.screenToReal(new ScreenPoint(mouseEvent.getX(), mouseEvent.getY())),
                        screenConverter.screenToReal(new ScreenPoint(mouseEvent.getX(), mouseEvent.getY())),
                        screenConverter.screenToReal(new ScreenPoint(mouseEvent.getX(), mouseEvent.getY()))
                );
                break;
        }
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        switch (mouseEvent.getButton()) {
            case MouseEvent.BUTTON3:
                previousPoint = null;
                break;
            case MouseEvent.BUTTON1:
                allEllipses.add(newEllipse);
                newEllipse = null;
                break;
        }
        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        ScreenPoint currentPoint = new ScreenPoint(mouseEvent.getX(), mouseEvent.getY());
        if (previousPoint != null) {
            ScreenPoint deltaScreen = new ScreenPoint(
                    currentPoint.getX() - previousPoint.getX(),
                    currentPoint.getY() - previousPoint.getY()
            );
            RealPoint deltaReal = screenConverter.screenToReal(deltaScreen);
            double vectorX = deltaReal.getX() - screenConverter.getCornerX();
            double vectorY = deltaReal.getY() - screenConverter.getCornerY();
            screenConverter.setCornerX(screenConverter.getCornerX() - vectorX);
            screenConverter.setCornerY(screenConverter.getCornerY() - vectorY);
            previousPoint = currentPoint;
        }
        if (newEllipse != null) {
            newEllipse.setWidthVector(
                    screenConverter.screenToReal(
                            new ScreenPoint(mouseEvent.getX(), screenConverter.realToScreen(newEllipse.getFrom()).getY())
                    )
            );
            newEllipse.setHeightVector(
                    screenConverter.screenToReal(
                            new ScreenPoint(screenConverter.realToScreen(newEllipse.getFrom()).getX(), mouseEvent.getY())
                    )
            );
        }
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int clicksAmount = e.getWheelRotation();
        double scaleCoefficient = 1;
        double stepCoefficient = (clicksAmount > 0) ? 1.1 : 0.95;
        for (int i = Math.abs(clicksAmount); i > 0; i--) {
            scaleCoefficient *= stepCoefficient;
        }
        screenConverter.setRealWidth(scaleCoefficient * screenConverter.getRealWidth());
        screenConverter.setRealHeight(scaleCoefficient * screenConverter.getRealHeight());
        repaint();
    }
}
