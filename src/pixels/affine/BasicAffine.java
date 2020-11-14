package pixels.affine;

import pixels.ScreenPoint;

public class BasicAffine implements IAffine {
    private final double[][] matrix = new double[][]{{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};

    public double[][] getMatrix() {
        return matrix;
    }

    @Override
    public ScreenPoint transform(ScreenPoint what, double[][] matrix) {
        what.setX(what.getX() * matrix[0][0] + what.getY() * matrix[1][0] + matrix[2][0]);
        what.setY(what.getY() * matrix[1][1] + what.getX() * matrix[0][1] + matrix[2][1]);
        return what;
    }

    public ScreenPoint transform(ScreenPoint what) {
        return transform(what, getMatrix());
    }
}
