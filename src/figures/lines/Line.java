package figures.lines;

import figures.Figure;
import pixels.RealPoint;

public class Line extends Figure {
    private RealPoint firstPoint, secondPoint;

    public Line(RealPoint firstPoint, RealPoint secondPoint) {
        this.firstPoint = firstPoint;
        this.secondPoint = secondPoint;
    }

    public Line(double x1, double y1, double x2, double y2) {
        this(new RealPoint(x1, y1), new RealPoint(x2, y2));
    }

    public RealPoint getFirstPoint() {
        return firstPoint;
    }

    public void setFirstPoint(RealPoint firstPoint) {
        this.firstPoint = firstPoint;
    }

    public RealPoint getSecondPoint() {
        return secondPoint;
    }

    public void setSecondPoint(RealPoint secondPoint) {
        this.secondPoint = secondPoint;
    }
}
