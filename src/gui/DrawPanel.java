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
            try {
                drawEllipse(ellipseDrawer, ellipse, ellipse.getColor());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (newEllipse != null) {
            drawEllipse(ellipseDrawer, newEllipse, newEllipse.getColor());
        }

    }

    public void drawEllipse(EllipseDrawer ellipseDrawer, Ellipse ellipse, Color color) {
        ScreenPoint screenedFromPoint = screenConverter.realToScreen(ellipse.getFrom());
        ellipseDrawer.drawEllipse(
                screenedFromPoint,
                screenConverter.realToScreen(ellipse.getWidthVector()).getX() - screenedFromPoint.getX(),
                screenConverter.realToScreen(ellipse.getHeightVector()).getY() - screenedFromPoint.getY(),
                ellipse.getTransformationMatrix(),
                color
        );
    }

    public void drawLine(LineDrawer lineDrawer, Line line) {
        lineDrawer.drawLine(screenConverter.realToScreen(line.getFirstPoint()), screenConverter.realToScreen(line.getSecondPoint()));
    }

    public boolean isPointBelongsToEllipse(int mouseX, int mouseY, int x0, int y0, int width, int height) {
        int y = Math.abs(height);
        int x = 0;
        // НАЧАЛО: переменные для облегчения участи процессора. Просто сохраним их, чтобы не пересчитывать каждый раз
        final int bSquared = height * height;
        final int aSquared = width * width;
        final int doubleASquared = aSquared * 2;
        final int quadrupleASquared = aSquared * 4;
        final int quadrupleBSquared = bSquared * 4;
        final int doubleBSquared = bSquared * 2;
        // КОНЕЦ: переменные для облегчения участи процессора
        int delta = doubleASquared * ((y - 1) * y) + aSquared + doubleBSquared * (1 - aSquared);
        // горизонтально-ориентированные кривые
        while (aSquared * y > bSquared * x) {
            if (x + x0 == mouseX && y + y0 == mouseY
                    || x + x0 == mouseX && y0 - y == mouseY
                    || x0 - x == mouseX && y + y0 == mouseY
                    || x0 - x == mouseX && y0 - y == mouseY) {
                return true;
            }
            if (delta >= 0) {
                y--;
                delta -= quadrupleASquared * (y);
            }
            delta += doubleBSquared * (3 + x * 2);
            x++;
        }
        delta = doubleBSquared * (x + 1) * x + doubleASquared * (y * (y - 2) + 1) + (1 - doubleASquared) * bSquared;
        // вертикально-ориентированные кривые
        while (y + 1 > 0) {
            if (x + x0 == mouseX && y + y0 == mouseY
                    || x + x0 == mouseX && y0 - y == mouseY
                    || x0 - x == mouseX && y + y0 == mouseY
                    || x0 - x == mouseX && y0 - y == mouseY) {
                return true;
            }
            if (delta <= 0) {
                x++;
                delta += quadrupleBSquared * x;
            }
            y--;
            delta += doubleASquared * (3 - y * 2);
        }
        return false;
    }

    public boolean tryToSelectEllipse(int mouseX, int mouseY, Ellipse ellipse) {
        ScreenPoint screenedFromPoint = screenConverter.realToScreen(ellipse.getFrom());
        int width = screenConverter.realToScreen(ellipse.getWidthVector()).getX() - screenedFromPoint.getX();
        int height = screenConverter.realToScreen(ellipse.getHeightVector()).getY() - screenedFromPoint.getY();
        int x0 = screenedFromPoint.getX();
        int y0 = screenedFromPoint.getY();
        // сложность 6n лучше, чем проверять для каждого эллипса для каждого пикселя приближенность, где сложность 6nm
        for (int i = -3; i < 4; i++) {
            for (int j = -3; j < 4; j++) {
                if (isPointBelongsToEllipse(mouseX, mouseY, x0 + j, y0 + i, width, height)) {
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
        repaint();
    }
}
