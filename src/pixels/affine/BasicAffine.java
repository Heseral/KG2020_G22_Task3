package pixels.affine;

import pixels.ScreenPoint;

public class BasicAffine implements IAffine {
    private final double[][] matrix = new double[][]{{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};

    @Override
    public void transformWidth(ScreenPoint what, double coefficient) {
        what.setX(what.getX() * coefficient);
    }

    @Override
    public void transformHeight(ScreenPoint what, double coefficient) {
        what.setY(what.getY() * coefficient);
    }

    @Override
    public void transformHorizontalShift(ScreenPoint what, double coefficient) {
        what.setX(what.getX() + coefficient);
    }

    @Override
    public void transformVerticalShift(ScreenPoint what, double coefficient) {
        what.setY(what.getY() + coefficient);
    }

    @Override
    public void transformHorizontalSlant(ScreenPoint what, double coefficient) {
        what.setX(what.getY() * coefficient);
    }

    @Override
    public void transformVerticalSlant(ScreenPoint what, double coefficient) {
        what.setY(what.getX() * coefficient);
    }

    public double[][] getMatrix() {
        return matrix;
    }
}
