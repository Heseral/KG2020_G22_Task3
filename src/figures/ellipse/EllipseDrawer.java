package figures.ellipse;

import pixels.PixelDrawer;
import pixels.RealPoint;
import pixels.ScreenConverter;
import pixels.ScreenPoint;
import pixels.affine.BasicAffine;
import pixels.affine.IAffine;

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
            createEllipse(ellipse);
        }
        ScreenPoint screenPointToDraw;
        for (RealPoint pixel : ellipse.getPixels()) {
            screenPointToDraw = screenConverter.realToScreen(pixel);
            pixelDrawer.colorPixel(screenPointToDraw.getX(), screenPointToDraw.getY(), color);
        }
    }

    public void createEllipse(Ellipse ellipse) {
        BasicAffine affine = ellipse.getAffine();
        ScreenPoint from = screenConverter.realToScreen(ellipse.getFrom());
        ScreenPoint screenedWidthVector = screenConverter.realToScreen(ellipse.getWidthVector());
        ScreenPoint screenedHeightVector = screenConverter.realToScreen(ellipse.getHeightVector());
        ScreenPoint widthVector = new ScreenPoint(
                screenedWidthVector.getX() - from.getX(),
                screenedWidthVector.getY() - from.getY()
        );
        ScreenPoint heightVector = new ScreenPoint(
                screenedHeightVector.getX() - from.getX(),
                screenedHeightVector.getY() - from.getY()
        );
        List<RealPoint> pixels = new ArrayList<>();
        ellipse.setPixels(pixels);
        int coefficientX = (int) Math.ceil(Math.abs(affine.getMatrix()[0][0]) + Math.abs(affine.getMatrix()[1][0]));
        int coefficientY = (int) Math.ceil(Math.abs(affine.getMatrix()[0][1]) + Math.abs(affine.getMatrix()[1][1]));
        int x0 = (int) (from.getX() / (affine.getMatrix()[0][0] == 0 ? 1 : affine.getMatrix()[0][0]) * coefficientX -
                from.getY() * affine.getMatrix()[1][0] * coefficientY);
        int y0 = (int) (from.getY() / (affine.getMatrix()[1][1] == 0 ? 1 : affine.getMatrix()[1][1]) * coefficientY -
                from.getX() * affine.getMatrix()[0][1] * coefficientX);
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
        ScreenPoint pointToDraw;
        // горизонтально-ориентированные кривые
        while (aSquared * y > bSquared * x) {
            x1 = x + x0;
            x2 = x0 - x;
            y1 = y + y0;
            y2 = y0 - y;

            pointToDraw = new ScreenPoint(x1, y1);
            affine.transform(pointToDraw);
            pointToDraw.setX((double) pointToDraw.getX() / coefficientX);
            pointToDraw.setY((double) pointToDraw.getY() / coefficientY);
            pixels.add(screenConverter.screenToReal(pointToDraw));
            pointToDraw = new ScreenPoint(x1, y2);
            affine.transform(pointToDraw);
            pointToDraw.setX((double) pointToDraw.getX() / coefficientX);
            pointToDraw.setY((double) pointToDraw.getY() / coefficientY);
            pixels.add(screenConverter.screenToReal(pointToDraw));
            pointToDraw = new ScreenPoint(x2, y1);
            affine.transform(pointToDraw);
            pointToDraw.setX((double) pointToDraw.getX() / coefficientX);
            pointToDraw.setY((double) pointToDraw.getY() / coefficientY);
            pixels.add(screenConverter.screenToReal(pointToDraw));
            pointToDraw = new ScreenPoint(x2, y2);
            affine.transform(pointToDraw);
            pointToDraw.setX((double) pointToDraw.getX() / coefficientX);
            pointToDraw.setY((double) pointToDraw.getY() / coefficientY);
            pixels.add(screenConverter.screenToReal(pointToDraw));

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

            pointToDraw = new ScreenPoint(x1, y1);
            affine.transform(pointToDraw);
            pointToDraw.setX((double) pointToDraw.getX() / coefficientX);
            pointToDraw.setY((double) pointToDraw.getY() / coefficientY);
            pixels.add(screenConverter.screenToReal(pointToDraw));
            pointToDraw = new ScreenPoint(x1, y2);
            affine.transform(pointToDraw);
            pointToDraw.setX((double) pointToDraw.getX() / coefficientX);
            pointToDraw.setY((double) pointToDraw.getY() / coefficientY);
            pixels.add(screenConverter.screenToReal(pointToDraw));
            pointToDraw = new ScreenPoint(x2, y1);
            affine.transform(pointToDraw);
            pointToDraw.setX((double) pointToDraw.getX() / coefficientX);
            pointToDraw.setY((double) pointToDraw.getY() / coefficientY);
            pixels.add(screenConverter.screenToReal(pointToDraw));
            pointToDraw = new ScreenPoint(x2, y2);
            affine.transform(pointToDraw);
            pointToDraw.setX((double) pointToDraw.getX() / coefficientX);
            pointToDraw.setY((double) pointToDraw.getY() / coefficientY);
            pixels.add(screenConverter.screenToReal(pointToDraw));

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
