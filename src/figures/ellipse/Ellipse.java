package figures.ellipse;

import pixels.RealPoint;

public class Ellipse {
    private RealPoint from;
    private RealPoint heightVector;
    private RealPoint widthVector;

    public Ellipse(RealPoint from, RealPoint widthVector, RealPoint heightVector) {
        onInit(from, widthVector, heightVector);
    }

    public void onInit(RealPoint centerFrom, RealPoint widthVector, RealPoint heightVector) {
        setFrom(centerFrom);
        setHeightVector(heightVector);
        setWidthVector(widthVector);
    }

    public void onInit(Ellipse ellipse) {
        onInit(ellipse.getFrom(), ellipse.getWidthVector(), ellipse.getHeightVector());
    }

    public RealPoint getFrom() {
        return from;
    }

    public void setFrom(RealPoint from) {
        this.from = from;
    }


    public RealPoint getHeightVector() {
        return heightVector;
    }

    public void setHeightVector(RealPoint heightVector) {
        this.heightVector = heightVector;
    }

    public RealPoint getWidthVector() {
        return widthVector;
    }

    public void setWidthVector(RealPoint widthVector) {
        this.widthVector = widthVector;
    }
}
