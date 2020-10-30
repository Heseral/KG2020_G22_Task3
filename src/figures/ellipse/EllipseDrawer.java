package figures.ellipse;

import pixels.PixelDrawer;
import pixels.RealPoint;
import pixels.ScreenPoint;

import java.awt.*;

public class EllipseDrawer {
    private PixelDrawer pixelDrawer;

    public EllipseDrawer(PixelDrawer pixelDrawer) {
        this.setPixelDrawer(pixelDrawer);
    }

    public Ellipse drawEllipse(ScreenPoint centerFrom, int width, int height, Color color) {
        return drawEllipse(centerFrom.getX(), centerFrom.getY(), width, height, color);
    }

    public Ellipse drawEllipse(int x0, int y0, int width, int height, Color color) {
        int y = height;
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

        return new Ellipse(new RealPoint(x0, y0), width, height);
    }

    public void fillEllipse() {

    }

    public void drawEllipse(int x0, int y0, int radius, Color color) {
        drawEllipse(x0, y0, radius, radius, color);
    }

    public void drawCircle(int x0, int y0, int radius, boolean antialiasing, Color color) {
        if (!antialiasing) {
            drawEllipse(x0, y0, radius, radius, color);
            return;
        }

        pixelDrawer.colorPixel(x0 + radius, y0, color);
        pixelDrawer.colorPixel(x0, y0 + radius, color);
        pixelDrawer.colorPixel(x0 - radius + 1, y0, color);
        pixelDrawer.colorPixel(x0, y0 - radius + 1, color);

        float y;
        Color smoothedColor;
        Color alternateSmoothedColor;
        for (int x = 0; x <= radius * Math.cos(Math.PI / 4); x++)
        {
            y = (float) Math.sqrt(radius * radius - x * x);
            smoothedColor = new Color(
                    color.getRed(),
                    color.getGreen(),
                    color.getBlue(),
                    255 - (int) ((y - (int) y) * 255)
            );
            alternateSmoothedColor = new Color(
                    color.getRed(),
                    color.getGreen(),
                    color.getBlue(),
                    (int) ((y - (int) y) * 255)
            );
            pixelDrawer.colorPixel(x0 - x, y0 + (int) y, smoothedColor);
            pixelDrawer.colorPixel(x0 - x, y0 + (int) y + 1, alternateSmoothedColor);
            pixelDrawer.colorPixel(x0 + x, y0 + (int) y, smoothedColor);
            pixelDrawer.colorPixel(x0 + x, y0 + (int) y + 1, alternateSmoothedColor);
            pixelDrawer.colorPixel(x0 + (int) y, y0 + x, smoothedColor);
            pixelDrawer.colorPixel(x0 + (int) y + 1, y0 + x, alternateSmoothedColor);
            pixelDrawer.colorPixel(x0 + (int) y, y0 - x, smoothedColor);
            pixelDrawer.colorPixel(x0 + (int) y + 1, y0 - x, alternateSmoothedColor);

            // компенсируем х-- снизу
            x++;
            pixelDrawer.colorPixel(x0 + x, y0 - (int) (y), alternateSmoothedColor);
            pixelDrawer.colorPixel(x0 + x, y0 - (int) (y) + 1, smoothedColor);
            pixelDrawer.colorPixel(x0 - x, y0 - (int) (y), alternateSmoothedColor);
            pixelDrawer.colorPixel(x0 - x, y0 - (int) (y) + 1, smoothedColor);
            pixelDrawer.colorPixel(x0 - (int) y, y0 - x, alternateSmoothedColor);
            pixelDrawer.colorPixel(x0 - (int) y + 1, y0 - x, smoothedColor);
            pixelDrawer.colorPixel(x0 - (int) y, y0 + x,alternateSmoothedColor);
            pixelDrawer.colorPixel(x0 - (int) y + 1, y0 + x, smoothedColor);
            x--;
        }
    }

    public PixelDrawer getPixelDrawer() {
        return pixelDrawer;
    }

    public void setPixelDrawer(PixelDrawer pixelDrawer) {
        this.pixelDrawer = pixelDrawer;
    }
}
