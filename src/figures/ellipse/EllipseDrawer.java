package figures.ellipse;

import pixels.PixelDrawer;
import pixels.RealPoint;
import pixels.ScreenConverter;
import pixels.ScreenPoint;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class EllipseDrawer {
    private PixelDrawer pixelDrawer;
    private ScreenConverter screenConverter;

    public EllipseDrawer(PixelDrawer pixelDrawer, ScreenConverter screenConverter) {
        this.setPixelDrawer(pixelDrawer);
        this.screenConverter = screenConverter;
    }

    public void drawEllipse(Ellipse ellipse, Color color, boolean shouldBeCreated) {
        if (shouldBeCreated) {
            createEllipse(ellipse, ellipse.getTransformationMatrix());
        }
        ScreenPoint screenPointToDraw;
        for (RealPoint pixel : ellipse.getPixels()) {
            screenPointToDraw = screenConverter.realToScreen(pixel);
            pixelDrawer.colorPixel(screenPointToDraw.getX(), screenPointToDraw.getY(), color);
        }
    }

    public void createEllipse(Ellipse ellipse, double[][] matrix) {
        ScreenPoint from = screenConverter.realToScreen(ellipse.getFrom());
        ScreenPoint screenedWidthVector = screenConverter.realToScreen(ellipse.getWidthVector());
        ScreenPoint screenedHeightVector = screenConverter.realToScreen(ellipse.getHeightVector());
        ScreenPoint widthVector = new ScreenPoint(screenedWidthVector.getX() - from.getX(), screenedWidthVector.getY() - from.getY());
        ScreenPoint heightVector = new ScreenPoint(screenedHeightVector.getX() - from.getX(), screenedHeightVector.getY() - from.getY());
        List<RealPoint> pixels = new ArrayList<>();
        ellipse.setPixels(pixels);
        int coefficientX = (int) Math.ceil(Math.abs(matrix[0][0]) + Math.abs(matrix[1][0]));
        int coefficientY = (int) Math.ceil(Math.abs(matrix[0][1]) + Math.abs(matrix[1][1]));
        // fixme: ъуъ, если у b и c разные знаки, то при перемещении экрана на ПКМ эллипс перемещается в 2 раза быстрее
        // fixme: ьеь, если у b и c одинаковые знаки, то эллипс вырождается в прямую или точку
        int x0 = (int) (from.getX() / (matrix[0][0] == 0 ? 1 : matrix[0][0]) * coefficientX - from.getY() * matrix[1][0] * coefficientY);
        int y0 = (int) (from.getY() / (matrix[1][1] == 0 ? 1 : matrix[1][1]) * coefficientY - from.getX() * matrix[0][1] * coefficientX);
        int width = widthVector.getX() * coefficientX;
        int height = heightVector.getY() * coefficientY;
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
        int x1;
        int x2;
        int y1;
        int y2;

        // горизонтально-ориентированные кривые
        while (aSquared * y > bSquared * x) {
            x1 = x + x0;
            x2 = x0 - x;
            y1 = y + y0;
            y2 = y0 - y;

            pixels.add(screenConverter.screenToReal(
                    new ScreenPoint(
                            (x1 * matrix[0][0] + y1 * matrix[1][0]) / coefficientX,
                            (y1 * matrix[1][1] + x1 * matrix[0][1]) / coefficientY
                    )
            ));
            pixels.add(screenConverter.screenToReal(
                    new ScreenPoint(
                            (x1 * matrix[0][0] + y2 * matrix[1][0]) / coefficientX,
                            (y2 * matrix[1][1] + x1 * matrix[0][1]) / coefficientY
                    )
            ));
            pixels.add(screenConverter.screenToReal(
                    new ScreenPoint(
                            (x2 * matrix[0][0] + y1 * matrix[1][0]) / coefficientX,
                            (y1 * matrix[1][1] + x2 * matrix[0][1]) / coefficientY
                    )
            ));
            pixels.add(screenConverter.screenToReal(
                    new ScreenPoint(
                            (x2 * matrix[0][0] + y2 * matrix[1][0]) / coefficientX,
                            (y2 * matrix[1][1] + x2 * matrix[0][1]) / coefficientY
                    )
            ));

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
            x1 = x + x0;
            x2 = x0 - x;
            y1 = y + y0;
            y2 = y0 - y;

            pixels.add(screenConverter.screenToReal(
                    new ScreenPoint(
                            (x1 * matrix[0][0] + y1 * matrix[1][0]) / coefficientX,
                            (y1 * matrix[1][1] + x1 * matrix[0][1]) / coefficientY
                    )
            ));
            pixels.add(screenConverter.screenToReal(
                    new ScreenPoint(
                            (x1 * matrix[0][0] + y2 * matrix[1][0]) / coefficientX,
                            (y2 * matrix[1][1] + x1 * matrix[0][1]) / coefficientY
                    )
            ));
            pixels.add(screenConverter.screenToReal(
                    new ScreenPoint(
                            (x2 * matrix[0][0] + y1 * matrix[1][0]) / coefficientX,
                            (y1 * matrix[1][1] + x2 * matrix[0][1]) / coefficientY
                    )
            ));
            pixels.add(screenConverter.screenToReal(
                    new ScreenPoint(
                            (x2 * matrix[0][0] + y2 * matrix[1][0]) / coefficientX,
                            (y2 * matrix[1][1] + x2 * matrix[0][1]) / coefficientY
                    )
            ));

            if (delta <= 0) {
                x++;
                delta += quadrupleBSquared * x;
            }
            y--;
            delta += doubleASquared * (3 - y * 2);
        }
    }

/*
    public void drawEllipse(ScreenPoint centerFrom, int width, int height, double[] matrix, Color color) {
        drawEllipse(centerFrom.getX(), centerFrom.getY(), width, height, matrix, color);
    }

    public void drawEllipse(int x0, int y0, int width, int height, double[] matrix, Color color) {
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
        int x1;
        int x2;
        int y1;
        int y2;
        int xToDraw;
        int yToDraw;
        // горизонтально-ориентированные кривые
        while (aSquared * y > bSquared * x) {
            x1 = x + x0;
            x2 = x0 - x;
            y1 = y + y0;
            y2 = y0 - y;

            xToDraw = (int) (x1 * matrix[0] + y1 * matrix[3]);
            yToDraw = (int) (x1 * matrix[1] + y1 * matrix[4]);
            pixelDrawer.colorPixel(xToDraw, yToDraw, color);
            xToDraw = (int) (x1 * matrix[0] + y2 * matrix[3]);
            yToDraw = (int) (x1 * matrix[1] + y2 * matrix[4]);
            pixelDrawer.colorPixel(xToDraw, yToDraw, color);
            xToDraw = (int) (x2 * matrix[0] + y1 * matrix[3]);
            yToDraw = (int) (x2 * matrix[1] + y1 * matrix[4]);
            pixelDrawer.colorPixel(xToDraw, yToDraw, color);
            xToDraw = (int) (x2 * matrix[0] + y2 * matrix[3]);
            yToDraw = (int) (x2 * matrix[1] + y2 * matrix[4]);
            pixelDrawer.colorPixel(xToDraw, yToDraw, color);
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
            x1 = x + x0;
            x2 = x0 - x;
            y1 = y + y0;
            y2 = y0 - y;

            xToDraw = (int) (x1 * matrix[0] + y1 * matrix[3]);
            yToDraw = (int) (x1 * matrix[1] + y1 * matrix[4]);
            pixelDrawer.colorPixel(xToDraw, yToDraw, color);
            xToDraw = (int) (x1 * matrix[0] + y2 * matrix[3]);
            yToDraw = (int) (x1 * matrix[1] + y2 * matrix[4]);
            pixelDrawer.colorPixel(xToDraw, yToDraw, color);
            xToDraw = (int) (x2 * matrix[0] + y1 * matrix[3]);
            yToDraw = (int) (x2 * matrix[1] + y1 * matrix[4]);
            pixelDrawer.colorPixel(xToDraw, yToDraw, color);
            xToDraw = (int) (x2 * matrix[0] + y2 * matrix[3]);
            yToDraw = (int) (x2 * matrix[1] + y2 * matrix[4]);
            pixelDrawer.colorPixel(xToDraw, yToDraw, color);

            if (delta <= 0) {
                x++;
                delta += quadrupleBSquared * x;
            }
            y--;
            delta += doubleASquared * (3 - y * 2);
        }
    }


 */

    public PixelDrawer getPixelDrawer() {
        return pixelDrawer;
    }

    public void setPixelDrawer(PixelDrawer pixelDrawer) {
        this.pixelDrawer = pixelDrawer;
    }
}
