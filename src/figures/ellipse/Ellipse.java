package figures.ellipse;

import pixels.RealPoint;
import pixels.affine.BasicAffine;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class Ellipse {
    private List<RealPoint> pixels = new ArrayList<>();
    private RealPoint from;
    private RealPoint heightVector;
    private RealPoint widthVector;
    private Color color = Color.BLACK;
    private boolean isSelected = false;
    private final BasicAffine affine = new BasicAffine();

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

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public List<RealPoint> getPixels() {
        return pixels;
    }

    public void setPixels(List<RealPoint> pixels) {
        this.pixels = pixels;
    }

    public BasicAffine getAffine() {
        return affine;
    }
}
