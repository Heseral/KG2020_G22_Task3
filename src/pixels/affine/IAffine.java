package pixels.affine;

import pixels.ScreenPoint;

public interface IAffine {
    void transformWidth(ScreenPoint what, double coefficient);
    void transformHeight(ScreenPoint what, double coefficient);
    void transformHorizontalShift(ScreenPoint what, double coefficient);
    void transformVerticalShift(ScreenPoint what, double coefficient);
    void transformHorizontalSlant(ScreenPoint what, double coefficient);
    void transformVerticalSlant(ScreenPoint what, double coefficient);
    default ScreenPoint transform(ScreenPoint what, double[][] matrix) {
        transformWidth(what, matrix[0][0]);
        transformHeight(what, matrix[1][1]);
        transformHorizontalShift(what, matrix[2][0]);
        transformVerticalShift(what, matrix[2][1]);
        transformHorizontalSlant(what, matrix[1][0]);
        transformVerticalSlant(what, matrix[0][1]);
        return what;
    }
}
