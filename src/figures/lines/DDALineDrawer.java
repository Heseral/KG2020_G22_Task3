package figures.lines;

import pixels.PixelDrawer;
import pixels.ScreenPoint;

import java.awt.*;

public class DDALineDrawer implements LineDrawer {
    private PixelDrawer pixelDrawer;

    public DDALineDrawer(PixelDrawer pixelDrawer) {
        this.pixelDrawer = pixelDrawer;
    }

    @Override
    public void drawLine(ScreenPoint firstPoint, ScreenPoint secondPoint, Color color) {
        int x1 = firstPoint.getX(), y1 = firstPoint.getY();
        int x2 = secondPoint.getX(), y2 = secondPoint.getY();
        double dx = x2 - x1;
        double dy = y2 - y1;

        if (Math.abs(dy) > Math.abs(dx)) {
            double reversedK = dx / dy;

            if (y1 > y2) {
                int tmp = y2;
                y2 = y1;
                y1 = tmp;
                tmp = x2;
                x2 = x1;
                x1 = tmp;
            }
            for (int i = y1; i < y2; i++) {
                double j = (i - y1) * reversedK + x1;
                pixelDrawer.colorPixel((int) j, i, color);
            }
        } else {

            double k = dy / dx;
            if (x1 > x2) {
                int tmp = y2;
                y2 = y1;
                y1 = tmp;
                tmp = x2;
                x2 = x1;
                x1 = tmp;
            }
            for (int j = x1; j <= x2; j++) {
                double i = (j - x1) * k + y1;
                pixelDrawer.colorPixel(j, (int) i, color);

            }
        }

    }

    @Override
    public void drawLine(ScreenPoint firstPoint, ScreenPoint secondPoint) {
        drawLine(firstPoint, secondPoint, Color.BLACK);
    }
}

