package gui;

import lines.DDALineDrawer;
import lines.Line;
import lines.LineDrawer;
import pixels.*;

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

    private ScreenConverter screenConverter = new ScreenConverter(-2, 2, 4, 4, 800, 600);

    private Line xAxis = new Line(-1, 0, 1, 0);
    private Line yAxis = new Line(0, -1, 0, 1);

    private ScreenPoint previousPoint = null;

    private ArrayList<Line> allLines = new ArrayList<>();

    private Line newLine = null;

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
        drawAll(lineDrawer);
        /**/
        lineDrawer.drawLine(screenConverter.realToScreen(xAxis.getFirstPoint()), screenConverter.realToScreen(xAxis.getSecondPoint()));
        lineDrawer.drawLine(screenConverter.realToScreen(yAxis.getFirstPoint()), screenConverter.realToScreen(yAxis.getSecondPoint()));
        /**/
        graphics.drawImage(bufferedImage, 0, 0, null);
    }

    public void drawAll(LineDrawer lineDrawer) {
        drawLine(lineDrawer, xAxis);
        drawLine(lineDrawer, yAxis);
        for (Line line : allLines) {
            drawLine(lineDrawer, line);
        }
        if (newLine != null) {
            drawLine(lineDrawer, newLine);
        }

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
                newLine = new Line(
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
                allLines.add(newLine);
                newLine = null;
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
        if (newLine != null) {
            newLine.setSecondPoint(screenConverter.screenToReal(currentPoint));
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
