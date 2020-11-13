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

    public DrawPanel(MainWindow mainWindow) {
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addMouseWheelListener(this);
        this.mainWindow = mainWindow;
    }

    private MainWindow mainWindow;

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
    private Ellipse selectedEllipse;
    private boolean shouldEllipseBeRecreated = false;

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
        drawAll(lineDrawer, new EllipseDrawer(pixelDrawer, screenConverter));
        graphics.drawImage(bufferedImage, 0, 0, null);
        setShouldEllipseBeRecreated(false);
    }

    public void drawAll(LineDrawer lineDrawer, EllipseDrawer ellipseDrawer) {
        drawLine(lineDrawer, xAxis);
        drawLine(lineDrawer, yAxis);
        for (Ellipse ellipse : allEllipses) {
            if (ellipse.isSelected()) {
                redrawEllipse(ellipseDrawer, ellipse, true);
                continue;
            }
            redrawEllipse(ellipseDrawer, ellipse, isShouldEllipseBeRecreated());
        }
        if (newEllipse != null) {
            redrawEllipse(ellipseDrawer, newEllipse, true);
        }

    }

    public void redrawEllipse(EllipseDrawer ellipseDrawer, Ellipse ellipse, boolean shouldBeRecreated) {
        ellipseDrawer.drawEllipse(ellipse, ellipse.getColor(), shouldBeRecreated);
    }

    public void drawLine(LineDrawer lineDrawer, Line line) {
        lineDrawer.drawLine(screenConverter.realToScreen(line.getFirstPoint()), screenConverter.realToScreen(line.getSecondPoint()));
    }

    public boolean isPointBelongsToEllipse(int mouseX, int mouseY, Ellipse ellipse) {
        ScreenPoint screenedPixel;
        for (RealPoint pixel : ellipse.getPixels()) {
            screenedPixel = screenConverter.realToScreen(pixel);
            if (screenedPixel.getX() == mouseX && screenedPixel.getY() == mouseY) {
                return true;
            }
        }
        return false;
    }

    public boolean tryToSelectEllipse(int mouseX, int mouseY, Ellipse ellipse) {
        // сложность 6n лучше, чем проверять для каждого эллипса для каждого пикселя приближенность, где сложность 6nm
        for (int i = -6; i < 7; i++) {
            for (int j = -6; j < 7; j++) {
                if (isPointBelongsToEllipse(mouseX + j, mouseY + j, ellipse)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void onEllipseSelected() {
        mainWindow.onEllipseSelected(selectedEllipse);
    }

    public void onEllipseDeselected() {
        mainWindow.onEllipseDeselected();
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
                // а как еще? пометки только для циклов работают, а это не цикл
                boolean shouldBrokeCase = false;
                for (Ellipse ellipse : allEllipses) {
                    if (tryToSelectEllipse(mouseEvent.getX(), mouseEvent.getY(), ellipse)) {
                        if (selectedEllipse != null && ellipse != selectedEllipse) {
                            selectedEllipse.setSelected(false);
                            selectedEllipse.setColor(Color.BLACK);
                            onEllipseDeselected();
                        }
                        ellipse.setColor(Color.RED);
                        ellipse.setSelected(true);
                        selectedEllipse = ellipse;
                        onEllipseSelected();
                        shouldBrokeCase = true;
                        break;
                    }
                }
                if (shouldBrokeCase) {
                    break;
                }
                if (selectedEllipse != null) {
                    selectedEllipse.setSelected(false);
                    selectedEllipse.setColor(Color.BLACK);
                    selectedEllipse = null;
                    onEllipseDeselected();
                }
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
                if (newEllipse == null) {
                    break;
                }
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
        setShouldEllipseBeRecreated(true);
        repaint();
    }

    public boolean isShouldEllipseBeRecreated() {
        return shouldEllipseBeRecreated;
    }

    public void setShouldEllipseBeRecreated(boolean shouldEllipseBeRecreated) {
        this.shouldEllipseBeRecreated = shouldEllipseBeRecreated;
    }
}
