package figures.ellipse;

import pixels.RealPoint;

public class Ellipse {
    private RealPoint from;
    private int height;
    private int width;

    public Ellipse(RealPoint from, int width, int height) {
        onInit(from, width, height);
    }

    public void onInit(RealPoint centerFrom, int widthTo, int heightTo) {
        setFrom(centerFrom);
        setHeight(heightTo);
        setWidth(widthTo);
    }

    public void onInit(Ellipse ellipse) {
        onInit(ellipse.getFrom(), ellipse.getWidth(), ellipse.getHeight());
    }

    public RealPoint getFrom() {
        return from;
    }

    public void setFrom(RealPoint from) {
        this.from = from;
    }


    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
