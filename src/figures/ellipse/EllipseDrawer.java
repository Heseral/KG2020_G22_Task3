package figures.ellipse;

import pixels.PixelDrawer;
import pixels.ScreenConverter;
import pixels.ScreenPoint;
import util.Misc;

import java.awt.*;

public class EllipseDrawer {
    private PixelDrawer pixelDrawer;
    private ScreenConverter screenConverter;

    public EllipseDrawer(PixelDrawer pixelDrawer, ScreenConverter screenConverter) {
        this.setPixelDrawer(pixelDrawer);
        this.screenConverter = screenConverter;
    }

    public void drawEllipse(Ellipse ellipse) {
        drawEllipse(
                screenConverter.realToScreen(ellipse.getFrom()),
                screenConverter.realToScreen(ellipse.getWidthVector()),
                screenConverter.realToScreen(ellipse.getHeightVector()),
                ellipse.getTransformationMatrix(),
                ellipse.getColor()
        );
    }

    public void drawEllipse(ScreenPoint from, ScreenPoint widthVector, ScreenPoint heightVector, double[][] matrix, Color color) {
        double[][] tempWidthVector = Misc.multiplyMatrices(new double[][]{{widthVector.getX(), widthVector.getY(), 1}}, matrix);
        double[][] tempHeightVector = Misc.multiplyMatrices(new double[][]{{heightVector.getX(), heightVector.getY(), 1}}, matrix);
        int x0 = from.getX();
        int y0 = from.getY();
        int width = widthVector.getX();
        int height = heightVector.getY();
        width = (int) (width *  matrix[0][0] + height * matrix[1][0]);
        height = (int) (height * matrix[1][1] + width * matrix[0][1]);
        x0 += matrix[2][0] + matrix[0][0] + matrix[1][0];
        y0 -= matrix[2][1] + matrix[1][1] + matrix[0][1];
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
            pixelDrawer.colorPixel(x + x0, y + y0, color);
            pixelDrawer.colorPixel(x + x0, y0 - y, color);
            pixelDrawer.colorPixel(x0 - x, y + y0, color);
            pixelDrawer.colorPixel(x0 - x, y0 - y, color);
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
            pixelDrawer.colorPixel(x + x0, y + y0, color);
            pixelDrawer.colorPixel(x + x0, y0 - y, color);
            pixelDrawer.colorPixel(x0 - x, y + y0, color);
            pixelDrawer.colorPixel(x0 - x, y0 - y, color);
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
