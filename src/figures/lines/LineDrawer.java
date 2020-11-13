package figures.lines;

import pixels.ScreenPoint;

import java.awt.*;

public interface LineDrawer {
    void drawLine(ScreenPoint firstPoint, ScreenPoint secondPoint, Color color);
    void drawLine(ScreenPoint firstPoint, ScreenPoint secondPoint);
}
